package org.belog.poopalkombat.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.belog.poopalkombat.model.*;
import org.belog.poopalkombat.view.*;

public class Controller implements KeyListener {
    //  other MVC parts
    private Model model;
    private View view;

    public Controller() {}

    // MVC parts setters
    public void setModel(Model model) { this.model = model; }
    public void setView(View view) { this.view = view; }

    // MVC parts getters
    public Model getModel() { return model; }
    public View getView() { return view; }

    private Model.KEYS getModelKey(KeyEvent key) {
        // converting KeyEvent key to Model.KEYS modelKey

        // getting integer code of key
        int keyCode = key.getKeyCode();

        // Model.KEYS.NULL == "empty" key
        Model.KEYS modelKey = Model.KEYS.NULL;

        switch (keyCode) {
            // setting value according to keyCode
            case KeyEvent.VK_UP:
                modelKey = Model.KEYS.UP;
                break;
            case KeyEvent.VK_DOWN:
                modelKey = Model.KEYS.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                modelKey = Model.KEYS.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                modelKey = Model.KEYS.RIGHT;
                break;
            case KeyEvent.VK_W:
                modelKey = Model.KEYS.W;
                break;
            case KeyEvent.VK_A:
                modelKey = Model.KEYS.A;
                break;
            case KeyEvent.VK_S:
                modelKey = Model.KEYS.S;
                break;
            case KeyEvent.VK_D:
                modelKey = Model.KEYS.D;
                break;
            case KeyEvent.VK_U:
                modelKey = Model.KEYS.U;
                break;
            case KeyEvent.VK_H:
                modelKey = Model.KEYS.H;
                break;
            case KeyEvent.VK_J:
                modelKey = Model.KEYS.J;
                break;
            case KeyEvent.VK_K:
                modelKey = Model.KEYS.K;
                break;
            case KeyEvent.VK_ENTER:
                modelKey = Model.KEYS.ENTER;
                break;
            case KeyEvent.VK_ESCAPE:
                modelKey = Model.KEYS.ESCAPE;
                break;
            case KeyEvent.VK_0:
                modelKey = Model.KEYS.ZERO;
                break;
            case KeyEvent.VK_1:
                modelKey = Model.KEYS.ONE;
                break;
            case KeyEvent.VK_2:
                modelKey = Model.KEYS.TWO;
                break;
            case KeyEvent.VK_3:
                modelKey = Model.KEYS.THREE;
                break;
            case KeyEvent.VK_4:
                modelKey = Model.KEYS.FOUR;
                break;
            case KeyEvent.VK_5:
                modelKey = Model.KEYS.FIVE;
                break;
            case KeyEvent.VK_6:
                modelKey = Model.KEYS.SIX;
                break;
            case KeyEvent.VK_7:
                modelKey = Model.KEYS.SEVEN;
                break;
            case KeyEvent.VK_8:
                modelKey = Model.KEYS.EIGHT;
                break;
            case KeyEvent.VK_9:
                modelKey = Model.KEYS.NINE;
                break;
            case KeyEvent.VK_PERIOD:
                modelKey = Model.KEYS.DOT;
                break;
            default:
                break;
        }

        return modelKey;
    }

    // KeyListener's methods

    @Override
    public void keyTyped(KeyEvent key) {}

    @Override
    public void keyPressed(KeyEvent key) {
        // sending modelKey to model
        this.model.keyPressed(getModelKey(key));
    }

    @Override
    public void keyReleased(KeyEvent key) {
        // sending modelKey to model
        this.model.keyReleased(getModelKey(key));
    }
}