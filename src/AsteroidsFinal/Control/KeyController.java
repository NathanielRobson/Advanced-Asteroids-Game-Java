package AsteroidsFinal.Control;

import AsteroidsFinal.Game.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter implements Controller {

    public Action action;
    Game game;

    public KeyController(Game game) {
        this.game = game;
        action = new Action();
    }

    public Action action() {
        return action;
    }

    @Override
    public Action getLastAction() {
        return action;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                action.thrust = 1;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                action.turn = -1;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                action.turn = +1;
                break;
            case KeyEvent.VK_T:
                action.teleport = true;
                break;
            case KeyEvent.VK_B:
                action.bomb = true;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = true;
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_P:
                Game.TogglePaused();
                break;
            case KeyEvent.VK_R:
                Game.restart();
                Game.restart = true;
                break;
            case KeyEvent.VK_H:
                Game.keyhelp = !Game.keyhelp;
                break;
            case KeyEvent.VK_ENTER:
                Game.begin = true;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                action.thrust = 0;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                action.turn = 0;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                action.turn = 0;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = false;
                //If the game has Ended or just begun allow the Enter Key to Restart the game
                break;
            case KeyEvent.VK_T:
                action.teleport = false;
                break;
            case KeyEvent.VK_B:
                action.bomb = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}