package game1;

import utilities.BasicController;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game1.Constants.*;

public class BasicShip {
    public static final int RADIUS = 8;
    // rotation velocity in radians per second
    public static final double STEER_RATE = 2* Math.PI;
    // acceleration when thrust is applied
    public static final double MAG_ACC = 5;
    // constant speed loss factor
    public static final double DRAG = 0.01;
    public static final Color SHIP_COLOR = new Color(0xFFF2FF);
    public static final Color FLAME_COLOR1 = new Color(0xC80003);
    public static final Color FLAME_COLOR2 = new Color(0xC86A00);
    public Vector2D position; // on frame
    public Vector2D velocity; // per second
    // direction in which the nose of the ship is pointing
    // this will be the direction in which thrust is applied
    // it is a unit vector representing the angle by which the ship has rotated
    public Vector2D direction;

    // controller which provides an Action object in each frame
    private BasicController ctrl;

    public int[] XP = {0,1,0,-1}; //sets the position of the ship
    public int[] YP = {1,-1,0,-1};
    public int[] XPTHRUST = {0,1,0,-1}; //sets position of thrust
    public int[] YPTHRUST = {-2,-1,0,-1};
    public int[] XPTHRUST2 = {0,1,0,-1};
    public int[] YPTHRUST2 = {-3,-1,0,-1};

    public BasicShip(BasicController ctrl) {
        this.ctrl = ctrl;
        position = new Vector2D(FRAME_WIDTH/2,FRAME_HEIGHT/2);
        velocity = new Vector2D(0,0);
        direction = new Vector2D(0,-1);
    }

    public void update() {
        direction.rotate(ctrl.action().turn * STEER_RATE * DT);

        velocity.addScaled(direction, ctrl.action().thrust * MAG_ACC)
                .mult(1 - DRAG);

        position.addScaled(velocity,DT).wrap(FRAME_WIDTH,FRAME_HEIGHT);

    }

    public void draw(Graphics2D g){

        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(-10,-10);
        g.setColor(SHIP_COLOR);
        g.fillPolygon(XP, YP, XP.length);
        if(ctrl.action().thrust==1){
            g.setColor(FLAME_COLOR2);
            g.fillPolygon(XPTHRUST2,YPTHRUST2, XPTHRUST2.length);
            g.setColor(FLAME_COLOR1);

            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);

        }
        g.setTransform(at);

        //g.scale(DRAWING_SCALE);
//        g.setColor(COLOR);
//        g.fillOval((int) (position.x - RADIUS), (int) position.y - RADIUS, 2 * RADIUS, 2 * RADIUS);
//        g.drawLine((int) (position.x), (int) (position.y), (int)(position.x +direction.x * 20), (int)(position.y + direction.y*20));
    }
}