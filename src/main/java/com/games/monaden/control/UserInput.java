package com.games.monaden.control;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 Class is responsible for holding the latest user input
 */
class UserInput implements EventHandler<Event> {
    private static UserInput userInput;
    private KeyCode movementKey;
    private KeyCode functionKey;

    private UserInput(){}

    @Override
    public void handle(Event event) {
        KeyEvent keyEvent = (KeyEvent) event;
        switch (keyEvent.getCode()){
            case UP:
                movementKey = KeyCode.UP;
                break;
            case DOWN:
                movementKey = KeyCode.DOWN;
                break;
            case LEFT:
                movementKey = KeyCode.LEFT;
                break;
            case RIGHT:
                movementKey = KeyCode.RIGHT;
                break;
            case ESCAPE:
                functionKey = KeyCode.ESCAPE;
                break;
            case ENTER:
                functionKey = KeyCode.ENTER;
                break;
            case SPACE:
                functionKey = KeyCode.SPACE;
                break;
            case PLUS:
                functionKey = KeyCode.PLUS;
                break;
            case MINUS:
                functionKey = KeyCode.MINUS;
                break;
            case N:
                functionKey = KeyCode.N;
                break;
        }
    }

    KeyCode getLatestMovementKey(){
        KeyCode key = movementKey;
        movementKey = null;
        return key;
    }

    KeyCode getLatestFunctionKey(){
        KeyCode key = functionKey;
        functionKey = null;
        return key;
    }

    static synchronized UserInput getInstance(){
        if (userInput == null){
            userInput = new UserInput();
        }
        return userInput;
    }
}
