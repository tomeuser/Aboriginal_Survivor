package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;

public class MenuButton {
	
	private int xPos, yPos, rowIndex, index;
	private int width = 140, height = 56;
	private int xOffsetCenter = (int)((width * Game.SCALE) / 2);
	private Gamestate state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
		initBounds();
	}


	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
	}


	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTON);
		for(int i = 0; i < imgs.length; i++) 
			imgs[i] = temp.getSubimage(i * width, rowIndex * height, width, height);	
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, (int)(width * Game.SCALE), (int)(height * Game.SCALE), null);
	}
	
	public void update() {
		index = 0;
		if(mouseOver)
			index = 1;
		if(mousePressed)
			index = 2;
	}
	
	public boolean isMouseOver() {
		return mouseOver;
	}


	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}


	public boolean isMousePressed() {
		return mousePressed;
	}


	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
	public Rectangle getBounds(){
		return bounds;
	}
	
	public void applyGamestate() {
		Gamestate.state = state;
		
	}
	
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
}
