package AsteroidsFinal.Game;

import AsteroidsFinal.GameObjects.GameObject;
import AsteroidsFinal.GameObjects.Particle;
import AsteroidsFinal.GameObjects.Powerups.Powerups;
import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import AsteroidsFinal.GameObjects.Ships.Saucer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static AsteroidsFinal.Game.Constants.FRAME_HEIGHT;
import static AsteroidsFinal.Game.Constants.FRAME_WIDTH;
import static AsteroidsFinal.Game.Game.*;

public class View extends JComponent {

    //background transformation
    private final AffineTransform BACKGROUND;
    // background colour
    private Game game;

    public View(Game game) {
        this.game = game;

        //background image positioning and sizing
        double iWidth = Sprite.MILKYWAY2.getWidth(null);
        double iHeight = Sprite.MILKYWAY2.getHeight(null);
        double stretchx = (iWidth > FRAME_WIDTH ? 1 :
                FRAME_WIDTH / iWidth);
        double stretchy = (iHeight > FRAME_HEIGHT ? 1 :
                FRAME_HEIGHT / iHeight);
        BACKGROUND = new AffineTransform();
        setPreferredSize(new Dimension((int) (FRAME_WIDTH * stretchx),
                (int) (FRAME_HEIGHT + 220 * stretchy)));

        BACKGROUND.scale(stretchx, stretchy);
    }

    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;

        //draw the background
        g.drawImage(Sprite.MILKYWAY2, BACKGROUND, null);


        //synchronized with the game class to prevent concurrency update exceptions and errors.
        synchronized (Game.class) {
            for (GameObject obj : Game.objects) {
                obj.draw(g);
            }
            if (game.particles != null) {
                for (Particle p : game.particles) {
                    p.draw(g);
                }
            }
            if (game.powerups != null) {
                for (Powerups po : game.powerups) {
                    po.draw(g);
                }
            }

            //if player loses draw this
            if (gameover) {
                g.setColor(Color.white);
                g.setFont(new Font("Monospaced", Font.BOLD, 56));
                String GAME_OVER_STRING = "Game Over! Final Score: " + SCORE;
                g.drawString(GAME_OVER_STRING, (FRAME_WIDTH / 2) - g.getFontMetrics().stringWidth(GAME_OVER_STRING) / 2, (FRAME_HEIGHT / 2));

            }

            //if player pauses draw this
            if (gamepause) {
                g.setFont(new Font("Monospaced", Font.BOLD, 56));
                String pausestring = "Game is Paused Press P to Resume Game";
                g.drawString(pausestring, (FRAME_WIDTH / 2) - g.getFontMetrics().stringWidth(pausestring) / 2, (FRAME_HEIGHT / 2));
                g.setFont(new Font("Monospaced", Font.BOLD, 22));
            }

            //if player wins draw this
            if (gamewin) {
                String winnerstring = "Congratulations You Win! Final Score: " + SCORE;
                String restartstring = "To Play Again Press R ";
                g.setColor(Color.GREEN);
                g.setFont(new Font("Monospaced", Font.BOLD, 56));
                g.drawString(winnerstring, (FRAME_WIDTH / 2) - g.getFontMetrics().stringWidth(winnerstring) / 2, (FRAME_HEIGHT / 2));
                g.drawString(restartstring, (FRAME_WIDTH / 2) - g.getFontMetrics().stringWidth(winnerstring) / 2, (FRAME_HEIGHT / 2)+70);

            }

            //draws help view
            if (keyhelp) {
                String keyhelpstr = "KEYBOARD BINDINGS";
                String disablestr = "DISABLE OVERLAY BY PRESSING >> H <<";
                String upstr = "ACCELERATE - D.Pad UP or W Key";
                String rightstr = "RIGHT turn - D.Pad Right or D Key";
                String leftstr = "LEFT turn - D.Pad Left or A Key";
                String telstr = "TELEPORT - T Key";
                String bombstr = "BOMB - B Key";
                String pausestr = "PAUSE - P Key";
                String restr = "RESTART - R Key";
                String helpstr = "HELP - H Key";
                String firestr = "FIRE - SPACEBAR";

                String rulesstr = "Rules: ";
                String astStr = "ASTEROID = 200 points";
                String saucerstr = "SAUCER = 800 points";
                String lifestr = "2000 points = Extra Life";
                String invstr = "SHIELD = Blue Power Up";
                String lifestr2 = "LIFE = Extra Life";

                //Help section, top right side
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.ITALIC, 26));
                g.drawString(keyhelpstr, FRAME_WIDTH - 360, 40);
                g.setColor(Color.CYAN);
                g.setFont(new Font("Monospaced", Font.PLAIN, 18));
                g.drawString(disablestr, FRAME_WIDTH - 400, 60);
                g.drawString(upstr, FRAME_WIDTH - 400, 80);
                g.drawString(rightstr, FRAME_WIDTH - 400, 100);
                g.drawString(leftstr, FRAME_WIDTH - 400, 120);
                g.drawString(telstr, FRAME_WIDTH - 400, 140);
                g.drawString(bombstr, FRAME_WIDTH - 400, 160);
                g.drawString(pausestr, FRAME_WIDTH - 400, 180);
                g.drawString(restr, FRAME_WIDTH - 400, 200);
                g.drawString(helpstr, FRAME_WIDTH - 400, 220);
                g.drawString(firestr, FRAME_WIDTH - 400, 240);

