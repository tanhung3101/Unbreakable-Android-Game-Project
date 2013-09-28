package com.me.unbreakable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject extends Rectangle {

	private int imageId;
	private float speed;
	private int direction;

	public GameObject(int imageId, float speed, int direction) {
		this.imageId = imageId;
		this.speed = speed;
		this.direction = direction;
	}

	public GameObject(int imageId) {
		this.imageId = imageId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void movement() {
		if (getDirection() == GameCore.DIRECTION_DOWN) {
			y -= getSpeed() * Gdx.graphics.getDeltaTime();
		} else if (getDirection() == GameCore.DIRECTION_UP) {
			y += getSpeed() * Gdx.graphics.getDeltaTime();
		} else if (getDirection() == GameCore.DIRECTION_LEFT_UP) {
			x -= getSpeed() * Gdx.graphics.getDeltaTime();
			y += getSpeed() * Gdx.graphics.getDeltaTime();
		} else if (getDirection() == GameCore.DIRECTION_LEFT_DOWN) {
			x -= getSpeed() * Gdx.graphics.getDeltaTime();
			y -= getSpeed() * Gdx.graphics.getDeltaTime();
		} else if (getDirection() == GameCore.DIRECTION_RIGHT_UP) {
			x += getSpeed() * Gdx.graphics.getDeltaTime();
			y += getSpeed() * Gdx.graphics.getDeltaTime();
		} else if (getDirection() == GameCore.DIRECTION_RIGHT_DOWN) {
			x += getSpeed() * Gdx.graphics.getDeltaTime();
			y -= getSpeed() * Gdx.graphics.getDeltaTime();
		} else if (getDirection() == GameCore.DIRECTION_RIGHT) {
			x += getSpeed() * Gdx.graphics.getDeltaTime();
		} else if (getDirection() == GameCore.DIRECTION_LEFT) {
			x -= getSpeed() * Gdx.graphics.getDeltaTime();
		}
	}
}
