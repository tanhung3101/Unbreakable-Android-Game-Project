package com.me.unbreakable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/*
 * Item type is:
 * + 1: life (heart)
 * + 2: long paddle
 * + 3: short paddle
 * + 4: gun paddle
 * 
 */
public class Item extends GameObject {

	private String name;
	private int type;
	private float duration;
	private float endDuration;

	public Item(String name, int type, int imageId, float duration) {
		super(imageId);
		this.name = name;
		this.type = type;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getEndDuration() {
		return endDuration;
	}

	public void setEndDuration(float endDuration) {
		this.endDuration = endDuration;
	}

	public void calculateEndDuration(float startTime) {

		this.setEndDuration(startTime + duration * GameCore.ONE_SECOND);

	}

}
