package org.belog.poopalkombat;

public class Main {
    public static void main(String[] args) {
        try {
            Game game = new Game();
            game.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}