package com.me.unbreakable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Monster extends Rectangle {
	private Texture image;
	private int movementSpeed;
	private int direction;

	public Monster(String imageLocation, int movementSpeed) {
		image = new Texture(Gdx.files.internal(imageLocation));
		this.movementSpeed = movementSpeed;
	}

	public Texture getImage() {
		return image;
	}

	public void setImage(String imageLocation) {
		this.image = new Texture(Gdx.files.internal(imageLocation));
	}

	public int getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void dispose(){
		image.dispose();
	}

}
