package AsteroidsFinal.GameObjects.Ships;

import AsteroidsFinal.Control.KeyController;
import AsteroidsFinal.Game.Constants;
import AsteroidsFinal.Game.Game;
import AsteroidsFinal.Game.Sprite;
import AsteroidsFinal.GameObjects.Asteroid;
import AsteroidsFinal.GameObjects.Bullet;
import AsteroidsFinal.GameObjects.GameObject;
import AsteroidsFinal.GameObjects.Powerups.Powerups;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.io.Serializable;

import static AsteroidsFinal.Game.Constants.*;
import static utilities.SoundManager.*;

public class PlayerShip extends Ship implements Serializable {

    private static final int RADIUS = 10;
    public static long SPAWNTIMER;
    public static boolean INVINCIBLE = false;
    public static boolean CHAINFIRE = false;
    public static int LIVES;
    public static int SCALE = 5;
    private static Vector2D initPos = new Vector2D(Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT / 2);
    private static Vector2D initDir = new Vector2D(0, -1);
    private static Image ship = Sprite.SHIP_01;
    private static Image shipshield = Sprite.SHIP_SHIELD;
    private static Image jetstream = Sprite.THRUSTER_01;
    public HelperPod pod = null;
    int[] XP = new int[]{0, 20, 15, 10, 5, -5, -10, -15, -20};
    int[] YP = new int[]{-20, 15, 20, 15, 25, 25, 15, 20, 15};
    int[] XPTHRUST = new int[]{-5, 5, 10, 0, -10};
    int[] YPTHRUST = new int[]{30, 30, 40, 35, 40};
    PlayerShip playerShip;

    private int INVcounter = 0;
    private int CHAcounter = 0;

    //PlayerShip constructor, superclass Ship
    public PlayerShip(Vector2D position, Vector2D velocity, double radius, KeyController keys) {
        super(position, velocity, radius, keys.action());
        this.keys = keys;
        this.radius = RADIUS;
        direction = new Vector2D(0, -1);
        //Spawntimer and lives
        LIVES = 3;
        //indicates when the player was made to allow for spawn protection
        SPAWNTIMER = System.currentTimeMillis();
    }

    //if the players ship is hit
    @Override
    public void hit() {
        //if player is hit and has more than 0 lives
        if (LIVES > 0) {
            hitShipsound();
            //score decrement by -200
            Game.adjScore(-200);
            //explode particles
            Game.explosion(this);
            //play bang sound
            SoundManager.play(bangLarge);
            this.dead = true;
            //lives decrement
            LIVES--;
            //respawn
            respawn();
        } else this.dead = true;
    }

    //Playership update method
    public void update() {
        super.update();

        //If chainfire power inactive, and action shoot then make bullet
        if (!CHAINFIRE && action.shoot) {
            mkBullet(this);

            //If CHAINFIRE true mkChainfirebullet for 180 loops
        } else if (CHAINFIRE) {
            CHAcounter++;
            if (CHAcounter < 180) {
                mkChainBullet();
            }
            if (CHAcounter > 180) {
                CHAcounter = 0;
                CHAINFIRE = false;
            }

        }
        //Spawn protection decrement timer
        SPAWNTIMER--;
        //Game Lost
        if (LIVES == 0) {
            this.dead = true;
            Game.gameoverString();
        }

        //Game Won
        if (Game.LEVEL >= 5) {
            this.dead = true;
            Game.winnerString();
        }

        //If lives go into minus this will fix that (rare occurance)
        if (LIVES < 0) {
            LIVES = 0;
        }

        //Invincible counter for powerup
        if (INVINCIBLE) {
            INVcounter++;
            if (INVcounter > 300) {
                INVcounter = 0;
                INVINCIBLE = false;
            }
        }

        //if player presses T, teleport to random position
        if (action.teleport) {
            teleportPlayer();
        }


        //make particles behind the players ship
        if (thrusting) {
            startThrust();
            Game.makeParticles(this, 10, 20);
        } else stopThrust();
        //drag the player slightly
        drag();
    }

    //method to create the pod helper
    public void mkPod(Game game) {

    }

    //Chainfire bullet method, removes time constraints of mkbullet method
    private void mkChainBullet() {
        action.shoot = false;
        play(fire);
        bullet = new Bullet(new Vector2D(position), new Vector2D(velocity), new Vector2D(direction), Color.CYAN, 2);
        bullet.position.addScaled(direction, (radius + bullet.radius) * 1.1);
        bullet.velocity.addScaled(direction, Bullet.INITIAL_SPEED);
    }

    //Respawn the ship
    public void respawn() {
        this.dead = false;
        SPAWNTIMER = System.currentTimeMillis();
        direction.set(new Vector2D(initDir));
        position.set(new Vector2D(initPos));
        velocity.set(new Vector2D(0, 0));
    }

    //Draw method to draw the original ship
    private void drawSprite(Graphics2D g) {
        Sprite imgship = new Sprite(ship, position, new Vector2D(direction
        ).rotate(1.57), radius * SCALE, radius * SCALE);
        imgship.draw(g);
        if (thrusting) {
            //If thrusting draw the jetstream image on top of the players ship.
            Sprite imgthrust = new Sprite(jetstream, position,
                    new Vector2D(direction).rotate(1.57), jetstream.getWidth(null) * 0.50, jetstream.getHeight(null) * 0.45);
            imgthrust.draw(g);

        }
    }

    //Teleport the player ship when pressing the T key
    private void teleportPlayer() {
        position.x = RANDOM.nextInt(FRAME_WIDTH);
        position.y = RANDOM.nextInt(FRAME_HEIGHT);

    }

    //If the player has a shield (Spawn Protection)
    private void drawShield(Graphics2D g2) {
        Sprite imgship = new Sprite(shipshield, position, new Vector2D(direction
        ).rotate(1.57), radius * SCALE, radius * SCALE);
        imgship.draw(g2);
        //if the player is thrusting
        if (thrusting) {
            Sprite imgthrust = new Sprite(jetstream, new Vector2D(position.x, position.y),
                    new Vector2D(direction).rotate(1.57), jetstream.getWidth(null) * 0.50, jetstream.getHeight(null) * 0.45);
            imgthrust.draw(g2);

        }
    }

    //Draws an original looking ship with thrusters

    //Unused, but still works
    /*public void drawPoly(Graphics2D g){
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(0.8,0.8);
        g.setColor(Color.BLUE);
        g.drawPolygon(XP, YP, XP.length);
        if (thrusting) {
            g.setColor(Color.orange);
            g.drawPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
        }
        g.setTransform(at);

    }*/

    //Draws either thrusting ship, normal ship, shield ship thrusting, or shield ship not thrusting.
    public void draw(Graphics2D g) {
        if (LIVES > 0) {
            drawSprite(g);
            //drawPoly(g);
            if (System.currentTimeMillis() - SPAWNTIMER < 5000 || INVINCIBLE) {
                drawShield(g);
            }
        }
    }

    //Can hit everything when not invincible and not just spawned
    @Override
    public boolean canHit(GameObject other) {
        if (INVINCIBLE) {
            //Invincible, cannot die
            return other instanceof Powerups;

        } else {
            //Spawn protection timer
            if (System.currentTimeMillis() - SPAWNTIMER > 5000) {
                return other instanceof Asteroid || other instanceof Powerups || other instanceof Saucer || other instanceof Bullet;
            } else return other instanceof Powerups;
        }
    }
}