                //Rules section, bottom right side
                g.setColor(Color.RED);
                g.setFont(new Font("Monospaced", Font.ITALIC, 22));
                g.drawString(rulesstr, FRAME_WIDTH - 340, 540);
                g.drawString(astStr, FRAME_WIDTH - 340, 560);
                g.drawString(saucerstr, FRAME_WIDTH - 340, 580);
                g.drawString(lifestr2, FRAME_WIDTH - 340, 600);
                g.drawString(invstr, FRAME_WIDTH - 340, 620);
                g.drawString(lifestr, FRAME_WIDTH - 340, 640);
            }
        }


        //Drawing Information Panel at the bottom of the frame.
        g.setColor(Color.BLACK);
        g.fillRect(0, FRAME_HEIGHT - 100, FRAME_WIDTH, 100);
        g.setColor(Color.PINK);
        g.fill3DRect(0, FRAME_HEIGHT - 100, FRAME_WIDTH, 10, true);
        g.setColor(Color.WHITE);

        //Drawing Strings in bottom panel
        g.setFont(new Font("Monospaced", Font.BOLD, 26));
        String livesstr = "Lives: ";
        String scorestr = "Score: ";
        String levelstr = "Level: ";
        g.drawString(livesstr + PlayerShip.LIVES, 50, FRAME_HEIGHT - 40);
        g.drawString(scorestr + Game.SCORE, FRAME_WIDTH - 250, FRAME_HEIGHT - 40);
        g.drawString(levelstr + Game.LEVEL, (FRAME_WIDTH / 2) - g.getFontMetrics().stringWidth(levelstr + "0") / 2, FRAME_HEIGHT - 40);

        //Information on how many enemies and asteroids there are left in the current level
        g.setFont(new Font("Monospaced", Font.ITALIC, 16));
        String asteroidstr = "Asteroids Inbound: ";
        String saucerstr = "Saucers Inbound: ";
        g.setColor(Color.ORANGE);
        g.drawString(asteroidstr + CURRENT_ASTEROID_COUNT, 15, 20);
        g.drawString(saucerstr + CURRENT_SAUCER_COUNT, 15, 42);

        g.setColor(Color.CYAN);
        String current_powerup = "Active Powerup: ";
        if (PlayerShip.CHAINFIRE) {
            g.drawString(current_powerup + "CHAINFIRE", 15, 62);
        } else if (PlayerShip.INVINCIBLE) {
            g.drawString(current_powerup + "INVINCIBLE", 15, 62);
        } else {
            g.drawString(current_powerup + "None", 15, 62);
        }

        if (bossfight) { // DRAW HP BAR
            g.setFont(new Font("Monospaced", Font.BOLD, 32));

            g.setColor(Color.RED);
            g.drawString("Boss Health Remaining: " + Saucer.HP, 500, 40);
            g.drawRect(500, 600, 800, 20);
            g.setColor(Color.red);

            g.fillRect(500, 600, Saucer.HP * 10, 20);

        }

        //If the game is restarted draw this
        if (restart) {
            String restartstring = "Game Was Restarted";
            g.setFont(new Font("Monospaced", Font.BOLD, 16));
            g.drawString(restartstring, (FRAME_WIDTH / 2) - g.getFontMetrics().stringWidth(restartstring) / 2, (FRAME_HEIGHT - 10));

        }

    }

    //returns the default frame size.
    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }
}
