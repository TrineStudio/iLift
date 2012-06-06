package com.ilift.usage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;

public class TranslateListener implements AnimationListener
	{
		private int endX;
		private Intent intent;
		private Bundle bundle;
		private Button bar;
		private Activity activity;
		
		public TranslateListener(int endX, Intent intent, Bundle bundle, Button bar, Activity activity)
		{
			this.endX = endX;
			this.intent = intent;
			this.bundle = bundle;
			this.bar = bar;
			this.activity = activity;
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			bar.layout(endX - bar.getWidth(), bar.getTop(), endX, bar.getTop() + bar.getHeight());
  
			if (bundle != null)
				intent.putExtras(bundle);
			
			if (intent != null)
			{
				activity.startActivity(intent);
				activity.finish();
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	}