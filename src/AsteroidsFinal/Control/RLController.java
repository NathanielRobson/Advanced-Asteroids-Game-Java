package AsteroidsFinal.Control;

import AsteroidsFinal.Game.Game;
import AsteroidsFinal.GameObjects.Bullet;
import AsteroidsFinal.GameObjects.GameObject;
import AsteroidsFinal.GameObjects.Ships.HelperPod;
import AsteroidsFinal.GameObjects.Ships.PlayerShip;
import utilities.Vector2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class RLController implements Controller {


    final static int NUM_STATES = 27; //Depends on the conditions we use to define the state space
    static final int[] fakeFeatures = {0, 2, 2};
    static final int fakeIdx = 80;
    final static int NUM_ACTIONS = 3;//Depends on how we can parametrize the lower level controller
    //******************************************
    //we saw these parameters and values in lecture 7

    //QVal represent the value of taking a specific in a specific state (lecture 7)
    // this is only an estimation computed on the base of the agent experience. It will change with learning
    //other parameters used for this version of the game
    private static final double NEAR = 150;
    private static final double VERYNEAR = 50;
    private static final double discount = 0.99;
    private static final double learning_fact = 0.1;
    // no time to explain these.. but they determine for how long the pod will behave quite randomly (exploring strategies) at the beginning
    private static final double explorationFactor = 1;
    // note that the qVal are static. This means that all the pods will have the same respons if they are in the same condition
    // they will also share their experience and learn faster
    private static double[] qVal = null;
    private static int[] counts = null;
    private static int[] countsXState = null;
    // same is true for the other elements in the game...
    public final Game game;
    SeekNShootTarget lowerLevelController;

    State currentState, oldState;
    int oldActionIndex = 0;
    private Action action = new Action();
    //to take take its own decision the controller may need access to the pod state and to mothership that droped it
    private HelperPod pod;
    //*********************************************
    private PlayerShip ship;
    private double oldV;
    private int currentActionIndex;
    // we let the pod inactive for some time after being drop
    private long activationTime;


    public RLController(Game pgame, PlayerShip pship) {
        game = pgame;
        ship = pship;
        //wait for half second before starting the pod
        activationTime = System.currentTimeMillis() + 500;
        oldState = new State();
        currentState = new State();

        //loading q values from files
        if (qVal == null) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("QVal.ser"));
                qVal = (double[]) in.readObject();
                in.close();
                if (qVal.length != (NUM_STATES * NUM_ACTIONS)) throw new Exception();
                System.out.println("QVal:" + Arrays.toString(qVal));
            } catch (Exception dad) {
                qVal = new double[NUM_STATES * NUM_ACTIONS];
            }
        }
        double[] trace = new double[NUM_STATES * NUM_ACTIONS];

        if (counts == null || countsXState == null) {

            try {
                ObjectInputStream in1 = new ObjectInputStream(new FileInputStream("Counts.ser"));
                counts = (int[]) in1.readObject();
                in1.close();
                in1 = new ObjectInputStream(new FileInputStream("CountsXState.ser"));
                countsXState = (int[]) in1.readObject();
                in1.close();
                if (counts.length != (NUM_STATES * NUM_ACTIONS) || (countsXState.length != NUM_STATES))
                    throw new Exception();
            } catch (Exception edasdx) {
                counts = new int[NUM_STATES * NUM_ACTIONS];
                countsXState = new int[NUM_STATES];
            }
        }

    }

    // call this before the game exits
    public static void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("QVal.ser")
            );
            out.writeObject(qVal);
            out.flush();
            out.close();


        } catch (Exception dad) {

        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("Counts.ser")
            );
            out.writeObject(counts);
            out.flush();
            out.close();


        } catch (Exception dad) {

        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("CountsXState.ser")
            );
            out.writeObject(countsXState);
            out.flush();
            out.close();


        } catch (Exception dad) {

        }

        System.out.println("QVal:" + Arrays.toString(qVal));

    }

    public void setPod(HelperPod helperPod) {
        pod = helperPod;
        //crates a low level controller controlling the pod
    }

    @Override
    public Action action() {
        return null;
    }

    @Override
    public Action getLastAction() {
        return null;
    }

    class State {
        int[] featureVec;
        int index;
        GameObject nearToShip, nearToPod;

        State() {
            index = fakeIdx;
            featureVec = fakeFeatures;
        }

        State(State ori) {
            index = ori.index;
            featureVec = ori.featureVec.clone();
        }

        void updateStateAndFeatures() {
            int[] featureVec1 = {
                    featureNearestObject(pod),
                    featureAlive(pod),
                    featureAlive(ship)};
            featureVec = featureVec1;
            index = 0;
            for (int i = 0; i < featureVec.length; i++) {
                index = featureVec[i] + 3 * index;
            }
        }

        boolean isNearestObjectNear(GameObject obj) {
            if (obj == pod) {
                return featureVec[0] == 1;
            }
            return false;

        }

        boolean isNearestObjectVeryNear(GameObject obj) {
            if (obj == pod) {
                return featureVec[0] == 0;
            }

            return false;
        }

        boolean isDead(GameObject obj) {
            if (obj == pod) {
                return featureVec[2] == 0;
            } else if (obj == ship) {
                return featureVec[3] == 0;
            }
            return false;
        }

        int featureAlive(GameObject obj) {
            if (obj.dead) return 0;
            else return 1;
        }

        GameObject getNearestEnemy(Vector2D pos) {
            GameObject result = null;
            double bestdist = Double.MAX_VALUE;
            for (GameObject object : Game.objects) {
                if (!(object instanceof HelperPod) && (object != ship) && !(object instanceof Bullet)) {
                    double d = pos.dist(object.position);
                    if (d < bestdist) {
                        result = object;
                        bestdist = d;
                    }
                }
            }
            return result;
        }

        int featureNearestObject(GameObject obj) {

            GameObject nearestEnemy = getNearestEnemy(obj.position);
            if (nearestEnemy == pod || nearestEnemy == ship)
                System.out.println("error");
            if (obj == pod) {
                nearToPod = nearestEnemy;
            }
            if (nearestEnemy != null) {
                double d = obj.position.dist(nearestEnemy.position);
                if (d < 0 || d > NEAR) return 2;
                if (d < VERYNEAR) return 0;
                return 1;

            }
            return 2;

        }

    }

}