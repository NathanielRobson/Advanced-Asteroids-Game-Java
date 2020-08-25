package AsteroidsFinal.GameObjects.Ships;

import AsteroidsFinal.Control.Action;
import AsteroidsFinal.Control.EnemyController;
import AsteroidsFinal.Control.SeekNShootTarget;
import AsteroidsFinal.Game.Game;
import AsteroidsFinal.Game.Sprite;
import AsteroidsFinal.GameObjects.Bullet;
import AsteroidsFinal.GameObjects.GameObject;
import utilities.Vector2D;

import java.awt.*;

import static AsteroidsFinal.Game.Constants.*;

public class EnemyShip extends Ship {

    private static final int RADIUS = 30;
    private static final double SHIP_VMAX = 200;
    static Action action;
    private static int flag;
    private static EnemyController ctrl;
    private static Game game;
    public boolean fleeing;
    public Vector2D position;
    public Vector2D velocity;
    private int hp;
    private Vector2D d;

    public EnemyShip(Game game, EnemyController ctrl, Action action) {
        super(new Vector2D(new Vector2D()), new Vector2D(new Vector2D()), RADIUS, action);
        EnemyShip.action = action;
        dead = false;
        EnemyShip.game = game;
        hp = 100;
        fleeing = false;
        EnemyShip.ctrl = ctrl;
        position = new Vector2D();
        velocity = new Vector2D();
        d = new Vector2D();
        flag = 0;
        reset();
    }

    public static EnemyShip makeEnemyShip() {
        return new EnemyShip(game, new SeekNShootTarget(), action);
    }

    //reset enemy ship
    public void reset() {
        position = new Vector2D(RANDOM.nextInt(FRAME_WIDTH), RANDOM.nextInt(FRAME_HEIGHT));
        velocity = new Vector2D(0, 0);
        d = new Vector2D(0, -1);
        d.normalise();
    }

    @Override
    public void update() {
        super.update();
        if (action.shoot) {
            if (flag == 10) {
                flag = 0;
                shootBullet();
            } else flag++;
        }
    }

    //Draws the enemy ship
    @Override
    public void draw(Graphics2D g) {
        Sprite ship = new Sprite(Sprite.ENEMY_SHIP_01, position, new Vector2D(d
        ).rotate(1.57), RADIUS, RADIUS);
        ship.draw(g);
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public boolean canHit(GameObject other) {
        if (other.getClass() == PlayerShip.class && PlayerShip.INVINCIBLE) return false;
        if (System.currentTimeMillis() - PlayerShip.SPAWNTIMER > 5000) {
            return other instanceof PlayerShip;
        }
        return other instanceof Bullet;
    }

    //Fires a bullet
    private void shootBullet() {
        Bullet b = new Bullet(new Vector2D(position), new Vector2D(velocity).addScaled(d, SHIP_VMAX), d, Color.ORANGE, 4);
        // make it clear the ship
        b.position.addScaled(b.velocity, (RADIUS + b.radius) * 1.5 / b.velocity.mag());
        Game.objects.add(b);
    }

    //If the enemy ship is hit
    @Override
    public void hit() {
        hp -= 1;
        if (hp <= 0) {
            dead = true;

        }
    }
}