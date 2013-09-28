package com.me.unbreakable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Boss extends GameObject {

	private int life;
	private int form;

	public Boss(int imageId, int life, float speed, int form,
			int direction) {
		super(imageId, speed, direction);
		this.life = life;
		this.form = form;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getForm() {
		return form;
	}

	public void setForm(int form) {
		this.form = form;
	}

	public int getHit() {
		// Change immage when get hit
		setImageId(GameCore.IMG_BOSS_GET_HIT);

		life -= 1;
		return life;
	}

	public void resetImage() {
		setImageId(GameCore.IMG_BOSS);
	}

	public void checkMovementArea() {
		if (x <= GameCore.BRICK_BLOCK_LEFT_RIGHT_PADDING) {
			x = GameCore.BRICK_BLOCK_LEFT_RIGHT_PADDING;
			setDirection(GameCore.DIRECTION_RIGHT);
		}

		else if (x + width >= GameCore.SCREEN_WIDTH
				- GameCore.BRICK_BLOCK_LEFT_RIGHT_PADDING) {
			x = GameCore.SCREEN_WIDTH - GameCore.BRICK_BLOCK_LEFT_RIGHT_PADDING
					- width;
			setDirection(GameCore.DIRECTION_LEFT);
		}

		else if (y + height >= GameCore.BOSS_TOP_LIMIT) {
			y = GameCore.BOSS_TOP_LIMIT - height;
			setDirection(GameCore.DIRECTION_DOWN);
		}

		else if (y <= GameCore.BOSS_BOTTOM_LIMIT) {
			setDirection(GameCore.DIRECTION_UP);
		}
	}
}
