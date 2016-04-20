package View;

import Model.GameObjects.GameObject;
import Model.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by paraply on 2016-04-19.
 */
public class Render {
    private static Render render; // Used by getInstance
    private GraphicsContext context;
    private World world; // World model provides information about what should be drawn
    private GameObject gameObject;

    private boolean has_loaded_images;
    Image images[] = new Image[100];

    // graphics context = main-canvas context
    // this is set by WindowController in its initialization
    public void setGraphicsContext(GraphicsContext context){
        this.context = context;
    }

    public void setWorld(World world){
        this.world = world;
    }

    public void addGameObject(GameObject gameObject){
        this.gameObject = gameObject;
    }

    //This class is currently singleton, since its instance needs to be accessed by both WindowController and the Model. May change...
    public static synchronized Render getInstance(){
        if (render == null){
            render = new Render();
        }
        return render;
    }

    private void load_images(){
        if (!has_loaded_images){
            images[0] = new Image("grass.png");
            images[1] = new Image("border.png");
            images[2] = new Image("cat.png");
            has_loaded_images = true;
        }
    }

    public void redraw(){
        load_images();
        draw_ground_tiles();
        draw_objects();
    }

    private void draw_ground_tiles(){
        for (int y = 0; y < World.mapSize; y++){
            for (int x = 0; x < World.mapSize; x++){
                draw_tile(world.getTileMap()[x][y], x,y);
            }
        }
    }
    private void draw_objects(){
        draw_tile(2,gameObject.getX(),gameObject.getY());
    }


    private void draw_tile(int index, int tile_x, int tile_y){
        context.drawImage(images[index], tile_x * World.tileSize , tile_y * World.tileSize);
    }

}