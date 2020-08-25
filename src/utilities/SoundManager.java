package utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import static AsteroidsFinal.Game.Game.gameover;

//SoundManager for AsteroidsFinal
public class SoundManager {

    private static int nBullet = 0;
    public static boolean thrusting = false;

    // this may need modifying
    private final static String path = "sounds/";

    //having too many clips open may cause
    //"LineUnavailableException: No Free Voices"
    public final static Clip[] bullets = new Clip[15];

    public final static Clip bangLarge = getClip("thrust");
    public final static Clip bangMedium = getClip("bangMedium");
    public final static Clip bangSmall = getClip("bangSmall");
    public final static Clip beat1 = getClip("beat1");
    public final static Clip beat2 = getClip("beat2");

    public final static Clip saucerBig = getClip("saucerBig");
    public final static Clip saucerSmall = getClip("saucerSmall");
    public final static Clip thrust = getClip("thrust");

    //added sounds
    public final static Clip retromusicloop = getClip("retromusicloop");
    public final static Clip pickupshield = getClip("shieldclank");
    public final static Clip extraShip = getClip("extraShip");
    public final static Clip chainfire = getClip("chainfire");
    public final static Clip fire = getClip("fire");
    public final static Clip shiphit = getClip("shiphit");

    public final static Clip[] clips = {bangLarge, bangMedium, bangSmall, beat1, beat2,
            extraShip, fire, saucerBig, saucerSmall, thrust};

    static {
        for (int i = 0; i < bullets.length; i++)
            bullets[i] = getClip("fire");
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 20; i++) {
            fire();
            Thread.sleep(100);
        }
        for (Clip clip : clips) {
            play(clip);
            Thread.sleep(1000);
        }
    }

    // methods which do not modify any fields
    public static void play(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

    private static Clip getClip(String filename) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path
                    + filename + ".wav"));
            clip.open(sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    // methods which modify (static) fields
    public static void fire() {
        // fire the n-th bullet and increment the index
        Clip clip = bullets[nBullet];
        clip.setFramePosition(0);
        clip.start();
        nBullet = (nBullet + 1) % bullets.length;
    }


    public static void startThrust() {
        if (!thrusting) {
            thrust.loop(-1);
            thrusting = true;
        }
    }

    public static void stopThrust() {
        thrust.loop(0);
        thrusting = false;
    }

    // Custom methods playing a particular sound
    public static void explodeAsteroid() {
        play(bangMedium);
    }

    public static void pickupLife() {
        play(extraShip);
    }

    public static void pickupShield() {play(pickupshield);}

    public static void pickupChainfire() {play(chainfire);}

    public static void hitShipsound() {play(shiphit);}

    public static void startMusic(){
        if (!gameover){
            retromusicloop.loop(-1);

        }
    }

}