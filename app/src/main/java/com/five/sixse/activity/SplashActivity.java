package com.five.sixse.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.five.sixse.R;


public class SplashActivity extends Activity {
	private static int SPLASH_TIME_OUT = 2000;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_splash_layout);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					Intent intent = new Intent(SplashActivity.this,MainActivity.class);
					startActivity(intent);
					finish();
				}

			}, SPLASH_TIME_OUT);


	}
}
