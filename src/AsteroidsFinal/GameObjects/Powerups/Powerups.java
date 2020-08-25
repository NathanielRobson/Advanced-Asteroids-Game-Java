package AsteroidsFinal.GameObjects.Powerups;

import AsteroidsFinal.Game.Sprite;
import AsteroidsFinal.GameObjects.GameObject;
import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;

import static AsteroidsFinal.Game.Constants.initVec;

public class Powerups extends GameObject {

    private static final int SIZE = 40;
    public boolean dead;
    private String type;

    public Powerups(Vector2D position, String type) {
        super(new Vector2D(position), initVec, SIZE);
        this.type = type;
    }

    //used string to identify which powerup to draw in the game
    @Override
    public void draw(Graphics2D g) {
        if (type.equals("extralife")) {
            drawLife(g);
        }
        if (type.equals("shield")) {
            drawShield(g);
        }
        if (type.equals("chainfire")) {
            drawChainfire(g);
        }
    }

    @Override
    public void update() {
        super.update();
    }

    //draw the life powerup in game
    private void drawLife(Graphics2D g) {
        Sprite imgLife = new Sprite(Sprite.LIFE, position, new Vector2D(new Vector2D(0, -1)
        ).rotate(1.57), 7 * 7, 7 * 7);
        imgLife.draw(g);

    }

    //draw the shield powerup in game
    private void drawShield(Graphics2D g) {
        Sprite imgLife = new Sprite(Sprite.SHIELD, position, new Vector2D(new Vector2D(0, -1)
        ).rotate(1.57), 7 * 7, 7 * 7);
        imgLife.draw(g);

    }

    //draw the chainfire powerup in game
    public void drawChainfire(Graphics2D g) {
        Sprite imgLife = new Sprite(Sprite.CHAINFIRE, position, new Vector2D(new Vector2D(0, -1)
        ).rotate(1.57), 7 * 7, 7 * 7);
        imgLife.draw(g);

    }


    @Override
    public void hit() {
        //Increment player life, play sound and set to null to prevent continuous drawing
        switch (type) {
            case "extralife":
                PlayerShip.LIVES += 1;
                type = "";
                SoundManager.pickupLife();
                this.dead = true;
                break;
            case "shield":
                PlayerShip.INVINCIBLE = true;
                type = "";
                SoundManager.pickupShield();
                this.dead = true;
                break;
            case "chainfire":
                PlayerShip.CHAINFIRE = true;
                type = "";
                SoundManager.pickupChainfire();
                this.dead = true;
                break;
        }
        this.dead = true;
    }

    //Only the ship can hit by colliding with them
    @Override
    public void collisionHandling(GameObject other) {
        if (this.getClass() != other.getClass() && this.overlap(other)) {
            if (other.canHit(this)) this.hit();
        }
    }

    //dead boolean
    @Override
    public boolean dead() {

        return dead;
    }

    //cannot hit any other class, can only be hit
    @Override
    public boolean canHit(GameObject other) {
        return false;
    }
}
