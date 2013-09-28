package com.me.unbreakable;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GameCore implements ApplicationListener {

	static final int BRICK_POINT = 10;
	static final int DIRECTION_UP = 0;
	static final int DIRECTION_DOWN = 1;
	static final int DIRECTION_LEFT_UP = 2;
	static final int DIRECTION_LEFT_DOWN = 3;
	static final int DIRECTION_RIGHT_UP = 4;
	static final int DIRECTION_RIGHT_DOWN = 5;
	static final int DIRECTION_LEFT = 6;
	static final int DIRECTION_RIGHT = 7;
	static int SCREEN_WIDTH = 480;
	static int SCREEN_HEIGHT = 800;
	static final int BALL_WIDTH = 20;
	static final int BALL_HEIGHT = 20;
	static final int BAR_SPEED = 500;
	static final int BAR_HEIGHT = 12;
	static final int BAR_SHORT_WIDTH = 60;
	static final int BAR_LONG_WIDTH = 120;
	static final int BAR_NORMAL_WIDTH = 92;
	static final int BALL_STOP = 0;
	static final int BALL_BOUNCING = 1;
	static final int TOP_PANEL_HEIGHT = 120;
	static final int BOTTOM_PANEL_HEIGHT = 80;
	static final int BRICK_WIDTH = 70;
	static final int BRICK_HEIGHT = 25;
	static final int BALL_DEFAULT_SPEED = 300;
	static final int BOMB_SPEED = 300;
	static final int BOMB_WIDTH = 40;
	static final int BOMB_HEIGHT = 60;
	static int BRICK_BLOCK_LEFT_RIGHT_PADDING = 35;
	static int BRICK_BLOCK_WIDTH = 410;
	static int BRICK_HORIZONTAL_SPACE = 15;
	static int BRICK_VERTICAL_SPACE = 5;
	static final int BRICK_BLOCK_TOP_PADDING = 100;
	static final int MAXIMUM_BRICK_TYPES = 3;
	static final int MAXIMUM_BRICK_DURABILITY = 3;
	static final int NUMBER_OF_LIFE = 3;
	static final int SCORE_PANEL_MOVE_SPEED = 500;
	static final int DRAW_TYPE_MAIN_MENU = 0;
	static final int DRAW_TYPE_GAME = 1;
	static final int DRAW_TYPE_HIGHSCORE = 2;
	static final int DRAW_TYPE_LEVEL_SELECTION = 3;
	static final int DRAW_TYPE_OPTION = 4;
	static final int BOSS_WIDTH = 100;
	static final int BOSS_HEIGHT = 101;
	static final int BOSS_FORM1_SPEED = 200;
	static final float ONE_SECOND = 1000000000;
	static final float BOSS_CHANGE_DIRECTION_DELAY = 2; // seconds
	static final int NUM_OF_DIRECTIONS = 8;
	static final int BOSS_STUN_TIME = 1;
	static final int BOSS_TOP_LIMIT = 600;
	static final int BOSS_BOTTOM_LIMIT = 250;
	static final int BAR_EXPLOSION_FRAME_COLS = 5;
	static final int BAR_EXPLOSION_FRAME_ROWS = 5;
	static final int BOMB_EXPLOSION_FRAME_COLS = 5;
	static final int BOMB_EXPLOSION_FRAME_ROWS = 5;
	static final int BOSS_LIVES = 10;
	static final int SHOW_MESSAGE_TIME = 2;
	static final int NUM_OF_HIGHSCORES = 5;

	static final int IMG_BOSS = 0;
	static final int IMG_BOSS_GET_HIT = 1;
	static final int IMG_BRICK_DURABILITY_1 = 2;
	static final int IMG_BRICK_DURABILITY_2 = 3;
	static final int IMG_BRICK_DURABILITY_3 = 4;
	static final int IMG_NORMAL_BOMB = 5;
	static final int IMG_SPECIAL_BOMB = 6;
	static final int IMG_SHORT_BAR = 7;
	static final int IMG_NORMAL_BAR = 8;
	static final int IMG_LONG_BAR = 9;
	static final int IMG_BALL = 10;
	static final int IMG_ITEM_RANDOM = 11;
	static final int IMG_ITEM_LIFE = 12;
	static final int IMG_ITEM_GUN_LONG_BAR = 13;
	static final int IMG_ITEM_GUN_SHORT_BAR = 14;
	static final int IMG_ITEM_GUN_NORMAL_BAR = 15;
	static final int NUM_OF_LEVELS = 3;

	static boolean isSoundOn, isVibrationOn;

	private static int drawType;
	private Ball ball;
	private Bar bar;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private ArrayList<Brick> bricks;
	private ArrayList<Bomb> bombs;
	static Texture backgroundImage, lifeFullImage, lifeDieImage,
			bottomPanelImage, bulletImage, scorePanelImage, boxItemImage,
			barExplosionSheet, bombExplosionSheet, iconGun, iconLongBar,
			iconShortBar, pauseImage, bossImage, bossGetHitImage,
			brickDurability_1Img, brickDurability_2Img, brickDurability_3Img,
			normalBombImg, specialBombImg, normalBarImg, shortBarImg,
			longBarImg, ballImg, gameTitleImg, menuBackImg;
	private Vector3 touchPosition;
	private boolean checkBarPosition = true;
	private long lastDropTime;
	private int dropDelay;
	private BitmapFont font;
	private int scoreTotal = 0;
	private int scoreBonusTime = 1;
	private final String scoreLabel = "Score:";
	private final String playerLabel = "Player:";
	private String playerName = "Superman";
	private boolean completeLevel;
	private Rectangle scorePanel;
	private boolean isGameOver;
	private ScrollLayer menuScrollLayer, highscoreScrollLayer,
			levelScrollLayer, optionScrollLayer;
	private Skin skin, highScoreSkin;
	private Stage stage;
	private TextButton btnStartGame, btnOptions, btnHighscores, btnQuit;
	private boolean alreadyChangedBallDirection;
	private Boss boss;
	private float bossGetHitTime = 0;
	private float lastBossChangeDirectionTime;
	private static boolean spawnBoss = false;
	private float lastBossStunTime = 0;
	private int currentLevel;
	private Random random = new Random();
	private Animation barExplosionAnimation, bombExplosionAnimation;
	private TextureRegion[] barExplosionFrames, bombExplosionFrames;
	private TextureRegion currentFrame;
	private float barExplosionStateTime;
	private int barExplosionCount = 0;
	private boolean barExplosion = false;
	private ArrayList<BombExplosion> bombExplosions = new ArrayList<BombExplosion>();
	private Rectangle pauseButton;
	private static boolean gamePause = true;
	private long showMessageTimeCount;
	private boolean showingMessage = false;
	private List<Long> highscores = new ArrayList<Long>(),
			scores = new ArrayList<Long>();
	private List<String> highscorePlayers = new ArrayList<String>(),
			players = new ArrayList<String>();
	private Table menuTable, highscoreTable, levelTable, optionTable;
	private boolean isLoading, confirmSubmitScore = false;
	private static boolean isConnecting = false;
	private int levelStartScore = 0;

	private int numberOfItems = 3;
	private final String ITEM_NAME_LONG_BAR = "Long bar";
	private final String ITEM_NAME_SHORT_BAR = "short bar";
	private final String ITEM_NAME_GUN_BAR = "gun bar";
	private final String ITEM_NAME_LIFE = "life";

	private final int ITEM_TYPE_LIFE = 1;
	private final int ITEM_TYPE_LONG_BAR = 2;
	private final int ITEM_TYPE_SHORT_BAR = 3;
	private final int ITEM_TYPE_GUN_BAR = 4;

	private final int ITEM_SPEED = 100;
	private final int ITEM_WIDTH = 32;
	private final int ITEM_HEIGHT = 32;
	private ArrayList<Item> itemDroppeds;
	private ArrayList<Rectangle> bullets;

	final int LONG_TYPE = 3;
	final int NORMAL_TYPE = 2;
	final int SHORT_TYPE = 1;
	final int BULLET_WIDTH = 32;
	final int BULLET_HEIGHT = 78;
	private long bullet_last_drop;
	private final int BULLET_SHOT_SPEED = 200;
	private final int BULLET_DELAY = 1;
	private final int ITEM_DURATION = 10;

	private Label highscoreTitle, scr1, scr2, scr3, scr4, scr5, name1, name2,
			name3, name4, name5, soundOn, vibrationOn;
	private SelectBox slbSound, slbVibration;
	private String[] slbItems = new String[] { "   On   ", "   Off   " };
	private Image btnBack;
	private TextButton btnLevel1, btnLevel2, btnLevel3, btnLevelBack,
			btnSaveOption, btnCancelOption;

	private int tempScore = 0;

	private String SCORE_TITLE_STRING = "";
	private int scoreRemainLife = 50;
	private String SCORE_LIFE_STRING = "Life:";
	private String SCORE_TOTAL_STRING = "Total:";
	private boolean drawLifeComplete = false;
	private boolean drawTotalComplete = false;
	private Rectangle retryLevelButton, nextLevelButton, exitButton;
	private static Texture retryButtonImage, nextButtonImage, exitButtonImage,
			gun_long_barImage, gun_short_barImage, gun_normal_barImage;

	private BitmapFont fontLarge;
	private static final int BOSS_POINT = 30;

	public static Sound pick_item_sound, click_button_sound,
			complete_level_sound, gameover_sound, ball_bouncing_sound,
			laser_shoot_sound;
	static Music bg_start_music, bg_boss_music;

	private Rectangle submitScoreButton;
	private Texture submitScoreImage;

	public static Sound brick_break_sound, bomb_explosion_sound,
			boss_hit_sound;

	public static boolean isSoundPlayed;

	@Override
	public void create() {

		// Initialize camera
		setCamera();
		// camera = new OrthographicCamera();
		// camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		BRICK_BLOCK_LEFT_RIGHT_PADDING = (SCREEN_WIDTH - BRICK_BLOCK_WIDTH) / 2;

		// Overriding the power of 2 check in Texture class
		Texture.setEnforcePotImages(false);

		// Default sound and vibration options
		isSoundOn = true;
		isVibrationOn = true;

		// set sound
		pick_item_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/pick_up_item_sound.wav"));
		laser_shoot_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/fire_bullet.wav"));
		click_button_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/click_button_sound.wav"));
		;
		complete_level_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/complete_level_sound.mp3"));
		gameover_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/gameover_sound.mp3"));
		;
		ball_bouncing_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/ball_bouncing_sound.mp3"));

		brick_break_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/break_brick.wav"));
		bomb_explosion_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/explode.wav"));

		// set background music
		bg_start_music = Gdx.audio.newMusic(Gdx.files
				.internal("sound/happy_life_bg_start_music.mp3"));
		bg_boss_music = Gdx.audio.newMusic(Gdx.files
				.internal("sound/boss_sound_3.mp3"));

		boss_hit_sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/boss_hit.mp3"));

		// Set default draw type
		drawType = DRAW_TYPE_MAIN_MENU;

		// set font of String
		font = new BitmapFont(Gdx.files.internal("font/font1.fnt"),
				Gdx.files.internal("font/font1.png"), false);

		fontLarge = new BitmapFont(Gdx.files.internal("font/font_large.fnt"),
				Gdx.files.internal("font/font_large_0.png"), false);
		// Load all images
		backgroundImage = new Texture(
				Gdx.files.internal("images/ancient_background_bright.png"));
		bottomPanelImage = new Texture(
				Gdx.files.internal("images/bottom_panel.png"));
		lifeFullImage = new Texture(Gdx.files.internal("images/life_full.png"));
		lifeDieImage = new Texture(Gdx.files.internal("images/life_die.png"));
		scorePanelImage = new Texture(
				Gdx.files.internal("images/aztec_score_panel.jpg"));
		boxItemImage = new Texture(Gdx.files.internal("images/wooden_box.png"));
		bulletImage = new Texture(Gdx.files.internal("images/fire_bullet.png"));
		iconGun = new Texture(Gdx.files.internal("images/item_gun.png"));
		iconLongBar = new Texture(
				Gdx.files.internal("images/item_long_bar.png"));
		iconShortBar = new Texture(
				Gdx.files.internal("images/item_short_bar.png"));
		pauseImage = new Texture(
				Gdx.files.internal("images/pause_circle_button.png"));
		bossImage = new Texture(Gdx.files.internal("images/boss_original.png"));
		bossGetHitImage = new Texture(
				Gdx.files.internal("images/boss_getHit_original.png"));
		brickDurability_1Img = new Texture(
				Gdx.files.internal("images/bricks/1.png"));
		brickDurability_2Img = new Texture(
				Gdx.files.internal("images/bricks/2.png"));
		brickDurability_3Img = new Texture(
				Gdx.files.internal("images/bricks/3.png"));
		normalBombImg = new Texture(
				Gdx.files.internal("images/boss_bomb_1_original.png"));
		specialBombImg = new Texture(
				Gdx.files.internal("images/boss_bomb_2_original.png"));
		normalBarImg = new Texture(Gdx.files.internal("images/normal_bar.png"));
		shortBarImg = new Texture(Gdx.files.internal("images/short_bar.png"));
		longBarImg = new Texture(Gdx.files.internal("images/long_bar.png"));
		ballImg = new Texture(Gdx.files.internal("images/ancient_ball.png"));
		gameTitleImg = new Texture(Gdx.files.internal("images/game_title.png"));
		menuBackImg = new Texture(
				Gdx.files.internal("images/menu_back_button.png"));
		retryButtonImage = new Texture(
				Gdx.files.internal("images/retryButton.png"));
		nextButtonImage = new Texture(
				Gdx.files.internal("images/nextButton.png"));
		exitButtonImage = new Texture(
				Gdx.files.internal("images/exitButton.png"));

		submitScoreImage = new Texture(
				Gdx.files.internal("images/submit_score.png"));

		gun_long_barImage = new Texture(
				Gdx.files.internal("images/item_gun_long.png"));
		gun_short_barImage = new Texture(
				Gdx.files.internal("images/item_gun_short.png"));
		gun_normal_barImage = new Texture(
				Gdx.files.internal("images/item_gun_normal.png"));

		// initialize bar and touchPosition
		bar = new Bar(IMG_NORMAL_BAR, NUMBER_OF_LIFE);
		bar.x = (SCREEN_WIDTH / 2) - (BAR_SHORT_WIDTH / 2);
		bar.y = 100;
		bar.width = BAR_NORMAL_WIDTH;
		bar.height = BAR_HEIGHT;

		touchPosition = new Vector3();
		touchPosition.set(bar.width / 2 + bar.x, bar.y, 0);

		// Initialize the ball Params: image location, movement speed, ball
		// status, ball default direction
		ball = new Ball(IMG_BALL, BALL_DEFAULT_SPEED, BALL_STOP, DIRECTION_UP);
		ball.width = BALL_WIDTH;
		ball.height = BALL_HEIGHT;
		moveBallToMiddleOfBar();

		// Initialize score panel
		scorePanel = new Rectangle();
		scorePanel.x = SCREEN_WIDTH;
		scorePanel.y = 0;
		scorePanel.width = SCREEN_WIDTH;
		scorePanel.height = SCREEN_HEIGHT;

		// Initialize boss
		// Params: imageLocation, numOfLives, speed, numOfForm, movement
		// direction
		boss = new Boss(IMG_BOSS, BOSS_LIVES, BOSS_FORM1_SPEED, 1, DIRECTION_UP);
		boss.x = SCREEN_WIDTH / 2 - (BOSS_WIDTH / 2);
		boss.y = SCREEN_HEIGHT / 2;
		boss.width = BOSS_WIDTH;
		boss.height = BOSS_HEIGHT;

		// Create pause button
		pauseButton = new Rectangle();
		pauseButton.width = pauseImage.getWidth();
		pauseButton.height = pauseImage.getHeight();
		pauseButton.x = SCREEN_WIDTH * 3 / 4 - (pauseImage.getWidth() / 2);
		pauseButton.y = 720;

		// create Retry Button and Next Level Button
		retryLevelButton = new Rectangle();
		retryLevelButton.width = retryButtonImage.getWidth();
		retryLevelButton.height = retryButtonImage.getHeight();
		retryLevelButton.x = (SCREEN_WIDTH / 2)
				- (retryButtonImage.getWidth() / 2);
		retryLevelButton.y = 200;

		nextLevelButton = new Rectangle();
		nextLevelButton.width = nextButtonImage.getWidth();
		nextLevelButton.height = nextButtonImage.getHeight();
		nextLevelButton.x = (SCREEN_WIDTH / 2)
				- (nextButtonImage.getWidth() / 2);
		nextLevelButton.y = 280;

		exitButton = new Rectangle();
		exitButton.width = exitButtonImage.getWidth();
		exitButton.height = exitButtonImage.getHeight();
		exitButton.x = (SCREEN_WIDTH / 2) - (exitButtonImage.getWidth() / 2);
		exitButton.y = 40;

		submitScoreButton = new Rectangle();
		submitScoreButton.width = submitScoreImage.getWidth();
		submitScoreButton.height = submitScoreImage.getHeight();
		submitScoreButton.x = (SCREEN_WIDTH / 2)
				- (submitScoreImage.getWidth() / 2);
		submitScoreButton.y = 120;

		// Drop bomb delay
		dropDelay = 3;

		// Initialize SpriteBatch
		batch = new SpriteBatch();

		// Initialize bombs array
		bombs = new ArrayList<Bomb>();

		// Initialize bricks
		bricks = new ArrayList<Brick>();

		// Initialize itemDroppeds;
		itemDroppeds = new ArrayList<Item>();

		// Initialize bullets;
		bullets = new ArrayList<Rectangle>();

		// Bar Explosion
		barExplosionSheet = new Texture(
				Gdx.files.internal("images/bar_explotion_animation.png"));
		TextureRegion[][] tmp = TextureRegion.split(barExplosionSheet,
				barExplosionSheet.getWidth() / BAR_EXPLOSION_FRAME_COLS,
				barExplosionSheet.getHeight() / BAR_EXPLOSION_FRAME_ROWS);
		barExplosionFrames = new TextureRegion[BAR_EXPLOSION_FRAME_COLS
				* BAR_EXPLOSION_FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < BAR_EXPLOSION_FRAME_ROWS; i++) {
			for (int j = 0; j < BAR_EXPLOSION_FRAME_COLS; j++) {
				barExplosionFrames[index++] = tmp[i][j];
			}
		}
		barExplosionAnimation = new Animation(0.025f, barExplosionFrames);
		barExplosionStateTime = 0f;

		// Bomb Explosion
		bombExplosionSheet = new Texture(
				Gdx.files.internal("images/bomb_explotion_animation.png"));
		TextureRegion[][] tmp2 = TextureRegion.split(bombExplosionSheet,
				bombExplosionSheet.getWidth() / BOMB_EXPLOSION_FRAME_COLS,
				bombExplosionSheet.getHeight() / BOMB_EXPLOSION_FRAME_ROWS);
		bombExplosionFrames = new TextureRegion[BOMB_EXPLOSION_FRAME_COLS
				* BOMB_EXPLOSION_FRAME_ROWS];
		index = 0;
		for (int i = 0; i < BOMB_EXPLOSION_FRAME_ROWS; i++) {
			for (int j = 0; j < BOMB_EXPLOSION_FRAME_COLS; j++) {
				bombExplosionFrames[index++] = tmp2[i][j];
			}
		}
		bombExplosionAnimation = new Animation(0.025f, bombExplosionFrames);

		// Main menu
		stage = new Stage(SCREEN_WIDTH, SCREEN_HEIGHT, true);
		stage.setCamera(camera);
		stage.setViewport(SCREEN_WIDTH, SCREEN_HEIGHT, true);

		skin = new Skin(Gdx.files.internal("ui/defaultskin.json"));

		// Main menu
		menuScrollLayer = new ScrollLayer(SCREEN_WIDTH, SCREEN_HEIGHT,
				SCREEN_WIDTH);

		stage.addActor(menuScrollLayer);

		menuTable = new Table();
		menuTable.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		int buttonWidth = SCREEN_WIDTH * 2 / 4;
		int buttonHeight = SCREEN_HEIGHT / 10;
		int paddingTop = 20;

		Image gameTitle = new Image(gameTitleImg);
		menuTable.add(gameTitle).width(SCREEN_WIDTH * 5 / 6)
				.height(SCREEN_HEIGHT / 6);

		menuTable.row();

		btnStartGame = new TextButton(MainActivity.getInstance().getResources()
				.getString(R.string.btnStartGame), skin);
		menuTable.add(btnStartGame).width(buttonWidth).height(buttonHeight)
				.padTop(30);

		menuTable.row();

		btnHighscores = new TextButton(MainActivity.getInstance()
				.getResources().getString(R.string.btnHighscores), skin);
		menuTable.add(btnHighscores).width(buttonWidth).height(buttonHeight)
				.padTop(paddingTop);

		menuTable.row();

		btnOptions = new TextButton(MainActivity.getInstance().getResources()
				.getString(R.string.btnOptions), skin);
		menuTable.add(btnOptions).width(buttonWidth).height(buttonHeight)
				.padTop(paddingTop);

		menuTable.row();

		btnQuit = new TextButton(MainActivity.getInstance().getResources()
				.getString(R.string.btnQuit), skin);
		menuTable.add(btnQuit).width(buttonWidth).height(buttonHeight)
				.padTop(paddingTop);

		menuTable.row();
		menuScrollLayer.addActor(menuTable);

		btnStartGame.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (isSoundOn) {
					click_button_sound.play();
				}

				// Check level complete status
				checkLevelCompleteStatus();

				stage.clear();
				stage.addActor(levelScrollLayer);
				drawType = DRAW_TYPE_LEVEL_SELECTION;
			}

		});

		btnQuit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (isSoundOn)
					click_button_sound.play();
				MainActivity.getInstance().finish();
			}

		});

		btnOptions.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				slbSound.setSelection(isSoundOn ? slbItems[0] : slbItems[1]);
				slbVibration.setSelection(isVibrationOn ? slbItems[0]
						: slbItems[1]);
				if (isSoundOn)
					click_button_sound.play();

				stage.clear();
				stage.addActor(optionScrollLayer);
				drawType = DRAW_TYPE_OPTION;
			}

		});

		btnHighscores.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isSoundOn)
					click_button_sound.play();
				stage.clear();
				stage.addActor(highscoreScrollLayer);
				drawType = DRAW_TYPE_HIGHSCORE;

				btnBack.setVisible(false);
				highscoreTitle.setVisible(false);
				scr1.setVisible(false);
				scr2.setVisible(false);
				scr3.setVisible(false);
				scr4.setVisible(false);
				scr5.setVisible(false);
				name1.setVisible(false);
				name2.setVisible(false);
				name3.setVisible(false);
				name4.setVisible(false);
				name5.setVisible(false);

				getHighscores();
			}

		});

		// Highscore menu
		highscoreScrollLayer = new ScrollLayer(SCREEN_WIDTH, SCREEN_HEIGHT,
				SCREEN_WIDTH);

		highscoreTable = new Table();
		highscoreTable.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		btnBack = new Image(menuBackImg);

		highScoreSkin = new Skin(Gdx.files.internal("ui/highscoreskin.json"));

		scr1 = new Label("", highScoreSkin);
		scr2 = new Label("", highScoreSkin);
		scr3 = new Label("", highScoreSkin);
		scr4 = new Label("", highScoreSkin);
		scr5 = new Label("", highScoreSkin);
		name1 = new Label("", highScoreSkin);
		name2 = new Label("", highScoreSkin);
		name3 = new Label("", highScoreSkin);
		name4 = new Label("", highScoreSkin);
		name5 = new Label("", highScoreSkin);

		highscoreTitle = new Label("High Scores", highScoreSkin);

		highscoreTable.add(highscoreTitle);
		highscoreTable.row();
		highscoreTable.add(scr1).padTop(paddingTop);
		highscoreTable.add(name1);
		highscoreTable.row();
		highscoreTable.add(scr2);
		highscoreTable.add(name2);
		highscoreTable.row();
		highscoreTable.add(scr3);
		highscoreTable.add(name3);
		highscoreTable.row();
		highscoreTable.add(scr4);
		highscoreTable.add(name4);
		highscoreTable.row();
		highscoreTable.add(scr5);
		highscoreTable.add(name5);
		highscoreTable.row();
		highscoreTable.add(btnBack).width(SCREEN_WIDTH * 1 / 4)
				.height(SCREEN_HEIGHT / 9).padTop(paddingTop).colspan(2);

		highscoreScrollLayer.addActor(highscoreTable);

		btnBack.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isSoundOn)
					click_button_sound.play();
				prepareToShowMainMenu();
			}

		});

		// Level selection menu
		levelScrollLayer = new ScrollLayer(SCREEN_WIDTH, SCREEN_HEIGHT,
				SCREEN_WIDTH);

		levelTable = new Table();
		levelTable.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		int levelButtonWidth = SCREEN_WIDTH * 2 / 4;
		int levelButtonHeight = SCREEN_HEIGHT / 8;

		Image gameTitle2 = new Image(gameTitleImg);
		levelTable.add(gameTitle2).width(SCREEN_WIDTH * 5 / 6)
				.height(SCREEN_HEIGHT / 6);

		levelTable.row();

		btnLevel1 = new TextButton("Level 1", skin);
		levelTable.add(btnLevel1).width(levelButtonWidth)
				.height(levelButtonHeight).padTop(paddingTop);

		levelTable.row();

		btnLevel2 = new TextButton("Level 2", skin);
		levelTable.add(btnLevel2).width(levelButtonWidth)
				.height(levelButtonHeight).padTop(paddingTop);

		levelTable.row();

		btnLevel3 = new TextButton("Level 3", skin);
		levelTable.add(btnLevel3).width(levelButtonWidth)
				.height(levelButtonHeight).padTop(paddingTop);

		levelTable.row();

		btnLevelBack = new TextButton("Back", skin);
		levelTable.add(btnLevelBack).width(levelButtonWidth)
				.height(levelButtonHeight).padTop(paddingTop);

		levelTable.row();

		levelScrollLayer.addActor(levelTable);

		btnLevel1.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!btnLevel1.isDisabled()) {
					if (isSoundOn)
						click_button_sound.play();
					currentLevel = 1;
					levelStartScore = 0;

					// Initialize gesture detector
					gestureDetector();

					completeLevel = false;
					isGameOver = false;
					gamePause = false;

					restartLevel();

					drawType = DRAW_TYPE_GAME;
				}
			}

		});

		btnLevel2.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!btnLevel2.isDisabled()) {
					currentLevel = 2;
					levelStartScore = 0;

					if (isSoundOn)
						click_button_sound.play();

					// Initialize gesture detector
					gestureDetector();

					completeLevel = false;
					isGameOver = false;
					gamePause = false;

					restartLevel();

					drawType = DRAW_TYPE_GAME;
				}
			}

		});

		btnLevel3.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!btnLevel3.isDisabled()) {
					currentLevel = 3;
					levelStartScore = 0;

					if (isSoundOn)
						click_button_sound.play();

					// Initialize gesture detector
					gestureDetector();

					completeLevel = false;
					isGameOver = false;
					gamePause = false;

					restartLevel();

					drawType = DRAW_TYPE_GAME;
				}
			}

		});

		btnLevelBack.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isSoundOn)
					click_button_sound.play();
				prepareToShowMainMenu();
			}

		});

		// Option menu
		optionScrollLayer = new ScrollLayer(SCREEN_WIDTH, SCREEN_HEIGHT,
				SCREEN_WIDTH);

		optionTable = new Table();
		optionTable.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		soundOn = new Label("Sound", highScoreSkin);
		// chkSound = new CheckBox("", highScoreSkin);
		slbSound = new SelectBox(slbItems, skin);
		optionTable.add(soundOn);
		optionTable.add(slbSound);
		optionTable.row();

		vibrationOn = new Label("Vibration", highScoreSkin);
		slbVibration = new SelectBox(slbItems, skin);
		optionTable.add(vibrationOn);
		optionTable.add(slbVibration);
		optionTable.row();

		btnSaveOption = new TextButton("Save", highScoreSkin);
		btnCancelOption = new TextButton("Cancel", highScoreSkin);
		optionTable.add(btnSaveOption).padTop(35).width(SCREEN_WIDTH / 4)
				.height(SCREEN_HEIGHT / 9);
		optionTable.add(btnCancelOption).padTop(35).width(SCREEN_WIDTH / 4)
				.height(SCREEN_HEIGHT / 9);

		optionScrollLayer.addActor(optionTable);

		btnSaveOption.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				isSoundOn = slbSound.getSelectionIndex() == 0 ? true : false;
				isVibrationOn = slbVibration.getSelectionIndex() == 0 ? true
						: false;

				isSoundPlayed = false;

				if (isSoundOn)
					click_button_sound.play();
				prepareToShowMainMenu();
			}

		});

		btnCancelOption.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isSoundOn)
					click_button_sound.play();
				prepareToShowMainMenu();
			}

		});

		// play background music
		bg_start_music.setLooping(true);
		if (isSoundOn) {
			bg_boss_music.stop();
			bg_start_music.play();
		}

		// Hide splash screen
		MainActivity.getInstance().hideSplashScreenDialog();
	}

	@Override
	public void render() {

		// Check sound state
		if (!isSoundPlayed) {
			isSoundPlayed = true;
			setMusicState();
		}

		// Clear the screen
		Gdx.gl.glClearColor(1, 1, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Tell the camera to update its matrices.
		camera.update();

		// Tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		if (isConnecting) {
			MainActivity.getInstance().showConnectingDialog();
		} else {
			MainActivity.getInstance().hideConnectingDialog();
		}

		switch (MainActivity.getInstance().getRequestGameCoreAction()) {

		case MainActivity.REQUEST_ACTION_HIGH_SCORE:
			MainActivity.getInstance().setRequestGameCoreAction(
					MainActivity.REQUEST_NO_ACTION);
			getHighscores();
			break;
		case MainActivity.REQUEST_ACTION_HIGH_SCORE_NO_INTERNET:
			MainActivity.getInstance().setRequestGameCoreAction(
					MainActivity.REQUEST_NO_ACTION);

			prepareToShowMainMenu();
			break;
		}

		switch (drawType) {
		case DRAW_TYPE_MAIN_MENU:
			drawMainMenu();
			break;
		case DRAW_TYPE_GAME:
			drawGameCore();
			break;
		case DRAW_TYPE_HIGHSCORE:
			drawHighscore();
			break;
		case DRAW_TYPE_LEVEL_SELECTION:
			drawLevelSelection();
			break;
		case DRAW_TYPE_OPTION:
			drawOptionMenu();
			break;
		default:
			break;
		}
	}

	private void drawMainMenu() {

		Gdx.input.setInputProcessor(stage);
		this.stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void drawOptionMenu() {

		Gdx.input.setInputProcessor(stage);
		this.stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void drawHighscore() {

		Gdx.input.setInputProcessor(stage);
		this.stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (isLoading) {
			MainActivity.getInstance().showLoadingDialog();
		} else {
			MainActivity.getInstance().hideLoadingDialog();
		}
	}

	private void drawLevelSelection() {

		Gdx.input.setInputProcessor(stage);
		this.stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void drawGameCore() {

		// Check clicked button in pause dialog
		if (gamePause) {
			int clickedButton = MainActivity.getInstance().getClickedButton();
			if (clickedButton == MainActivity.BTN_RESUME) {
				gamePause = false;
			} else if (clickedButton == MainActivity.BTN_RESTART) {
				if (completeLevel)
					scoreTotal = levelStartScore;

				restartLevel();
				gamePause = false;
			} else if (clickedButton == MainActivity.BTN_EXIT) {

				prepareToShowMainMenu();
			}
		}

		// check duration effect of item of the bar
		checkDurationEffectOfItem();

		// Begin a new batch
		batch.begin();
		batch.draw(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0,
				480, 800, false, false);
		// batch.draw(monster.getImage(), monster.x, monster.y);
		batch.draw(getImage(ball.getImageId()), ball.x, ball.y);
		batch.draw(bottomPanelImage, 0, 0, SCREEN_WIDTH,
				bottomPanelImage.getHeight(), 0, 0,
				bottomPanelImage.getWidth(), bottomPanelImage.getHeight(),
				false, false);
		batch.draw(pauseImage, pauseButton.x, pauseButton.y);
		drawAcquireditems();

		// Draw bricks
		for (Brick brick : bricks) {
			batch.draw(getImage(brick.getImageId()), brick.x, brick.y);
		}

		// draw dropped item (box item)
		for (Item item : itemDroppeds) {
			batch.draw(getImage(item.getImageId()), item.x, item.y);
		}

		// draw bullet if the gun item is activated
		if (bullets.size() > 0) {
			for (Rectangle bullet : bullets) {
				batch.draw(bulletImage, bullet.x, bullet.y);
			}
		}

		// draw Score
		font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		font.draw(batch, scoreLabel, 35, 780);
		font.draw(batch, String.valueOf(scoreTotal), 130, 780);
		// font.draw(batch, playerLabel, 25, 780);
		// font.draw(batch, playerName, 125, 780);

		// draw Life Heart Score
		drawLifeImage();

		// Draw bomb
		for (Bomb bomb : bombs) {
			batch.draw(getImage(bomb.getImageId()), bomb.x, bomb.y);
		}

		if (barExplosion) {
			bombs.clear();
			barExplosion();
			batch.draw(getImage(boss.getImageId()), boss.x, boss.y);

			batch.end();
			return;
		} else {
			if (!isGameOver) {
				batch.draw(getImage(bar.getImageId()), bar.x, bar.y
						- (bar.height / 2));
			}
			batch.end();
		}

		if ((completeLevel || isGameOver) && !gamePause && !showingMessage) {

			batch.begin();
			batch.draw(scorePanelImage, scorePanel.x, scorePanel.y,
					SCREEN_WIDTH, SCREEN_HEIGHT, 0, 0, 480, 800, false, false);
			batch.end();

			if (scorePanel.x > 0) {
				scorePanel.x -= SCORE_PANEL_MOVE_SPEED
						* Gdx.graphics.getDeltaTime();
			} else {
				scorePanel.x = 0;

				if (completeLevel) {

					// draw complete level score
					drawScoreAfterCompleteLeve();

				} else if (isGameOver) {

					// draw gameover score
					drawScoreAfterGameOver();
				}

				if (confirmSubmitScore) {
					confirmSubmitScore = false;

					MainActivity.getInstance().setScoreToSubmit(scoreTotal);
					MainActivity.getInstance().showPlayerNameDialog();
				}

			}

		} else if (showingMessage) {

			float delayTime = TimeUtils.nanoTime() - showMessageTimeCount;

			for (int i = 0; i < SHOW_MESSAGE_TIME; i++) {
				delayTime -= ONE_SECOND;
			}

			if (delayTime > 0) {
				showingMessage = false;
				MainActivity.getInstance().hideImageMessageDialog();
			} else {
				if (isGameOver) {

					MainActivity.getInstance().showImageMessageDialog(
							MainActivity.MESSAGE_GAME_OVER);
				} else if (completeLevel) {
					MainActivity.getInstance().showImageMessageDialog(
							MainActivity.MESSAGE_LEVEL_COMPLETE);
				} else if (currentLevel == 1) {
					MainActivity.getInstance().showImageMessageDialog(
							MainActivity.MESSAGE_LEVEL_1);
				} else if (currentLevel == 2) {
					MainActivity.getInstance().showImageMessageDialog(
							MainActivity.MESSAGE_LEVEL_2);
				} else if (currentLevel == 3) {
					MainActivity.getInstance().showImageMessageDialog(
							MainActivity.MESSAGE_LEVEL_3);
				}
			}
		} else if (!gamePause) {

			alreadyChangedBallDirection = false;

			// Update Batch
			batch.begin();

			// Draw boss
			if (spawnBoss) {
				batch.draw(getImage(boss.getImageId()), boss.x, boss.y);

				// Reset boss image (After boss gets hit for 1 sec)
				if (bossGetHitTime != 0
						&& (TimeUtils.nanoTime() - bossGetHitTime > ONE_SECOND / 4)) {
					boss.resetImage();
				}

				// Bomb explosion
				for (BombExplosion be : bombExplosions) {
					bombExplosion(be);
				}
				// Remove complete explosions
				for (int i = 0; i < bombExplosions.size(); i++) {
					if (bombExplosions.get(i).getExplosionCount() >= BOMB_EXPLOSION_FRAME_COLS
							* BOMB_EXPLOSION_FRAME_ROWS) {
						bombExplosions.remove(i);
					}
				}

				// Bomb movement
				bombMovement();

				float delayTime = TimeUtils.nanoTime() - lastBossStunTime;
				for (int i = 0; i < BOSS_STUN_TIME; i++) {
					delayTime -= ONE_SECOND;
				}
				if (delayTime > 0) {
					// Change boss movement direction
					delayTime = TimeUtils.nanoTime()
							- lastBossChangeDirectionTime;
					for (int i = 0; i < BOSS_CHANGE_DIRECTION_DELAY; i++) {
						delayTime -= ONE_SECOND;
					}
					if (delayTime > 0) {
						changeBossMovingDirection();
						lastBossChangeDirectionTime = TimeUtils.nanoTime();
					}
					bossMovement();

					delayTime = TimeUtils.nanoTime() - lastDropTime;
					for (int i = 0; i < dropDelay; i++) {
						delayTime -= ONE_SECOND;
					}
					if (delayTime > 0) {
						createBomb();
					}
				}
			}
			batch.end();

			// item Movement
			droppedItemMovement();

			bulletMovement();

			// create the bullet

			if (bar.isBullet()) {

				float delayTime = TimeUtils.nanoTime() - bullet_last_drop;

				for (int i = 0; i < BULLET_DELAY; i++) {

					delayTime -= ONE_SECOND;

				}
				if (delayTime > 0) {
					if (isSoundOn)
						laser_shoot_sound.play();
					createBullet();
				}

			}

			// Key input listener
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				bar.x -= BAR_SPEED * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				bar.x += BAR_SPEED * Gdx.graphics.getDeltaTime();

			// Move bar to touch position
			if (checkBarPosition
					&& (touchPosition.y < SCREEN_HEIGHT - TOP_PANEL_HEIGHT)
					&& !barExplosion) {

				float movementLength = BAR_SPEED * Gdx.graphics.getDeltaTime();
				if (touchPosition.x <= bar.x) {
					bar.x -= movementLength;
				} else {
					if (bar.x + movementLength > touchPosition.x - bar.width
							/ 2) {
						bar.x = touchPosition.x - bar.width / 2;
					} else {
						bar.x += movementLength;
					}
				}

				// Make sure the bar stay within the screen
				if (bar.x < 0) {
					bar.x = 0;
					checkBarPosition = false;
				}
				if (bar.x + bar.width > SCREEN_WIDTH) {
					bar.x = SCREEN_WIDTH - bar.width;
					checkBarPosition = false;
				}

				// When ball is not bouncing, move its position to middle of
				// paddle
				if (ball.getStatus() == BALL_STOP) {
					moveBallToMiddleOfBar();
				}
			}

			// Operate ball movement
			if (ball.getStatus() == BALL_BOUNCING) {
				ballMovement();
			}
		}
	}

	public void createBrickWallLevel1() {

		int startXForEven = BRICK_BLOCK_LEFT_RIGHT_PADDING;
		int startY = SCREEN_HEIGHT - TOP_PANEL_HEIGHT - BRICK_BLOCK_TOP_PADDING;
		int durability, type;
		bricks.clear();

		int startXForOdd = 145;

		for (int i = 1; i < 5; i++) {

			for (int j = 1; j < 5; j++) {

				// Random durability
				durability = random.nextInt(MAXIMUM_BRICK_DURABILITY) + 1;

				Brick brick = null;
				switch (durability) {
				case 1:
					brick = new Brick(IMG_BRICK_DURABILITY_1, 0, durability);
					break;
				case 2:
					brick = new Brick(IMG_BRICK_DURABILITY_2, 0, durability);
					break;
				case 3:
					brick = new Brick(IMG_BRICK_DURABILITY_3, 0, durability);
					break;
				}

				if (i % 2 == 0) {
					brick.x = startXForEven;
					brick.y = startY;
					startXForEven += 80;

				} else {
					brick.x = startXForOdd;
					brick.y = startY;
					startXForOdd += 80;
				}

				brick.width = BRICK_WIDTH;
				brick.height = BRICK_HEIGHT;
				bricks.add(brick);

			}
			startXForEven = BRICK_BLOCK_LEFT_RIGHT_PADDING;
			startXForOdd = 145;
			startY -= 30;

		}

	}

	private void createBrickWallLevel2() {

		int startX = BRICK_BLOCK_LEFT_RIGHT_PADDING;
		int startY = SCREEN_HEIGHT - TOP_PANEL_HEIGHT - BRICK_BLOCK_TOP_PADDING;
		int durability, type;

		bricks.clear();

		for (int j = 0; j < 8; j++) {

			for (int i = 0; i < 4; i++) {

				// Random type
				// type = random.nextInt(MAXIMUM_BRICK_TYPES) + 1;

				// Random durability
				durability = random.nextInt(MAXIMUM_BRICK_DURABILITY) + 1;

				Brick brick = null;
				switch (durability) {
				case 1:
					brick = new Brick(IMG_BRICK_DURABILITY_1, 0, durability);
					break;
				case 2:
					brick = new Brick(IMG_BRICK_DURABILITY_2, 0, durability);
					break;
				case 3:
					brick = new Brick(IMG_BRICK_DURABILITY_3, 0, durability);
					break;
				}

				brick.x = startX;
				brick.y = startY;
				brick.width = BRICK_WIDTH;
				brick.height = BRICK_HEIGHT;
				bricks.add(brick);

				switch (i) {
				case 0:
					startX += brick.width + BRICK_HORIZONTAL_SPACE;
					break;
				case 1:
					startX += (brick.width + BRICK_HORIZONTAL_SPACE) * 2;
					break;
				case 2:
					startX += brick.width + BRICK_HORIZONTAL_SPACE;
					break;
				}

			}
			startX = BRICK_BLOCK_LEFT_RIGHT_PADDING;

			if (j % 2 == 0)
				startY -= (BRICK_HEIGHT + BRICK_VERTICAL_SPACE);
			else
				startY -= (BRICK_HEIGHT + BRICK_VERTICAL_SPACE) * 2;
		}
	}

	private void createBrickWallLevel3() {

		int startX = BRICK_BLOCK_LEFT_RIGHT_PADDING;
		int startY = SCREEN_HEIGHT - TOP_PANEL_HEIGHT - BRICK_BLOCK_TOP_PADDING;
		int durability, type;

		bricks.clear();

		for (int x = 0; x < 3; x++) {

			for (int j = 0; j < 3; j++) {

				if (j % 2 == 0) {

					for (int i = 0; i < 5; i++) {

						// Random type
						// type = random.nextInt(MAXIMUM_BRICK_TYPES) + 1;

						// Random durability
						durability = random.nextInt(MAXIMUM_BRICK_DURABILITY) + 1;

						Brick brick = null;
						switch (durability) {
						case 1:
							brick = new Brick(IMG_BRICK_DURABILITY_1, 0,
									durability);
							break;
						case 2:
							brick = new Brick(IMG_BRICK_DURABILITY_2, 0,
									durability);
							break;
						case 3:
							brick = new Brick(IMG_BRICK_DURABILITY_3, 0,
									durability);
							break;
						}

						brick.x = startX;
						brick.y = startY;
						brick.width = BRICK_WIDTH;
						brick.height = BRICK_HEIGHT;
						bricks.add(brick);

						startX += brick.width + BRICK_HORIZONTAL_SPACE;
					}
				} else {
					for (int i = 0; i < 2; i++) {

						// Random type
						// type = random.nextInt(MAXIMUM_BRICK_TYPES) + 1;

						// Random durability
						durability = random.nextInt(MAXIMUM_BRICK_DURABILITY) + 1;

						Brick brick = null;
						switch (durability) {
						case 1:
							brick = new Brick(IMG_BRICK_DURABILITY_1, 0,
									durability);
							break;
						case 2:
							brick = new Brick(IMG_BRICK_DURABILITY_2, 0,
									durability);
							break;
						case 3:
							brick = new Brick(IMG_BRICK_DURABILITY_3, 0,
									durability);
							break;
						}

						brick.x = startX;
						brick.y = startY;
						brick.width = BRICK_WIDTH;
						brick.height = BRICK_HEIGHT;
						bricks.add(brick);

						startX += (brick.width + BRICK_HORIZONTAL_SPACE) * 4;
					}
				}

				startX = BRICK_BLOCK_LEFT_RIGHT_PADDING;
				startY -= (BRICK_HEIGHT + BRICK_VERTICAL_SPACE);
			}

			// asdasdas
		}
	}

	private void barExplosion() {
		if (barExplosionCount < BAR_EXPLOSION_FRAME_COLS
				* BAR_EXPLOSION_FRAME_ROWS) {

			barExplosionStateTime += Gdx.graphics.getDeltaTime();
			currentFrame = barExplosionAnimation.getKeyFrame(
					barExplosionStateTime, true);

			batch.draw(currentFrame, bar.x - (bar.width / 2), bar.y - 20);

			barExplosionCount++;
		} else {
			barExplosion = false;
		}
	}

	private void bombExplosion(BombExplosion be) {
		if (be.getExplosionCount() < BOMB_EXPLOSION_FRAME_COLS
				* BOMB_EXPLOSION_FRAME_ROWS) {

			be.increaseBombExplosionStateTime();
			currentFrame = bombExplosionAnimation.getKeyFrame(
					be.getBombExplosionStateTime(), true);

			batch.draw(currentFrame, be.getX(), be.getY());

			be.increaseExplosionCount();
		}
	}

	private void changeBossMovingDirection() {
		boss.setDirection(random.nextInt(NUM_OF_DIRECTIONS));
	}

	private void bossMovement() {

		// Change boss movement direction
		boss.movement();

		// Check boss movement area
		boss.checkMovementArea();

		// Ball hits boss
		if (ball.overlaps(boss)) {

			if (isSoundOn)
				boss_hit_sound.play();

			// calculate score when ball hit boss
			calculateScoreWhenBallHitBoss();
			ballHitsBoss();

			if (currentLevel == 2 || currentLevel == 3) {
				createSpecialBomb();
			}
		}
	}

	/*
	 * Change ball position, direction, and check ball overlap events
	 */
	private void ballMovement() {

		// Change position of ball
		ball.movement();

		/*
		 * Change direction on hitting each side of the screen
		 */
		changeBallDirectionWhenHitScreenSides();

		// Ball hits the bar
		if (ball.overlaps(bar))

			ballHitsBar();

		// Ball hits brick
		if (!spawnBoss)
			actionCollisionOfBallAndBrick();
	}

	private void ballHitsBoss() {

		int bossLife = boss.getHit();
		if (bossLife > 0) {
			bossGetHitTime = TimeUtils.nanoTime();
			changeBallDirectionOnHittingObstacle(boss);

			// if the boss life is 7 or 4, generate the item
			if (boss.getLife() == 7 || boss.getLife() == 4) {

				// create item
				createItemFromBoss();
			}
		} else {
			completeLevel();
		}

		lastBossStunTime = TimeUtils.nanoTime();
	}

	private void ballHitsBar() {

		if (isVibrationOn) {
			MainActivity.getInstance().getVibrator()
					.vibrate(MainActivity.BOMB_VIBRATION_TIME);
		}

		ball.y = bar.y + bar.height;
		if (isSoundOn)
			ball_bouncing_sound.play();

		if (ball.getDirection() == DIRECTION_DOWN) {
			// if (ball.x >= (bar.width * 2 / 5 + bar.x)
			// && ball.x <= (bar.width * 3 / 5 + bar.x))
			// ball.setDirection(DIRECTION_UP);
			if (ball.x <= (bar.width / 2 + bar.x))
				ball.setDirection(DIRECTION_LEFT_UP);
			else
				ball.setDirection(DIRECTION_RIGHT_UP);

		} else {
			if (ball.getDirection() == DIRECTION_RIGHT_DOWN) {
				if (ball.x <= (bar.width / 2 + bar.x))
					ball.setDirection(DIRECTION_LEFT_UP);
				else
					ball.setDirection(DIRECTION_RIGHT_UP);
			} else if (ball.getDirection() == DIRECTION_LEFT_DOWN) {
				if (ball.x > (bar.width / 2 + bar.x))
					ball.setDirection(DIRECTION_RIGHT_UP);
				else
					ball.setDirection(DIRECTION_LEFT_UP);
			}
		}
		scoreBonusTime = 1;
	}

	private void changeBallDirectionWhenHitScreenSides() {
		boolean checkGameOver = ball.changeDirectionWhenHitScreenSides(bar);

		if (checkGameOver) {
			// gameover or lost life
			checkGameOverOrContinous();
		}
	}

	private void getTouchPosition() {
		touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(touchPosition);
	}

	private void barMovementWithLongPress() {

		if (checkBarPosition)
			bar.x = touchPosition.x - bar.width / 2;

		if (touchPosition.x >= 0
				&& (touchPosition.x + bar.width) <= SCREEN_WIDTH) {
			checkBarPosition = true;
		}
	}

	private void barMovementWithQuickTouch() {

		if (touchPosition.x >= 0
				&& (touchPosition.x + bar.width) <= SCREEN_WIDTH) {
			checkBarPosition = true;
		}
	}

	public void calculateScoreLevel1() {

		// count Score
		Log.i("Test:", "Bonus count:" + String.valueOf(scoreBonusTime));
		scoreTotal += BRICK_POINT * scoreBonusTime;
		scoreBonusTime++;
		Log.i("Test:", "Bonus score:" + String.valueOf(scoreTotal));
		Log.i("Test:", "Bonus after count:" + String.valueOf(scoreBonusTime));

	}

	public void calculateScoreWhenBulletHitBrick() {

		scoreTotal += BRICK_POINT;

	}

	public void calculateScoreWhenBulletHitBoss() {

		scoreTotal += BOSS_POINT;
	}

	public void calculateScoreWhenBallHitBoss() {

		scoreTotal += BOSS_POINT * scoreBonusTime;
		scoreBonusTime++;

	}

	/*
	 * Action when the ball hit the brick +animation: the brick will slash and
	 * being destroyed
	 */
	public void actionCollisionOfBallAndBrick() {

		Iterator<Brick> iter = bricks.iterator();

		while (iter.hasNext()) {
			Brick brickTemp = iter.next();

			if (brickTemp.overlaps(ball)) {
				if (isSoundOn)
					brick_break_sound.play();
				// calculate Score
				calculateScoreLevel1();

				// Change ball direction
				if (!alreadyChangedBallDirection) {
					changeBallDirectionOnHittingObstacle(brickTemp);
				}

				// Check brick durability
				brickTemp.decreaseDurability();

				if (brickTemp.getDurability() == 0) {

					// create Item image and dropped it
					if (brickTemp.getItem() != null) {
						Log.i("Item", "index: " + iter.toString() + ","
								+ brickTemp.getItem().getName());
						createDroppedItem(brickTemp, brickTemp.getItem());

						// add item into the itemlist of Bar
					}

					// Delete the overlapped brick
					iter.remove();
				}

			}
		}

		if (bricks.isEmpty()) {
			spawnBoss = true;
			lastBossChangeDirectionTime = TimeUtils.nanoTime();
			lastBossStunTime = TimeUtils.nanoTime();
			lastDropTime = TimeUtils.nanoTime();

			// bg_start_music.stop();
			bg_boss_music.setLooping(true);
			if (isSoundOn) {
				bg_start_music.stop();
				bg_boss_music.play();
			}

		}

	}

	private void changeBallDirectionOnHittingObstacle(Rectangle obstacle) {

		float movementLength = ball.getSpeed() * Gdx.graphics.getDeltaTime();

		/*
		 * Change ball direction
		 */
		if (ball.getDirection() == DIRECTION_RIGHT_DOWN) {

			// Ball hits left side of the brick
			if ((obstacle.x + obstacle.width - ball.x - ball.width > ball.width * 2 / 3)
					&& (obstacle.y + obstacle.height - ball.y > movementLength)) {
				// ball.x = brickTemp.x - ball.width;
				ball.setDirection(DIRECTION_LEFT_DOWN);
			}
			// Ball hits top side of the brick
			else if (ball.x + ball.width - obstacle.x > movementLength) {
				// ball.y = brickTemp.y + brickTemp.height;
				ball.setDirection(DIRECTION_RIGHT_UP);
			}

		} else if (ball.getDirection() == DIRECTION_LEFT_DOWN) {

			// Ball hits right side of the brick
			if ((ball.x + ball.width - obstacle.x - obstacle.width > ball.width * 2 / 3)
					&& (obstacle.y + obstacle.height - ball.y > movementLength)) {
				// ball.x = brickTemp.x + brickTemp.width;
				ball.setDirection(DIRECTION_RIGHT_DOWN);
			}
			// Ball hits top side of the brick
			else {
				// ball.y = brickTemp.y + brickTemp.height;
				ball.setDirection(DIRECTION_LEFT_UP);
			}

		} else if (ball.getDirection() == DIRECTION_RIGHT_UP) {

			// Ball hits left side of the brick
			if ((obstacle.x + obstacle.width - ball.x - ball.width > ball.width * 2 / 3)
					&& (ball.y + ball.height - obstacle.y > movementLength)) {
				// ball.x = brickTemp.x + brickTemp.width;
				ball.setDirection(DIRECTION_LEFT_UP);
			}
			// Ball hits top side of the brick
			else {
				// ball.y = brickTemp.y + brickTemp.height;
				ball.setDirection(DIRECTION_RIGHT_DOWN);
			}

		} else if (ball.getDirection() == DIRECTION_LEFT_UP) {

			// Ball hits right side of the brick
			if ((ball.x + ball.width - obstacle.x - obstacle.width > ball.width * 2 / 3)
					&& (ball.y + ball.height - obstacle.y > movementLength)) {
				// ball.x = brickTemp.x + brickTemp.width;
				ball.setDirection(DIRECTION_RIGHT_UP);
			}
			// Ball hits top side of the brick
			else {
				// ball.y = brickTemp.y + brickTemp.height;
				ball.setDirection(DIRECTION_LEFT_DOWN);
			}

		} else if (ball.getDirection() == DIRECTION_UP) {
			ball.y = obstacle.y - ball.height;
			if (obstacle instanceof Boss) {
				int direction = random.nextInt();

				if (direction == 0) {
					ball.setDirection(DIRECTION_LEFT_DOWN);
				} else {
					ball.setDirection(DIRECTION_RIGHT_DOWN);
				}
			} else {
				ball.setDirection(DIRECTION_DOWN);
			}

		} else {
			ball.y = obstacle.y + obstacle.height;
			if (obstacle instanceof Boss) {
				int direction = random.nextInt();

				if (direction == 0) {
					ball.setDirection(DIRECTION_LEFT_UP);
				} else {
					ball.setDirection(DIRECTION_RIGHT_UP);
				}
			} else {
				ball.setDirection(DIRECTION_UP);
			}
		}

		alreadyChangedBallDirection = true;
	}

	private void gestureDetector() {
		class GestureDetectorListener implements GestureListener {

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				getTouchPosition();

				if (!gamePause) {
					if (touchPosition.y < SCREEN_HEIGHT - TOP_PANEL_HEIGHT
							&& !barExplosion) {
						barMovementWithQuickTouch();

						if (touchPosition.x >= pauseButton.x
								&& touchPosition.x <= (pauseButton.x + pauseButton.width)
								&& touchPosition.y >= pauseButton.y
								&& touchPosition.y <= (pauseButton.y + pauseButton.height)) {
							pauseGame();
						}
					}
				}

				return true;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				getTouchPosition();

				if (!gamePause) {
					if (touchPosition.y < SCREEN_HEIGHT - TOP_PANEL_HEIGHT
							&& !barExplosion) {
						if (ball.getStatus() == BALL_STOP) {
							ball.setStatus(BALL_BOUNCING);
						} else {
							barMovementWithQuickTouch();
						}
					}

					if (touchPosition.x >= pauseButton.x
							&& touchPosition.x <= (pauseButton.x + pauseButton.width)
							&& touchPosition.y >= pauseButton.y
							&& touchPosition.y <= (pauseButton.y + pauseButton.height)) {
						if (isSoundOn)
							click_button_sound.play();
						pauseGame();
					}
				}

				if (completeLevel || isGameOver) {

					// retry button action
					if (touchPosition.x >= retryLevelButton.x
							&& touchPosition.x <= (retryLevelButton.x + retryLevelButton.width)
							&& touchPosition.y >= retryLevelButton.y
							&& touchPosition.y <= (retryLevelButton.y + retryLevelButton.height)) {
						if (isSoundOn)
							click_button_sound.play();

						if (completeLevel)
							scoreTotal = levelStartScore;

						restartLevel();

					} else
					// exit button action
					if (touchPosition.x >= exitButton.x
							&& touchPosition.x <= (exitButton.x + exitButton.width)
							&& touchPosition.y >= exitButton.y
							&& touchPosition.y <= (exitButton.y + exitButton.height)) {
						if (isSoundOn)
							click_button_sound.play();
						prepareToShowMainMenu();

					}

					if (completeLevel) {
						// next level action
						if (touchPosition.x >= nextLevelButton.x
								&& touchPosition.x <= (nextLevelButton.x + nextLevelButton.width)
								&& touchPosition.y >= nextLevelButton.y
								&& touchPosition.y <= (nextLevelButton.y + nextLevelButton.height)) {

							if (currentLevel != 3) {
								// next level
								if (isSoundOn)
									click_button_sound.play();

								levelStartScore = scoreTotal;
								currentLevel++;
								restartLevel();
							}
						}
					}

					if (touchPosition.x >= submitScoreButton.x
							&& touchPosition.x <= (submitScoreButton.x + submitScoreButton.width)
							&& touchPosition.y >= submitScoreButton.y
							&& touchPosition.y <= (submitScoreButton.y + submitScoreButton.height)) {

						if (isSoundOn)
							click_button_sound.play();

						confirmSubmitScore = true;
					}

				}

				return true;
			}

			@Override
			public boolean longPress(float x, float y) {
				getTouchPosition();

				if (!gamePause) {
					if (touchPosition.y < SCREEN_HEIGHT - TOP_PANEL_HEIGHT
							&& !barExplosion) {
						barMovementWithQuickTouch();
					}

					if (touchPosition.x >= pauseButton.x
							&& touchPosition.x <= (pauseButton.x + pauseButton.width)
							&& touchPosition.y >= pauseButton.y
							&& touchPosition.y <= (pauseButton.y + pauseButton.height)) {
						if (isSoundOn)
							click_button_sound.play();
						pauseGame();
					}
				}

				if (completeLevel || isGameOver) {

					// retry button action
					if (touchPosition.x >= retryLevelButton.x
							&& touchPosition.x <= (retryLevelButton.x + retryLevelButton.width)
							&& touchPosition.y >= retryLevelButton.y
							&& touchPosition.y <= (retryLevelButton.y + retryLevelButton.height)) {
						if (isSoundOn)
							click_button_sound.play();

						if (completeLevel)
							scoreTotal = levelStartScore;

						restartLevel();

					} else
					// exit button action
					if (touchPosition.x >= exitButton.x
							&& touchPosition.x <= (exitButton.x + exitButton.width)
							&& touchPosition.y >= exitButton.y
							&& touchPosition.y <= (exitButton.y + exitButton.height)) {
						if (isSoundOn)
							click_button_sound.play();
						prepareToShowMainMenu();

					}

					if (completeLevel) {
						// next level action
						if (touchPosition.x >= nextLevelButton.x
								&& touchPosition.x <= (nextLevelButton.x + nextLevelButton.width)
								&& touchPosition.y >= nextLevelButton.y
								&& touchPosition.y <= (nextLevelButton.y + nextLevelButton.height)) {

							if (currentLevel != 3) {
								// next level
								if (isSoundOn)
									click_button_sound.play();

								levelStartScore = scoreTotal;
								currentLevel++;
								restartLevel();
							}
						}
					}

					if (touchPosition.x >= submitScoreButton.x
							&& touchPosition.x <= (submitScoreButton.x + submitScoreButton.width)
							&& touchPosition.y >= submitScoreButton.y
							&& touchPosition.y <= (submitScoreButton.y + submitScoreButton.height)) {

						if (isSoundOn)
							click_button_sound.play();

						confirmSubmitScore = true;
					}

				}

				return true;
			}

			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				return true;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				getTouchPosition();

				if (touchPosition.y < SCREEN_HEIGHT - TOP_PANEL_HEIGHT
						&& !barExplosion) {
					barMovementWithLongPress();
				}

				return true;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				return true;
			}

			@Override
			public boolean pinch(Vector2 initialPointer1,
					Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				return true;
			}

		}

		GestureDetector gestureDetector = new GestureDetector(20, 0.5f, 2,
				0.15f, new GestureDetectorListener());
		Gdx.input.setInputProcessor(gestureDetector);
	}

	private void createBomb() {

		Bomb bomb_down = new Bomb(IMG_NORMAL_BOMB, BOMB_SPEED, DIRECTION_DOWN);

		bomb_down.x = boss.x + boss.width / 3;
		bomb_down.y = boss.y - 5;
		bomb_down.width = BOMB_WIDTH;
		bomb_down.height = BOMB_HEIGHT;

		Bomb bomb_up = new Bomb(IMG_NORMAL_BOMB, BOMB_SPEED, DIRECTION_UP);

		bomb_up.x = boss.x + boss.width / 3;
		bomb_up.y = boss.y - 5;
		bomb_up.width = BOMB_WIDTH;
		bomb_up.height = BOMB_HEIGHT;

		if (currentLevel == 3) {
			Bomb bomb_left = new Bomb(IMG_NORMAL_BOMB, BOMB_SPEED,
					DIRECTION_LEFT_DOWN);

			bomb_left.x = boss.x + boss.width / 3;
			bomb_left.y = boss.y - 5;
			bomb_left.width = BOMB_WIDTH;
			bomb_left.height = BOMB_HEIGHT;

			Bomb bomb_right = new Bomb(IMG_NORMAL_BOMB, BOMB_SPEED,
					DIRECTION_RIGHT_DOWN);

			bomb_right.x = boss.x + boss.width / 3;
			bomb_right.y = boss.y - 5;
			bomb_right.width = BOMB_WIDTH;
			bomb_right.height = BOMB_HEIGHT;

			bombs.add(bomb_left);
			bombs.add(bomb_right);
		}

		bombs.add(bomb_up);
		bombs.add(bomb_down);
		lastDropTime = TimeUtils.nanoTime();
	}

	private void createSpecialBomb() {

		Bomb leftBomb = new Bomb(IMG_SPECIAL_BOMB, BOMB_SPEED,
				DIRECTION_LEFT_UP);

		leftBomb.x = boss.x + boss.width / 3;
		leftBomb.y = boss.y - 5;
		leftBomb.width = BOMB_WIDTH;
		leftBomb.height = BOMB_HEIGHT;

		Bomb rightBomb = new Bomb(IMG_SPECIAL_BOMB, BOMB_SPEED,
				DIRECTION_RIGHT_UP);

		rightBomb.x = boss.x + boss.width / 3;
		rightBomb.y = boss.y - 5;
		rightBomb.width = BOMB_WIDTH;
		rightBomb.height = BOMB_HEIGHT;

		bombs.add(leftBomb);
		bombs.add(rightBomb);

		// if (currentLevel == 2 || currentLevel == 3) {
		// int bombSide = random.nextInt(2) + 1;
		//
		// if (bombSide == 1) {
		// bombs.add(leftBomb);
		// } else {
		// bombs.add(rightBomb);
		// }
		// } else {
		// bombs.add(leftBomb);
		// bombs.add(rightBomb);
		// }

	}

	private void checkGameOverOrContinous() {
		Log.i("gameover", "gameover");
		// if the life is greater than 0
		if (bar.getNoOfLife() > 0) {

			int temp = bar.getNoOfLife();

			// minus the life by one
			bar.setNoOfLife(--temp);
			clearAllItem();
			moveBallToMiddleOfBar();
			ball.setStatus(BALL_STOP);
			isGameOver = false;

		} else {
			// set the life is zero
			bar.setNoOfLife(0);
			ball.x = -100;
			ball.y = -100;
			ball.setStatus(BALL_STOP);
			clearAllItem();
			showMessageTimeCount = TimeUtils.nanoTime();
			showingMessage = true;
			isGameOver = true;

			// stop bg music, start the game over sound
			bg_boss_music.stop();
			bg_start_music.stop();
			if (isSoundOn)
				gameover_sound.play();

		}
	}

	/*
	 * draw the life of the player
	 */
	public void drawLifeImage() {
		int startX = 35;
		int startY = 720;

		for (int i = 0; i < 3; i++) {
			Rectangle lifeObject = new Rectangle();
			lifeObject.x = startX;
			lifeObject.y = startY;
			lifeObject.width = 32;
			lifeObject.height = 32;

			if (bar.getNoOfLife() == 3) {

				// draw the full life
				batch.draw(lifeFullImage, lifeObject.x, lifeObject.y);

				// if the life is 2
			} else if (bar.getNoOfLife() == 2) {

				if (i == 2) {

					// draw the died life
					batch.draw(lifeDieImage, lifeObject.x, lifeObject.y);
				} else {

					// draw the full life
					batch.draw(lifeFullImage, lifeObject.x, lifeObject.y);
				}

				// if the life is 1
			} else if (bar.getNoOfLife() == 1) {

				if (i >= 1) {
					// draw the died life
					batch.draw(lifeDieImage, lifeObject.x, lifeObject.y);
				} else {

					// draw the full life
					batch.draw(lifeFullImage, lifeObject.x, lifeObject.y);
				}

			} else if (bar.getNoOfLife() == 0) {
				batch.draw(lifeDieImage, lifeObject.x, lifeObject.y);
			}

			startX += 35;
		}

	}

	/*
	 * create the dropped item image when the brick is hit
	 */

	public void createDroppedItem(Brick brick, Item item) {

		Log.i("Item", "create Dropped Item");
		item.x = brick.x;
		item.y = brick.y;

		item.width = ITEM_WIDTH;
		item.height = ITEM_HEIGHT;

		itemDroppeds.add(item);

	}

	/*
	 * dropped item movement after the brick is hit
	 */
	public void droppedItemMovement() {

		Iterator<Item> iter = itemDroppeds.iterator();

		while (iter.hasNext()) {

			Item item = iter.next();
			item.y -= ITEM_SPEED * Gdx.graphics.getDeltaTime();

			if (item.y - BOTTOM_PANEL_HEIGHT <= 0) {

				iter.remove();

			} else {
				if (item.overlaps(bar)) {

					// active item effect
					// play sound
					if (isSoundOn)
						pick_item_sound.play();

					activeTypeOfBar(item);
					iter.remove();
					Log.i("Item",
							"staus bar: " + bar.getLong_short_normarl_type());
				}
			}

		}

	}

	public void activeTypeOfBar(Item item) {

		Log.i("Item", "do active");

		switch (item.getType()) {
		case ITEM_TYPE_LONG_BAR:

			// check existed item is true
			if (checkExistedItemOfBar(item.getType())) {

				// update the end duratime of the item
				item.calculateEndDuration(TimeUtils.nanoTime());

				bar.updateItem(item);

			} else {

				// check if there is short bar item
				if (checkExistedItemOfBar(ITEM_TYPE_SHORT_BAR)) {

					bar.removeItem(ITEM_TYPE_SHORT_BAR);

				}
				// add item into the list of the bar and do the effect

				Log.i("Item", "time before:" + TimeUtils.nanoTime());

				// calculate the end duration of item
				item.calculateEndDuration(TimeUtils.nanoTime());

				Log.i("Item", "time after:" + item.getEndDuration());
				bar.addItem(item);

				bar.setLong_short_normarl_type(LONG_TYPE);

			}

			bar.setImageId(getImageIdOfBarByType(bar
					.getLong_short_normarl_type()));
			bar.width = BAR_LONG_WIDTH;

			break;

		case ITEM_TYPE_SHORT_BAR:

			Log.i("Item", "do active short bar");

			// check existed item is true
			if (checkExistedItemOfBar(item.getType())) {

				// update the end duratime of the item

				item.calculateEndDuration(TimeUtils.nanoTime());
				Log.i("Item", "duplicate item out" + item.getEndDuration());
				bar.updateItem(item);

				for (Item it : bar.getItemList()) {
					if (it.getType() == item.getType()) {
						Log.i("Item", "duplicate item in" + it.getEndDuration());
					}
				}
			} else {

				// check if there is long bar item
				if (checkExistedItemOfBar(ITEM_TYPE_LONG_BAR)) {

					bar.removeItem(ITEM_TYPE_LONG_BAR);

				}
				// add item into the list of the bar and do the effect
				Log.i("Item", "add new item");
				Log.i("Item", "time before:" + TimeUtils.nanoTime());

				// calculate the end duration of item
				item.calculateEndDuration(TimeUtils.nanoTime());

				Log.i("Item", "time after:" + item.getEndDuration());
				bar.addItem(item);

				bar.setLong_short_normarl_type(SHORT_TYPE);

			}

			bar.setImageId(getImageIdOfBarByType(bar
					.getLong_short_normarl_type()));
			bar.width = BAR_SHORT_WIDTH;

			break;

		case ITEM_TYPE_LIFE:

			bar.increaseLifeByOne();

			break;

		case ITEM_TYPE_GUN_BAR:

			Log.i("Item", "do active gun bar");

			if (checkExistedItemOfBar(item.getType())) {

				// update the ended time
				item.calculateEndDuration(TimeUtils.nanoTime());
				bar.updateItem(item);

			} else {

				// add new item
				item.calculateEndDuration(TimeUtils.nanoTime());
				bar.addItem(item);
				bar.setBullet(true);

			}

			bar.setImageId(getImageIdOfBarByType(bar
					.getLong_short_normarl_type()));

			break;

		default:
			break;
		}

	}

	public boolean checkExistedItemOfBar(int itemType) {

		boolean check = false;

		for (Item it : bar.getItemList()) {

			if (it.getType() == itemType) {
				check = true;
			}

		}
		return check;
	}

	public int getImageIdOfBarByType(int type) {

		int barImageId = 0;

		if (bar.getLong_short_normarl_type() == SHORT_TYPE) {

			if (bar.isBullet()) {

				barImageId = IMG_ITEM_GUN_SHORT_BAR;

			} else {
				// short
				barImageId = IMG_SHORT_BAR;
			}

		} else if (bar.getLong_short_normarl_type() == NORMAL_TYPE) {

			if (bar.isBullet()) {
				Log.i("check", "gun_normal");
				barImageId = IMG_ITEM_GUN_NORMAL_BAR;

			} else {
				barImageId = IMG_NORMAL_BAR;
			}

		} else if (bar.getLong_short_normarl_type() == LONG_TYPE) {

			if (bar.isBullet()) {
				barImageId = IMG_ITEM_GUN_LONG_BAR;
			} else {
				barImageId = IMG_LONG_BAR;
			}
		}
		return barImageId;
	}

	/*
	 * generate the random number of Items in one round Max number of items: 7
	 * Min number of items: 3
	 */

	public int getGeneratedNumberOfItemInARound() {

		Random random = new Random();

		return random.nextInt(4) + 3;
	}

	// add item into the brick
	public boolean addItemIntoBrick(int index, Item item) {

		boolean check = false;

		if (bricks.get(index).getItem() == null) {
			bricks.get(index).setItem(item);
			check = true;

			Log.i("Item", "item added:" + bricks.get(index).getItem().getName());
		}

		return check;

	}

	// auto generator item
	public Item generateItem() {

		/*
		 * Item type is: + 1: life (heart) + 2: long paddle + 3: short paddle +
		 * 4: gun paddle
		 */

		Item temp = null;
		int type;
		Random random = new Random();

		type = random.nextInt(4) + 1;

		switch (type) {

		case 1:
			temp = new Item(ITEM_NAME_LIFE, ITEM_TYPE_LIFE, IMG_ITEM_LIFE,
					ITEM_DURATION);

			break;
		case 2:

			temp = new Item(ITEM_NAME_LONG_BAR, ITEM_TYPE_LONG_BAR,
					IMG_ITEM_RANDOM, ITEM_DURATION);
			break;
		case 3:
			temp = new Item(ITEM_NAME_SHORT_BAR, ITEM_TYPE_SHORT_BAR,
					IMG_ITEM_RANDOM, ITEM_DURATION);
			break;

		case 4:
			temp = new Item(ITEM_NAME_GUN_BAR, ITEM_TYPE_GUN_BAR,
					IMG_ITEM_RANDOM, ITEM_DURATION);
			break;
		}

		return temp;
	}

	// generate the item and add into the brick, this method will be
	// called in the OnCreate() method
	public void generateItemAndAddIntoBrick() {

		numberOfItems = getGeneratedNumberOfItemInARound();
		int count = 0;
		int index;
		boolean check;
		Random random = new Random();
		Log.i("Item", "no of Item:" + String.valueOf(numberOfItems));
		while (count < numberOfItems) {

			Item item = generateItem();
			index = random.nextInt(bricks.size() - 1);

			check = addItemIntoBrick(index, item);
			if (check) {

				count++;
			}

		}

	}

	/*
	 * draw in render
	 */

	public void checkDurationEffectOfItem() {

		if (bar.getItemList().size() > 0) {

			for (Item it : bar.getItemList()) {

				if (it.getEndDuration() < TimeUtils.nanoTime()) {

					Log.i("Item", "time up, remove item ");
					// deactiveEffect(it.getType());
					if (it.getType() == ITEM_TYPE_GUN_BAR) {
						bar.setBullet(false);
					}

					bar.removeItem(it);
					break;

				}
			}
		}
		// set active of the remained items
		if (bar.getItemList().size() > 0) {

			for (Item it : bar.getItemList()) {

				// continue active the
			}
		} else {

			// reset bar in normal
			bar.setLong_short_normarl_type(NORMAL_TYPE);
			bar.setImageId(getImageIdOfBarByType(bar
					.getLong_short_normarl_type()));
			bar.width = BAR_NORMAL_WIDTH;
		}

	}

	public void deactiveEffect(int itemType) {

		switch (itemType) {

		case ITEM_TYPE_GUN_BAR:

			bar.setBullet(false);

			break;

		default:

			break;

		}

	}

	// create a bullet when gun item is activated
	public void createBullet() {

		// Rectangle bomb = new Rectangle();
		//
		// bomb.x = monster.x + monster.width / 3;
		// bomb.y = monster.y - 5;
		// bomb.width = BOMB_WIDTH;
		// bomb.height = BOMB_HEIGHT;
		//
		// bombs.add(bomb);
		// lastDropTime = TimeUtils.nanoTime();

		Rectangle bullet = new Rectangle();

		bullet.x = bar.x + bar.width / 3;
		bullet.y = bar.y + 5;

		bullet.width = BULLET_WIDTH;
		bullet.height = BULLET_HEIGHT;

		bullets.add(bullet);
		bullet_last_drop = TimeUtils.nanoTime();

	}

	public void bulletMovement() {

		Iterator<Rectangle> iter = bullets.iterator();

		while (iter.hasNext()) {

			Rectangle bullet = iter.next();
			bullet.y += BULLET_SHOT_SPEED * Gdx.graphics.getDeltaTime();

			if (bullet.y + BULLET_HEIGHT >= SCREEN_HEIGHT - TOP_PANEL_HEIGHT) {
				iter.remove();
			} else {

				for (Brick brick : bricks) {

					if (brick.overlaps(bullet)) {
						if (isSoundOn)
							brick_break_sound.play();

						// calculate Score when bullet hit brick
						calculateScoreWhenBulletHitBrick();
						// delete the brick

						iter.remove();

						brick.decreaseDurability();

						if (brick.getDurability() == 0) {

							// create Item image and dropped it
							if (brick.getItem() != null) {
								Log.i("Item", "index: " + iter.toString() + ","
										+ brick.getItem().getName());
								createDroppedItem(brick, brick.getItem());

								// add item into the itemlist of Bar
							}
							bricks.remove(brick);
						}

						break;
					}
				}

				if (spawnBoss) {
					if (bullet.overlaps(boss)) {

						if (isSoundOn) {
							boss_hit_sound.play();
						}

						calculateScoreWhenBulletHitBoss();
						bulletHitBoss();
						iter.remove();
					}
				}
			}
		}

	}

	public void clearAllItem() {

		// reset bar image
		bar.setLong_short_normarl_type(NORMAL_TYPE);
		bar.setImageId(getImageIdOfBarByType(bar.getLong_short_normarl_type()));
		bar.width = BAR_NORMAL_WIDTH;
		bar.setBullet(false);
		// clear all items
		bar.removeAllItem();
		bullets.clear();

	}

	public void bulletHitBoss() {

		int bossLife = boss.getHit();
		if (bossLife > 0) {
			bossGetHitTime = TimeUtils.nanoTime();
		} else {
			completeLevel();
		}

	}

	public void createItemFromBoss() {

		Item item = generateItem();

		Log.i("Item", "create Dropped Item");
		item.x = boss.x;
		item.y = boss.y - 5;

		item.width = ITEM_WIDTH;
		item.height = ITEM_HEIGHT;

		itemDroppeds.add(item);

	}

	public void drawScoreAfterCompleteLeve() {
		int tempTotalScoreWithoutBonus = scoreTotal;
		SCORE_TITLE_STRING = "COMPLETE LEVEL";
		batch.begin();

		fontLarge.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		fontLarge.draw(batch, SCORE_TITLE_STRING, 125, 550);

		fontLarge.draw(batch, scoreLabel, 150, 500);
		// fun the score animation
		if (tempScore < tempTotalScoreWithoutBonus) {

			tempScore += 30;

		} else {
			tempScore = tempTotalScoreWithoutBonus;
			drawLifeComplete = true;
		}
		fontLarge.draw(batch, String.valueOf(tempScore), 300, 500);

		// draw the Life score
		if (drawLifeComplete) {

			fontLarge.draw(batch, SCORE_LIFE_STRING, 150, 450);
			fontLarge
					.draw(batch,
							bar.getNoOfLife() + " x "
									+ String.valueOf(scoreRemainLife), 300, 450);
			drawTotalComplete = true;
		}

		if (drawTotalComplete) {
			int bonusScore = (bar.getNoOfLife() * scoreRemainLife);
			int tempTotal = scoreTotal + bonusScore;

			fontLarge.draw(batch, SCORE_TOTAL_STRING, 150, 400);
			fontLarge.draw(batch, String.valueOf(tempTotal), 300, 400);

		}

		drawButtonAfterComplete();

		batch.end();

	}

	public void drawScoreAfterGameOver() {

		int tempTotalScoreWithoutBonus = scoreTotal;
		SCORE_TITLE_STRING = "GAMEOVER";
		batch.begin();

		fontLarge.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		fontLarge.draw(batch, SCORE_TITLE_STRING, 125, 550);
		fontLarge.draw(batch, scoreLabel, 150, 500);
		// fun the score animation
		if (tempScore < tempTotalScoreWithoutBonus) {

			tempScore += 30;

		} else {
			tempScore = tempTotalScoreWithoutBonus;
			drawTotalComplete = true;
		}
		fontLarge.draw(batch, String.valueOf(tempScore), 300, 500);

		if (drawTotalComplete) {
			int bonusScore = (bar.getNoOfLife() * scoreRemainLife);
			int tempTotal = scoreTotal + bonusScore;

			fontLarge.draw(batch, SCORE_TOTAL_STRING, 150, 450);
			fontLarge.draw(batch, String.valueOf(tempTotal), 300, 450);

		}

		drawButtonAfterComplete();
		batch.end();

	}

	public void drawButtonAfterComplete() {

		if (completeLevel) {

			// draw retry button and next button
			batch.draw(retryButtonImage, retryLevelButton.x, retryLevelButton.y);
			batch.draw(exitButtonImage, exitButton.x, exitButton.y);
			batch.draw(submitScoreImage, submitScoreButton.x,
					submitScoreButton.y);

			if (currentLevel < NUM_OF_LEVELS)
				batch.draw(nextButtonImage, nextLevelButton.x,
						nextLevelButton.y);

		}
		if (isGameOver) {

			// draw retry button and next button
			batch.draw(retryButtonImage, retryLevelButton.x, retryLevelButton.y);
			batch.draw(submitScoreImage, submitScoreButton.x,
					submitScoreButton.y);
			batch.draw(exitButtonImage, exitButton.x, exitButton.y);

		}

	}

	private void bombMovement() {

		Iterator<Bomb> iter = bombs.iterator();
		while (iter.hasNext()) {
			Bomb bomb = iter.next();
			bomb.movement();
			bomb.changeDirectionWhenHitScreenSides();

			if (bomb.y - BOTTOM_PANEL_HEIGHT <= 0) {
				iter.remove();
			}

			else {
				if (bomb.overlaps(bar)) {
					if (isSoundOn)
						bomb_explosion_sound.play();
					// dropSound.play();
					if (isVibrationOn) {
						MainActivity.getInstance().getVibrator()
								.vibrate(MainActivity.BOMB_VIBRATION_TIME);
					}

					barExplosion = true;
					barExplosionCount = 0;
					checkGameOverOrContinous();
					clearAllItem();
					iter.remove();
				} else if (ball.getStatus() != BALL_STOP && bomb.overlaps(ball)) {
					if (isVibrationOn) {
						MainActivity.getInstance().getVibrator()
								.vibrate(MainActivity.BOMB_VIBRATION_TIME);
					}

					bombExplosions.add(new BombExplosion(bomb.x, bomb.y));
					changeBallDirectionOnHittingObstacle(bomb);
					iter.remove();
				}
			}
		}
	}

	private void moveBallToMiddleOfBar() {
		ball.x = bar.x + (bar.width / 2) - (ball.width / 2);
		ball.y = bar.y + bar.height + (ball.height / 3);
		ball.setDirection(DIRECTION_UP);
	}

	private void drawAcquireditems() {
		ArrayList<Item> items = bar.getItemList();
		int x = 20, y = 25;

		if (!items.isEmpty()) {
			for (Item item : items) {

				switch (item.getType()) {
				case ITEM_TYPE_GUN_BAR:
					batch.draw(iconGun, x, y);
					x += iconGun.getWidth() + 20;
					break;
				case ITEM_TYPE_LONG_BAR:
					batch.draw(iconLongBar, x, y);
					x += iconLongBar.getWidth() + 20;
					break;
				case ITEM_TYPE_SHORT_BAR:
					batch.draw(iconShortBar, x, y);
					x += iconShortBar.getWidth() + 20;
					break;
				}
			}
		}
	}

	public static void pauseGame() {
		if (drawType == DRAW_TYPE_GAME) {
			try {
				gamePause = true;
				MainActivity.getInstance().showPauseGameDialog();
			} catch (Exception ex) {
				Log.e("Exception", "Pause game exception:/n" + ex.toString());
			}
		}
	}

	private void restartLevel() {
		scoreBonusTime = 1;

		if (!completeLevel) {
			scoreTotal = 0;
			levelStartScore = 0;
		}

		isSoundPlayed = false;
		spawnBoss = false;
		completeLevel = false;
		isGameOver = false;
		drawLifeComplete = false;
		drawTotalComplete = false;

		bombs.clear();
		boss.setLife(BOSS_LIVES);
		boss.x = SCREEN_WIDTH / 2 - (BOSS_WIDTH / 2);
		boss.y = SCREEN_HEIGHT / 2;
		boss.resetImage();

		switch (currentLevel) {
		case 1:
			createBrickWallLevel1();
			break;
		case 2:
			createBrickWallLevel2();
			break;
		case 3:
			createBrickWallLevel3();
			break;
		}

		itemDroppeds.clear();
		clearAllItem();
		generateItemAndAddIntoBrick();

		bar.x = (SCREEN_WIDTH / 2) - (BAR_SHORT_WIDTH / 2);
		bar.y = 100;
		bar.setBullet(false);
		bar.removeAllItem();
		bar.setNoOfLife(NUMBER_OF_LIFE);

		touchPosition.set(bar.width / 2 + bar.x, bar.y, 0);

		ball.setStatus(BALL_STOP);
		moveBallToMiddleOfBar();

		barExplosion = false;
		bombExplosions.clear();

		showingMessage = true;
		showMessageTimeCount = TimeUtils.nanoTime();
	}

	private Texture getImage(int id) {
		switch (id) {
		case IMG_BOSS:
			return bossImage;

		case IMG_BOSS_GET_HIT:
			return bossGetHitImage;

		case IMG_BRICK_DURABILITY_1:
			return brickDurability_1Img;

		case IMG_BRICK_DURABILITY_2:
			return brickDurability_2Img;

		case IMG_BRICK_DURABILITY_3:
			return brickDurability_3Img;

		case IMG_NORMAL_BOMB:
			return normalBombImg;

		case IMG_SPECIAL_BOMB:
			return specialBombImg;

		case IMG_SHORT_BAR:
			return shortBarImg;

		case IMG_NORMAL_BAR:
			return normalBarImg;

		case IMG_LONG_BAR:
			return longBarImg;

		case IMG_BALL:
			return ballImg;

		case IMG_ITEM_RANDOM:
			return boxItemImage;

		case IMG_ITEM_LIFE:
			return lifeFullImage;

		case IMG_ITEM_GUN_LONG_BAR:
			return gun_long_barImage;

		case IMG_ITEM_GUN_SHORT_BAR:
			return gun_short_barImage;

		case IMG_ITEM_GUN_NORMAL_BAR:
			return gun_normal_barImage;
		}

		return null;
	}

	public void getHighscores() {

		if (MainActivity.getInstance().hasConnection()) {
			isLoading = true;
			try {
				scores.clear();
				players.clear();
				highscores.clear();
				highscorePlayers.clear();

				ParseQuery query = new ParseQuery("Scores");
				query.findInBackground(new FindCallback() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {

						for (ParseObject p : objects) {

							scores.add(p.getLong("score"));
							players.add(p.getString("playerName"));
						}

						int index, i;
						long tmp1, tmp2, score, iniScore = 0;
						String str1, str2, name, iniName = "---";

						for (i = 0; i < NUM_OF_HIGHSCORES; i++) {
							highscores.add(iniScore);
							highscorePlayers.add(iniName);
						}

						for (int x = 0; x < scores.size(); x++) {

							index = NUM_OF_HIGHSCORES;
							score = scores.get(x);
							name = players.get(x);

							for (i = NUM_OF_HIGHSCORES - 1; i >= 0; i--) {
								if (score > highscores.get(i)) {
									index = i;
								} else {
									break;
								}
							}

							if (index != NUM_OF_HIGHSCORES) {
								tmp1 = score;
								str1 = name;

								for (i = index; i < NUM_OF_HIGHSCORES; i++) {
									tmp2 = highscores.get(i);
									highscores.set(i, tmp1);
									tmp1 = tmp2;

									str2 = highscorePlayers.get(i);
									highscorePlayers.set(i, str1);
									str1 = str2;
								}
							}
						}

						highscoreTitle.setText("Highscore");

						scr1.setText(String.valueOf(highscores.get(0)));
						scr2.setText(String.valueOf(highscores.get(1)));
						scr3.setText(String.valueOf(highscores.get(2)));
						scr4.setText(String.valueOf(highscores.get(3)));
						scr5.setText(String.valueOf(highscores.get(4)));

						name1.setText(highscorePlayers.get(0));
						name2.setText(highscorePlayers.get(1));
						name3.setText(highscorePlayers.get(2));
						name4.setText(highscorePlayers.get(3));
						name5.setText(highscorePlayers.get(4));

						btnBack.setVisible(true);
						highscoreTitle.setVisible(true);
						scr1.setVisible(true);
						scr2.setVisible(true);
						scr3.setVisible(true);
						scr4.setVisible(true);
						scr5.setVisible(true);
						name1.setVisible(true);
						name2.setVisible(true);
						name3.setVisible(true);
						name4.setVisible(true);
						name5.setVisible(true);

						isLoading = false;
					}
				});

			} catch (Exception ex) {
				isLoading = false;
				Log.e("Exception", ex.toString());
			}
		} else {
			MainActivity.getInstance().setTargetAtivity(
					MainActivity.TARGET_ACT_HIGH_SCORE);
			MainActivity.getInstance().connectToInternet();
		}
	}

	public static void setConnecting(boolean status) {
		isConnecting = status;
	}

	private void checkLevelCompleteStatus() {

		if (MainActivity.getInstance().getLevelCompleteStatus(2)) {
			btnLevel2.setDisabled(false);
			btnLevel2.setColor(Color.WHITE);
		} else {
			btnLevel2.setDisabled(true);
			btnLevel2.setColor(Color.GRAY);
		}

		if (MainActivity.getInstance().getLevelCompleteStatus(3)) {
			btnLevel3.setDisabled(false);
			btnLevel3.setColor(Color.WHITE);
		} else {
			btnLevel3.setDisabled(true);
			btnLevel3.setColor(Color.GRAY);
		}
	}

	private void prepareToShowMainMenu() {

		stage.clear();
		stage.addActor(menuScrollLayer);
		drawType = DRAW_TYPE_MAIN_MENU;
	}

	private void completeLevel() {

		showingMessage = true;
		showMessageTimeCount = TimeUtils.nanoTime();
		completeLevel = true;

		if (currentLevel != 3)
			if (!MainActivity.getInstance().getLevelCompleteStatus(
					currentLevel + 1)) {

				MainActivity.getInstance().updateLevelCompleteStatus(
						currentLevel + 1);
			}

		// stop bg music, start the game over sound
		bg_boss_music.stop();
		bg_start_music.stop();
		if (isSoundOn)
			complete_level_sound.play();
	}

	public static void setMusicState() {
		if (!isSoundOn) {
			bg_start_music.stop();
			bg_boss_music.stop();
		} else {
			if (spawnBoss) {
				bg_start_music.stop();
				bg_boss_music.play();
			} else {
				bg_boss_music.stop();
				bg_start_music.play();
			}
		}
	}

	@Override
	public void dispose() {

		gameTitleImg.dispose();
		bossGetHitImage.dispose();
		brickDurability_1Img.dispose();
		brickDurability_2Img.dispose();
		brickDurability_3Img.dispose();
		normalBombImg.dispose();
		specialBombImg.dispose();
		normalBarImg.dispose();
		shortBarImg.dispose();
		longBarImg.dispose();
		ballImg.dispose();
		bossImage.dispose();
		bossGetHitImage.dispose();
		iconGun.dispose();
		iconLongBar.dispose();
		iconShortBar.dispose();
		bulletImage.dispose();
		boxItemImage.dispose();
		backgroundImage.dispose();
		batch.dispose();
		bombExplosionSheet.dispose();
		barExplosionSheet.dispose();
		highScoreSkin.dispose();
		skin.dispose();

		gun_long_barImage.dispose();
		gun_normal_barImage.dispose();
		gun_short_barImage.dispose();
		retryButtonImage.dispose();
		nextButtonImage.dispose();
		exitButtonImage.dispose();

		pick_item_sound.dispose();
		click_button_sound.dispose();
		laser_shoot_sound.dispose();
		ball_bouncing_sound.dispose();
		complete_level_sound.dispose();
		gameover_sound.dispose();

		bg_boss_music.dispose();
		bg_start_music.dispose();
		boss_hit_sound.play();

		submitScoreImage.dispose();

		MainActivity.getInstance().closeLocalDB();
	}

	@Override
	public void pause() {
		// if (drawType == DRAW_TYPE_GAME)
		// pauseGame();
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}

	public void setCamera() {

		float deviceWidth = Gdx.graphics.getWidth();
		float deviceHeight = Gdx.graphics.getHeight();
		float aspect = SCREEN_WIDTH / SCREEN_HEIGHT;

		if (deviceWidth / deviceHeight >= aspect) {
			// White bars left and right
			SCREEN_WIDTH = (int) (SCREEN_HEIGHT * deviceWidth / deviceHeight);
		} else {
			// White bars top and bottom
			SCREEN_HEIGHT = (int) (SCREEN_WIDTH * deviceHeight / deviceWidth);
		}

		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 0);

	}
}
