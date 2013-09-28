package com.me.unbreakable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.ParseException;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AndroidApplication {

	static MainActivity mainActivity;
	static final int BTN_RESUME = 0;
	static final int BTN_RESTART = 1;
	static final int BTN_OPTIONS = 2;
	static final int BTN_EXIT = 3;
	static final int BTN_TRY_CONNECT_TO_INTERNET = 4;
	static final int TARGET_ACT_SUBMIT_SCORE = 0;
	static final int TARGET_ACT_HIGH_SCORE = 1;
	static final int MESSAGE_GAME_OVER = 0;
	static final int MESSAGE_LEVEL_COMPLETE = 1;
	static final int MESSAGE_LEVEL_1 = 2;
	static final int MESSAGE_LEVEL_2 = 3;
	static final int MESSAGE_LEVEL_3 = 4;
	static final int CONNECTING_WAIT_TIME = 3000; // 3 seconds
	static final int REQUEST_NO_ACTION = 0;
	static final int REQUEST_ACTION_HIGH_SCORE = 1;
	static final int REQUEST_ACTION_HIGH_SCORE_NO_INTERNET = 2;
	static final int SCORE_SUBMITTED_NOTITY_TIME = 2000;
	static final int NORMAL_VIBRATION_TIME = 100;
	static final int BOMB_VIBRATION_TIME = 300;
	private Dialog pauseDialog, confirmDialog, imageMessageDialog,
			loadingDialog, connectingDialog, scoreSubmittedDialog,
			splashScreenDialog, playerNameDialog, optionDialog;
	private Handler dialogHandler;
	private int clickedButton = -1;
	private int confirmType;
	private int msgType;
	private int targetAtivity;
	private long scoreToSubmit;
	private String nameToSubmit;
	private Timer timer;
	private int requestGameCoreAction;
	private GameDatabase gdb;
	private Map<String, Boolean> levelCompleteStatus;
	private Vibrator vibrator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mainActivity = this;

		// Initialize vibrator service
		vibrator = (Vibrator) MainActivity.getInstance().getSystemService(
				Context.VIBRATOR_SERVICE);

		// Initialize connection to parse.com
		Parse.initialize(this, "Vr2v1cntLKsAkvbEFl94DtmGddZq058Yf08cjTMC",
				"oJULUXLyBGpYMC9tapokCq212MbWoVmRQ66C91qw");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// Make all objects public, remove to make all private
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);

		// Initialize dialogs
		dialogHandler = new Handler();
		createDialogs();
		showSplashScreenDialog();
		setDialogButtonListener();

		// Get data from local database
		levelCompleteStatus = new HashMap<String, Boolean>();
		openLocalDB();
		getLocalDBData();

		// Initialize game core
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		cfg.useAccelerometer = false;
		cfg.useCompass = false;

		initialize(new GameCore(), cfg);
	}

	public static MainActivity getInstance() {

		if (mainActivity == null) {
			mainActivity = new MainActivity();
		}

		return mainActivity;
	}

	private void openLocalDB() {
		if (gdb != null) {
			gdb.close();
		}
		gdb = new GameDatabase();
		gdb.open(this);
	}

	private void getLocalDBData() {
		Cursor dataCursor = gdb.getAllData();

		if (dataCursor.moveToFirst()) {
			do {
				levelCompleteStatus.put(dataCursor.getString(0),
						dataCursor.getInt(1) > 0 ? true : false);
			} while (dataCursor.moveToNext());
		}
		dataCursor.close();
	}

	public void updateLevelCompleteStatus(int level) {
		levelCompleteStatus.put(String.valueOf(level), true);
		gdb.updateDB(String.valueOf(level), true);
	}

	public boolean getLevelCompleteStatus(int level) {
		try {
			return levelCompleteStatus.get(String.valueOf(level));
		} catch (Exception ex) {
			Log.e("Exception", "getLevelCompleteStatus: " + ex.toString());
		}

		return false;
	}

	public void closeLocalDB() {
		gdb.close();
	}

	public void submitScore(long score, String name) {

		if (hasConnection()) {
			ParseObject po = new ParseObject("Scores");
			po.put("score", score);
			po.put("playerName", name);
			po.saveInBackground();

			showScoreSubmittedDialog();
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					hideScoreSubmittedDialog();
				}
			}, SCORE_SUBMITTED_NOTITY_TIME);
		} else {
			scoreToSubmit = score;
			nameToSubmit = name;
			targetAtivity = TARGET_ACT_SUBMIT_SCORE;
			connectToInternet();
		}
	}

	public void setScoreToSubmit(long score) {
		scoreToSubmit = score;
	}

	public void connectToInternet() {
		try {
			GameCore.setConnecting(true);

			// Enable Wifi
			WifiManager wifiManager;
			wifiManager = (WifiManager) this
					.getSystemService(this.WIFI_SERVICE);
			wifiManager.setWifiEnabled(true);

			// Enable 3G
			ConnectivityManager dataManager;
			dataManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			Method dataMtd = ConnectivityManager.class.getDeclaredMethod(
					"setMobileDataEnabled", boolean.class);
			dataMtd.setAccessible(true);
			dataMtd.invoke(dataManager, true);

			// Check Internet connection status after try to connect
			postConnectToInterner();
		} catch (Exception ex) {
			Log.e("Exception", "connectToInternet: " + ex.toString());
		}
	}

	private void postConnectToInterner() {

		try {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {

					if (hasConnection()) {
						GameCore.setConnecting(false);

						switch (targetAtivity) {

						case TARGET_ACT_SUBMIT_SCORE:
							submitScore(scoreToSubmit, nameToSubmit);
							break;
						case TARGET_ACT_HIGH_SCORE:
							requestGameCoreAction = REQUEST_ACTION_HIGH_SCORE;
							break;
						}
					} else if (isConnecting()) {
						// Extends the connecting wait time
						timer.cancel();
						postConnectToInterner();
					} else {
						confirmType = BTN_TRY_CONNECT_TO_INTERNET;
						showConfirmDialog();
					}
				}
			}, CONNECTING_WAIT_TIME);
		} catch (Exception ex) {
			Log.e("Exception", "postConnectToInternet: " + ex.toString());
		}
	}

	public boolean isConnecting() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

	public boolean hasConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

	public Vibrator getVibrator() {
		return vibrator;
	}

	public void setTargetAtivity(int targetAtivity) {
		this.targetAtivity = targetAtivity;
	}

	public int getRequestGameCoreAction() {
		return requestGameCoreAction;
	}

	public void setRequestGameCoreAction(int requestGameCoreAction) {
		this.requestGameCoreAction = requestGameCoreAction;
	}

	private void createDialogs() {
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

		// Create pause dialog
		pauseDialog = new Dialog(this);
		pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pauseDialog.setContentView(R.layout.pause_menu);
		pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		pauseDialog.setCancelable(false);
		pauseDialog.getWindow().setLayout((int) (screenWidth * 3 / 4),
				(int) (screenHeight * 3 / 4));

		// Create confirm dialog
		confirmDialog = new Dialog(this);
		confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		confirmDialog.setContentView(R.layout.confirm_box);
		confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		confirmDialog.setCancelable(false);
		confirmDialog.getWindow().setLayout((int) (screenWidth * 3 / 4),
				(int) (screenHeight * 2 / 5));

		playerNameDialog = new Dialog(this);
		playerNameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		playerNameDialog.setContentView(R.layout.player_name_box);
		playerNameDialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(0));
		playerNameDialog.setCancelable(false);
		playerNameDialog.getWindow().setLayout((int) (screenWidth * 3 / 4),
				(int) (screenHeight * 2 / 5));

		imageMessageDialog = new Dialog(this);
		imageMessageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		imageMessageDialog.setContentView(R.layout.image_message);
		imageMessageDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(0));
		imageMessageDialog.setCancelable(false);
		imageMessageDialog.getWindow().setLayout((int) (screenWidth * 3 / 4),
				(int) (screenHeight / 8));

		loadingDialog = new Dialog(this);
		loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loadingDialog.setContentView(R.layout.loading);
		loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		loadingDialog.setCancelable(false);
		loadingDialog.getWindow().setLayout((int) (screenWidth * 2 / 4),
				(int) (screenHeight / 8));

		connectingDialog = new Dialog(this);
		connectingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		connectingDialog.setContentView(R.layout.connecting);
		connectingDialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(0));
		connectingDialog.setCancelable(false);
		connectingDialog.getWindow().setLayout((int) (screenWidth * 2 / 4),
				(int) (screenHeight / 8));

		scoreSubmittedDialog = new Dialog(this);
		scoreSubmittedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		scoreSubmittedDialog.setContentView(R.layout.score_submitted);
		scoreSubmittedDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(0));
		scoreSubmittedDialog.setCancelable(false);
		scoreSubmittedDialog.getWindow().setLayout((int) (screenWidth * 2 / 4),
				(int) (screenHeight / 8));

		splashScreenDialog = new Dialog(this);
		splashScreenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		splashScreenDialog.setContentView(R.layout.splash_screen);
		splashScreenDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(0));
		splashScreenDialog.setCancelable(false);
		splashScreenDialog.getWindow().setLayout((int) (screenWidth),
				(int) (screenHeight));

		optionDialog = new Dialog(this);
		optionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		optionDialog.setContentView(R.layout.option_menu);
		optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		optionDialog.setCancelable(false);
		optionDialog.getWindow().setLayout((int) (screenWidth * 3 / 4),
				(int) (screenHeight * 3 / 4));
	}

	public void showPauseGameDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (pauseDialog != null && !pauseDialog.isShowing()) {
					pauseDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hidePauseGameDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (pauseDialog != null && pauseDialog.isShowing())
					pauseDialog.dismiss();
			}
		});
	}

	public void showLoadingDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (loadingDialog != null && !loadingDialog.isShowing()) {
					loadingDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hideLoadingDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (loadingDialog != null && loadingDialog.isShowing())
					loadingDialog.dismiss();
			}
		});
	}

	public void showOptionDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (optionDialog != null && !optionDialog.isShowing()) {
					CheckBox chkSound = (CheckBox) optionDialog
							.findViewById(R.id.chkSound);
					chkSound.setChecked(GameCore.isSoundOn);

					CheckBox chkVibration = (CheckBox) optionDialog
							.findViewById(R.id.chkVibration);
					chkVibration.setChecked(GameCore.isVibrationOn);

					optionDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hideOptionDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (optionDialog != null && optionDialog.isShowing())
					optionDialog.dismiss();
			}
		});
	}

	public void showSplashScreenDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (splashScreenDialog != null
						&& !splashScreenDialog.isShowing()) {
					splashScreenDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hideSplashScreenDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (splashScreenDialog != null
						&& splashScreenDialog.isShowing())
					splashScreenDialog.dismiss();
			}
		});
	}

	public void showScoreSubmittedDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (scoreSubmittedDialog != null
						&& !scoreSubmittedDialog.isShowing()) {
					scoreSubmittedDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hideScoreSubmittedDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (scoreSubmittedDialog != null
						&& scoreSubmittedDialog.isShowing())
					scoreSubmittedDialog.dismiss();
			}
		});
	}

	public void showConnectingDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (connectingDialog != null && !connectingDialog.isShowing()) {
					connectingDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hideConnectingDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (connectingDialog != null && connectingDialog.isShowing())
					connectingDialog.dismiss();
			}
		});
	}

	public void showPlayerNameDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (playerNameDialog != null && !playerNameDialog.isShowing()) {
					playerNameDialog.show();
					EditText et = (EditText) playerNameDialog
							.findViewById(R.id.txtPlayerName);
					et.setText("");

					clickedButton = -1;
				}
			}
		});
	}

	public void hidePlayerNameDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (playerNameDialog != null && playerNameDialog.isShowing())
					playerNameDialog.dismiss();
			}
		});
	}

	public void showImageMessageDialog(int msgType) {
		this.msgType = msgType;
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (imageMessageDialog != null
						&& !imageMessageDialog.isShowing()) {

					ImageView imgView = (ImageView) imageMessageDialog
							.findViewById(R.id.image);

					switch (MainActivity.getInstance().getMsgType()) {
					case MESSAGE_GAME_OVER:
						imgView.setBackgroundResource(R.drawable.msg_game_over);
						break;
					case MESSAGE_LEVEL_COMPLETE:
						imgView.setBackgroundResource(R.drawable.msg_level_complete);
						break;
					case MESSAGE_LEVEL_1:
						imgView.setBackgroundResource(R.drawable.msg_level_1);
						break;
					case MESSAGE_LEVEL_2:
						imgView.setBackgroundResource(R.drawable.msg_level_2);
						break;
					case MESSAGE_LEVEL_3:
						imgView.setBackgroundResource(R.drawable.msg_level_3);
						break;
					}

					imageMessageDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hideImageMessageDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (imageMessageDialog != null
						&& imageMessageDialog.isShowing())
					imageMessageDialog.dismiss();
			}
		});
	}

	public void showConfirmDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (confirmDialog != null && !confirmDialog.isShowing()) {
					TextView txtView1 = (TextView) confirmDialog
							.findViewById(R.id.confirmText1);
					TextView txtView2 = (TextView) confirmDialog
							.findViewById(R.id.confirmText2);
					txtView2.setText("");
					txtView2.setVisibility(TextView.GONE);

					switch (confirmType) {
					case BTN_RESTART:
						txtView1.setText("Restart this level?");
						break;
					case BTN_EXIT:
						txtView1.setText("Exit to main menu?");
						break;
					case BTN_TRY_CONNECT_TO_INTERNET:
						txtView2.setVisibility(TextView.VISIBLE);
						txtView1.setText("No internet connection!");
						txtView2.setText("Try to connect?");
						break;
					}

					confirmDialog.show();
					clickedButton = -1;
				}
			}
		});
	}

	public void hideConfirmDialog() {
		dialogHandler.post(new Runnable() {

			@Override
			public void run() {
				if (confirmDialog != null && confirmDialog.isShowing())
					confirmDialog.dismiss();
			}
		});
	}

	private void setDialogButtonListener() {

		Button resume = (Button) pauseDialog.findViewById(R.id.btnResume);
		resume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				clickedButton = BTN_RESUME;
				hidePauseGameDialog();
			}
		});

		Button restart = (Button) pauseDialog.findViewById(R.id.btnRestart);
		restart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				confirmType = BTN_RESTART;
				hidePauseGameDialog();
				showConfirmDialog();
			}
		});

		Button option = (Button) pauseDialog.findViewById(R.id.btnOptions);
		option.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hidePauseGameDialog();
				showOptionDialog();
			}
		});

		Button exit = (Button) pauseDialog.findViewById(R.id.btnExit);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				confirmType = BTN_EXIT;
				hidePauseGameDialog();
				showConfirmDialog();
			}
		});

		Button noConfirmBox = (Button) confirmDialog.findViewById(R.id.btnNo);
		noConfirmBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hideConfirmDialog();

				if (confirmType == BTN_RESTART || confirmType == BTN_EXIT)
					showPauseGameDialog();
				if (confirmType == BTN_TRY_CONNECT_TO_INTERNET) {
					GameCore.setConnecting(false);

					if (targetAtivity == TARGET_ACT_HIGH_SCORE) {
						requestGameCoreAction = REQUEST_ACTION_HIGH_SCORE_NO_INTERNET;
					}
				}

			}
		});

		Button yesConfirmBox = (Button) confirmDialog.findViewById(R.id.btnYes);
		yesConfirmBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switch (confirmType) {
				case BTN_RESTART:
					clickedButton = BTN_RESTART;
					break;
				case BTN_EXIT:
					clickedButton = BTN_EXIT;
					break;
				case BTN_TRY_CONNECT_TO_INTERNET:
					connectToInternet();
					break;
				}
				hideConfirmDialog();
			}
		});

		Button noPlayerNameBox = (Button) playerNameDialog
				.findViewById(R.id.btnNo);
		noPlayerNameBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hidePlayerNameDialog();
			}
		});

		Button yesPlayerNameBox = (Button) playerNameDialog
				.findViewById(R.id.btnYes);
		yesPlayerNameBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EditText et = (EditText) playerNameDialog
						.findViewById(R.id.txtPlayerName);
				String inputText = et.getText().toString();

				if (inputText.trim().length() == 0) {
					Toast.makeText(MainActivity.getInstance(),
							"Player Name cannot be blank", Toast.LENGTH_SHORT)
							.show();
				} else {
					nameToSubmit = et.getText().toString();
					submitScore(scoreToSubmit, nameToSubmit);
					hidePlayerNameDialog();
				}
			}
		});

		Button saveOption = (Button) optionDialog.findViewById(R.id.btnSave);
		saveOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CheckBox chkSound = (CheckBox) optionDialog
						.findViewById(R.id.chkSound);
				GameCore.isSoundOn = chkSound.isChecked();

				CheckBox chkVibration = (CheckBox) optionDialog
						.findViewById(R.id.chkVibration);
				GameCore.isVibrationOn = chkVibration.isChecked();

				GameCore.isSoundPlayed = false;

				hideOptionDialog();
				showPauseGameDialog();
			}
		});

		Button cancelOption = (Button) optionDialog
				.findViewById(R.id.btnCancel);
		cancelOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				hideOptionDialog();
				showPauseGameDialog();
			}
		});
	}

	public int getConfirmType() {
		return confirmType;
	}

	public void setConfirmType(int confirmType) {
		this.confirmType = confirmType;
	}

	public int getClickedButton() {
		return clickedButton;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			GameCore.pauseGame();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public int getMsgType() {
		return msgType;
	}

}