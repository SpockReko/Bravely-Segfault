package com.games.monaden.control;

import com.games.monaden.model.*;
import com.games.monaden.model.dialog.Dialog;
import com.games.monaden.model.events.DialogEvent;
import com.games.monaden.model.events.Event;
import com.games.monaden.model.gameobject.Character;
import com.games.monaden.model.gameobject.GameObject;
import com.games.monaden.model.primitives.MovementDirection;
import com.games.monaden.model.primitives.Point;
import com.games.monaden.model.primitives.Tile;
import com.games.monaden.model.primitives.Transition;
import com.games.monaden.services.dialog.DialogLoader;
import com.games.monaden.services.level.LevelLoader;
import com.games.monaden.services.tile.TileLoader;
import com.games.monaden.view.Render;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.*;

/**
 Class is responsible for handling all input and supplying it to the correct controller
 Also initializes the game, loads levels and generally ties everything together.
 Could possibly be broken up into smaller classes?
 */
public class GameLoop extends AnimationTimer implements Observer {

    private final static int FREQUENCY = 16;
    private int countDown = FREQUENCY;
    private int npcCountDown = FREQUENCY*5;

    private double volume = 0.0;

    private final static int STARTSCREEN_FADING = 64;
    private int currentStartFade;

    private World world;
    private CharacterController playerCharacter;

    private AudioController audioController;
    private HashMap<Integer, Tile> tileMap;

