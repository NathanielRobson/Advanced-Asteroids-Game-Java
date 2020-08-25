package game1;


import game2.Constants;

import javax.swing.*;
import java.awt.*;


public class BasicView extends JComponent {
    // background colour
    public static final Color BG_COLOR = Color.black;

    private BasicGame game;
    private BasicAsteroid ast;

    public BasicView(BasicGame game) {
        this.game = game;
    }

    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        // paint the background
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        for(BasicAsteroid b : game.asteroids){
            b.draw(g);
        }
        game.ship.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}
