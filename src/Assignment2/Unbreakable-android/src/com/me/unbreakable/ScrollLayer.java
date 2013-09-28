package com.me.unbreakable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class ScrollLayer extends Group {

	private float ScreenWidth;
	private float ScreenHeight;

	private float sectionWidth, sectionHeight;
	private Group sections;
	private float amountX = 0;

	private int transmission = 0;
	private float stopSection = 0;
	private float speed = 500; // 1500;
	private int currentSection = 1;
	private float flingSpeed = 1000;

	private float overscrollDistance = 500;

	private Rectangle cullingArea = new Rectangle();
	private Actor touchFocusedChild;
	private ActorGestureListener actorGestureListener;

	public ScrollLayer(float ScreenWidth, float ScreenHeight, float PageWidth) {

		this.ScreenWidth = ScreenWidth;
		this.ScreenHeight = ScreenHeight;

		Texture bg = new Texture(Gdx.files.internal("images/aztec_menu.png"));
		Image frame = new Image(bg);
		frame.setSize(ScreenWidth, ScreenHeight);
		frame.setZIndex(0);
		this.addActor(frame);

		sections = new Group();
		sections.setZIndex(1);
		this.addActor(sections);

		sectionWidth = PageWidth;
		sectionHeight = ScreenHeight;

		actorGestureListener = new ActorGestureListener() {

			@Override
			public void tap(InputEvent event, float x, float y, int count,
					int button) {

			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX,
					float deltaY) {

				if (amountX < -overscrollDistance)
					return;
				if (amountX > (sections.getChildren().size - 1) * sectionWidth
						+ overscrollDistance)
					return;

				amountX -= deltaX;

				cancelTouchFocusedChild();
			}

			@Override
			public void fling(InputEvent event, float velocityX,
					float velocityY, int button) {

				if (Math.abs(velocityX) > flingSpeed) {

					if (velocityX > 0)
						setStopSection(currentSection - 2);
					else
						setStopSection(currentSection);

				}

				cancelTouchFocusedChild();
			}

			@Override
			public void touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (event.getTarget() instanceof Label) {
					touchFocusedChild = event.getTarget();
				}

			}

		};

		this.addListener(actorGestureListener);

	}

	public void addWidget(Actor widget) {
		widget.setX(this.sections.getChildren().size * sectionWidth);
		widget.setY(0);
		widget.setWidth(sectionWidth);
		widget.setHeight(sectionHeight);

		sections.addActor(widget);

	}

	private int calculateCurrentSection() {
		int section = Math.round(amountX / sectionWidth) + 1;
		if (section > sections.getChildren().size)
			return sections.getChildren().size;
		if (section < 1)
			return 1;
		return section;
	}

	private int getSectionsCount() {
		return sections.getChildren().size;
	}

	private void setStopSection(int stoplineSection) {

		if (stoplineSection < 0)
			stoplineSection = 0;
		if (stoplineSection > this.getSectionsCount() - 1)
			stoplineSection = this.getSectionsCount() - 1;

		stopSection = stoplineSection * sectionWidth;

		if (amountX < stopSection) {
			transmission = 1;
		} else {
			transmission = -1;
		}
	}

	private void move(float delta) {

		if (amountX < stopSection) {
			if (transmission == -1) {
				amountX = stopSection;
				currentSection = calculateCurrentSection();
				return;
			}
			amountX += speed * delta;

		} else if (amountX > stopSection) {
			if (transmission == 1) {
				amountX = stopSection;
				currentSection = calculateCurrentSection();
				return;
			}
			amountX -= speed * delta;
		}
	}

	@Override
	public void act(float delta) {

		sections.setX(-amountX + (ScreenWidth / 2 - sectionWidth / 2));

		// cullingArea.set( -sections.getX() + 50, sections.getY(), sectionWidth
		// - 100, sectionHeight );
		// sections.setCullingArea(cullingArea);

		cullingArea.set(
				sections.getX() > 0 ? sections.getX() : -sections.getX(),
				sections.getY(), ScreenWidth, ScreenHeight);
		sections.setCullingArea(cullingArea);

		if (actorGestureListener.getGestureDetector().isPanning()) {
			setStopSection(calculateCurrentSection() - 1);
		} else {
			move(delta);
		}
	}

	void cancelTouchFocusedChild() {

		if (touchFocusedChild == null)
			return;

		try {
			this.getStage().cancelTouchFocus(this.actorGestureListener, this);

		} catch (Exception e) {

		}

		touchFocusedChild = null;
	}

	public void setFlingSpeed(float _flingSpeed) {
		flingSpeed = _flingSpeed;
	}

	public void setSpeed(float _speed) {
		speed = _speed;
	}

	public void setOverscrollDistance(float _overscrollDistance) {
		overscrollDistance = _overscrollDistance;
	}

	public void MoveToNextPage() {
		if (stopSection >= (getSectionsCount() - 1) * sectionWidth)
			return;

		stopSection += sectionWidth;
		transmission = 1;
	}

	public void MoveToPreviousPage() {
		if (stopSection <= 0)
			return;

		stopSection -= sectionWidth;
		transmission = -1;
	}

	public void MoveToPage(int page) {
		if (page < 0 || page >= getSectionsCount())
			return;
		float tmp = stopSection;
		stopSection = page * sectionWidth;
		if (stopSection > tmp)
			transmission = 1;
		else
			transmission = -1;
	}

	public void SelectPage(int page) {
		if (page < 0 || page >= getSectionsCount())
			return;
		stopSection = page * sectionWidth;
		amountX = stopSection;
	}

}