package com.games.monaden.services.level;

import com.games.monaden.model.Transition;
import com.games.monaden.model.World;
import com.games.monaden.model.events.DialogEvent;
import com.games.monaden.model.gameobject.Character;
import com.games.monaden.model.gameobject.GameObject;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 2016/05/19.
 */
public class LevelLoader {

    private int [][] tileMap;
    private List<GameObject> gameObjects;
    private List<Character> interactables;
    private List<Transition> transitions;
    private List<DialogEvent> events;


    SAXParser parser;
    SAXParserFactory factory = SAXParserFactory.newInstance();
    LevelParser levelParser;

    public LevelLoader () {
        tileMap = new int[World.MAP_SIZE][World.TILE_SIZE];
        gameObjects = new ArrayList<>();
        interactables = new ArrayList<>();
        levelParser = new LevelParser();
        transitions = new ArrayList<>();
        events = new ArrayList<>();
        try {
            parser = factory.newSAXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLevel (String levelName) {

        levelParser.clearTilemap();
        levelParser.clearGameObjects();
        levelParser.clearInteractables();
        levelParser.clearTransitions();

        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("maps/" + levelName);
        try {
            parser.parse(is, levelParser);

            gameObjects.clear();
            gameObjects = levelParser.getGameObjects();

            interactables.clear();
            interactables = levelParser.getInteractables();

            tileMap = levelParser.getTileMap();

            transitions.clear();
            transitions = levelParser.getTransitions();

            events.clear();
            events = levelParser.getEvents();

        } catch (Exception e) {
            System.err.println("GameLoop : Loadlevel failed to read file: " + levelName);
            e.printStackTrace();
        }
    }

    public int[][] getTileMap () {
        return this.tileMap;
    }

    public List<GameObject> getGameObjects () {
        return this.gameObjects;
    }

    public List<Character> getInteractables () {
        return this.interactables;
    }

    public List<Transition> getTransitions () {
        return this.transitions;
    }

    public List<DialogEvent> getEvents () {
        return this.events;
    }
}
