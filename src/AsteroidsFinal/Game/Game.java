package AsteroidsFinal.Game;

import AsteroidsFinal.Control.KeyController;
import AsteroidsFinal.GameObjects.Asteroid;
import AsteroidsFinal.GameObjects.DeathField;
import AsteroidsFinal.GameObjects.GameObject;
import AsteroidsFinal.GameObjects.Particle;
import AsteroidsFinal.GameObjects.Powerups.Powerups;
import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import AsteroidsFinal.GameObjects.Ships.Saucer;
import AsteroidsFinal.Score.Score;
import AsteroidsFinal.Score.ScoreFrame;
import AsteroidsFinal.Score.ScoreWriter;
import utilities.JEasyFrame;
import utilities.SoundManager;
import utilities.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static AsteroidsFinal.Game.Constants.*;
import static AsteroidsFinal.GameObjects.Asteroid.asteroidInteract;
import static AsteroidsFinal.GameObjects.Ships.PlayerShip.SPAWNTIMER;


public class Game {
    public static List<GameObject> alive;
    public static java.util.List<Particle> createdParticles;
    public static java.util.List<Powerups> createdPowerups;
    public static List<GameObject> objects;
    public static KeyController keys;
    public static Powerups powman = null;
    public static PlayerShip playerShip;
    public static Saucer saucer;
    public static DeathField deathField;
    public static int LEVEL = 1;
    public static int SCORE = 0;
    public static int CURRENT_ASTEROID_COUNT;
    public static int CURRENT_SAUCER_COUNT;
    public static boolean gamepause;
    public static boolean gameover;
    public static boolean gamewin;
    public static boolean bossfight;
    public static boolean keyhelp;
    public static boolean restart;
    public static boolean gamerunning = true;
    public static boolean begin = false;
    private static Vector2D initPos = new Vector2D(Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT / 2);
    private static Vector2D initDir = new Vector2D(0, -1);
    private static ScoreWriter scoreWriter;
    private static JEasyFrame jeasyframe;
    private static String name;
    private static int GAME_START_PROTECTION = 4000;
    private static int ASTEROID_RADIUS = 40;
    private static int PLAYER_RADIUS = 10;
    public List<Particle> particles = new LinkedList<>();
    public List<Powerups> powerups = new LinkedList<>();

    public Game() {
        objects = new ArrayList<>();
        keys = new KeyController(this);
        deathField = new DeathField();
        objects.add(deathField);

        playerShip = new PlayerShip(initPos, initDir, PLAYER_RADIUS, keys);
        playerShip.position = initPos;
        objects.add(playerShip);

        if (LEVEL < 4) {
            saucer = Saucer.makeSaucer(playerShip, SAUCER_HP + LEVEL);
            objects.add(saucer);

        }
        for (int i = 0; i < Constants.N_INITIAL_ASTEROIDS; i++) {
            objects.add(Asteroid.makeRandomAsteroid(ASTEROID_RADIUS));
        }
        keyhelp = true;
        gameover = false;
        gamepause = false;
        gamewin = false;

        name = JOptionPane.showInputDialog("WHAT IS YOUR NAME SPACE MAGGOT!", "NOOB");
        int dialog = JOptionPane.showOptionDialog(null, "ARE YOU READY? Press OK to Begin!", "WARNING Advanced Asteroids INBOUND!",
                2, 2, null, null, null);
        if (dialog == JOptionPane.YES_OPTION) {
            begin = true;
            //Protect player for a bit longer at the beginning of the game
            SPAWNTIMER += GAME_START_PROTECTION;
        } else {
            //player wasn't ready to start
            System.exit(0);
        }
    }

    //Main game method
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        View view = new View(game);
        JEasyFrame theFrame = new JEasyFrame(view, "Advanced Asteroids Game");
        theFrame.addKeyListener(keys);
        theFrame.setResizable(false);

