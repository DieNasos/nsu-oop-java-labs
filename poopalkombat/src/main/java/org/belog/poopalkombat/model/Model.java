package org.belog.poopalkombat.model;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

import org.belog.poopalkombat.model.entity.Fighter;
import org.belog.poopalkombat.model.gamescenes.GameScene;
import org.belog.poopalkombat.view.*;
import org.belog.poopalkombat.controller.*;

public class Model {
    //  other MVC parts
    private View view;
    private Controller controller;

    public enum KEYS { NULL, UP, DOWN, LEFT, RIGHT, W, A, S, D, U, H, J, K, ENTER, ESCAPE, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DOT }
    // available control keys names
    // NULL == 'empty' key just for some initialization

    protected HashMap<Model.KEYS, Boolean> keysPressedMap;
    // <key, true> == key is pressed
    // <key, false> == key is NOT pressed

    public enum COLORS { BLACK, RED, BLUE, YELLOW }
    // available colors

    // game scenes
    private HashMap<String, GameScene> scenes;
    private String currentSceneName;
    private String previousSceneName;

    // properties (with scene classes names)
    private Properties properties;

    // fighters
    private Fighter[] fighters;
    private String[] fightersNames;  // names of chosen fighters
    private int winnerID = 0;

    // online mode
    private boolean isOnlineMode = false;
    // false == playing on one computer
    // true == playing on two computers
    private boolean isServer;
    // true == server
    // false == client
    private boolean isConnected;
    private boolean chose;
    // true == we chose our fighter
    // false == we did not
    private boolean isWinner = true;    // for now

    public Model(String gameSceneName) throws IOException {
        fightersNames = new String[2];

        // loading properties
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/properties/scenes.properties"));

        // creating game scenes map
        scenes = new HashMap<String, GameScene>();

        // creating keys states map
        keysPressedMap = new HashMap<Model.KEYS, Boolean>();
        // setting all keys unpressed
        setKeysPressed(false);

        this.currentSceneName = gameSceneName;  // setting first scene's name
    }

    // MVC parts setters
    public void setView(View view) { this.view = view; }
    public void setController(Controller controller) { this.controller = controller; }

    // MVC parts getters
    public View getView() { return view; }
    public Controller getController() { return controller; }

    // scenes

    public GameScene getCurrentScene() { return scenes.get(currentSceneName); }

    public void setScene(String gameSceneName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!scenes.containsKey(gameSceneName)) {
            // scene with such a name is not registered -> registering new scene

            // creating game scene by its name
            GameScene gameScene = createGameScene(gameSceneName);

            // registering scene
            scenes.put(gameSceneName, gameScene);
        }

        // setting scene as current
        previousSceneName = currentSceneName;
        currentSceneName = gameSceneName;
    }

    public void setScene(String gameSceneName, GameScene scene) {
        // putting scene in scenes map
        scenes.put(gameSceneName, scene);

        // setting scene as current
        previousSceneName = currentSceneName;
        currentSceneName = gameSceneName;
    }

    public void setPreviousScene() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        setScene(previousSceneName);
    }

    private void checkCurrentSceneExistence() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // view may want to work with current scene when it's not initialized yet =>
        // => so we must check its existence to not admit null-pointer-exception

        if (scenes.isEmpty()) {
            // there are no scenes in scenes map
            // => must set current scene
            setScene(currentSceneName);    // setting the first scene
        }
    }

    // deleting scene from scenes map
    public void destroyScene(String sceneName) { scenes.remove(sceneName); }

    private GameScene createGameScene(String gameSceneName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // getting scene object by class name
        String gameSceneClassName = properties.getProperty(gameSceneName);
        Class<GameScene> gameSceneClass = (Class<GameScene>) Class.forName(gameSceneClassName);
        Constructor<GameScene> gameSceneConstructor = gameSceneClass.getConstructor(Model.class);
        return gameSceneConstructor.newInstance(this);   // creating and returning scene
    }

    public void update() {
        try {
            // updating current scene
            scenes.get(currentSceneName).update();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        checkCurrentSceneExistence();
        // scenes draw themselves using view class
        // no graphic work in model-part of program
        try {
            scenes.get(currentSceneName).draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // control methods

    public void keyPressed(KEYS key) {
        keysPressedMap.put(key, true);   // key is pressed
    }

    public void keyReleased(KEYS key) {
        keysPressedMap.put(key, false);  // key is un-pressed
    }

    private void setKeysPressed(Boolean state) {
        // setting state to all keys in map
        for (Model.KEYS key : Model.KEYS.values()) {
            keysPressedMap.put(key, state);
        }
    }

    public boolean isKeyPressed(Model.KEYS key) { return keysPressedMap.get(key); }

    // background parameters getters

    public String getBackgroundImageFileName() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        checkCurrentSceneExistence();
        return scenes.get(currentSceneName).getBackgroundImageFileName();
    }

    public double getBackgroundX() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        checkCurrentSceneExistence();
        return scenes.get(currentSceneName).getBackgroundX();
    }

    public double getBackgroundY() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        checkCurrentSceneExistence();
        return scenes.get(currentSceneName).getBackgroundY();
    }

    // winner
    public int getWinnerID() { return winnerID; }
    public void setWinnerID(int id) { winnerID = id; }
    public boolean isWinner() { return isWinner; }
    public void setIsWinner(boolean isWinner) { this.isWinner = isWinner; }

    // online mode

    // isOnlineMode
    public boolean isOnlineMode() { return isOnlineMode; }
    public void setIsOnlineMode(boolean isOnlineMode) { this.isOnlineMode = isOnlineMode; }
    // isServer
    public boolean isServer() { return isServer; }
    public void setIsServer(boolean isServer) { this.isServer = isServer; }
    // isConnected
    public boolean isConnected() { return isConnected; }
    public void setIsConnected(boolean isConnected) { this.isConnected = isConnected; }
    // chose
    public boolean getChose() { return chose; }
    public void setChose(boolean chose) { this.chose = chose; }

    // fighters

    public Fighter getFighter(int index) { return fighters[index]; }

    public void createFighters() {
        // creating and setting fighters
        fighters = new Fighter[2];
        fighters[0] = new Fighter(getCurrentScene(), fightersNames[0], true);
        fighters[0].setFacingRight();
        fighters[1] = new Fighter(getCurrentScene(), fightersNames[1], false);
        fighters[1].setFacingLeft();

        // binding fighters together
        fighters[0].setOtherFighter(fighters[1]);
        fighters[1].setOtherFighter(fighters[0]);
    }

    // fighters names setter/getter
    public void setFightersNames(String[] fightersNames) { this.fightersNames = fightersNames; }
    public String[] getFightersNames() { return fightersNames; }
}