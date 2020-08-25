package AsteroidsFinal.GameObjects;

import AsteroidsFinal.Game.Constants;
import utilities.Vector2D;

import java.awt.*;


public abstract class GameObject {
    public Vector2D position;
    public Vector2D velocity;
    public boolean dead;
    public double radius;
    public int distToShip;

    public GameObject(Vector2D position, Vector2D velocity, double radius) {
        this.position = new Vector2D(position);
        this.velocity = new Vector2D(velocity);
        this.radius = radius;
        dead = false;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void update() {
        position.addScaled(velocity, Constants.DT);
        position.wrap(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT - 110);
    }

    public void hit() {
        dead = true;
    }

    public abstract void draw(Graphics2D g);

    public abstract boolean dead();

    public abstract boolean canHit(GameObject other); // abstract canHit method, for checking if given objects can collide


    public boolean overlap(GameObject other) {
        return position.dist(other.position) <= this.radius || position.dist(other.position) <= other.radius;
    }

    public void collisionHandling(GameObject other) {
        if (this.getClass() != other.getClass() && this.overlap(other)) {
            if (other.getClass() == Asteroid.class && this.getClass() == Bullet.class) {
                this.hit();
                return;
            }
            if (this.canHit(other)) other.hit();
            if (other.canHit(this)) this.hit();

        }
    }


    public String toString() {
        return this.getClass().getSimpleName() + "(" + position.x + "," + position.y + ")";
    }

}
