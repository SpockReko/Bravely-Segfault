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
    private final List<DialogChoice> choices;
    private File imageFile;

    public int getChoiceCount(){return choices.size();}

    /**
     * Constructor for when there is no text yet. Text is to be added later.
     */
    public Dialog(){
        this.choices = new ArrayList<>();
    }

    public Dialog(String dialogText){
        this.dialogText = dialogText;
        this.choices = new ArrayList<>();
    }
    
    public void addChoice(DialogChoice dc){
        this.choices.add(dc);
    }
    
    public Dialog makeAChoice(int id, Inventory inventory){
        DialogChoice d = choices.get(id);
        if(d.reqSatisfied(inventory)){
            return d.getDialog();
        }
        return null;
    }
    
    public String getChoiceText(int id){
        return choices.get(id).getChoiceText();
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
    public void setChild (DialogChoice child) {
        choices.add(child);
    }

    
    public String toString(){
        String result = getDialogText();
        for(int i = 0; i < getChoiceCount(); i++){
            result += "\n*" + getChoiceText(i);
        }
        return result;
    }
}
