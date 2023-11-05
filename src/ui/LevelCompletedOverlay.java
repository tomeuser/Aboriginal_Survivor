package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.URM_SIZE;

public class LevelCompletedOverlay {

	private Playing playing;
	private UrmButton menu, again;
	private BufferedImage img;
	private int bgX, bgY, bgW,bgH;
	
	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = (int)(553 * Game.SCALE);
		int againX = (int)(668 * Game.SCALE);
		int y = (int) (320 * Game.SCALE);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
		again = new UrmButton(againX, y, URM_SIZE, URM_SIZE, 1);

		}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.GAMECOMPLETED);
		bgW = (int) (img.getWidth() * Game.SCALE);
		bgH = (int) (img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int)(200 * Game.SCALE);
	}
	
	public void update() {
		
		menu.update();
		again.update();
	}
	
	public void draw(Graphics g) {
		//Background
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		
		//Sound buttons
		menu.draw(g);
		again.draw(g);
		
	}
	
	public void mousePressed(MouseEvent e) {
		
		if(isIn(e, menu))
			menu.setMousePressed(true);
		else if(isIn(e, again))
			again.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		
		if(isIn(e, menu)) {
			if(menu.isMousePressed()) {
				Gamestate.state = Gamestate.MENU;
				playing.resetAll();
				playing.unpauseGame();
			}
				
		}
		else if(isIn(e, again)) {
			if(again.isMousePressed()) {
				playing.resetAll();
				playing.unpauseGame();
			}
		}
		
		menu.resetBools();
		again.resetBools();



	}
	
	public void mouseMoved(MouseEvent e) {
		menu.setMouseOver(false);
		again.setMouseOver(false);
		
		if(isIn(e, menu))
			menu.setMouseOver(true);
		else if(isIn(e, again))
			again.setMouseOver(true);
	}
	
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
	
}
