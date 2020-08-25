package AsteroidsFinal.GameObjects;

import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

public class DeathField extends GameObject {

    //if player flies into it he dies.
    public DeathField() {
        super(new Vector2D(1500, 400), new Vector2D(0, 0), 110);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        for (int i = 0; i < 30; i += 2) {
            g.drawOval(1500, 300, 230 + i / 2, 230 + i / 2);
        }
    }

    @Override
    public boolean dead() {
        return false;
    }

    @Override
    public boolean canHit(GameObject o) {
        return o instanceof PlayerShip;
    }


    @Override
    public void hit() {
        this.dead = false;
    }
}
