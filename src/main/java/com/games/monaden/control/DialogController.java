package com.games.monaden.control;

import com.games.monaden.model.dialog.Dialog;
import com.games.monaden.model.inventory.Inventory;
import com.games.monaden.view.Render;
import javafx.scene.input.KeyCode;

import java.util.Observable;

/**
 Class is responsible for any input concerning dialogs.

 */
public class DialogController extends Observable{
    private Dialog currentDialog;
    private Inventory inventory = new Inventory();
    private int selection = 0;

    public void setCurrentDialog(Dialog d){currentDialog = d;}

    public boolean handleMovement(KeyCode moveReq){
        if (moveReq != null && currentDialog.getChoiceCount(inventory) != 0) {
            if(moveReq == KeyCode.UP){
                selection = currentDialog.selectUp(selection, inventory);
                Render.getInstance().getDialog().select(selection);
                return true;
            }
            else if(moveReq == KeyCode.DOWN) {
                selection = currentDialog.selectDown(selection, inventory);
                Render.getInstance().getDialog().select(selection);
                return true;
            }
        }

        return false;

    }

    public boolean handleSpecial(KeyCode funcReq){
        if (funcReq != null) {
            if(funcReq == KeyCode.SPACE) {
                if(currentDialog.getChoiceCount(inventory) == 0){
                    Render.getInstance().getDialog().hideDialog();
                    return true;
                }
                else {
                    Dialog temp = currentDialog.makeAChoice(selection, inventory);
                    if (temp.getDialogText().equals("")) {
                        Render.getInstance().getDialog().hideDialog();
                        return true;
                    } else {
                        startDialog(temp);
                    }
                }
            }
        }
        return false;
    }

    //Should be called whenever a dialog is started (subdialogs as well)
    //Otherwise dialog transitions and item additions will not be handled properly
    public void startDialog(Dialog dialog) {
        currentDialog = dialog;
        if(dialog.getItem() != null){
            inventory.addItem(dialog.getItem());
        }
        if(dialog.getTransition() != null){
            setChanged();
            notifyObservers(dialog.getTransition());
            Render.getInstance().getDialog().hideDialog();
        }
        else {
            selection = 0;
            Render.getInstance().getDialog().newDialog(dialog, inventory);
            if(!inventory.getItemList().isEmpty()) System.out.println(inventory.toString());
        }
    }
}
