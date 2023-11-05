package levels;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class LevelManager {

	private Game game;
	private BufferedImage levelSprite[];
	private Level levelOne;
	
	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levelOne = new Level(LoadSave.GetLevelData());

		//levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
	}
	
	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[20];
		for(int j = 0; j < 5; j++) {
			for(int i = 0; i < 4; i++) {
				int index = j * 4 + i;
				levelSprite[index] = img.getSubimage(49 * i, 49 * j, 49, 49);
			}
		}
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		for(int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
			for(int i = 0; i < levelOne.getLevelData()[0].length; i++) {
				int index = levelOne.getSpriteIndex(i, j);
				g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xLvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
		}	
	}
	
	public Level getCurrentLevel() {
		return levelOne;
	}
	
	public void update() {
		
	}
}
