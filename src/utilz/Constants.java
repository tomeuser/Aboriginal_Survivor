package utilz;

import main.Game;

public class Constants {
	
	public static class PowerStar_Constants{
		public static final int POWERSTAR = 100;

		public static final int STAR_WIDTH_DEFAULT = 50;
		public static final int STAR_HEIGTH_DEFAULT = 50;
		public static final int STAR_WIDTH = (int)(STAR_WIDTH_DEFAULT * Game.SCALE);
		public static final int STAR_HEIGTH = (int)(STAR_HEIGTH_DEFAULT * Game.SCALE);
	}
	
	public static class Enemy_Constants{
		public static final int WOLF = 0;
		
		public static final int IDLE = 2;
		public static final int RUNNING = 0;
		public static final int ATTACK = 3;
		public static final int HIT = 1;
		public static final int DEAD = 4;

		public static final int WOLF_WIDTH_DEFAULT = 159;
		public static final int WOLF_HEIGTH_DEFAULT = 76;
		public static final int WOLF_WIDTH = (int)(WOLF_WIDTH_DEFAULT * Game.SCALE);
		public static final int WOLF_HEIGTH = (int)(WOLF_HEIGTH_DEFAULT * Game.SCALE);
		
		public static final int WOLF_DRAWOFFSET_X = (int)(0 * Game.SCALE);
		public static final int WOLF_DRAWOFFSET_Y = (int)(9 * Game.SCALE);
		
		public static int GetEnemySpriteAmount(int enemy_state) {
			switch(enemy_state) {
			case IDLE:
				return 5;
			case RUNNING:
				return 5;
			case ATTACK:
				return 5;
			case HIT:
				return 1;
			case DEAD:
				return 1;
			default:
				return 1;
			}
		}
	}

	
	public static class Clounds{
		public static final int CLOUD_WIDTH_DEFAULT = 399;
		public static final int CLOUD_HEIGTH_DEFAULT = 87;
		public static final int CLOUD_WIDTH = (int)(CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int CLOUD_HEIGTH = (int)(CLOUD_HEIGTH_DEFAULT * Game.SCALE);
	}
	
	public static class UI{
		public static class PauseButtons{
			public static final int DEFAULT_SOUND_SIZE = 42;
			public static final int SOUND_SIZE = (int)(DEFAULT_SOUND_SIZE * Game.SCALE);
		}	
		public static class URMButtons{
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
		}
		public static class VolumeButtons{
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;
			
			public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
	}
	
	//Moving directions of the Character
	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	//TODO Getting more animations to include this class to the game panel	
	// Declares movement variables 
	public static class PlayerConstants{
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;

		
		// Class for figuring out the length of the animation Array depending on the movement of the character
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case RUNNING:
				return 4;
			case IDLE:
			case HIT:
				return 3;
			case DEAD:
				return 3;
			case ATTACK:
				return 2;
			default:
				return 1;
			}
		}


		
	}
}
