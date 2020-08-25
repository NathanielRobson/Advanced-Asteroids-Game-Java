package AsteroidsFinal.Control;

import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import AsteroidsFinal.GameObjects.Ships.Saucer;

public abstract class AIController implements Controller {

    protected Action action;
    protected Saucer saucer;
    protected PlayerShip target;

    //provides the AI a target to follow (player)
    public AIController(PlayerShip target) {
        this.target = target;
        this.action = new Action();
    }

    public Action action() {
        return action;
    }

    public void theSaucer(Saucer saucer) {
        this.saucer = saucer;
    }

    public void update() {
    }


}
