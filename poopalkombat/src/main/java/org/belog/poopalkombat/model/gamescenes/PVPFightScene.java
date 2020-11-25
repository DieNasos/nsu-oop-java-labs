package org.belog.poopalkombat.model.gamescenes;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.belog.poopalkombat.model.Background;
import org.belog.poopalkombat.model.Model;

public class PVPFightScene extends GameScene {
    public PVPFightScene(Model model) {
        try {
            this.model = model;	// binding scene with its model
            this.view = model.getView();	// binding scene with its model's view
            // creating background
            bg = new Background(this,"/backgrounds/background.gif", 1);
            bg.setOffset(0, 0);

            // creating fighters
            model.createFighters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // checking if playing music
        if (!view.isMusicPlaying()) {
            // not playing => start playing
            view.playMusic("/music/voroniny_hard_flexxx.wav");
        }

        // updating background
        bg.update();

        // updating fighters
        model.getFighter(0).update();
        model.getFighter(1).update();

        // checking if ESCAPE is pressed
        if (model.isKeyPressed(Model.KEYS.ESCAPE)) {
            view.stopMusic();   // stop playing music
            if (!model.isOnlineMode()) {
                // offline mode =>
                model.setScene("PAUSE_SCENE");  // pause
            } else {
                // online mode =>
                model.setIsConnected(false);    // disconnecting
                model.setScene("MAIN_MENU_SCENE");  // exit
            }
            model.keyReleased(Model.KEYS.ESCAPE);   // setting key un-pressed
        }

        if (model.getWinnerID() != 0 && !model.isOnlineMode()) {
            // for offline mode only!
            // there is winner == end of fight
            view.stopMusic();   // stop playing music
            model.setScene("WIN_SCENE");
        }
    }

    @Override
    public void draw() throws IOException, FontFormatException {
        // drawing hints
        if (!model.isOnlineMode()) {
            // offline mode => printing control keys for both players + [ESC]'s role is PAUSE
            view.drawString("[W] | [U] == jump", "lobster", Model.COLORS.BLACK, 34, 21, 42);
            view.drawString("[A]/[S] | [H]/[K] == left/right", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 2);
            view.drawString("[S] | [J] == punch", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 3);
            view.drawString("[ESC] == pause", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 4);
        } else {
            // online mode => printing control keys for current player + [ESC]'s role is EXIT
            if (model.isServer()) {
                // W-A-S-D for server
                view.drawString("[W] == jump", "lobster", Model.COLORS.BLACK, 34, 21, 42);
                view.drawString("[A]/[S] == left/right", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 2);
                view.drawString("[S] == punch", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 3);

            } else {
                // U-H-J-K for client
                view.drawString("[U] == jump", "lobster", Model.COLORS.BLACK, 34, 21, 42);
                view.drawString("[H]/[K] == left/right", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 2);
                view.drawString("[J] == punch", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 3);
            }
            view.drawString("[ESC] == exit", "lobster", Model.COLORS.BLACK, 34, 21, 42 * 4);
        }
        view.drawString("health", "lobster", Model.COLORS.RED, 34, 21, 42 * 5);
        view.drawString("ult lvl", "lobster", Model.COLORS.BLUE, 34, 21, 42 * 6);

        // drawing fighters
        model.getFighter(0).draw();
        model.getFighter(1).draw();
    }
}