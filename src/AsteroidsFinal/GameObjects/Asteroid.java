package AsteroidsFinal.GameObjects;

import AsteroidsFinal.Game.Constants;
import AsteroidsFinal.Game.Game;
import AsteroidsFinal.Game.Sprite;
import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import static AsteroidsFinal.Game.Constants.*;

public class Asteroid extends GameObject {

    public static final double MAX_SPEED = 120;
    private static final int RADIUS1 = 35;
    private static final int RADIUS2 = 30;
    public static double RADIUS = 40;
    public static List<Asteroid> smallAsteroids = new ArrayList<>();
    static Asteroid ast;
    private static int ASTEROID_VALUE = 100;
    private static double x;
    private static double y;
    private static double velX;
    private static double velY;
    public boolean attract = false;
    private Image asteroidimage = Sprite.ASTEROID1;  // 26 x 26
    private double mass;
    private double density;

    public Asteroid(Vector2D position, Vector2D velocity, double radius) {
        super(position, velocity, radius);
    }

    public static Asteroid makeRandomAsteroid(int radius) {
        return new Asteroid(new Vector2D((RANDOM.nextDouble() * (FRAME_WIDTH - 2 * radius)),
                (RANDOM.nextDouble() * (FRAME_HEIGHT - 2 * radius))), new Vector2D((RANDOM.nextDouble() * MAX_SPEED * (-2) + 120),
                (RANDOM.nextDouble() * MAX_SPEED * (-2) + 120)), radius);
    }

    //asteroid interact
    public static void asteroidInteract1(Asteroid other) {

        if (ast.position.dist(other.position) > 50) {
            ast.velocity.rotate(Math.PI / 2);
        }
    }

    private static Vector2D randomVelocity() {
        return Vector2D.polar(Math.random() * 2 * Math.PI,
                Math.abs(Constants.RANDOM.nextGaussian() * 20));
    }


    //asteroid interact method, asteroids interact with one another to adjust their velocity vector
    public static void asteroidInteract(GameObject object, GameObject object2) {
        double distance, objx, objy, obj2x, obj2y;
        double dx = 0;
        double dy = 0;

        objx = (object.position.x - object2.position.x);
        objy = (object.position.y - object2.position.y);

        obj2x = (object2.position.x - object.position.x);
        obj2y = (object2.position.y - object.position.y);

        double Aangle = Math.atan2(objy, objx);
        double Bangle = Math.atan2(obj2y, obj2x);

        if (object.velocity.x < RANDOM.nextDouble() * MAX_SPEED * (-2) && object.velocity.y < RANDOM.nextDouble() * MAX_SPEED * (-2)) {
            if (object.position.dist(object2.position) < 100) {
                object.velocity.add(new Vector2D(Math.cos(Aangle)*0.15, Math.sin(Aangle)*0.15));
            }
        }
    }

    @Override
    public void update() {
        super.update();

    }

    //asteroid interact
/*
        distance = Math.sqrt(AdiffX * AdiffX + AdiffY * AdiffY);
        distance3 = distance * distance * distance;
        dx += (350 * AdiffX) / distance3;
        dy += (350 * AdiffY) / distance3;

        dx *= 0.15;
        dy *= 0.15;


        object.velocity.x += dx;
        object.velocity.y += dy;
        object.position.x += object.velocity.x;
        object.position.y += object.position.y;

        object2.velocity.x += dx;
        object2.velocity.y += dy;
        object2.position.x += object2.velocity.x;
        object2.position.y += object2.position.y;*/



  /*  public boolean collidesWith(Point p) {
        final double diffX = p.x - x, diffY = p.y - y, rads = radius + p.radius;
        return diffX * diffX + diffY * diffY <= rads * rads;
    }*/

  //asteroid interact
    public boolean equals(GameObject o) {
        if (o instanceof Asteroid) {
            final Asteroid p = (Asteroid) o;
            return p.position.x == position.x && p.position.y == position.y && p.velocity.x == velocity.x && p.velocity.y == velocity.y &&
                    p.radius == radius;
        } else {
            return false;
        }
    }
    void checkDistances() {
        for (GameObject a : Game.alive) {
            for (GameObject b : Game.alive)
                if (a instanceof Asteroid && b instanceof Asteroid) {
                    asteroidInteract((Asteroid) a, (Asteroid) b, 5);
                }
        }
    }
    void asteroidInteract(Asteroid ast, Asteroid bst, int scale) {
        // Asteroids gravitate toward each other

        if (ast.overlap(bst)) {
            System.out.println("collide");
            asteroidCollide(ast, bst);
        }
    }

    //asteroidinteract
    void asteroidCollide(GameObject ast, GameObject bst) {
        var astmass = ast.radius * ast.radius;
        var bstmass = bst.radius * bst.radius;

        Vector2D diff = new Vector2D(
                ast.position.x - bst.position.x,
                ast.position.y - bst.position.y);
        diff.normalise();

        ast.velocity.x += bstmass / astmass * diff.x / 10;
        ast.velocity.y += bstmass / astmass * diff.y / 10;


        ast.velocity.x += Math.random() - 0.5;
        ast.velocity.y += Math.random() - 0.5;

        ast.velocity.x *= 0.9;
        ast.velocity.y *= 0.9;
    }

    //draws the asteroid sprite and adjusts the sixe of the asteroid when hit
    public void draw(Graphics2D g) {
        double imW = asteroidimage.getWidth(null);
        double imH = asteroidimage.getHeight(null);
        AffineTransform t = new AffineTransform();

        if (radius == RADIUS)
            t.scale(radius * 2 / imW, radius * 2 / imH);
        else if (radius == RADIUS1) {
            t.scale((radius * 2 / imW) * 0.8, (radius * 2 / imH) * 0.8);
        } else if (radius == RADIUS2) {
            t.scale((radius * 2 / imW) * 0.5, (radius * 2 / imH) * 0.5);
        }
        t.translate(-imW / 2, -imH / 2);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(asteroidimage, t, null);
        g.setTransform(t0);
    }

    //if hit, drop random power up and split asteroids
    @Override
    public void hit() {
        SoundManager.explodeAsteroid();
        Game.adjScore(ASTEROID_VALUE);
        Game.explosion(this);
        //if random int equals 0, drops a random powerup at the same position the object died
        if (RANDOM.nextInt(35) == 0) {
            Game.dropPowerup(this);
        }
        if (this.radius > RADIUS2) {
            split();
        }
        this.dead = true;
    }

    //split method creates 3 smaller asteroids and adds them to the array
    private void split() {
        Asteroid split1 = (new Asteroid(new Vector2D(position), new Vector2D(velocity.x, velocity.y).rotate(RANDOM.nextInt(90)), radius - 5));
        Asteroid split2 = (new Asteroid(new Vector2D(position), new Vector2D(velocity.x, velocity.y).rotate(RANDOM.nextInt(180)), radius - 5));
        Asteroid split3 = (new Asteroid(new Vector2D(position), new Vector2D(velocity.x, velocity.y).rotate(RANDOM.nextInt(360)), radius - 5));
        smallAsteroids.add(split1);
        smallAsteroids.add(split2);
        smallAsteroids.add(split3);
    }

    //which classes can hit
    @Override
    public boolean canHit(GameObject other) {
        if (other.getClass() == PlayerShip.class && PlayerShip.INVINCIBLE) return false;
        if (System.currentTimeMillis() - PlayerShip.SPAWNTIMER > 5000) {
            return other instanceof PlayerShip;
        }
        return other instanceof Bullet;
    }

    //dead boolean
    @Override
    public boolean dead() {
        return dead;
    }
}