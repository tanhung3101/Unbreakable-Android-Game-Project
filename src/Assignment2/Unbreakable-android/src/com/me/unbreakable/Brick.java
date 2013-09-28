package com.me.unbreakable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Brick extends GameObject {

	private int durability;
	private int type;
	private Item item;

	public Brick(int imageId, int type, int durability) {
		super(imageId);
		this.durability = durability;
		this.type = type;
		this.item = null;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public void decreaseDurability() {

		// Brick with durability of 4 will be unable to destroy
		if (durability <= 3) {
			durability--;
		}

		// Refresh image
		if (durability > 0) {
			switch (durability) {
			case 1:
				setImageId(GameCore.IMG_BRICK_DURABILITY_1);
				break;
			case 2:
				setImageId(GameCore.IMG_BRICK_DURABILITY_2);
				break;
			case 3:
				setImageId(GameCore.IMG_BRICK_DURABILITY_3);
				break;
			}
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}
