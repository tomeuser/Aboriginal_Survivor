package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	
	public Entity(float x, float y, int width, int height){
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int)hitbox.x - xLvlOffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
	}
	
	protected void initHitbox(float x, float y, int width, int height) {
		hitbox = new Rectangle2D.Float((int)x, (int)y, width, height);
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
}
