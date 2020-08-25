package AsteroidsFinal.GameObjects;

import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import AsteroidsFinal.GameObjects.Ships.Saucer;
import utilities.Vector2D;

import java.awt.*;


public class Bullet extends GameObject {

    public static final double INITIAL_SPEED = 300;
    public static final int TIME_TO_LIVE = 110;
    public static final int RADIUS = 3;
    private int TTL;
    private int size;
    private Color color;

    public Bullet(Vector2D position, Vector2D velocity, Vector2D direction, Color color, int size) {
        super(position, velocity, RADIUS);
        this.TTL = TIME_TO_LIVE;
        this.size = size;
        this.color = color;
    }

    //bullet update method
    @Override
    public void update() {
        super.update();
        TTL--;
        if (TTL <= 0) {
            dead = true;
        }
    }

    //draws the default bullet
    @Override
    public void draw(Graphics2D g) {
        int x = (int) position.x;
        int y = (int) position.y;
        g.setColor(color);
        g.fillOval((x - size), (y - size), (2 * size), (2 * size));

    }


    //dead boolean returns the time to live value of the bullet
    @Override
    public boolean dead() {
        return TTL <= 0;
    }

    //which objects can it hit
    @Override
    public boolean canHit(GameObject other) {
        //Can hit player if not invincible
        if (System.currentTimeMillis() - PlayerShip.SPAWNTIMER > 5000) {
            if (other instanceof PlayerShip) {
                return !PlayerShip.INVINCIBLE;
            }
        }
        //Can hit asteroid or saucer
        return other instanceof Asteroid || other instanceof Saucer;

    }

    //if hit, time to live = 0 to kill bullet
    @Override
    public void hit() {
        TTL = 0;
    }
}
