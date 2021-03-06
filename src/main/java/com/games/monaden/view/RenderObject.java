package com.games.monaden.view;

import com.games.monaden.model.gameobject.GameObject;
import com.games.monaden.model.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * This class draws objects without animations on the screen.
 * It is a superclass to AnimatedObject that handles animations.
 *
 * There should only be one instance of this class that is handled by Render.
 */

class RenderObject {
    GameObject gameObject;
    private Image image;
    int x,y;                        // objects position in the world
    int imageSrcX;                  // Coordinates to get a specific picture
    private int imageSrcY;          // from the tileset
    private GraphicsContext context;
    private boolean initFailed;

    // Create a new instance of the RenderObject
    RenderObject(GameObject gameObject, GraphicsContext context){
        try {
            this.gameObject = gameObject;
            image = new Image(gameObject.getImagePath());
            this.context = context;
        }catch (Exception e){
            System.err.println("RenderObject: Constructor cannot create image: " + gameObject.getImagePath());
            initFailed = true;
        }
    }

    int zOrder(){
        return gameObject.getzOrder();
    }

    // x,y values specifies where in the world the character should be drawn
    private void calculateWorldCoordinates(){
        x = gameObject.getPosition().getX() * World.TILE_SIZE;
        y =  gameObject.getPosition().getY() * World.TILE_SIZE;
    }

    // The X value from the source image is always 0 in an RenderObject without animation.
    private void calculateSourceX(){
        imageSrcX = 0;
    }

    // The Y value depends on which direction the object is facing.
    // If the object is stationary then it will default to downwards
    // and use the top image from the tileset.
    void calculateSourceY(){
        switch (gameObject.getDirection()){
            case LEFT:
                imageSrcY = gameObject.getHeight();
                break;
            case RIGHT:
                imageSrcY = gameObject.getHeight() * 2;
                break;
            case UP:
                imageSrcY = gameObject.getHeight() * 3;
                break;
            default: //DOWN
                imageSrcY = 0;
        }
    }

    // Calculate coordinates and draw to context
    public void draw(){
        if (initFailed){
            return;
        }
        calculateWorldCoordinates();
        calculateSourceX();
        calculateSourceY();
        drawToContext();
    }

    // Call JavaFX canvas to draw the image on the canvas
    void drawToContext(){
        if (context == null){
            return;
        }
        context.drawImage(image, imageSrcX, imageSrcY, gameObject.getWidth(), gameObject.getHeight(), x, y, gameObject.getWidth(), gameObject.getHeight());
    }


}
