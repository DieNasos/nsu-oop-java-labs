package org.belog.poopalkombat.model.gamescenes;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.belog.poopalkombat.model.Background;
import org.belog.poopalkombat.model.Model;
import org.belog.poopalkombat.view.View;

public abstract class GameScene {
	// MVC parts
	protected Model model;
	protected View view;

	protected Background bg;

	// MVC parts getters
	public Model getModel() { return model; }
	public View getView() { return view; }

	// background parameters getters
	public String getBackgroundImageFileName() { return bg.getImageFileName(); }
	public double getBackgroundX() { return bg.getX(); }
	public double getBackgroundY() { return bg.getY(); }

	// abstract methods
	public abstract void update() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
	public abstract void draw() throws IOException, FontFormatException;
}