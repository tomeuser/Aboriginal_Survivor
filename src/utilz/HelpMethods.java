package utilz;
import static utilz.Constants.Directions.*;

import static utilz.HelpMethods.IsSolid;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import main.Game;

public class HelpMethods {
	
	public static boolean IsEntityOnFloor(Float hitbox, int[][] levelData) { 
		// Check the pixel below bottom left and right 
		if(!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData))
			if(!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) 
				return false;
		return true;
	}

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
		
		if(!IsSolid(x, y, levelData)) 
			if(!IsSolid(x + width, y + height, levelData)) 
				if(!IsSolid(x + width, y, levelData)) 
					if(!IsSolid(x, y + height, levelData)) 
						if(!IsSolid(x, y + (1*height/4), levelData))
							if(!IsSolid(x + width, y + (1*height/4), levelData))
								if(!IsSolid(x, y + (2*height/4), levelData))
									if(!IsSolid(x + width, y + (2*height/4), levelData)) 		
										if(!IsSolid(x, y + (3*height/4), levelData))
											if(!IsSolid(x + width, y + (3*height/4), levelData)) 	
										return true;
		return false;
					
	}
	public static boolean IsSolid(float x, float y, int[][] levelData) {
		int maxWidth = levelData[0].length * Game.TILES_SIZE;
		if(x < 0 || x >= maxWidth)
			return true;
		if(y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
	
		return IsTileSolid((int)xIndex, (int)yIndex, levelData);
	}
	
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];
				
				if( value != 15) 
					return true;
				
				return false;
	}
	
	public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
		if(xSpeed > 0) {
			//right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		}
		else {
			//left
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	//false!!!
	public static float getEntityRightUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
		//int currentTileDown = (int)((hitbox.y / Game.TILES_SIZE) + 1);

		if(airSpeed > 0) {
			//Falling - touching floor
			
			return hitbox.y;
		} else {
			//Jumping
			return currentTile * Game.TILES_SIZE;

		}
	}
	
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][]lvlData, float dir) {
		if(dir == LEFT)
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	
	public static boolean isSigthClear(int[][] lvlData, Rectangle2D.Float hitbox, Rectangle2D.Float hitbox2, int tileY2) {
		int firstXTile = (int)(hitbox.x/ Game.TILES_SIZE);
		int secondXTile = (int)(hitbox2.x/ Game.TILES_SIZE);

		if(firstXTile > secondXTile) {
			for(int i = 0; i < firstXTile - secondXTile; i++) {
				if(IsTileSolid(secondXTile + i, tileY2, lvlData))
					return false;
				if(!IsTileSolid(secondXTile + i, tileY2 + 1, lvlData))
					return false;
			}
		}else {
			for(int i = 0; i < secondXTile - firstXTile; i++) {
				if(IsTileSolid(firstXTile + i, tileY2, lvlData))
					return false;
				if(!IsTileSolid(firstXTile + i, tileY2 + 1, lvlData))
					return false;
			}
		}

		return true;
	}

}
