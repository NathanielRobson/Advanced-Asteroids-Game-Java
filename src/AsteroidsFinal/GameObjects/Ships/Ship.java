package AsteroidsFinal.GameObjects.Ships;

import AsteroidsFinal.Control.Action;
import AsteroidsFinal.Control.KeyController;
import AsteroidsFinal.Game.Constants;
import AsteroidsFinal.GameObjects.Bullet;
import AsteroidsFinal.GameObjects.GameObject;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;

import static AsteroidsFinal.Game.Game.createdParticles;
import static AsteroidsFinal.Game.Game.createdPowerups;
import static utilities.SoundManager.fire;
import static utilities.SoundManager.play;

public abstract class Ship extends GameObject {

    private static final double STEER_RATE = Math.PI / 40.0;
    private static final double MAG_ACC = 400;
    private static final double DRAG = 0.011;
    public static final double LOSS = 0.9999;
    public static Bullet bullet = null;
    //Delay between bullet shots, will be adjusted when picking up chainfire.
    private static long SHOT_DELAY = 220;
    public ArrayList<Bullet> bullets;
    public Vector2D direction;
    public boolean thrusting = false;
    public boolean canShoot;
    public transient KeyController keys;
    public transient long lastShot = System.currentTimeMillis();
    protected Action action;

    public Ship(Vector2D position, Vector2D velocity, double radius, Action action) {
        super(position, velocity, radius);
        direction = new Vector2D(0, -1);
        this.action = action;
        resetShip();
    }

    //Reset ship
    public void resetShip() {
        canShoot = true;
        createdParticles = new ArrayList<>();
        createdPowerups = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    //update
    public void update() {
        //update the ships position and velocity.
        position.addScaled(velocity, Constants.DT);
        position.wrap(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT - 110);
        direction.rotate(action.turn * STEER_RATE);
        velocity.addScaled(direction, action.thrust * MAG_ACC * Constants.DT);
        velocity.mult(LOSS);
        thrusting = (action.thrust > 0);
        drag();
    }

    //mkBullet method for each enemy and player
    public void mkBullet(GameObject object) {
        Color ColA;
        int BULLET_SIZE;
        if (object instanceof Saucer) {
            ColA = Color.GREEN;
            BULLET_SIZE = 5;
        } else {
            ColA = Color.RED.brighter();
            BULLET_SIZE = 3;
        }
        if (!canShoot) return;
        long delayTime = System.currentTimeMillis();
        if (action.shoot && (delayTime - lastShot > SHOT_DELAY)) {
            action.shoot = false;
            play(fire);
            bullet = new Bullet(new Vector2D(position), new Vector2D(velocity), new Vector2D(direction), ColA, BULLET_SIZE);
            bullet.position.addScaled(direction, (radius + bullet.radius) * 1.1);
            bullet.velocity.addScaled(direction, Bullet.INITIAL_SPEED);
            lastShot = delayTime;
        }
        if (System.currentTimeMillis() - lastShot > 220) canShoot = true;
    }

    //drags the player to allow for a normal feel ingame
    public void drag() {
        if (this.velocity.x != 0) {
            this.velocity.subtract(this.velocity.x * DRAG, 0);
        }
        if (this.velocity.y != 0) {
            this.velocity.subtract(0, this.velocity.y * DRAG);
        }
    }

    //dead boolean
    @Override
    public boolean dead() {
        return dead;
    }
}
