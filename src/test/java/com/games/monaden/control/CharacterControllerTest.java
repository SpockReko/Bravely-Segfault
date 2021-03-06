package com.games.monaden.control;

import com.games.monaden.model.dialog.Dialog;
import com.games.monaden.model.primitives.Point;
import com.games.monaden.model.primitives.Transition;
import com.games.monaden.model.World;
import com.games.monaden.model.events.DialogEvent;
import com.games.monaden.model.gameobject.Character;
import com.games.monaden.model.gameobject.GameObject;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Philip on 2016-05-20.
 */
public class CharacterControllerTest {

    private static World startWorld;
    private World world;
    private CharacterController characterController;

    @BeforeClass
    public static void initClass() {
        startWorld = new World();
        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(new GameObject(new Point(4, 5), null, true));
        List<Character> interactables = new ArrayList<>();
        interactables.add(new Character(new Point(10, 10), null));
        interactables.get(0).setDialog(new Dialog("Hello there."));
        List<Transition> transitions = new ArrayList<>();
        List<DialogEvent> events = new ArrayList<>();
        startWorld.setCurrentLevel(gameObjects, interactables, transitions, events);
    }

    @Before
    public void initTest() {
        world = startWorld;
    }

    @Test
    public void moveUpBlocked() {
        characterController = new CharacterController(new Point(4, 6));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.UP, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveDownBlocked() {
        characterController = new CharacterController(new Point(4, 4));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.DOWN, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveLeftBlocked() {
        characterController = new CharacterController(new Point(5, 5));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.LEFT, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveRightBlocked() {
        characterController = new CharacterController(new Point(3, 5));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.RIGHT, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveUpUnblocked() {
        characterController = new CharacterController(new Point(3, 3));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.RIGHT, world);
        assertFalse(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveDownUnblocked() {
        characterController = new CharacterController(new Point(3, 3));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.RIGHT, world);
        assertFalse(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveLeftUnblocked() {
        characterController = new CharacterController(new Point(3, 3));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.RIGHT, world);
        assertFalse(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveRightUnblocked() {
        characterController = new CharacterController(new Point(3, 3));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.RIGHT, world);
        assertFalse(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveUpEdge() {
        characterController = new CharacterController(new Point(0, 0));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.UP, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveDownEdge() {
        characterController = new CharacterController(new Point(0, World.MAP_SIZE-1));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.DOWN, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveLeftEdge() {
        characterController = new CharacterController(new Point(0, 0));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.LEFT, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void moveRightEdge() {
        characterController = new CharacterController(new Point(World.MAP_SIZE-1, 0));
        Point oldPos = characterController.getPlayerPos();
        characterController.handleMovement(KeyCode.RIGHT, world);
        assertTrue(oldPos.equals(characterController.getPlayerPos()));
    }

    @Test
    public void upEdgeException() {
        characterController = new CharacterController(new Point(0,0));
        ArrayIndexOutOfBoundsException check = null;
        try{
            characterController.handleMovement(KeyCode.UP, world);
        }catch(ArrayIndexOutOfBoundsException e){
            check = e;
        }
        assertNull(check);
    }

    @Test
    public void downEdgeException() {
        characterController = new CharacterController(new Point(0,World.MAP_SIZE-1));
        ArrayIndexOutOfBoundsException check = null;
        try{
            characterController.handleMovement(KeyCode.DOWN, world);
        }catch(ArrayIndexOutOfBoundsException e){
            check = e;
        }
        assertNull(check);
    }

    @Test
    public void leftEdgeException() {
        characterController = new CharacterController(new Point(0,0));
        ArrayIndexOutOfBoundsException check = null;
        try{
            characterController.handleMovement(KeyCode.LEFT, world);
        }catch(ArrayIndexOutOfBoundsException e){
            check = e;
        }
        assertNull(check);
    }

    @Test
    public void rightEdgeException() {
        characterController = new CharacterController(new Point(World.MAP_SIZE-1,0));
        ArrayIndexOutOfBoundsException check = null;
        try{
            characterController.handleMovement(KeyCode.RIGHT, world);
        }catch(ArrayIndexOutOfBoundsException e){
            check = e;
        }
        assertNull(check);
    }

    @Test
    public void upInteraction() {
        characterController = new CharacterController(new Point(10, 11));
        characterController.handleMovement(KeyCode.UP, world);
        Dialog dialog = characterController.handleInteractions(KeyCode.SPACE, world);
        assertTrue(dialog.getDialogText().equals("Hello there."));
    }

    @Test
    public void downInteraction() {
        characterController = new CharacterController(new Point(10, 9));
        characterController.handleMovement(KeyCode.DOWN, world);
        Dialog dialog = characterController.handleInteractions(KeyCode.SPACE, world);
        assertTrue(dialog.getDialogText().equals("Hello there."));
    }

    @Test
    public void leftInteraction() {
        characterController = new CharacterController(new Point(11, 10));
        characterController.handleMovement(KeyCode.LEFT, world);
        Dialog dialog = characterController.handleInteractions(KeyCode.SPACE, world);
        assertTrue(dialog.getDialogText().equals("Hello there."));
    }

    @Test
    public void rightInteraction() {
        characterController = new CharacterController(new Point(9, 10));
        characterController.handleMovement(KeyCode.RIGHT, world);
        Dialog dialog = characterController.handleInteractions(KeyCode.SPACE, world);
        assertTrue(dialog.getDialogText().equals("Hello there."));
    }

}