package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static utilz.Constants.Enemy_Constants.*;
import static utilz.Constants.PowerStar_Constants.*;
import javax.imageio.ImageIO;

import entities.Enemy;
import entities.PowerStar;
import main.Game;

public class LoadSave {

	public static final String PLAYER_ATLAS = "Aboriginal_Surviver_GameCharacter_V2.png";
	public static final String LEVEL_ATLAS = "Aboriginal_Surviver_GameSprite.png";
	//public static final String LEVEL_ONE_DATA = "level_one_data.png";
	public static final String LEVEL_ONE_DATA = "lvl_one_data_long.png";
	public static final String MENU_BUTTON = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTON = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "Aboriginal_Surviver_LevelImg.png";
	//public static final String PLAYING_BACKGROUND_IMG = "Aboriginal_Surviver_LevelImg-V2.png";
	public static final String PLAYING_BACKGROUND_IMG = "AboriginalSurviver_BackgroundImg.png";
	public static final String CLOUDS_IMG = "Aboriginal_Surviver_clouds.png";
	public static final String ENEMIE_WOLF = "Enemie_Wolf.png";
	public static final String GAMECOMPLETED = "completed_sprite.png";
	public static final String GAMEOVER = "gameOver_sprite.png";
	public static final String PlayerHeart = "PlayerHeart.png";
	public static final String STAR = "stern3.png";
	public static final String STARBAR = "sternBar.png";




	//Imports ImageFiles
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			 img = ImageIO.read(is);
		}
		 catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("IMAGE IMPORTED");	
		} finally {
			try {
				is.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static ArrayList<Enemy>getEnemy(){
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		ArrayList<Enemy>list = new ArrayList<>();
		for(int j = 0; j < img.getHeight(); j++) {
			for(int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if(value == WOLF)
					list.add(new Enemy(i * Game.TILES_SIZE + (Game.TILES_SIZE - 103 * Game.SCALE)/2, j * Game.TILES_SIZE + (Game.TILES_SIZE - 68 * Game.SCALE - 1), WOLF_WIDTH, WOLF_HEIGTH)); 
			}	
		}
		return list;
	}
	
	//Imports Level Data via RGB value of ImageFile for generating a Level
	public static int [][] GetLevelData(){
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		int levelData[][] = new int [img.getHeight()][img.getWidth()];
		
		for(int j = 0; j < img.getHeight(); j++) {
			for(int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if(value >= 48)
					value = 0;
				levelData[j][i] = value; 
			}	
		}
		return levelData;

	}

	public static ArrayList<PowerStar> getPowerStar() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		ArrayList<PowerStar>list = new ArrayList<>();
		for(int j = 0; j < img.getHeight(); j++) {
			for(int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if(value == POWERSTAR)
					list.add(new PowerStar(i * Game.TILES_SIZE - 1, j * Game.TILES_SIZE - 1, STAR_WIDTH, STAR_HEIGTH)); 
			}	
		}
		return list;
	}
}
