package org.belog.poopalkombat.model.gamescenes;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.belog.poopalkombat.model.*;
import org.belog.poopalkombat.model.net.server.TCPServer;

public class NewSessionScene extends GameScene {
    // server
    private TCPServer server;
    private String ip;
    private int port = 2048;

    public NewSessionScene(Model model) {
        try {
            this.model = model;	// binding scene with its model
            this.view = model.getView();	// binding scene with its model's view

            // creating background
            bg = new Background(this,"/backgrounds/background_with_logo.gif", 1);
            bg.setOffset(0.1, 0);

            // getting current machine's IP address
            ip = TCPServer.getIP();

            try {
                // creating and starting server
                server = new TCPServer(port, model);
                Thread serverThread = new Thread(server);
                serverThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            try {
                server.close(); // stopping server
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.setPreviousScene();   // back
            model.keyReleased(Model.KEYS.ENTER);	// setting key un-pressed
        }
    }

    @Override
    public void draw() throws IOException, FontFormatException {
        // draw text
        String str1 = "waiting for opponent...";
        String str2 = "IP: " + ip;
        String str3 = "port: " + port;
        view.drawString(str1, "lobster", Model.COLORS.BLACK, 34, 100, 450);
        view.drawString(str2, "lobster", Model.COLORS.BLUE, 34, 100, 525);
        view.drawString(str3, "lobster", Model.COLORS.BLUE, 34, 100, 575);
        view.drawString("back", "lobster", Model.COLORS.RED, 34, 100, 650);
    }
}