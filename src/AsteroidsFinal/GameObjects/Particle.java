package AsteroidsFinal.GameObjects;

import AsteroidsFinal.Game.Constants;
import utilities.Vector2D;

import java.awt.*;

public class Particle extends GameObject {

    private static final int PARTICLE_SPEED = 15;
    private static final int TTL = 10;
    private static final int SIZE = 3;
    public boolean dead;
    private Color COLOR;
    private int ttl;
    private long creationTime;
    private int counting = 200;

    public Particle(Vector2D position, Vector2D velocity, Color color) {
        super(new Vector2D(position), randomVelocity().add(velocity), SIZE);

        this.ttl = Constants.RANDOM.nextInt(TTL);
        this.dead = false;
        this.creationTime = System.currentTimeMillis();
        COLOR = color;
    }

    private static Vector2D randomVelocity() {
        return Vector2D.polar(Math.random() * 2 * Math.PI,
                Math.abs(Constants.RANDOM.nextGaussian() * PARTICLE_SPEED));
    }


    @Override
    public void update() {
        super.update();
        if (System.currentTimeMillis() - creationTime > 500) {
            if (System.currentTimeMillis() - creationTime > 1000) dead = true;
            if (System.currentTimeMillis() - creationTime > counting) {
                counting = counting * 2;
                COLOR = COLOR.darker();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(COLOR);
        g.fillOval((int) position.x, (int) position.y, SIZE, SIZE);
    }

    @Override
    public boolean dead() {
        return dead;
    }

    @Override
    public boolean canHit(GameObject other) {
        return false;

    }
}
