package AsteroidsFinal.GameObjects.Ships;

import AsteroidsFinal.Control.AIController;
import AsteroidsFinal.Game.Game;
import AsteroidsFinal.Game.Sprite;
import AsteroidsFinal.GameObjects.Bullet;
import AsteroidsFinal.GameObjects.GameObject;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;

import static AsteroidsFinal.Game.Constants.*;

public class Saucer extends Ship {

    private static final int RADIUS = 100;
    public static Bullet bullet = null;
    public static int HP = 10;
    //Saucer default Variables
    private int saucerscore = 400;
    private int SCALE = 5;
    private AIController AIctrl; //provides an Action object in each frame
    private Image saucer1 = Sprite.ENEMY_SHIP_01;  //200 x 200 px


    public Saucer(Vector2D position, Vector2D velocity, double radius, int saucerhealth, AIController controller) {
        super(position, velocity, radius, controller.action());
        this.AIctrl = controller;
        HP = saucerhealth;
        this.AIctrl.theSaucer(this);
    }

    public static Saucer makeSaucer(PlayerShip playerShip, int HP) {
        return new Saucer(new Vector2D(RANDOM.nextDouble() * FRAME_WIDTH,
                RANDOM.nextDouble() * FRAME_HEIGHT), new Vector2D(0, 0), 10, HP, new SeekNShoot(playerShip));
    }

    public static Saucer makeEnemyBoss(PlayerShip playerShip, int HP) {
        return new Saucer(new Vector2D(FRAME_WIDTH / 2, 50), new Vector2D(0, 0), RADIUS / 3, HP, new EnemyBoss(playerShip));
    }

    //Saucer update method, adds particles and follows the player
    @Override
    public void update() {
        //Update saucer position and make particles
        super.update();
        AIctrl.update();
        if (action.shoot) {
            mkBullet(this);
        }
        Game.makeParticles(this, 0, 0);

    }

    @Override
    public void draw(Graphics2D g) {
        //Draw saucer
        Sprite ship = new Sprite(saucer1, position, new Vector2D(direction
        ).rotate(1.57), radius * SCALE, radius * SCALE);
        ship.draw(g);
    }

    //Can hit player if not invincible and can hit buller
    @Override
    public boolean canHit(GameObject other) {
        if (other.getClass() == PlayerShip.class && PlayerShip.INVINCIBLE) return false;
        if (System.currentTimeMillis() - PlayerShip.SPAWNTIMER > 5000) {
            return other instanceof PlayerShip;
        }
        return other instanceof Bullet;
    }

    @Override
    public void hit() {
        if (HP > 0) {
            HP--;
        } else {
            Game.explosion(this);
            //if random int equals 0, drops a random powerup at the same position the object died
            if (RANDOM.nextInt(35) == 0) {
                Game.dropPowerup(this);
            }
            this.dead = true;
            Game.adjScore(saucerscore);
            SoundManager.play(SoundManager.bangSmall);

        }
    }
}

/*

    Saucer(Controller ctrl) {
        super(new Vector2D(FRAME_WIDTH / 2, 50), new Vector2D(0, 0), RADIUS);
        this.ctrl = ctrl;
        direction = new Vector2D(0, 1);
        dead = false;
    }

    public void update() {
        position.x += ctrl.action().move_x;
        position.y += ctrl.action().move_y;
        if (ctrl.action().shoot_s) {
            mkBullet();
            ctrl.action().shoot_s = false;
        }
        super.update();
    }

    @Override
    public Bullet mkBullet() {
        bullet = new Bullet(new Vector2D(position), new Vector2D(velocity), new Vector2D(direction));
        bullet.radius = 5;
        bullet.position.addScaled(direction, 120);// avoid immediate collision with playerShip
        bullet.velocity.addScaled(direction, 300);

        SoundManager.play(SoundManager.beat1);
        return bullet;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform sauTransf = new AffineTransform();
        double rot = direction.angle() + Math.PI / 2;
        sauTransf.rotate(rot, position.x, position.y);
        sauTransf.translate(-saucer1.getWidth(null) / 2, -saucer1.getHeight(null) / 2);
        sauTransf.translate(position.x, position.y);
        for (int i = 0; i < LEVEL; i++){
            sauTransf.scale(radius * 2 / saucer1.getWidth(null), radius * 2 / saucer1.getWidth(null));
            g.drawImage(saucer1, sauTransf,null);
        }
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public boolean canHit(GameObject other) {
        return other.getClass() != Asteroid.class;
    }

    public void hit() {
        SoundManager.play(SoundManager.beat2);
        HP--;
        if (HP == 0) {
            dead = true;
            Game.bossfight = false; // leave boss fight
            nextLevel = true; // go to next level
        }
    }

    @Override
    public String toString() {
        return "Saucer";
    }
}
*/
