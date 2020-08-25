package AsteroidsFinal.Control;

public class Action {
    public static boolean shield; //protects the player
    public int thrust; // 0 = off, 1 = on
    public int turn; // -1 = left turn, 0 = no turn, 1 = right turn
    public boolean shoot; //make bullet
    public boolean teleport; //teleports the player to a random position
    public boolean bomb; //Kills all enemies on screen


    @Override
    public String toString() {
        return "Action: " + thrust + "," + turn + "," + shoot + ")";
    }

}
