package org.belog.poopalkombat.model.net.message;

public class FighterParameters extends Message {
    // position coordinates
    private int x;
    private int y;

    // jumping
    private boolean isJumping;
    private int currJumpSpeed;

    // falling
    private boolean isFalling;
    private int currFallingSpeed;

    // fighter's health/damage
    private int health;
    private int damage;

    // punching
    private boolean isPunching;

    // ult
    private int currUltLvl;
    private boolean isUlt;

    // time
    private double lastTimeMs;  // last time update

    private boolean facingRight;
    // true == fighter looks to the right
    // false fighter looks to the left

    // animation
    private int currentFrameID;
    private boolean shouldReflect;

    public FighterParameters(
            int x, int y,
            boolean isJumping, int currJumpSpeed,
            boolean isFalling, int currFallingSpeed,
            int health, int damage, boolean isPunching,
            int currUltLvl, boolean isUlt,
            double lastTimeMs, boolean facingRight,
            int currentFrameID, boolean shouldReflect)
    {
        // calling Message's constructor
        super(MessageType.FIGHTER_PARAMETERS, "FIGHTER_PARAMETERS");

        // setting parameters
        this.x = x;
        this.y = y;
        this.isJumping = isJumping;
        this.currJumpSpeed = currJumpSpeed;
        this.isFalling = isFalling;
        this.currFallingSpeed = currFallingSpeed;
        this.health = health;
        this.damage = damage;
        this.isPunching = isPunching;
        this.currUltLvl = currUltLvl;
        this.isUlt = isUlt;
        this.lastTimeMs = lastTimeMs;
        this.facingRight = facingRight;
        this.currentFrameID = currentFrameID;
        this.shouldReflect = shouldReflect;
    }

    // getters

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean getIsJumping() { return isJumping; }
    public int getCurrJumpSpeed() { return currJumpSpeed; }
    public boolean getIsFalling() { return isFalling; }
    public int getCurrFallingSpeed() { return currFallingSpeed; }
    public int getHealth() { return health; }
    public int getDamage() { return damage; }
    public boolean getIsPunching() { return isPunching; }
    public int getCurrUltLvl() { return currUltLvl; }
    public boolean getIsUlt() { return isUlt; }
    public double getLastTimeMs() { return lastTimeMs; }
    public boolean getFacingRight() { return facingRight; }
    public int getCurrentFrameID() { return currentFrameID; }
    public boolean getShouldReflect() { return shouldReflect; }
}