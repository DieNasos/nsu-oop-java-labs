package org.belog.poopalkombat;

import java.io.IOException;

import org.belog.poopalkombat.exceptions.InvalidResolutionException;
import org.belog.poopalkombat.exceptions.NullMVCPartException;
import org.belog.poopalkombat.model.*;
import org.belog.poopalkombat.view.*;
import org.belog.poopalkombat.controller.*;

public class Game {
    // MVC parts
    Model model;
    View view;
    Controller controller;

    Game() throws IOException {
        // creating MVC parts (except of view!)
        model = new Model("MAIN_MENU_SCENE");
        controller = new Controller();

        // binding model and controller together
        model.setController(controller);
        controller.setModel(model);
    }

    public void play() throws IOException, NullMVCPartException, InvalidResolutionException {
        // creation of view == start of the game =>
        // => we must create it last
        view = new View(model, controller, "pOOPal Kombat", 1000, 700, 1);
    }
}