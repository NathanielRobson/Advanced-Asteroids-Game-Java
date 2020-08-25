package AsteroidsFinal.GameObjects.Ships;

import AsteroidsFinal.Control.AIController;
import AsteroidsFinal.Control.Action;
import AsteroidsFinal.Game.Game;

public class EnemyBoss extends AIController {
    private static int counter = 0;
    private Action action = new Action();
    private PlayerShip playerShip;
    private boolean top;
    private boolean bottom;

    //Saucer saucer;
    public EnemyBoss(PlayerShip target) {
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

    //Updates the Boss, follows a line and changes position and shoots at the player
    //As the health gets lower, the boss fires more bullets at a faster rate
    @Override
    public void update() {
        double ydist = playerShip.position.y - saucer.position.y;
        double xdist = playerShip.position.x - saucer.position.x;
        double angle = Math.atan2(ydist, xdist);

        this.saucer.direction.y = Math.sin(angle);
        this.saucer.direction.x = Math.cos(angle);
        this.saucer.velocity.x = 5;
        this.saucer.position.x += 3;

        if (counter % 510 == 0) {
            bottom = true;
            top = false;
        }
        if (counter % 240 == 0) {
            top = true;
            bottom = false;
        }
        if (top) {
            this.saucer.position.y = 140;
        }
        if (bottom) {
            this.saucer.position.y = 500;
        }

        counter++;

        int hp = 80;
        if (Saucer.HP >= 70) {
            action.shoot = counter % 70 == 0;
        } else if (Saucer.HP * 0.25 < hp) {  // 25 percent hp
            action.shoot = counter % 10 == 0;

        } else if (Saucer.HP * 0.5 < hp) {  // 50 percent hp
            action.shoot = counter % 20 == 0;

        } else if (Saucer.HP * 0.8 < hp) {  // 80 percent hp
            action.shoot = counter % 30 == 0;
        }
    }
}