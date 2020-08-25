package AsteroidsFinal.Game;

import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class Sprite {

    public static Image ENEMY_SHIP_01, ENEMY_THRUSTER_01, ASTEROID1, MILKYWAY2, LIFE,
            SHIP_01, THRUSTER_01, SHIELD, CHAINFIRE, PLAYER_HELPER, SHIP_SHIELD;

    static {
        try {
            ASTEROID1 = ImageManager.loadImage("asteroid");
            MILKYWAY2 = ImageManager.loadImage("background-purple");
            LIFE = ImageManager.loadImage("extralife");
            ENEMY_SHIP_01 = ImageManager.loadImage("enemysaucer");
            ENEMY_THRUSTER_01 = ImageManager.loadImage("enemyjetstream");
            SHIP_01 = ImageManager.loadImage("playership");
            THRUSTER_01 = ImageManager.loadImage("jetstream");
            CHAINFIRE = ImageManager.loadImage("chainfire");
            SHIELD = ImageManager.loadImage("shield");
            SHIP_SHIELD = ImageManager.loadImage("ship_shield");
            PLAYER_HELPER = ImageManager.loadImage("playerhelper");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector2D position;
    private Image image;
    private Vector2D direction;
    private double width;
    private double height;

    public Sprite(Image image, Vector2D s, Vector2D direction, double width,
                  double height) {
        super();
        this.image = image;
        this.position = s;
        this.direction = direction;
        this.width = width;
        this.height = height;
    }

    public double getRadius() {
        return (width + height) / 4.0;
    }

    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Double((position.x - width / 2), position.y - height / 2, width,
                height);
    }

    public void draw(Graphics2D g) {
        double imW = image.getWidth(null);
        double imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.rotate(direction.angle(), 0, 0);
        t.scale(width / imW, height / imH);
        t.translate(-imW / 2.0, -imH / 2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(image, t, null);
        g.setTransform(t0);
        g.setColor(Color.RED);
    }

}
