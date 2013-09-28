package com.me.unbreakable;

import com.badlogic.gdx.Gdx;

public class BombExplosion {
	private float x, y;
	private int explosionCount;
	private float bombExplosionStateTime;

	public BombExplosion(float x, float y) {
		super();
		this.x = x;
		this.y = y;
		this.explosionCount = 0;
		this.bombExplosionStateTime = 0f;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getExplosionCount() {
		return explosionCount;
	}

	public void setExplosionCount(int explosionCount) {
		this.explosionCount = explosionCount;
	}

	public float getBombExplosionStateTime() {
		return bombExplosionStateTime;
	}

	public void setBombExplosionStateTime(float bombExplosionStateTime) {
		this.bombExplosionStateTime = bombExplosionStateTime;
	}

	public void increaseBombExplosionStateTime() {
		this.bombExplosionStateTime += Gdx.graphics.getDeltaTime();
	}

	public void increaseExplosionCount() {
		this.explosionCount++;
	}
}
