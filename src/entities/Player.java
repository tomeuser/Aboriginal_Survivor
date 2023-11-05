package entities;

import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PowerStar_Constants.STAR_HEIGTH;
import static utilz.Constants.PowerStar_Constants.STAR_HEIGTH_DEFAULT;
import static utilz.Constants.PowerStar_Constants.STAR_WIDTH;
import static utilz.Constants.PowerStar_Constants.STAR_WIDTH_DEFAULT;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity{
	
	private Playing playing;
	private BufferedImage[][] animations;
	private int aniTick = 0, aniIndex = 0, aniSpeed = 15;
	private int playerAction = IDLE;
	private boolean left, right, up, down, jump; 
	private boolean moving = false, attacking = false;
	private float playerSpeed = 2.0f * Game.SCALE;
	private int[][] levelData;
	private float xDrawOffset = 50 * Game.SCALE;
	private float yDrawOffset = 8 * Game.SCALE;
	private int flipX = 0;
	private int flipW = 1;
	private boolean attackChecked;
	private boolean active = true;
	private boolean starPowerActive = false;
	private int aniTickStar = 0;
	private boolean aniBool;
	
	//Gravity / jumping
	private float airSpeed = 0f;
	private float gravity = 0.08f * Game.SCALE;
	private float jumpSpeed = -4.5f * Game.SCALE;
	private float fallSpeedAfterCollision = 2.5f * Game.SCALE;
	private boolean inAir = false;
	private boolean hitted = false;
	
	//StatusBarUI
	private BufferedImage[] statusBarImg;
	private BufferedImage[] starImg;
	private int statusBarX = (int)(990 *Game.SCALE), statusBarY = (int)(30 * Game.SCALE), statusBarWidth = (int)(41 * Game.SCALE), statusBarHeight = (int)(Game.SCALE* 34);
	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private int maxHealthLvl = 12;
	private int healthLvl = maxHealthLvl;
	private int[] hearts = {0, 0, 0, 0};
	
	//AttackBox
	private Rectangle2D.Float attackBox;
	
	public Player(float x, float y, int width, int height, Playing playing){
		super(x, y, width, height);
		this.playing = playing;
		loadAnimation();
		initHitbox(x, y, (int)(40 * Game.SCALE), (int)(152 * Game.SCALE));
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(350* Game.SCALE), (int)(20 * Game.SCALE));
	}
	
	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
		if(!IsEntityOnFloor(hitbox, levelData))
			inAir = true;
	}
	
	private void loadAnimation() {
		// Loads player animation pictures
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[14][4];
		//Set "STANDING" Sub Images to Array
		animations[0][0] = img.getSubimage(6, 347, 133, 163);
		animations[0][1] = img.getSubimage(151, 347, 134, 163); 
		animations[0][2] = img.getSubimage(296, 347, 134, 163);
		animations[0][3] = img.getSubimage(6, 347, 133, 163);
		//Set "WALKING" Sub Images to Array
		animations[1][0] = img.getSubimage(6, 177, 134, 162); 
		animations[1][1] = img.getSubimage(151, 176, 134, 163); 
		animations[1][2] = img.getSubimage(6, 177, 134, 162);
		animations[1][3] = img.getSubimage(296, 176, 134, 162);
		//Set "ATTACKING" Sub Images to Array
		animations[2][0] = img.getSubimage(6, 5, 134, 163); 
		animations[2][1] = img.getSubimage(6, 5, 134, 163);
		animations[2][2] = img.getSubimage(6, 347, 133, 163);
		animations[2][3] = img.getSubimage(151, 347, 134, 163); 
		//Set "HIT" Sub Images to Array
		animations[3][0] = img.getSubimage(440, 518, 134, 163); 
		animations[3][1] = img.getSubimage(585, 518, 134, 163); 
		animations[3][2] = img.getSubimage(729, 518, 134, 163);
		animations[3][3] = img.getSubimage(729, 518, 134, 163);
		//Set "STATUS BAR" Sub Images to Array
		statusBarImg = new BufferedImage[4];
		for(int i = 0; i < 4; i++)
			statusBarImg[i] = LoadSave.GetSpriteAtlas(LoadSave.PlayerHeart).getSubimage(i * 53, 0, 41, 34);
		//Set "STAR" Sub Images to Array
		starImg = new BufferedImage[2];
		for(int i = 0; i < 2; i++)
			starImg[i] = LoadSave.GetSpriteAtlas(LoadSave.STARBAR).getSubimage(i * 73, 0, STAR_WIDTH_DEFAULT, STAR_HEIGTH_DEFAULT);		
	}
	
	public void update() {
		updateHealthBar();
		if(currentHealth <= 0) {
			active = false;
			playing.setGameOver(true);
			return;
		}
		updateAttackBox();
		updatePos();
		checkCollitionWithStars();
		if(attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();
	}
	
	private void checkAttack() {
		if(attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		playing.checkEnemyHit(levelData, this);	
	}
	
	private void checkCollitionWithStars() {
		playing.checkKollitionWithStars(this);
	}
	
	private void updateAttackBox() {
		if(right) 
			attackBox.x = hitbox.x + hitbox.width;
		else if(left) 
			attackBox.x = hitbox.x - attackBox.width;
		attackBox.y = hitbox.y + (int)hitbox.height/2;
	}
	
	private void updateHealthBar() {
		int allHeart = 0;
		for (int i = 0; i < hearts.length; i++ )
			allHeart += hearts[i];
		healthLvl = (int)((currentHealth / (float)maxHealth) * maxHealthLvl);		
		if((maxHealthLvl - allHeart)!= healthLvl)
			convertToImg();
	}
	
	private void convertToImg() {
		if(hearts[0] < 3) 
			editHeart(0);
		else if(hearts[1] < 3)
			editHeart(1);
		else if(hearts[2] < 3)
			editHeart(2);
		else if(hearts[3] < 3)
			editHeart(3);
	}
	
	private void editHeart(int index) {
		for(int i = 0; i < 3; i++) {
			int allHeart = 0;
			for (int j = 0; j < hearts.length; j++)
				allHeart += hearts[j];
			if(((maxHealthLvl - allHeart)== healthLvl) || (hearts[index] == 3))
				break;
			hearts[index] += 1;
		}
	}
	
	public void render(Graphics g, int xLvlOffset) {
		if(active)
			g.drawImage(animations[playerAction][this.aniIndex], (int)(hitbox.x - this.xDrawOffset) - xLvlOffset + flipX, (int)(hitbox.y - this.yDrawOffset), this.width * flipW, this.height, null);
		drawUI(g);
		drawStar(g);
	}
	
	private void drawStar(Graphics g) {
		aniTickStar++;
		if(aniTickStar >= 30) {
			aniTickStar = 0;
			aniBool = !aniBool;
		}
		if(aniBool && starPowerActive)
			g.drawImage(starImg[0], statusBarX + (int)(Game.SCALE* 207), statusBarY + STAR_HEIGTH, (int)(STAR_WIDTH * 0.75), (int)(STAR_HEIGTH * 0.75), null);
		else if(starPowerActive)
			g.drawImage(starImg[1], statusBarX + (int)(Game.SCALE* 207), statusBarY + STAR_HEIGTH, (int)(STAR_WIDTH * 0.75), (int)(STAR_HEIGTH * 0.75), null);	
	}
	
//	private void drawAttackBox(Graphics g, int lvlOffset) {
//		g.setColor(Color.red);
//		g.drawRect((int)(attackBox.x - lvlOffset), (int)(attackBox.y), (int)attackBox.width, (int)(attackBox.height));
//	}
	
	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg[hearts[3]], statusBarX + (int)(Game.SCALE* 53), statusBarY, statusBarWidth, statusBarHeight, null);
		g.drawImage(statusBarImg[hearts[2]], statusBarX + (int)(Game.SCALE* 106), statusBarY, statusBarWidth, statusBarHeight, null);
		g.drawImage(statusBarImg[hearts[1]], statusBarX + (int)(Game.SCALE* 159), statusBarY, statusBarWidth, statusBarHeight, null);
		g.drawImage(statusBarImg[hearts[0]], statusBarX + (int)(Game.SCALE* 212), statusBarY, statusBarWidth, statusBarHeight, null);	
	}
	
	private void updateDrawOffset() {
		if(playerAction == RUNNING) {
			xDrawOffset = 50 * Game.SCALE;
			yDrawOffset = 8 * Game.SCALE;
		}else if(playerAction == IDLE) {
			xDrawOffset = 50 * Game.SCALE;
			yDrawOffset = 8 * Game.SCALE;
		}else if(playerAction == ATTACK) {
			xDrawOffset = 50 * Game.SCALE;
			yDrawOffset = 8 * Game.SCALE;	
		}
	}
	
	private void updateAnimationTick() {
		aniTick++;
		if(moving == true)
			aniSpeed = 20;
		else 
			aniSpeed = 40;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
				hitted = false;
			}
		}
	}
	
	private void setAnimation() {
		int startAni = playerAction;
		if(moving) 
			playerAction = RUNNING;
		else
			playerAction = IDLE;	
		if(attacking)
			playerAction = ATTACK;
		if(hitted)
			playerAction = HIT;
		updateDrawOffset();
		if(startAni != playerAction)
			resetAniTick();
	}
	
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		if(jump)
			jump();
		if(!inAir)
			if((!right && !left) || (right && left))
				return;
		float xSpeed = 0;
		if(right) {
			xSpeed += playerSpeed;
			flipX = 0;
			flipW = 1;	
		}
		if(left) {
			xSpeed -=playerSpeed;	
			flipX = width;
			flipW = -1;
		}
		if(!inAir) {
			if(!IsEntityOnFloor(hitbox, levelData)) 
				inAir = true;
		}
		if(inAir) {
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				if(airSpeed > 0 && IsEntityOnFloor(hitbox, levelData))
					resetInAir();
				else if(airSpeed > 0)
					airSpeed = 0;
				else 
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		}
		else 
			updateXPos(xSpeed);
		moving = true;	
		if(((int)(hitbox.y + hitbox.height + 1)) == (Game.GAME_HEIGHT)) 
			this.currentHealth = 0;
	}
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
	}
	
	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}
	
	private void updateXPos(float xSpeed) {
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) 
			hitbox.x += xSpeed;	
		else 
			hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		hitted = true;
		if(currentHealth <= 0) 
			currentHealth = 0;
		else if(currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}
	
	public void resetDirBooleans() {
		left = false;
		up = false;
		right = false;
		down = false;
		jump = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
	public boolean isLeft() {
		return left;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public boolean isRight() {
		return right;
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public boolean isUp() {
		return up;
	}
	
	public void setUp(boolean up) {
		this.up = up;
	}
	
	public boolean isDown() {
		return down;
	}
	
	public void setDown(boolean down) {
		this.down = down;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public void resetAll() {
		resetDirBooleans();
		flipX = 0;
		flipW = 1;
		active = true;
		inAir = false;
		attacking = false;
		moving = false;
		this.jump = false;
		playerAction = IDLE;
		currentHealth = maxHealth;
		for(int i = 0; i < 4; i++)
			hearts[i] = 0;
		hitbox.x = x;
		hitbox.y = y;
		if(!IsEntityOnFloor(hitbox, levelData))
			inAir = true;
	}
	
	public Rectangle2D.Float getAttackBox(){
		return attackBox;
	}
	
	public void setActive(boolean b) {
		this.active = b;
	}
	
	public boolean getPlayerActive() {
		return active;
	}

	
	public boolean isStarPowerActive() {
		return starPowerActive;
	}
	
	public void setStarPowerActive(boolean starPowerActive) {
		this.starPowerActive = starPowerActive;
	}
}
