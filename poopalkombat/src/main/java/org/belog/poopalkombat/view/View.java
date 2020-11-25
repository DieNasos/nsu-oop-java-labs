package org.belog.poopalkombat.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import javax.swing.*;

import org.belog.poopalkombat.exceptions.InvalidResolutionException;
import org.belog.poopalkombat.exceptions.NullMVCPartException;
import org.belog.poopalkombat.model.*;
import org.belog.poopalkombat.controller.*;

public class View extends JPanel implements Runnable {
    //  other MVC parts
    private Model model;
    private Controller controller;

    // game window
    private JFrame window;
    String windowName;

    // dimension
    private int width;
    private int height;
    private int scale;

    // game thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 500 / FPS;

    // image
    private String backgroundImageFileName;
    private BufferedImage backgroundImage;
    private BufferedImage image;
    private Graphics2D graphics;

    // maps
    private HashMap<String, Font> fonts;    // fonts

    // audio
    AudioPlayer player;

    public View(Model model, Controller controller, String windowName, int width, int height, int scale) throws NullMVCPartException, InvalidResolutionException {
        // here is where the game starts!

        super();    // calling parent's constructor

        // binding this view with other MVC parts
        this.model = model;
        this.controller = controller;

        // checking MVC parts initialization
        if (model == null) {
            throw new NullMVCPartException("View :: start :: MODEL IS NOT INITIALIZED");
        }
        if (controller == null) {
            throw new NullMVCPartException("View :: start :: CONTROLLER IS NOT INITIALIZED");
        }

        // binding other MVC parts with this view
        this.model.setView(this);
        this.controller.setView(this);

        this.windowName = windowName;
        this.width = width;
        this.height = height;
        this.scale = scale;

        fonts = new HashMap<String, Font>();    // creating fonts map
        player = new AudioPlayer(); // creating audio player

        setPreferredSize(new Dimension(width * scale, height * scale));
        setFocusable(true);
        requestFocus();

        // getting screen resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // checking if screen resolution fits view parameters
        if (screenSize.width <= width || screenSize.height <= height) {
            throw new InvalidResolutionException("INVALID SCREEN RESOLUTION :: SHOULD BE >= 1000 * 700");
        }

        // window initialization
        window = new JFrame(windowName);
        window.setContentPane(this);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            // creating new thread == game thread
            thread = new Thread(this);
            // adding controller as key listener =>
            // => keyboard input that controller catches will affect us
            addKeyListener(this.controller);
            thread.start(); // starting game thread
        }
    }

    private void init() throws NullMVCPartException, IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // creating background image
        backgroundImageFileName = model.getBackgroundImageFileName();
        backgroundImage = ImageIO.read(getClass().getResourceAsStream(backgroundImageFileName));

        // initializing image + graphics -> starting the game
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) image.getGraphics();
        running = true;
    }

    @Override
    public void run() {
        try {
            init();
        } catch (NullMVCPartException | IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        long start;
        long elapsed;
        long wait;

        // game loop
        while (running) {
            start = System.nanoTime();

            try {
                // main game loop actions
                update();
                draw();
                drawToScreen();
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;

            if (wait < 0) { wait = 5; }

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // update background

        // getting current backgroundImageFileName
        String tmp_bg_ifn = model.getBackgroundImageFileName();

        if (tmp_bg_ifn != backgroundImageFileName) {
            // if background image filename was changed => changing background image
            // setting new filename
            backgroundImageFileName = tmp_bg_ifn;
            // loading new image
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(backgroundImageFileName));
        }

        // update model
        model.update();
    }

    // draw

    private void draw() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // draw bg
        drawBackground();
        // draw model
        model.draw();
    }

    private void drawToScreen() {
        Graphics graphics = getGraphics();
        graphics.drawImage(image, 0, 0, width * scale, height * scale, null);
        graphics.dispose();
    }

    private void drawBackground() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // getting background coordinates
        int bgX = (int)model.getBackgroundX();
        int bgY = (int)model.getBackgroundY();

        // drawing background image
        graphics.drawImage(backgroundImage, bgX, bgY, null);

        // if background is displaced =>
        // => drawing bg-image again to fill empty space on the screen
        if (bgX < 0) {
            graphics.drawImage(backgroundImage, bgX + width, bgY, null);
        }
        if (bgX > 0) {
            graphics.drawImage(backgroundImage, bgY - width, bgY, null);
        }
    }

    public void drawString(String string, String fontName, Model.COLORS color, int size, int x, int y) throws IOException, FontFormatException {
        if (!fonts.containsKey(fontName)) {
            // no such a font -> registering
            fonts.put(fontName, Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/fonts/" + fontName + ".ttf")).deriveFont((float)size));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/fonts/" + fontName + ".ttf")));
        }

        graphics.setFont(fonts.get(fontName));  // setting font

        switch (color) {
            // setting color
            case RED:
                graphics.setColor(Color.RED);
                break;
            case BLUE:
                graphics.setColor(Color.BLUE);
                break;
            case YELLOW:
                graphics.setColor(Color.YELLOW);
                break;
            default:
                graphics.setColor(Color.BLACK);
                break;
        }

        graphics.drawString(string, x, y);  // drawing string
    }

    public void drawAnimationFrame(Animation animation, float x, float y) {
        if (animation.getShouldReflect()) {
            // should reflect frame (horizontally) => drawing reflected frame
            // creating copy of current frame
            BufferedImage reflectedImage = animation.getCurrentFrame();
            // reflecting copy of frame
            reflectedImage = getHorizontalReflectTransform(reflectedImage.getWidth()).filter(reflectedImage, null);
            // drawing reflected frame
            graphics.drawImage(reflectedImage, (int)x, (int)y, null);
        } else {
            // drawing animation frame itself without reflecting
            graphics.drawImage(animation.getCurrentFrame(), (int)x, (int)y, null);
        }
    }

    public void drawImage(String imageFileName, int subX, int subY, int subWidth, int subHeight, int x, int y) throws IOException {
        // drawing image by file name
        // creating image
        BufferedImage image = ImageIO.read(getClass()
                .getResourceAsStream(imageFileName))
                .getSubimage(subX, subY, subWidth, subHeight);
        // drawing
        graphics.drawImage(image, x, y, null);
    }

    // reflect transform

    private AffineTransformOp getHorizontalReflectTransform(double imageWidth) {
        // returns transform for image horizontal reflection
        AffineTransform at = AffineTransform.getScaleInstance(-1, 1);
        at.translate(-imageWidth, 0);
        // returning transform
        return new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    }

    // MVC parts setters
    public void setModel(Model model) { this.model = model; }
    public void setController(Controller controller) { this.controller = controller; }

    // MVC parts getters
    public Model getModel() { return model; }
    public Controller getController() { return controller; }

    // dimension getters
    public int get_width() { return width; }
    public int get_height() { return height; }
    public int get_scale() { return scale; }

    // audio

    public void playMusic(String musicFileName) {
        player.setSourceFile(musicFileName);
        player.play();
    }

    public void stopMusic() { player.stop(); }
    public boolean isMusicPlaying() { return player.isPlaying(); }

    // state
    public boolean getIsRunning() { return running; }
}