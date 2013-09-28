package com.me.unbreakable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Ball extends GameObject {
	private int status;

	Ball(int imageId, float speed, int status, int direction) {
		super(imageId, speed, direction);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean changeDirectionWhenHitScreenSides(Bar bar) {

		boolean hitWall=false;
      
		if (x < 0) {
			x = 0;
			hitWall=true;
			if (getDirection() == GameCore.DIRECTION_LEFT_UP) {
				setDirection(GameCore.DIRECTION_RIGHT_UP);
			} else if (getDirection() == GameCore.DIRECTION_LEFT_DOWN) {
				setDirection(GameCore.DIRECTION_RIGHT_DOWN);
			}
		}

		if (x + width > GameCore.SCREEN_WIDTH) {
			x = GameCore.SCREEN_WIDTH - width;
			hitWall=true;
			if (getDirection() == GameCore.DIRECTION_RIGHT_UP) {
				setDirection(GameCore.DIRECTION_LEFT_UP);
			} else if (getDirection() == GameCore.DIRECTION_RIGHT_DOWN) {
				setDirection(GameCore.DIRECTION_LEFT_DOWN);
			}
		}

		if (y + height <= bar.y) {
			return true;
		}

		if (y + height > GameCore.SCREEN_HEIGHT - GameCore.TOP_PANEL_HEIGHT) {
			y = GameCore.SCREEN_HEIGHT - GameCore.TOP_PANEL_HEIGHT - height;
			hitWall=true;
			if (getDirection() == GameCore.DIRECTION_LEFT_UP) {
				setDirection(GameCore.DIRECTION_LEFT_DOWN);
			} else if (getDirection() == GameCore.DIRECTION_RIGHT_UP) {
				setDirection(GameCore.DIRECTION_RIGHT_DOWN);
			} else if (getDirection() == GameCore.DIRECTION_UP) {
				setDirection(GameCore.DIRECTION_DOWN);
			}
		}
		if(GameCore.isSoundOn){
			
			if(hitWall){
				GameCore.ball_bouncing_sound.play();
			}

			
		}
		
		
		return false;
	}

}
