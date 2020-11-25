package org.belog.poopalkombat.model.gamescenes;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.belog.poopalkombat.model.*;
import org.belog.poopalkombat.model.net.client.TCPClient;

public class JoinSessionScene extends GameScene {
    // client
    private TCPClient client;

    // server's attributes
    private String ip;
    private String port;

    private boolean isIPTyped = false;

    public JoinSessionScene(Model model) {
        try {
            this.model = model;	// binding scene with its model
            this.view = model.getView();	// binding scene with its model's view

            // creating background
            bg = new Background(this,"/backgrounds/background_with_logo.gif", 1);
            bg.setOffset(0.1, 0);

            // creating empty strings for ip and port
            ip = "";
            port = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // updating background
        bg.update();

        // checking if there are pressed keys

        if (model.isKeyPressed(Model.KEYS.ESCAPE)) {
            model.setPreviousScene();   // back
            model.keyReleased(Model.KEYS.ESCAPE);	// setting key un-pressed
        }

        if (model.isKeyPressed(Model.KEYS.ENTER)) {
            if (!isIPTyped) {
                // IP is typed
                isIPTyped = true;
            } else {
                // IP and port are typed
                join();
            }
            model.keyReleased(Model.KEYS.ENTER);	// setting key un-pressed
        }

        if (model.isKeyPressed(Model.KEYS.ZERO)) {
            // typing number
            if (!isIPTyped) { ip += '0'; }
            else { port += '0'; }
            model.keyReleased(Model.KEYS.ZERO);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.ONE)) {
            // typing number
            if (!isIPTyped) { ip += '1'; }
            else { port += '1'; }
            model.keyReleased(Model.KEYS.ONE);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.TWO)) {
            // typing number
            if (!isIPTyped) { ip += '2'; }
            else { port += '2'; }
            model.keyReleased(Model.KEYS.TWO);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.THREE)) {
            // typing number
            if (!isIPTyped) { ip += '3'; }
            else { port += '3'; }
            model.keyReleased(Model.KEYS.THREE);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.FOUR)) {
            // typing number
            if (!isIPTyped) { ip += '4'; }
            else { port += '4'; }
            model.keyReleased(Model.KEYS.FOUR);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.FIVE)) {
            // typing number
            if (!isIPTyped) { ip += '5'; }
            else { port += '5'; }
            model.keyReleased(Model.KEYS.FIVE);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.SIX)) {
            // typing number
            if (!isIPTyped) { ip += '6'; }
            else { port += '6'; }
            model.keyReleased(Model.KEYS.SIX);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.SEVEN)) {
            // typing number
            if (!isIPTyped) { ip += '7'; }
            else { port += '7'; }
            model.keyReleased(Model.KEYS.SEVEN);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.EIGHT)) {
            // typing number
            if (!isIPTyped) { ip += '8'; }
            else { port += '8'; }
            model.keyReleased(Model.KEYS.EIGHT);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.NINE)) {
            // typing number
            if (!isIPTyped) { ip += '9'; }
            else { port += '9'; }
            model.keyReleased(Model.KEYS.NINE);  // setting key un-pressed
        }
        if (model.isKeyPressed(Model.KEYS.DOT)) {
            // typing number
            if (!isIPTyped) { ip += '.'; }
            model.keyReleased(Model.KEYS.DOT);  // setting key un-pressed
        }
    }

    @Override
    public void draw() throws IOException, FontFormatException {
        // draw text

        String str1 = "ip + [ENTER] + port + [ENTER] == join session";
        String str2 = "[ESC] for cancel";
        String str3 = "IP: " + ip;
        String str4 = "port: " + port;

        view.drawString(str1, "lobster", Model.COLORS.BLACK, 34, 100, 450);
        view.drawString(str2, "lobster", Model.COLORS.BLACK, 34, 100, 500);
        if (!isIPTyped) {
            view.drawString(str3, "lobster", Model.COLORS.BLUE, 34, 100, 600);
        } else {
            view.drawString(str3, "lobster", Model.COLORS.RED, 34, 100, 600);
        }
        view.drawString(str4, "lobster", Model.COLORS.BLUE, 34, 100, 650);
    }

    private void join() {
        try {
            Thread client = new Thread(new TCPClient(ip, Integer.valueOf(port), model));
            client.start();
            // client.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}