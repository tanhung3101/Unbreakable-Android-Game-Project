package com.me.unbreakable;

import android.app.Activity;
import android.os.Bundle;

public class StartupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity.getInstance();
	}

}
