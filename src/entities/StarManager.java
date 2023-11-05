package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.PowerStar_Constants.*;

public class StarManager {

	private Playing playing;
	private BufferedImage[] img;
	private ArrayList<PowerStar> powerStars = new ArrayList<PowerStar>();
	private Thread starPowerOn;
	private StarPowerProcess starPowerProcess;
	private int instance = 0;
	
	public StarManager(Playing playing) {
		this.playing = playing;
		addImg();
		addPowerStars();
		starPowerProcess = new StarPowerProcess(playing);		
	}

	private void addPowerStars() {
		powerStars = LoadSave.getPowerStar();
	}
	
	private void addImg() {
		img = new BufferedImage[9];
		for(int i = 0; i < 9; i++)
			img[i] = LoadSave.GetSpriteAtlas(LoadSave.STAR).getSubimage(i * 73, 0, STAR_WIDTH_DEFAULT, STAR_HEIGTH_DEFAULT);
	}
	
	public void update(int[][] lvlData, Player player) {
		for(PowerStar p : powerStars) 
			if(p.isActive()) 
				p.update(lvlData, player);
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		drawStars(g, xLvlOffset);
	}
	
	private void drawStars(Graphics g, int xLvlOffset) {
		for(PowerStar p : powerStars) {
			if(p.isActive())
				g.drawImage(img[p.getAniIndex()], (int)(p.getHitbox().x - xLvlOffset), (int)p.getHitbox().y, STAR_WIDTH, STAR_HEIGTH, null);
		}	
	}
	
	public void checkKollition(Player player) {
		for(PowerStar p : powerStars) 
			if(player.getHitbox().intersects(p.getHitbox())){
				if( p.isActive()) {
					p.setActive(false);
					instance++;
					System.out.println("Instance before Thread:" + instance);
					starPowerOn = new Thread(starPowerProcess);
					starPowerOn.start();
					System.out.println("Collition: True");
					return;
				}	
			}	
	}
	
	public void resetAllStars() {
		for(PowerStar p : powerStars) {
			p.resetStar();
			playing.getEnemyManager().setDamage(25);
			playing.getPlayer().setStarPowerActive(false);
		}	
	}
	
	public BufferedImage[] getImg() {
		return img;
	}
	
	public int getInstances() {
		return instance;
	}
	
	public void minInstances() {
		 instance--;
	}
}