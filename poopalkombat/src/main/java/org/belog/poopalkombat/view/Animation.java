package org.belog.poopalkombat.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Animation {
    private BufferedImage spritesheet;  // image with frames

    private ArrayList<BufferedImage> frames;    // frames list

    // frames IDs
    private int previousFrameID = -1;
    private int currentFrameID;

    private boolean shouldReflect;
    // true == should reflect images before drawing
    // false == should not

    // time
    // delays (== time between animation frames)
    private double staticDelayTimeMs = 250;
    private double dynamicDelayTimeMs = 250;
    // last time values
    private double staticLastTimeMs;
    private double dynamicLastTimeMs;

    public Animation(String spritesheetFileName) throws IOException {
        // opening sprites file
        spritesheet = ImageIO.read(getClass().getResourceAsStream(spritesheetFileName));
        // creating frames list
        frames = new ArrayList<>();
        // noting times for the first time
        staticLastTimeMs = System.currentTimeMillis();
        dynamicLastTimeMs = System.currentTimeMillis();
    }

    // frames list

    public void addFrame(int x, int y, int width, int height) {
        // getting sub-image of spritesheet and putting it in frames list
        frames.add(spritesheet.getSubimage(x, y, width, height));

        if (frames.size() == 1) {
            // this is the first frame in list => let's set it as current for now
            setCurrentFrame(0, false);
        }
    }

    public void removeFrame(int frameID) { frames.remove(frameID); }

    // current frame

    public void setCurrentFrame(int frameID, boolean savePrevious) {
        if (savePrevious) { previousFrameID = currentFrameID; }
        currentFrameID = frameID;
    }

    public BufferedImage getCurrentFrame() { return frames.get(currentFrameID); }
    public int getCurrentFrameID() { return currentFrameID; }

    // should reflect
    public boolean getShouldReflect() { return shouldReflect; }
    public void setShouldReflect(boolean shouldReflect) { this.shouldReflect = shouldReflect; }

    public Point getFrameSize(int frameID) {
        // .x == width
        // .y == height
        return new Point(frames.get(frameID).getWidth(), frames.get(frameID).getHeight());
    }

    public void updateDynamic() {
        // updating walking animation
        if (System.currentTimeMillis() - dynamicLastTimeMs >= dynamicDelayTimeMs) {
            // waited delay time
            // setting frame's pair
            if (currentFrameID % 2 == 0) {
                currentFrameID++;
            } else {
                currentFrameID--;
            }
            // updating time
            dynamicLastTimeMs = System.currentTimeMillis();
        }
    }

    public void updateStatic() {
        // updating punch animation
        if (System.currentTimeMillis() - staticLastTimeMs >= staticDelayTimeMs) {
            // waited delay time
            if (previousFrameID != -1) {
                setCurrentFrame(previousFrameID, false);
            }
            // updating time
            staticLastTimeMs = System.currentTimeMillis();
        }
    }
}