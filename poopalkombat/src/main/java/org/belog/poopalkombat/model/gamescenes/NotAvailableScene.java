package org.belog.poopalkombat.model.gamescenes;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.belog.poopalkombat.model.*;

public class NotAvailableScene extends GameScene {
    public NotAvailableScene(Model model) {
        try {
            this.model = model;	// binding scene with its model
            this.view = model.getView();	// binding scene with its model's view
            // creating background
            bg = new Background(this,"/backgrounds/background_with_logo.gif", 1);
            bg.setOffset(0, 0);
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
            model.setPreviousScene();	// selecting option 'back'
            model.keyReleased(Model.KEYS.ENTER);	// setting key un-pressed
        }
    }

    @Override
    public void draw() throws IOException, FontFormatException {
        // draw text & 'back' option
        view.drawString("sorry, this option is not available yet...", "lobster", Model.COLORS.BLACK, 34, 100, 500);
        view.drawString("back", "lobster", Model.COLORS.RED, 34, 100, 500 + 2 * 42);
    }
}