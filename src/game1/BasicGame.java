package game1;

import utilities.JEasyFrame;
import utilities.KeyController;

import java.util.ArrayList;
import java.util.List;
import static game1.Constants.DELAY;

public class BasicGame {
    public static final int N_INITIAL_ASTEROIDS = 5;
    public List<BasicAsteroid> asteroids;
    public BasicShip ship;
    public KeyController ctrl;



    public BasicGame(){
        asteroids = new ArrayList<>();
        for(int i = 0; i < N_INITIAL_ASTEROIDS; i++){
            asteroids.add(BasicAsteroid.makeRandomAsteroid());
        }
        ctrl = new KeyController();
        ship = new BasicShip(ctrl);
    }

    public static void main(String[] args) throws Exception {
        BasicGame game = new BasicGame();
        BasicView view = new BasicView(game);
        new JEasyFrame(view, "Basic Game").addKeyListener(game.ctrl);
        while (true){
            game.update();
            view.repaint();
            Thread.sleep(DELAY);
        }
    }

    public void update(){

        for (BasicAsteroid a : asteroids){
            a.update();
        }
        ship.update();
    }
}
