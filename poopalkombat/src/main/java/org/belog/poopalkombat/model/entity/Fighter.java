package org.belog.poopalkombat.model.entity;

import java.awt.*;
import java.io.IOException;

import org.belog.poopalkombat.model.Model;
import org.belog.poopalkombat.model.gamescenes.GameScene;
import org.belog.poopalkombat.model.net.message.FighterParameters;
import org.belog.poopalkombat.view.Animation;

public class Fighter {
    private GameScene scene;    // scene that fighter belongs to

    private String spritesheetFileName; // sprites

    private boolean isFirst;    // is this fighter the first (of two)?

    private Fighter otherFighter;   // opponent

    // position coordinates
    private int x;
    private int y;

    // size (pixels)
    private int width = 175;
    private int height = 350;

    // moving
    private int moveSpeed = 10;

    // jumping
    private boolean isJumping = false;
    private int maxJumpSpeed = 20;
    private int minJumpSpeed = 5;
    private int currJumpSpeed = maxJumpSpeed;
    private int jumpHeight = 125;   // from top border

    // gravity
    private boolean isFalling = false;
    private int maxFallingSpeed = 10;
    private int minFallingSpeed = 5;
    private int currFallingSpeed = minFallingSpeed;
    private int groundLevelHeight = 700;    // from top border

    // fighter's health/damage
    private int health = 100;
    private int damage = 5;

    // punching
    private boolean isPunching;

    // ult
    private int maxUltLvl = 100;
    private int currUltLvl = 0;
    private int ultStep = 10;
    private boolean isUlt;

    // time
    private int timeBetweenPunchesMs = 500;
    private double lastTimeMs;  // last time update

    private boolean facingRight = true; // default
    // true == fighter looks to the right
    // false fighter looks to the left

    private boolean isWinner = false;

    // animation
    private Animation animation;

    private enum DIRECTIONS { UP, DOWN, LEFT, RIGHT }
    // available directions for moving

