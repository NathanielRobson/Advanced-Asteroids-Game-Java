package AsteroidsFinal.GameObjects.Ships;


import AsteroidsFinal.Control.AIController;
import AsteroidsFinal.Control.Action;
import AsteroidsFinal.Game.Game;

public class SeekNShoot extends AIController {
    private Action action = new Action();
    private PlayerShip playerShip;

    //Saucer saucer;
    public SeekNShoot(PlayerShip target) {
        super(target); // constructor
        this.playerShip = Game.playerShip;
    }

    @Override
    public Action action() {
        return this.action;
    }

    @Override
    public Action getLastAction() {
        return null;
    }

    //Seeks and shoots the player when it gets close enough
    @Override
    public void update() {
        double ydist = (playerShip.position.y - saucer.position.y);
        double xdist = (playerShip.position.x - saucer.position.x);
        double angle = Math.atan2(ydist, xdist);

        if (Math.abs(ydist) > 200 || Math.abs(xdist) > 200) {
            saucer.velocity.y = Math.sin(angle) * 150;
            saucer.velocity.x = Math.cos(angle) * 150;
        } else {
            this.action.shoot = true;
        }
        saucer.direction.y = Math.sin(angle);
        saucer.direction.x = Math.cos(angle);
    }
}