package org.belog.poopalkombat.model;

import org.belog.poopalkombat.model.gamescenes.GameScene;

public class Background {
	GameScene scene;	// scene that this background belongs to

	String imageFileName;

	// coordinates of left top corner
	private double x;
	private double y;

	// offset
	private double dx;
	private double dy;

	private double moveScale;
	
	public Background(GameScene gameScene, String imageFileName, double moveScale) {
		try {
			this.scene = gameScene;
			this.imageFileName = imageFileName;
			this.moveScale = moveScale;

			x = 0;
			y = 0;
			dx = 0;
			dy = 0;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % scene.getModel().getView().get_width();
		this.y = (y * moveScale) % scene.getModel().getView().get_height();
	}
	
	public void setOffset(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}

	// getters
	public double getX() { return x; }
	public double getY() { return y; }
	public String getImageFileName() { return imageFileName; }
}