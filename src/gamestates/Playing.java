package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import entities.EnemyManager;
import entities.Player;
import entities.StarManager;
import levels.LevelManager;
import levels.TimerApp;
import main.Game;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Clounds.*;

public class Playing extends State implements Statemethods{
 
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private StarManager starManager;
	private boolean paused = false;
	private PauseOverlay pauseOverlay;
	private int xLvlOffset;
	private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	private boolean gameOver = false;
	private GameOverOverlay gameOverOverlay;
	private boolean gameCompleted = false;
	private LevelCompletedOverlay levelCompletedOverlay;
	private BufferedImage backgroundImg, cloud;
	private TimerApp timerApp;

	public Playing(Game game) {
		super(game);
		initClasses();
	}
	
	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		starManager = new StarManager(this);
		player = new Player(200, 20, (int)(134*Game.SCALE), (int)(163*Game.SCALE), this);
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(this);
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
		cloud = LoadSave.GetSpriteAtlas(LoadSave.CLOUDS_IMG);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		timerApp = new TimerApp(this);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public void unpauseGame() {
		paused = false;
		timerApp.setPaused(false);
	}

	public void update() {
		if(paused) 
			pauseOverlay.update();
		else {
			if(gameCompleted) {
				levelCompletedOverlay.update();
				player.resetDirBooleans();
			}
			else if(gameOver)
				gameOverOverlay.update();
			timerApp.update();
			levelManager.update();
			checkCloseBorder();
			player.update();	
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			starManager.update(levelManager.getCurrentLevel().getLevelData(), player);
		}
	}

	private void checkCloseBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;
		if(diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if(diff < leftBorder)
			xLvlOffset += diff - leftBorder;
		if(xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if(xLvlOffset <0)
			xLvlOffset = 0;	
	}
	
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		drawClouds(g);
		levelManager.draw(g, xLvlOffset);
		starManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		timerApp.drawTimer(g);
		if(paused) 
			pauseOverlay.draw(g);
		else if(gameOver)
			gameOverOverlay.draw(g);
		else if(gameCompleted) 
			levelCompletedOverlay.draw(g);			
	}
	
	private void drawClouds(Graphics g) {
		int y = 30;
		for(float i = 0; i < 6; i += 1.5) {
			g.drawImage(cloud, 150 + (int)(i * CLOUD_WIDTH - xLvlOffset * 0.3), (int)((80 + y) *Game.SCALE), CLOUD_WIDTH, CLOUD_HEIGTH, null);
			y *= -1;
		}	
	} 
	
	public void resetAll() {
		gameCompleted = false;
		gameOver = false;
		paused = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		starManager.resetAllStars();
		timerApp.resetTimer();
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;	
	}

	public void mouseDragged(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mouseDragged(e);
	}

	public void mouseClicked(MouseEvent e) {
		if(!gameOver && !paused && !gameCompleted)
			if(e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);		
	}

	public void mousePressed(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mousePressed(e);
			else if(gameCompleted)
				levelCompletedOverlay.mousePressed(e);
		}
		else
			gameOverOverlay.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseReleased(e);
			else if(gameCompleted)
				levelCompletedOverlay.mouseReleased(e);
		}
		else
			gameOverOverlay.mouseReleased(e);
	}

	public void mouseMoved(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseMoved(e);
			else if(gameCompleted)
				levelCompletedOverlay.mouseMoved(e);
		}
		else 
			gameOverOverlay.mouseMoved(e);
	}

	public void keyPressed(KeyEvent e) {
		if(!gameOver && !gameCompleted)
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				timerApp.setPaused(true);
			}
	}

	public void keyReleased(KeyEvent e) {
		if(!gameOver && !gameCompleted)
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;	
			}
	}
	
	public int getXLvlOffset() {
		return xLvlOffset;
	}
	
	public void checkEnemyHit(int[][] lvlData, Player player) {
		enemyManager.checkEnemyHit(lvlData, player);
	}
	
	public void checkKollitionWithStars(Player player) {
		starManager.checkKollition(player);
	}
	
	public void setGameCompleted(boolean b) {
		this.gameCompleted = b;
	}
	
	public StarManager getStarManager() {
		return starManager;
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}	
}
	

