package AsteroidsFinal.GameObjects.Ships;

import AsteroidsFinal.Control.Action;
import AsteroidsFinal.Control.Controller;

//Basic rotate and shoot ai class
public class RotateNShoot implements Controller {
    private Action action = new Action();

    @Override
    public Action action() {
        action.shoot = false;
        action.turn = 1;
        return action;
    }

    @Override
    public Action getLastAction() {
        return null;
    }
}