    public Fighter(GameScene scene, String fighterName, boolean isFirst) {
        try {
            this.scene = scene; // binding fighter with its scene
            spritesheetFileName = "/sprites/" + fighterName + ".gif";   // getting spritesheet filename
            this.isFirst = isFirst;

            // setting fighter in position according to its serial number (first/second)
            if (isFirst) { x = 0; }
            else { x = scene.getView().get_width() - width; }
            y = scene.getView().get_height() - height;

            lastTimeMs = System.currentTimeMillis();    // pinpointing time

            // creating animation
            createAnimation();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAnimation() throws IOException {
        // creating animation
        animation = new Animation(spritesheetFileName);

        // adding frames
        for (int i = 0; i < 8; i++) {
            animation.addFrame(width * i, 0, width, height);
        }

        // setting current frame
        animation.setCurrentFrame(0, false);

        // setting should we reflect frames before drawing them
        if (isFirst) {
            animation.setShouldReflect(false);
        } else {
            animation.setShouldReflect(true);
        }
    }

    public void setOtherFighter(Fighter fighter) { otherFighter = fighter; }

    // 'facing'

    // 'facing' setters
    public void setFacingLeft() { facingRight = false; }
    public void setFacingRight() { facingRight = true; }

    private void checkFacing(boolean correctValue) {
        // checking if we're looking to the wrong direction
        if (facingRight != correctValue) {
            // inverting 'facing'
            facingRight = correctValue;
            // inverting 'should reflect' value
            animation.setShouldReflect(!animation.getShouldReflect());
        }
    }

    public void update() {
        // updating static (punch) animation
        animation.updateStatic();

        if (isJumping) {
            // flying up
            move(DIRECTIONS.UP);
        }
        if (isFalling) {
            // falling down
            move(DIRECTIONS.DOWN);
        }
        if (isPunching) {
            punch();    // punching opponent (or air)
        }

        // checking if keys are pressed
        // control is different for the first and the second fighter!
        // opponent's keys are blocked in online-mode
        if (isFirst) {
            if (scene.getModel().isKeyPressed(Model.KEYS.W)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && scene.getModel().isServer())) {
                    // offline mode OR (online mode AND server)
                    jump();
                }
            }
            if (scene.getModel().isKeyPressed(Model.KEYS.A)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && scene.getModel().isServer())) {
                    // offline mode OR (online mode AND server)
                    move(DIRECTIONS.LEFT);
                    if (!isJumping && !isFalling) {
                        animation.updateDynamic();  // updating dynamic (walking) animation
                    }
                }
            }
            if (scene.getModel().isKeyPressed(Model.KEYS.D)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && scene.getModel().isServer())) {
                    // offline mode OR (online mode AND server)
                    move(DIRECTIONS.RIGHT);
                    if (!isJumping && !isFalling) {
                        animation.updateDynamic();  // updating dynamic (walking) animation
                    }
                }
            }
            if (scene.getModel().isKeyPressed(Model.KEYS.S)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && scene.getModel().isServer())) {
                    // offline mode OR (online mode AND server)
                    isPunching = true;
                    scene.getModel().keyReleased(Model.KEYS.S); // setting key un-pressed
                }
            }
        } else {
            if (scene.getModel().isKeyPressed(Model.KEYS.U)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && !scene.getModel().isServer())) {
                    // offline mode OR (online mode AND client)
                    jump();
                }
            }
            if (scene.getModel().isKeyPressed(Model.KEYS.H)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && !scene.getModel().isServer())) {
                    // offline mode OR (online mode AND client)
                    move(DIRECTIONS.LEFT);
                    if (!isJumping && !isFalling) {
                        animation.updateDynamic();  // updating dynamic (walking) animation
                    }
                }
            }
            if (scene.getModel().isKeyPressed(Model.KEYS.K)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && !scene.getModel().isServer())) {
                    // offline mode OR (online mode AND client)
                    move(DIRECTIONS.RIGHT);
                    if (!isJumping && !isFalling) {
                        animation.updateDynamic();  // updating dynamic (walking) animation
                    }
                }
            }
            if (scene.getModel().isKeyPressed(Model.KEYS.J)) {
                if (!scene.getModel().isOnlineMode() || (scene.getModel().isOnlineMode() && !scene.getModel().isServer())) {
                    // offline mode OR (online mode AND client)
                    isPunching = true;  // punching
                    scene.getModel().keyReleased(Model.KEYS.J); // setting key un-pressed
                }
            }
        }
    }

    public void draw() throws IOException, FontFormatException {
        // telling view to draw fighter's HP
        scene.getView().drawString(String.valueOf(health), "lobster", Model.COLORS.RED, 34, x, y - 42*2);

        // telling view to draw fighter's ult-lvl (%)
        // YELLOW for 100% / BLUE for other cases
        Model.COLORS color = (isUlt) ? Model.COLORS.YELLOW : Model.COLORS.BLUE;
        scene.getView().drawString(String.valueOf(100 * currUltLvl / maxUltLvl) + "%", "lobster", color, 34, x, y - 42);

        // telling view to draw current animation frame
        scene.getView().drawAnimationFrame(animation, x, y);
    }

    private void jump() {
        // if we're on the ground => start jumping
        if (!isFalling) { isJumping = true; }
    }

    private void move(DIRECTIONS direction) {
        switch (direction) {
            case UP:
                // changing coordinate
                y -= currJumpSpeed;
                if (currJumpSpeed > minJumpSpeed) {
                    // decreasing jumping speed
                    currJumpSpeed--;
                }
                if (y <= jumpHeight) {
                    // reached jump height
                    y = jumpHeight;
                    isJumping = false;  // stop jumping
                    // resetting jump speed
                    currJumpSpeed = maxJumpSpeed;
                    isFalling = true;   // start falling
                }
                break;
            case DOWN:
                // changing coordinate
                y += currFallingSpeed;
                if (currFallingSpeed < maxFallingSpeed) {
                    // increasing falling speed (gravity works)
                    currFallingSpeed++;
                }
                if (y >= groundLevelHeight - height) {
                    // reached the ground
                    y = groundLevelHeight - height;
                    isFalling = false;  // stop falling
                    // resetting falling speed
                    currFallingSpeed = minFallingSpeed;
                }
                break;
            case LEFT:
                // changing coordinate
                x -= moveSpeed;
                if (x < 0) {
                    // reached left border
                    x = 0;
                }
                // checking if we're looking to the right direction
                checkFacing(false);
                break;
            case RIGHT:
                // changing coordinate
                x += moveSpeed;
                if (x > scene.getView().get_width() - width) {
                    // reached right border
                    x = scene.getView().get_width() - width;
                }
                // checking if we're looking to the right direction
                checkFacing(true);
                break;
            default:
                break;
        }
    }

    private void punch() {
        if (System.currentTimeMillis() - lastTimeMs >= timeBetweenPunchesMs) {
            // waited time between punches

            // setting punch frame
            animation.setCurrentFrame(animation.getCurrentFrameID() + 2, true);

            if (intersects(otherFighter)) {
                // damaging other fighter
                otherFighter.loseHP(damage);

                if (!isUlt) {
                    // increasing ult-level
                    currUltLvl += ultStep;

                    if (currUltLvl == maxUltLvl) {
                        ult();
                    }
                }
            }

            // updating time
            lastTimeMs = System.currentTimeMillis();
        }
        // not punching anymore
        isPunching = false;
    }

    private void ult() {
        isUlt = true;
        // setting new animation frames
        // 1) setting 'chill' as previous
        animation.setCurrentFrame(animation.getCurrentFrameID() + 2, false);
        // 2) setting 'attacking' as current
        animation.setCurrentFrame(animation.getCurrentFrameID() + 2, true);
        // increasing damage
        damage *= 2;
    }

    public void loseHP(int damage) {
        health -= damage;
        if (health <= 0) {
            // opponent wins

            if (scene.getModel().isOnlineMode()) {
                // online mode
                scene.getModel().setIsWinner(false);
            }

            // setting winner ID
            if (isFirst) {
                scene.getModel().setWinnerID(2);
            } else {
                scene.getModel().setWinnerID(1);
            }
        }
    }

    public Rectangle getRectangle() {
        // returns 'rectangle frame' around fighter
        return new Rectangle(x, y, width, height);
    }

    public boolean intersects(Fighter otherFighter) {
        Rectangle r1 = getRectangle();
        Rectangle r2 = otherFighter.getRectangle();
        return r1.intersects(r2);
    }

    public FighterParameters createParametersPack() {
        // creating fighter's parameters pack for sending to opponent
        FighterParameters params = new FighterParameters(
                x, y,
                isJumping, currJumpSpeed,
                isFalling, currFallingSpeed,
                health, damage, isPunching,
                currUltLvl, isUlt,
                lastTimeMs, facingRight,
                animation.getCurrentFrameID(), animation.getShouldReflect());
        return params;
    }

    public void setParameters(FighterParameters params) {
        // setting parameters from pack
        x = params.getX();
        y = params.getY();
        isJumping = params.getIsJumping();
        currJumpSpeed = params.getCurrJumpSpeed();
        isFalling = params.getIsFalling();
        currFallingSpeed = params.getCurrFallingSpeed();
        health = params.getHealth();
        damage = params.getDamage();
        isPunching = params.getIsPunching();
        currUltLvl = params.getCurrUltLvl();
        isUlt = params.getIsUlt();
        lastTimeMs = params.getLastTimeMs();
        facingRight = params.getFacingRight();
        animation.setCurrentFrame(params.getCurrentFrameID(), true);
        animation.setShouldReflect(params.getShouldReflect());
    }
}