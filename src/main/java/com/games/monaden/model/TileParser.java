package com.games.monaden.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Philip on 2016-04-21.
 * Parses
 */
public class TileParser extends DefaultHandler {
    private boolean bName = false;
    private boolean bGraphics = false;
    private boolean bSolid = false;

    private int id;
    private String name;
    private File filepath;
    private boolean solidness = false;

    private List<Tile> tileList = new ArrayList<>();

    @Override
    public void startElement (String uri, String localName, String qName,
                              Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("tile")) {
            id = Integer.parseInt(attributes.getValue("id"));
        } else if (qName.equalsIgnoreCase("name")) {
            bName = true;
        } else if (qName.equalsIgnoreCase("graphics")) {
            bGraphics = true;
        } else if (qName.equalsIgnoreCase("solidness")) {
            bSolid = true;
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("tile")) {
            createTile();
        }
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException {
        if (bName) {
            this.name = new String(ch, start, length);
//            System.out.println("Name found: " + name);
            bName = false;
        } else if (bGraphics) {
            this.filepath = new File(new String (ch, start, length));
//            System.out.println("Filepath found: " + filepath);
            bGraphics = false;
        } else if (bSolid) {
            if ("solid".equalsIgnoreCase(new String(ch, start, length))) {
                this.solidness = true;
            }
//            System.out.println("Solidness found: " + solidness);
            bSolid = false;
        }
    }

    /**
     * Should be called after fully parsing one tile
     */
    private void createTile () {
        try {
            tileList.add(new Tile(id, name, solidness, filepath));
        } catch (NullPointerException e) {
            System.err.println("Something was missing when creating a tile");
        }
    }

    /**
     * Should be called after fully parsing all tiles from a file.
     * @return a sorted copy of the list of all parsed tiles.
     */
    public List<Tile> getTiles () {
        Collections.sort(tileList);
        return new ArrayList<>(tileList);
    }
}
