package View;

import Model.GameObjects.GameObject;
import Model.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by mike on 2016-04-21.
 */
public class RenderObject {
    private GameObject gameObject;
    private GraphicsContext context;
    private Image image;
    private final int IMAGE_HEIGHT = 32, IMAGE_WIDTH = 32;
    private int animation_part = 0;
    private int x,y;

    public RenderObject(GameObject gameObject, GraphicsContext graphicsContext, String imageSection, String imageName){
        this.gameObject = gameObject;
        context = graphicsContext;
        image = new Image( imageSection + "/" + imageName + ".png");
    }

    public void draw(){


        int image_x_src, image_y_src ;
        switch (gameObject.getDirection()){
            case BACK:
                image_y_src = IMAGE_HEIGHT * 3;
                break;
            case LEFT:
                image_y_src = IMAGE_HEIGHT;
                break;
            case RIGHT:
                image_y_src = IMAGE_HEIGHT * 2;
                break;
            default: //FRONT
                image_y_src = 0;
        }

        if (gameObject.getTransitionTicks() > 0){
            image_x_src = (animation_part % 3) * IMAGE_WIDTH;
            if (gameObject.getTransitionTicks() % 4 == 0){
                animation_part++;
            }
            switch (gameObject.getDirection()){
                case BACK: y =  (gameObject.getY() * World.tileSize)   + gameObject.getTransitionTicks() ;
                    break;
                case FRONT: y =  (gameObject.getY() * World.tileSize)   - gameObject.getTransitionTicks();
                    break;
                case LEFT: x =  (gameObject.getX() * World.tileSize) +  gameObject.getTransitionTicks();
                    break;
                case RIGHT: x =  (gameObject.getX() * World.tileSize)  - gameObject.getTransitionTicks();
                    break;
            }

        }else{
            image_x_src = 0;
            x = gameObject.getX() * World.tileSize;
            y =  gameObject.getY() * World.tileSize;
        }

        context.drawImage(image, image_x_src,image_y_src, IMAGE_HEIGHT, IMAGE_HEIGHT, x, y, IMAGE_HEIGHT, IMAGE_HEIGHT);
    }
}
