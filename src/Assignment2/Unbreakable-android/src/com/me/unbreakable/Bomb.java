package com.me.unbreakable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Bomb extends GameObject {

	public Bomb(int imageId, float speed, int direction) {
		super(imageId, speed, direction);
	}

	public void changeDirectionWhenHitScreenSides() {
		if (x < 0) {
			x = 0;

			if (getDirection() == GameCore.DIRECTION_LEFT_UP) {
				setDirection(GameCore.DIRECTION_RIGHT_UP);
			} else if (getDirection() == GameCore.DIRECTION_LEFT_DOWN) {
				setDirection(GameCore.DIRECTION_RIGHT_DOWN);
			}
		}

		if (x + width > GameCore.SCREEN_WIDTH) {
			x = GameCore.SCREEN_WIDTH - width;

			if (getDirection() == GameCore.DIRECTION_RIGHT_UP) {
				setDirection(GameCore.DIRECTION_LEFT_UP);
			} else if (getDirection() == GameCore.DIRECTION_RIGHT_DOWN) {
				setDirection(GameCore.DIRECTION_LEFT_DOWN);
			}
		}

		if (y + height > GameCore.SCREEN_HEIGHT - GameCore.TOP_PANEL_HEIGHT) {
			y = GameCore.SCREEN_HEIGHT - GameCore.TOP_PANEL_HEIGHT - height;

			if (getDirection() == GameCore.DIRECTION_LEFT_UP) {
				setDirection(GameCore.DIRECTION_LEFT_DOWN);
			} else if (getDirection() == GameCore.DIRECTION_RIGHT_UP) {
				setDirection(GameCore.DIRECTION_RIGHT_DOWN);
			} else if (getDirection() == GameCore.DIRECTION_UP) {
				setDirection(GameCore.DIRECTION_DOWN);
			}
		}

	}
}
