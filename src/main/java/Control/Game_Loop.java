package Control;

import Model.World;
import View.Render;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

/**
 * Created by paraply on 2016-04-13.
 */
public class Game_Loop extends AnimationTimer {

    // Could probably use inspiration from
    // https://carlfx.wordpress.com/2012/04/09/javafx-2-gametutorial-part-2/

    private final int COUNTDOWN = 20; //Its the final countdown
    private int counting_down = COUNTDOWN;

    private World world;

    public void initialize_game(){
        world = new World();
        Render.getInstance().setWorld(world);
    }

    @Override
    public void handle(long now) {

        if (counting_down > 0){
            counting_down--;
        }else{
            User_Input user_input = User_Input.getInstance();
            KeyCode latest_movement_request = user_input.getLatestMovementKey();
            if (latest_movement_request != null) {
                switch (latest_movement_request) {
                    case UP:
                        System.out.println("MOVE UP");
                        break;
                    case DOWN:
                        System.out.println("MOVE DOWN");
                        break;
                    case LEFT:
                        System.out.println("MOVE LEFT");
                        break;
                    case RIGHT:
                        System.out.println("MOVE RIGHT");
                        break;
                }
            }

            KeyCode latest_function_request = user_input.getLatestFunctionKey();
            if (latest_function_request != null) {
                switch (latest_function_request) {
                    case ESCAPE:
                        System.out.println("ESCAPE");
                        System.exit(0);
                        break;
                }
            }

            Render.getInstance().redraw();
            counting_down = COUNTDOWN;
        }
    }
}
