/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.games.monaden.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stefan
 */
public class Dialog {
    
    private String dialogText;
    private final List<String> text;
    private final List<Dialog> choices;
    private File imageFile;

    public int getChoiceCount(){return choices.size();}

    /**
     * Constructor for when there is no text yet. Text is to be added later.
     */
    public Dialog(){
        this.text = new ArrayList<>();
        this.choices = new ArrayList<>();
    }

    public Dialog(String dialogText){
        this.dialogText = dialogText;
        this.text = new ArrayList<>();
        this.choices = new ArrayList<>();
    }
    
    public void readInChoices(String text, Dialog dialog){
        this.text.add(text);
        this.choices.add(dialog);
    }
    
    public Dialog makeAChoice(int id){
        return choices.get(id);
    }
    
    public String getChoiceText(int id){
        return text.get(id);
    }

    public String getDialogText(){
        return dialogText;
    }
    
    public void setDialogText(String text) {
        this.dialogText = text;
    }

    public void setImageFile (File file) {
        this.imageFile = file;
    }

    public File getImageFile(){
        return imageFile;
    }

    /**
     * Sets a single child for when there is no choice.
     * @param child
     */
    public void setChild (Dialog child) {
        choices.add(child);
    }

    /**
     * Used to move to the next child. Meant to be used if there is only one child (no choices).
     * @return
     */
    public Dialog traverse () {
        return choices.get(0);
    }

    public String toString(){
        String result = getDialogText();
        for(int i = 0; i < getChoiceCount(); i++){
            result += "\n*" + getChoiceText(i);
        }
        return result;
    }
}
