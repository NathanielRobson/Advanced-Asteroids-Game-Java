package game1;

import utilities.Vector2D;

import static game1.Constants.DT;
import static game1.Constants.FRAME_HEIGHT;
import static game1.Constants.FRAME_WIDTH;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;


public class BasicAsteroid {
    public static final int RADIUS = 10;
    public static final double MAX_SPEED = 190;
    public static Random rand = new Random();
    public Vector2D position;
    public Vector2D velocity;

    public BasicAsteroid(Vector2D position, Vector2D velocity){
        this.position = position;
        this.velocity = velocity;
    }

	public static BasicAsteroid makeRandomAsteroid(){
        Vector2D position = new Vector2D((rand.nextDouble()*(FRAME_WIDTH - 2 * RADIUS)),
                (rand.nextDouble() * (FRAME_HEIGHT - 2 * RADIUS)));

        Vector2D velocity = new Vector2D((rand.nextDouble()*MAX_SPEED * (-2) + 200),
                (rand.nextDouble() * MAX_SPEED * (-2) + 200));

        BasicAsteroid myAsteroid = new BasicAsteroid(position,velocity);

        System.out.println(myAsteroid);
        return myAsteroid;
    }

    public void update(){

        position.addScaled(velocity, DT);
        position.wrap(FRAME_WIDTH,FRAME_HEIGHT);

    }


    public void draw(Graphics2D g){
        g.setColor(Color.RED);
        g.fillOval((int) position.x - RADIUS, (int) position.y - RADIUS, 2 * RADIUS, 2 * RADIUS);

    }

    @Override
    public String toString(){
        return "Asteroid with coords: (" + position.x + ", " + position.y + "), velocity: (" + velocity.x + ", " + velocity.y + ")";
    }
}