    private DialogController dialogController;

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof DialogEvent) {
            DialogEvent de = (DialogEvent)arg;
            startDialog((Dialog)de.getEventContent());
        } else if (arg instanceof String) {
            setLevel((String) arg);
        } else if (arg instanceof Transition){
            Transition t = (Transition)arg;
            playerCharacter.transitionEvent(t);
            setLevel(t.newLevel);
            inputState = InputState.MOVEMENT;
        }
    }

    //The input state determines to which controller we want to send input.
    //This could probably be handled with a controller interface instead!
    private enum InputState { MOVEMENT, DIALOG, STARTSCREEN, STARTSCREEN_FADING}
    private InputState inputState = InputState.STARTSCREEN; // The first state we are in is the start screen

    public GameLoop () {
        tileMap = new HashMap<>();
        playerCharacter = new CharacterController();
        playerCharacter.addObserver(this);
        dialogController = new DialogController();
        dialogController.addObserver(this);
        audioController = new AudioController();
    }

    public void initializeGame() {
        tileMap = new TileLoader().loadTiles();
        world = new World();
        setLevel("scene_1/ea.xml");
        Render.getInstance().setWorld(world);

        audioController.playMusic(0);

        //start dialog, ställ in karaktär och ställ in namn.
        //dialogController.startDialog("start");
        // skapa metoderna set name i world klassen,

    }
    /**
     * Sets the world's current level to be what the given file is.
     * @param levelName File path to XML
     */
    private void setLevel (String levelName) {
        LevelLoader levelLoader = new LevelLoader();
        levelLoader.loadLevel(levelName);
        int [][] primTileMap = levelLoader.getTileMap();
        List<GameObject> gameObjects = new ArrayList<>();

        outerloop:
        for (int y = 0; y < World.MAP_SIZE; y++) {
            for (int x = 0; x < World.MAP_SIZE; x++) {
                Tile currentTile =  findTile(primTileMap[y][x]);
                if (currentTile == null){
                    System.err.println("Bad tile @ X" + x + " Y:" + y);
                    break outerloop;
                }
                GameObject newGameObject = new GameObject(new Point(x, y), currentTile.getFilepath().toString(), currentTile.isSolid());
                newGameObject.setContinuousAnimation(currentTile.isAnimated());
                gameObjects.add(newGameObject);
            }
        }

        gameObjects.addAll(levelLoader.getGameObjects());
        List<Character> interactables = levelLoader.getInteractables();
        DialogLoader dialogLoader = new DialogLoader();

        //Add dialog to each character
        for (Character c : interactables) {
            c.setDialog(dialogLoader.parseDialog(c.getDialogFile().getPath()));
        }

        //Handle events in the level
        List<DialogEvent> events = levelLoader.getEvents();
        for (DialogEvent de : events) {
            Dialog dialog = dialogLoader.parseDialog(de.getFilepath().getPath());
            de.setEventContent(dialog);
        }

        List<String> musicList = levelLoader.getMusicList();
        for (String path : musicList) {

            audioController.addMusic(path);
        }
        world.setCurrentLevel(gameObjects, interactables, levelLoader.getTransitions(), events);
    }

    private Tile findTile (int tileNr) {
        return tileMap.get(tileNr);
    }

    private void handleEvents (Event event) {
        if (event instanceof DialogEvent) {
            Dialog dialog = (Dialog)event.getEventContent();
            startDialog(dialog);
        }
    }

    @Override
    public void handle(long now) {
        if (inputState == InputState.STARTSCREEN){
            if (UserInput.getInstance().getLatestFunctionKey() == null){
                return;
            }else{
                currentStartFade = STARTSCREEN_FADING;
                inputState = InputState.STARTSCREEN_FADING;
                Render.getInstance().startscreenFade();

            }
        }
        else if (inputState == InputState.STARTSCREEN_FADING){
            currentStartFade--;
            if (currentStartFade == 0){
                Render.getInstance().hideStartScreen();
                inputState = InputState.MOVEMENT;
                return;
            }

        }

        Render.getInstance().redraw();

        if (countDown > 0){  // used to add a delay (better than sleep) to user movement
            countDown--;
        }
        else if(inputState == InputState.MOVEMENT) {
            UserInput userInput = UserInput.getInstance();
            KeyCode moveReq = userInput.getLatestMovementKey();
            if (moveReq != null) {
                playerCharacter.handleMovement(moveReq, world);
                audioController.playSound("step"); // This causes a lot of errors from test suite.
                countDown = FREQUENCY;
            }

            KeyCode funcReq = userInput.getLatestFunctionKey();

            if (funcReq != null) {
                Dialog dialog = playerCharacter.handleInteractions(funcReq, world);
                if (dialog != null) {
                    startDialog(dialog);
                }else if (funcReq == KeyCode.PLUS) {

                    volume = audioController.volumeUp();

                } else if (funcReq == KeyCode.MINUS) {

                    volume = audioController.volumeDown();

                } else if (funcReq == KeyCode.N) {

                    audioController.stopMusic();
                    audioController.playMusic(1);
                }
            }
        }
        else if(inputState == InputState.DIALOG){
            UserInput userInput = UserInput.getInstance();
            KeyCode moveReq = userInput.getLatestMovementKey();
            if(dialogController.handleMovement(moveReq)){
                countDown = FREQUENCY;
            }

            KeyCode funcReq = userInput.getLatestFunctionKey();
            if(dialogController.handleSpecial(funcReq)){
                inputState = InputState.MOVEMENT;
            }
        }
        /**
         *  Move all objects in the level that have a moving scheme.
         */
        if(npcCountDown != 0) {
            npcCountDown --;
        }else if(npcCountDown == 0){
            npcCountDown = FREQUENCY*10;
            for (Character npc : world.getInteractables()) {

                if (npc.getMovements() != null) {
                    npcMove(npc);
                }
            }

        }
    }


    //Used if dialogs are triggered from event (for example stepping on a tile)
    private void startDialog(Dialog dialog) {
        inputState = InputState.DIALOG;
        dialogController.startDialog(dialog);
    }


    private void npcMove(Character npc){

        CharacterController npcController = new CharacterController(npc);
        KeyCode[] moveScheme = npc.getMovements();
        int move = getMove(moveScheme.length, npc.getStep());
        KeyCode key = moveScheme[move];
        npcController.handleMovement(key, world);
        npc.addStep();
    }

    private int getMove(int length, int i){
        return i % length;
    }
}
