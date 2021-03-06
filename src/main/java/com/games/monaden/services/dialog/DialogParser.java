package com.games.monaden.services.dialog;

import com.games.monaden.model.dialog.Dialog;
import com.games.monaden.model.dialog.DialogChoice;
import com.games.monaden.model.inventory.KeyItem;
import com.games.monaden.model.primitives.Point;
import com.games.monaden.model.primitives.Transition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.*;

/**
 Responsible for parsing dialog files from xml and constructing dialog objects from them.
 TODO: Should be possible to have several items being added
 */
public class DialogParser extends DefaultHandler{

    private boolean bDialog;
    private boolean bText;
    private boolean bResponse;
    private boolean bChoice;
    private boolean bRequirement;
    private boolean bSubDialog;
    private boolean bAvatar;
    private boolean bItemName;
    private boolean bItemDescription;
    private boolean bFileName;
    private boolean bNewPosition;

    private KeyItem item;
    private String itemName;
    private String itemDescription;

    private Transition transition;
    private String fileName;
    private Point newPosition;

    private Dialog root;
    private boolean gotRoot = false;

    private Dialog currentDialog;

    private Stack<Dialog> parents = new Stack<>();
    private String choiceText;
    private List<String> requirements = new ArrayList<>();

    @Override
    public void startElement (String uri, String localName, String qName,
                              Attributes attributes) throws SAXException {
        switch (qName.toLowerCase()) {
            case "dialog":
                bDialog = true;
                break;
            case "image":
                bAvatar = true;
                break;
            case "text":
                bText = true;
                break;
            case "response":
                bResponse = true;
                break;
            case "choice":
                bChoice = true;
                break;
            case "subdialog":
                bSubDialog = true;
                break;
            case "keyitem":
                bRequirement = true;
                break;
            case "itemname":
                bItemName = true;
                break;
            case "description":
                bItemDescription = true;
                break;
            case "filename":
                bFileName = true;
                break;
            case "newposition":
                bNewPosition = true;
                break;
        }
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException, IllegalArgumentException {
        if (bDialog) {
            DialogChoice child = new DialogChoice(new Dialog(), "");
            if (currentDialog != null) {
                currentDialog.setChild(child);
                parents.push(currentDialog);
            }
            if (!gotRoot) {
                gotRoot = true;
                root = child.getDialog();
            }
                currentDialog = child.getDialog();
            bDialog = false;
        } else if (bAvatar) {
            currentDialog.setImageFile(new File(new String(ch, start, length)));
            bAvatar = false;
        } else if(bText) {
                currentDialog.setDialogText(new String(ch, start, length));
            bText = false;
        } else if (bResponse) {

            bResponse = false;
        } else if (bChoice) {
            choiceText = new String(ch, start, length);
            bChoice = false;
        } else if (bSubDialog) {
            if (!gotRoot) {
                throw new IllegalArgumentException("subDialog: gotRoot wtf");
            }

            DialogChoice child = new DialogChoice(new Dialog(), choiceText);
            for(String s : requirements){
                child.addRequirement(s);
            }
            currentDialog.addChoice(child);
            parents.push(currentDialog);
            currentDialog = child.getDialog();
            requirements.clear();

            bSubDialog = false;
        } else if(bRequirement){
            requirements.add(new String(ch, start, length));
            bRequirement = false;
        } else if(bItemName){
            itemName = new String(ch, start, length);
            bItemName = false;
        } else if(bItemDescription){
            itemDescription = new String(ch, start, length);
            bItemDescription = false;
        } else if(bFileName){
            fileName = new String(ch, start, length);
            bFileName = false;
        } else if(bNewPosition){
            String [] point = new String(ch, start, length).split(",");
            newPosition = new Point(Integer.parseInt(point[0])
                    , Integer.parseInt(point[1]));
            bNewPosition = false;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName) throws SAXException{
        switch (qName.toLowerCase()) {
            case "dialog":
                if(item != null){
                    currentDialog.setItem(item);
                    item = null;
                }
                if(transition != null){
                    currentDialog.setTransition(transition);
                    transition = null;
                }
                //TODO: Create dialog object, link to parent
                if (!parents.empty()) {
                    currentDialog = parents.pop();
                }
                break;
            case "subdialog":
                if (!parents.empty()) {
                    currentDialog = parents.pop();
                }
                if(item != null){
                    currentDialog.setItem(item);
                    item = null;
                }
                break;
            case "additem":
                item = new KeyItem(itemName, itemDescription);
                break;
            case "transition":
                transition = new Transition(new Point(0,0), newPosition, fileName);
                break;
        }
    }

    /**
     * Returns the start of the parsed dialog
     * @return root of the dialog tree
     */
    public Dialog getRoot () {
        return this.root;
    }
    public void reset () {
        gotRoot = false;
        root = null;
        currentDialog = null;
        parents = new Stack<>();
        choiceText = null;
        transition = null;
        item = null;
        requirements.clear();
    }
}