        //constant loop
        while (gamerunning && begin) {
            game.update();
            game.updateParticles();
            view.repaint();
            Thread.sleep(DELAY);
        }
    }

    //Make particles depending on the game object
    public static void makeParticles(GameObject object, int spaceA, int spaceB) {
        Color colA, colB;
        if (object.getClass() == PlayerShip.class) {
            colA = Color.CYAN.darker();
            colB = Color.GRAY.brighter();
        } else {
            colA = Color.GREEN.darker();
            colB = Color.BLUE.darker();
        }
        double roti;
        for (int i = 0; i < 10; i++) {
            roti = -0.2 * RANDOM.nextDouble();
            switch (RANDOM.nextInt(4)) {
                case 0:
                    createdParticles.add(new Particle(new Vector2D(object.position.x + spaceA, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-1).rotate(roti), colA));
                    break;
                case 1:
                    createdParticles.add(new Particle(new Vector2D(object.position.x - spaceA, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-1).rotate(roti), colA));
                    break;
                case 2:
                    createdParticles.add(new Particle(new Vector2D(object.position.x - spaceB, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-1).rotate(roti), colB));
                    break;
                case 3:
                    createdParticles.add(new Particle(new Vector2D(object.position.x + spaceB, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-1).rotate(roti), colB));
                    break;
            }
        }
    }

    //Make explosion particles depending on the game object
    public static void explosion(GameObject object) {
        Color ColA, ColB, ColC, ColD, ColE, ColF, ColG, ColH;
        if (object.getClass() == Saucer.class) {
            ColA = Color.GREEN.darker();
            ColB = Color.BLUE.brighter();
            ColC = Color.PINK.darker().darker();
            ColD = Color.CYAN.darker();
            ColE = Color.YELLOW.darker();
            ColF = Color.WHITE.brighter();
            ColG = Color.WHITE.darker();
            ColH = Color.CYAN.brighter();
        } else {
            ColA = Color.GRAY.darker();
            ColB = Color.GRAY.brighter();
            ColC = Color.ORANGE.darker();
            ColD = Color.ORANGE.brighter();
            ColE = Color.RED.darker();
            ColF = Color.RED.brighter();
            ColG = Color.YELLOW.darker();
            ColH = Color.YELLOW.brighter();
        }
        double roti;
        for (int i = 0; i < 250; i++) {
            roti = -0.2 * RANDOM.nextDouble();
            switch (RANDOM.nextInt(7)) {
                case 0:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColA));
                    break;
                case 1:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColB));
                    break;
                case 2:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColC));
                    break;
                case 3:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColD));
                    break;
                case 4:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColE));
                    break;
                case 5:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColF));
                    break;
                case 6:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColG));
                    break;
                case 7:
                    createdParticles.add(new Particle(new Vector2D(object.position.x, object.position.y).addScaled(object.velocity, -0.1), new Vector2D(object.velocity).mult(-0.5).rotate(roti), ColH));
                    break;

            }

        }
    }

    //Toggle pause by pressing the P key
    public static void TogglePaused() {
        gamepause = !gamepause;

    }

    //Begin boss fight method
    private static void setBossfight() {
        //adds a boss to the game
        saucer = Saucer.makeEnemyBoss(playerShip, 80);
        objects.add(saucer);

    }

    //String displaying that the player has lost and removes all game objects
    public static String gameoverString() {
        gameover = true;

        for (GameObject obj : objects) {
            obj.dead = true;
        }
        deathScore();
        return "Game Over! Final Score: " + SCORE;
    }

    //String displaying that the player has won and removes all game objects
    public static String winnerString() {
        for (GameObject obj : objects) {
            obj.dead = true;
        }
        deathScore();
        return "Congratulations You Win! Score: " + SCORE;
    }

    //Restart the game methods, sets default values of ships and asteroids in the game.
    public static void restart() {
        objects.clear();
        Constants.N_INITIAL_ASTEROIDS = 3;
        Constants.N_INITIAL_SAUCERS = 1;
        SCORE = 0;
        LEVEL = 1;
        deathField = new DeathField();
        objects.add(deathField);

        playerShip.dead = true;
        playerShip.position = new Vector2D(Constants.FRAME_WIDTH / 2, Constants.FRAME_HEIGHT / 2);
        playerShip.velocity = new Vector2D(0, 0);
        playerShip = new PlayerShip(initPos, initDir, 10, keys);

        for (int i = 0; i < Constants.N_INITIAL_ASTEROIDS; i++) {
            objects.add(Asteroid.makeRandomAsteroid(ASTEROID_RADIUS));
        }
        if (LEVEL != 4) {
            saucer = Saucer.makeSaucer(playerShip, SAUCER_HP);
            objects.add(saucer);
        }
        playerShip.dead = false;
        objects.add(playerShip);
        gameover = false;
        gamewin = false;
        bossfight = false;
    }

    //Adjust the score depending on what the player does in-game
    public static void adjScore(int x) {
        SCORE += x;

        //If the player accumulates 2000 points they wil receive an extra life.
        if ((SCORE > 1) && (SCORE % 2000) == 0) {
            PlayerShip.LIVES++;
        }
    }

    //If the player dies, the score will be saved to file and displayed
    static void deathScore() {
        openScores();
        Score playerScore = new Score(name, SCORE, new Date());

        scoreWriter.addScoreToFile(playerScore);
        scoreWriter.createScoreList();

        String EndGameText = "WELL DONE MAGGOT, YOUR FINAL SCORE IS: " + playerScore.getScore();
        JOptionPane.showMessageDialog(jeasyframe, EndGameText);
        scoreWriter.displayScores();

    }

    //Switch case method, Random power up spawn at the objects position
    public static void dropPowerup(GameObject object) {
        Vector2D pos = object.position;
        int rand = RANDOM.nextInt(3);
        switch (rand) {
            case (0):
                powman = new Powerups(pos, "extralife");
                break;
            case (1):
                powman = new Powerups(pos, "shield");
                break;
            case (2):
                powman = new Powerups(pos, "chainfire");
                break;
        }
    }

    //Show the scores frame to the player
    private static void openScores() {
        ScoreFrame thescoreframe = new ScoreFrame();
        scoreWriter = new ScoreWriter(null, thescoreframe);
        thescoreframe.ScoreBuilder(scoreWriter);
        scoreWriter.createScoreList();
        scoreWriter.displayScores();

    }

    //Game update method
    public void update() {
        //Keeps count of the total asteroids and enemies
        CURRENT_ASTEROID_COUNT = 0;
        CURRENT_SAUCER_COUNT = 0;

        //Alive list to store alive in game objects
        alive = new ArrayList<>();
        if (!gamepause) {
            synchronized (Game.class) {
                SoundManager.startMusic();
                //calls each objects update method
                for (GameObject obj : objects) {
                    obj.update();
                    if (!obj.dead && powman != null) {
                        alive.add(powman);
                        powman = null;
                    }
                    //add each alive object to the alive list
                    if (!obj.dead) alive.add(obj);

                    //if bullet fired, add to objects
                    if (PlayerShip.bullet != null) {
                        alive.add(PlayerShip.bullet);
                        PlayerShip.bullet = null;
                    }
                    //if small asteroids produced, add to alive list
                    if (!Asteroid.smallAsteroids.isEmpty()) {
                        alive.addAll(Asteroid.smallAsteroids);
                        Asteroid.smallAsteroids.clear();
                    }
                }
                objects.clear();
                objects.addAll(alive);

                //Check collisions between all objects in the objects list
                for (GameObject obj1 : objects) {
                    for (GameObject obj2 : objects) {
                        obj1.collisionHandling(obj2);
                    }
                }

                //Count the amount of asteroids and saucers alive in game
                for (GameObject a : alive) {
                    for (GameObject b : alive) {
                        if (a.getClass() == Asteroid.class && b.getClass() == Asteroid.class) {
                            if (!a.equals(b)) {
                                if (!b.equals(a)) {
                                    asteroidInteract(a, b);
                                }
                            }

                        }
                    }
                    if (a instanceof Asteroid) {
                        CURRENT_ASTEROID_COUNT++;
                    } else if (a instanceof Saucer) {
                        CURRENT_SAUCER_COUNT++;
                    }
                }
            }

            if (CURRENT_ASTEROID_COUNT == 0 && CURRENT_SAUCER_COUNT == 0 && !gameover && !gamewin) {
                nextlvl();
            }
        }
    }

    //Update the particles
    public void updateParticles() {
        if (!gamepause) {
            if (!createdParticles.isEmpty()) {
                particles.addAll(createdParticles);
                createdParticles.clear();
            }
            ArrayList<Particle> livingParticles = new ArrayList<>();
            for (Particle p : particles) {
                if (!p.dead) {
                    livingParticles.add(p);
                    p.update();
                }
            }
            particles.clear();
            particles.addAll(livingParticles);
            livingParticles.clear();
        }
    }

    //method to create next level
    private void nextlvl() {
        objects.clear();
        LEVEL++;
        SCORE += 1000;

        //Bossfight at level 4
        if (LEVEL == 4) {
            bossfight = true;
            setBossfight();
        }

        //Game complete at level 5
        if (LEVEL >= 5) {
            gamewin = true;
            winnerString();
        }

        //Initialise the next level, increment the asteroids by 1 and increment the saucers by 2
        if (LEVEL < 5) {
            deathField = new DeathField();
            objects.add(deathField);
            objects.add(playerShip);
            Constants.N_INITIAL_SAUCERS += 1;
            Constants.N_INITIAL_ASTEROIDS += 2;
            for (int i = 0; i < Constants.N_INITIAL_ASTEROIDS; i++) {
                objects.add(Asteroid.makeRandomAsteroid(40));
            }
            if (LEVEL < 4) {
                for (int j = 0; j < Constants.N_INITIAL_SAUCERS; j++) {
                    saucer = Saucer.makeSaucer(playerShip, SAUCER_HP + LEVEL);
                    objects.add(saucer);
                }
            }
            playerShip.respawn();
        }
    }
}
