package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import ui.MenuButton;
import utilz.LoadSave;
import main.Game;

public class Menu extends State implements Statemethods  {
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundImg, backgroundImgFullScreen;
	private int menuX, menuY, menuWidth, menuHeight;
	
	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		backgroundImgFullScreen = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int)(backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int)(backgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (100 * Game.SCALE);	
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int)(240 * Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int)(275 * game.SCALE), 1, Gamestate.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int)(310 * Game.SCALE), 2, Gamestate.QUIT);
	}

	@Override
	public void update() {
		for(MenuButton mb: buttons)
			mb.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImgFullScreen, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		buttons[0].draw(g);
		buttons[2].draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MenuButton mb: buttons) 
			if(isIn(e, mb))
				mb.setMousePressed(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MenuButton mb: buttons) 
			if(isIn(e, mb)) {
				if(mb.isMousePressed())
					mb.applyGamestate();
				break;
			}
		resetButtons();
	}

	private void resetButtons() {
		for(MenuButton mb: buttons)
			mb.resetBools();	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for(MenuButton mb: buttons)
			mb.setMouseOver(false);	
		for(MenuButton mb: buttons)
			if(isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			Gamestate.state = Gamestate.PLAYING;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
