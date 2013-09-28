package com.me.unbreakable;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bar extends GameObject {

	private final int LONG_TYPE = 3;
	private final int NORMAL_TYPE = 2;
	private final int SHORT_TYPE = 1;

	private int NoOfLife;
	private ArrayList<Item> itemList;
	private int long_short_normarl_type;
	private boolean isBullet;

	public Bar(int imageId, int life) {
		super(imageId);
		this.NoOfLife = life;
		itemList = new ArrayList<Item>();
		long_short_normarl_type = 2;
		this.isBullet = false;
	}

	public int getNoOfLife() {
		return NoOfLife;
	}

	public void setNoOfLife(int noOfLife) {
		NoOfLife = noOfLife;
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}

	public int getLong_short_normarl_type() {
		return long_short_normarl_type;
	}

	public void setLong_short_normarl_type(int long_short_normarl_type) {
		this.long_short_normarl_type = long_short_normarl_type;
	}

	public boolean isBullet() {
		return isBullet;
	}

	public void setBullet(boolean isBullet) {
		this.isBullet = isBullet;
	}

	public void addItem(Item item) {
		itemList.add(item);
	}

	public void updateItem(Item item) {

		for (Item it : itemList) {

			if (it.getType() == item.getType()) {
				it.setEndDuration(item.getEndDuration());
			}
		}

	}

	public void removeAllItem() {
		itemList.clear();
	}

	public void removeItem(Item item) {

		Iterator<Item> iter = itemList.iterator();
		while (iter.hasNext()) {

			Item temp = iter.next();
			if (temp.getType() == item.getType()) {
				iter.remove();
			}
		}
	}

	public void removeItem(int itemType) {

		Iterator<Item> iter = itemList.iterator();
		while (iter.hasNext()) {

			Item temp = iter.next();
			if (temp.getType() == itemType) {
				iter.remove();
			}
		}
	}

	public void increaseLifeByOne() {

		if (this.NoOfLife >= 3) {
			this.NoOfLife = 3;
		} else if (this.NoOfLife >= 0) {
			this.NoOfLife++;
		}

	}

}
