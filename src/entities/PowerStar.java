package entities;

public class PowerStar extends Entity {
	
	private int aniTick, aniSpeed = 25, aniIndex;
	private boolean active = true;
	
	public PowerStar(float x, float y, int width, int height) {
		super(x, y, width, height);
		initHitbox(x, y, width, height);
	}
	
	public void update(int[][] lvlData, Player player){
		updateAnimationTick();
	}
	
	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= 9)
				aniIndex = 0;
		}
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean b) {
		active = b;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
	public void resetStar() {
		hitbox.x = x;
		hitbox.y = y;
		active = true;
	}
}
	

