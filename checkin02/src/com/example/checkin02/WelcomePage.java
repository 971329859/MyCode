package com.example.checkin02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/**
 * 
 * @author ���ʽ�
 *  4.7
 */
public class WelcomePage extends Activity {
	private final long SPLASH_LENGTH = 2000;
	 Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_page);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				Intent intent=new Intent(WelcomePage.this,LoginPage.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_LENGTH);
		
	}
}
