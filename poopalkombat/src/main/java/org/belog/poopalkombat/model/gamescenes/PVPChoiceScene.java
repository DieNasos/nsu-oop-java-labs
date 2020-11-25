package org.belog.poopalkombat.model.gamescenes;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.belog.poopalkombat.model.*;

public class PVPChoiceScene extends GameScene {
    private int currentChoice = 0;  // id of current option

    // available options
    private String[] options = {
            "Stepan",
            "Dude",
            "Mark",
            "Max",
            "back"
    };

    // fighters
    private String[] fightersNames;
    private int choicesCounter = 0;

    public PVPChoiceScene(Model model) {
        try {
            this.model = model;	// binding scene with its model
            this.view = model.getView();	// binding scene with its model's view
            // creating background
            bg = new Background(this,"/backgrounds/background.gif", 1);
            bg.setOffset(0, 0);

            // creating strings for fighters names
            fightersNames = new String[2];
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
        if (model.getChose()) {
            // we chose -> waiting for opponent (drawing no options)
            view.drawString("waiting for opponent...", "lobster", Model.COLORS.BLACK, 34, 150, 350);
            return;
        }

        if (choicesCounter < 2) {
            // draw hint
            if (!model.isOnlineMode()) {
                // offline mode
                view.drawString("player " + String.valueOf(choicesCounter + 1) + ", choose your fighter", "lobster", Model.COLORS.BLACK, 34, 200, 100);
            } else {
                // online mode
                view.drawString("choose your fighter", "lobster", Model.COLORS.BLACK, 34, 200, 100);
            }
        }

        if (currentChoice < options.length - 1) {
            // if not choosing 'back' option => drawing current fighter
            view.drawImage("/sprites/" + options[currentChoice] + ".gif", 0, 0, 175, 350, 400, 200);
        }

        // draw options
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                if (choicesCounter == 0) {
                    view.drawString(options[i], "lobster", Model.COLORS.RED, 34, 200, 350 + i * 42);
                } else {
                    view.drawString(options[i], "lobster", Model.COLORS.BLUE, 34, 200, 350 + i * 42);
                }
            } else {
                view.drawString(options[i], "lobster", Model.COLORS.BLACK, 34, 200, 350 + i * 42);
            }
        }
    }

    private void select() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (currentChoice == options.length - 1) {
            choicesCounter = 0;
            model.setPreviousScene();   // => back
            return;
        }

        // choosing fighter
        fightersNames[choicesCounter] = options[currentChoice];
        choicesCounter++;

        if (model.isOnlineMode()) {
            // online mode
            model.setChose(true); // we chose and waiting for opponent
            view.stopMusic();   // music stops
            choicesCounter = 0; // resetting number of choices
            model.setFightersNames(fightersNames);  // setting fighters names
            // model.setScene("PVP_FIGHT_SCENE");  // => play
        }

        if (choicesCounter > 1 && !model.isOnlineMode()) {
            // offline mode
            // second fighter was chosen
            view.stopMusic();   // music stops
            choicesCounter = 0; // resetting number of choices
            model.setFightersNames(fightersNames);  // setting fighters names
            model.setScene("PVP_FIGHT_SCENE");  // => play
        }
    }
}