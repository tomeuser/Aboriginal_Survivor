package entities;

import static utilz.Constants.Enemy_Constants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import main.Game;

public  class Enemy extends Entity {
	
	private int aniIndex, enemyState = RUNNING;
	private int aniTick, aniSpeed = 25;
	private boolean firstUpdate = true;
	private boolean inAir = false;
	private float fallSpeed;
	private float gravity = 0.04f * Game.SCALE;
	private float walkSpeed = 0.5f * Game.SCALE;
	private float walkDir = LEFT;
	public int xDrawOffset = (int)(56 * Game.SCALE);
	public int yDrawOffset = (int)(7 * Game.SCALE);
	private int tileY;
	private float attackDistance = Game.TILES_SIZE * 2;
	private int maxHealth = 100;
	private int currentHealth = maxHealth;
	private boolean  active = true;
	private boolean attackChecked;
	private int enemyDamage = 25;	
	private Rectangle2D.Float attackBox;
	private int attackBoxOffset = (int)(10 * Game.SCALE);

	public Enemy(float x, float y, int width, int height) {
		super(x, y, width, height);
		initHitbox(x, y, (int)(103 * Game.SCALE), (int)(68 * Game.SCALE));
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(hitbox.x, hitbox.y, hitbox.width + attackBoxOffset, (int)(40*Game.SCALE));
	}
	
	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetEnemySpriteAmount(enemyState)) {
				aniIndex = 0;
				switch(enemyState) {
				case ATTACK, HIT -> enemyState = IDLE;
				case DEAD -> active = false;
				}
			}
		}
	}
	
	public void update(int[][] lvlData, Player player){
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		updateAttackbox();
	}
	
	private void updateAttackbox() {
		if(walkDir == RIGHT)
			attackBox.x = hitbox.x;
		else 
			attackBox.x = hitbox.x - attackBoxOffset;
		attackBox.y = hitbox.y;
	}

	private void updateBehaviour(int[][] lvlData, Player player) {
		firstUpdateCheck(lvlData);
		if(inAir) 
			updateInAir(lvlData);
		else {
			switch(enemyState) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				// tests if player is close and attackable
				if(player.getPlayerActive())
					if(canSeePlayer(lvlData, player)) {
						turnTowardsPlayer(player);
						if(isPlayerCloseForAttack(player)) {
							newState(ATTACK);
						}
					}
				move(lvlData);
				break;
			case ATTACK: 
				if(aniIndex == 4)
					attackChecked = false;
				if(aniIndex == 0 && !attackChecked)
					checkEnemyHit(player);
				break;
			case HIT:
				break;
			}
		}	
	}
	
	private void checkEnemyHit(Player player) {
		if(attackBox.intersects(player.hitbox))
			player.changeHealth(-enemyDamage);
		attackChecked = true;
	}

	public int flipX() {
		if(walkDir == RIGHT)
			return -xDrawOffset;
		else 
			return width;
	}
	
	public int flipW() {
		if(walkDir == RIGHT) 
			return 1;
		else 
			return -1;
	}
	
	private void changeWalkDir() {
		if(walkDir == LEFT) 
			walkDir = RIGHT;
		else 
			walkDir = LEFT;
	}
	
	private void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}
	
	private void firstUpdateCheck(int[][] lvlData) {
		if(firstUpdate) {
			if(!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;
			firstUpdate = false;
		}
	}
	
	private void updateInAir(int[][] lvlData) {
			if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)){
				hitbox.y += fallSpeed;
				fallSpeed += gravity;
			} else {
				inAir = false;
				fallSpeed = 0;
				hitbox.y += 1;
				tileY = (int)((hitbox.y + hitbox.height) / Game.TILES_SIZE);
			}
	}
	
	private void move(int[][] lvlData) {
		float xSpeed = 0;
		if(walkDir == LEFT && this.enemyState != ATTACK) {
			xSpeed = -walkSpeed;
		}else if(walkDir == RIGHT && this.enemyState != ATTACK){
			xSpeed = walkSpeed;
		}
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
			if(IsFloor(hitbox, xSpeed, lvlData, walkDir)) {
				hitbox.x += xSpeed;
				return;
			}
		}			
		changeWalkDir();
	}
	
	private void turnTowardsPlayer(Player player) {
		if(player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	public boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int)((player.getHitbox().y + player.getHitbox().height) / Game.TILES_SIZE);
		if(playerTileY == tileY)
			if(isPlayerInRange(player)) {
				if(isSigthClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}
		return false;
	}

	private boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}
	
	private boolean isPlayerCloseForAttack(Player player) {
		int absValue;
		if(walkDir == RIGHT) {
			absValue = (int)(player.hitbox.x - (attackBox.x + attackBox.width));
			return absValue <= -13;
		}else {
			absValue = (int)(attackBox.x - (player.hitbox.x + player.hitbox.width));
			return absValue <= -17;
		}
	}

	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getEnemyState() {
		return enemyState;
	}
	
	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int)(attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	public void hurt(int amount) {
		currentHealth -= amount;
		if(currentHealth <= 0)
			newState(DEAD);
		else 
			newState(HIT);
	}
	
	public boolean isActive() {
		return active;
	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		fallSpeed = 0;
		this.enemyDamage = 25;
	}
	
	public void setEnemyDamage(int i) {
		enemyDamage = i;
	}
}
