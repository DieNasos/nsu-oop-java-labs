package org.belog.poopalkombat.model.gamescenes;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.belog.poopalkombat.model.*;

public class RoleChoiceScene extends GameScene {
    private int currentChoice = 0;  // id of current option

    // available options
    private String[] options = {
            "start new session",
            "join session",
            "back"
    };

    public RoleChoiceScene(Model model) {
        try {
            this.model = model;	// binding scene with its model
            this.view = model.getView();	// binding scene with its model's view

            // creating background
            bg = new Background(this,"/backgrounds/background_with_logo.gif", 1);
            bg.setOffset(-0.1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // updating background
        bg.update();

        // checking if there are pressed keys

        if (model.isKeyPressed(Model.KEYS.ENTER)) {
            select();	// selecting option

            model.keyReleased(Model.KEYS.ENTER);	// setting key un-pressed
        }

        if ((model.isKeyPressed(Model.KEYS.UP)) || (model.isKeyPressed(Model.KEYS.W))) {
            currentChoice--;	// moving to upper option
            if (currentChoice == -1) {
                currentChoice = options.length - 1;	// reached the top border
            }

            // setting keys un-pressed
            model.keyReleased(Model.KEYS.UP);
            model.keyReleased(Model.KEYS.W);
        }

        if ((model.isKeyPressed(Model.KEYS.DOWN)) || (model.isKeyPressed(Model.KEYS.S))) {
            currentChoice++;	// moving to lower option
            if (currentChoice == options.length) {
                currentChoice = 0;	// reached the bottom border
            }

            // setting keys un-pressed
            model.keyReleased(Model.KEYS.DOWN);
            model.keyReleased(Model.KEYS.S);
        }
    }

    @Override
    public void draw() throws IOException, FontFormatException {
        // draw options
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                view.drawString(options[i], "lobster", Model.COLORS.RED, 34, 100, 500 + i * 42);
            } else {
                view.drawString(options[i], "lobster", Model.COLORS.BLACK, 34, 100, 500 + i * 42);
            }
        }
    }

    private void select() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        switch (currentChoice) {
            case 0:
                model.setScene("NEW_SESSION_SCENE");
                break;
            case 1:
                model.setScene("JOIN_SESSION_SCENE");
                break;
            case 2:
                model.setScene("PVP_MENU_SCENE");
                break;
            default:
                break;
        }
    }
}