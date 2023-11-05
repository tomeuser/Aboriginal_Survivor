package entities;

import static utilz.Constants.Enemy_Constants.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import gamestates.Playing;
import utilz.LoadSave;

public class EnemyManager {
	
	private Playing playing;
	private BufferedImage[][]wolfArray;
	private ArrayList<Enemy> wolfs = new ArrayList<>();
	private int damage = 20;
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}
	
	private void addEnemies() {
		wolfs = LoadSave.getEnemy();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for(Enemy w : wolfs) {
			if(w.isActive()) {
				w.update(lvlData, player);
				isAnyActive = true;
			}
		}
		if(!isAnyActive)
			playing.setGameCompleted(true);
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		drawWolfs(g, xLvlOffset);
	}
	
	private void drawWolfs(Graphics g, int xLvlOffset) {
		for(Enemy w : wolfs) {
			int enemyState = w.getEnemyState();
			if(enemyState > 2)
				enemyState = 2;
			if(w.isActive())
				g.drawImage(wolfArray[enemyState][w.getAniIndex()], (int)(w.getHitbox().x - xLvlOffset  + w.flipX()), (int)w.getHitbox().y - w.yDrawOffset, WOLF_WIDTH * w.flipW(), WOLF_HEIGTH, null);
		}	
	}

	public void checkEnemyHit(int[][] lvlData, Player player) {
		for(Enemy w: wolfs) 
			if(player.getAttackBox().intersects(w.getHitbox())){
				w.hurt(damage);
				return;
			}	
	}
	
	private void loadEnemyImgs() {
		wolfArray = new BufferedImage[3][6];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMIE_WOLF);
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < wolfArray[i].length; j++) 
				wolfArray[i][j] = temp.getSubimage( j* WOLF_WIDTH_DEFAULT, i * WOLF_HEIGTH_DEFAULT , WOLF_WIDTH_DEFAULT, WOLF_HEIGTH_DEFAULT);
		}		
		for(int i = 0; i < 6; i++)
			wolfArray[2][i] = wolfArray[0][0];
	}
	
	public void resetAllEnemies() {
		for(Enemy w : wolfs)
			w.resetEnemy();
	}
	
	public void setDamage(int i) {
		damage = i; 
	}
	
	public void setEnemyDamage(int i) {
		for(Enemy w: wolfs) 
			w.setEnemyDamage(i);			
	}
}
