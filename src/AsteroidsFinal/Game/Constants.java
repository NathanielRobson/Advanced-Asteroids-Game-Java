package AsteroidsFinal.Game;

import utilities.Vector2D;

import java.awt.*;
import java.util.Random;

public class Constants {
    public static final int FRAME_HEIGHT = 906 - 120;
    public static final int FRAME_WIDTH = 1890;
    public static final Dimension FRAME_SIZE = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    public static final Vector2D initVec = new Vector2D(0, 0);
    public static final Random RANDOM = new Random();
    public static final int DELAY = 20;
    public static final double DT = DELAY / 1000.0;

    //Initial asteroids
    public static int N_INITIAL_ASTEROIDS = 3;
    //Initial saucers
    public static int N_INITIAL_SAUCERS = 1;

    //The Saucers initial Health (6 hits)
    public static int SAUCER_HP = 6;
}
