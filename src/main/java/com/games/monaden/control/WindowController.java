package com.games.monaden.control;

/**
 * Created by paraply on 2016-04-13.
 */

import com.games.monaden.view.Render;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowController implements Initializable{
    @FXML private Canvas mainCanvas; // Our canvas where the game will be drawn
    @FXML private VBox dialogbox;
    @FXML private Label question;
    @FXML private Label answer1;
    @FXML private Label answer2;
    @FXML private Label answer3;
    // Called when JavaFX initialized the window.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainCanvas.setFocusTraversable(true); // May be necessary to get key events
        mainCanvas.setOnKeyPressed(UserInput.getInstance());
        Render.getInstance().setGraphicsContext(mainCanvas.getGraphicsContext2D()); // Get the canvas context and send it to Render
        Render.getInstance().setDialogObjects(dialogbox, question, answer1,answer2,answer3);
    }

}
