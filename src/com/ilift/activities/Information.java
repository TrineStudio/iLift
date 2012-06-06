package com.ilift.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.Sitp.ilift.R;
import com.ilift.entity.TUserinfo;
import com.ilift.service.LogOutService;
import com.ilift.service.ResourceService;
import com.ilift.usage.FlyingPhoto;
import com.ilift.usage.TranslateListener;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Information extends Activity {
	/** Called when the activity is first created. */

	public static int[] sexConfigure = { 0, 0 };
	
	public static final String IMAGE_UNSPECIFIED = "image/*";  
	
	public static final int COMPLETE = 0;
    public static final int PHOTORESOULT = 3;
    
	// private LinearLayout linearLayout;

	private ImageView occupation;
	private ImageView portrait;
	
	private TextView carType;
	private TextView userName;
	private TextView userCollege;


	private Button informationBar;
	private Button selectBar;
	private Button newPhoto;
	private Button selectPhoto;
	
	private boolean IsFirst = false;


	private Bundle paras;

	private static final String[] sex_array_en = { "male", "female" };
	
	
	private int lastX;
	private int height;
	private int width;
	private int informationBarTop;
	private int informationBarLeft;
	private int initialLeft;
	
	private Handler handler = null;

	private DisplayMetrics dm;

	private TUserinfo tUserInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		// linearLayout = (LinearLayout)findViewById(R.id.information);

		occupation = (ImageView) findViewById(R.id.occupation);
		carType = (TextView) findViewById(R.id.carType);
		informationBar = (Button)findViewById(R.id.bar);
		informationBar.setOnTouchListener(new BarOnTouch());
		userName = (TextView) findViewById(R.id.name);
		userCollege = (TextView) findViewById(R.id.college);
		selectBar = (Button)findViewById(R.id.selectBar);
		newPhoto = (Button)findViewById(R.id.newPhoto);
		portrait = (ImageView)findViewById(R.id.portrait);
		selectPhoto = (Button)findViewById(R.id.selectPhoto);
	
		
		selectPhoto.setOnClickListener(new SelectPhoto());
		newPhoto.setOnClickListener(new NewPhoto());
		
		selectBar.setOnTouchListener(new SelectBarOnTouch(selectBar));

		paras = getIntent().getExtras();

		handler = new Handler();
		
		tUserInfo = (TUserinfo) ResourceService.getUserInfo();

		carType.setText(tUserInfo.getCarModel());
		userName.setText(tUserInfo.getUsername());
		userCollege.setText(tUserInfo.getCollege());
		if (carType.getText().toString().equals("none"))
			carType.setText("无");
			
		if (!tUserInfo.getJob().equals("0"))
		{
			occupation.setImageDrawable(getResources().getDrawable(R.drawable.teacher));
		}
		
		if (ResourceService.getUserPortrait() != null)
		{
			portrait.setImageBitmap(ResourceService.getUserPortrait());
			Log.d("ilift", "Portrait Loaded!");
		}
	}
	
	class SelectPhoto implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(Intent.ACTION_PICK, null);  
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);  
            startActivityForResult(intent, COMPLETE);
		}
	}

	class Back implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(Information.this, MainPage.class);
			startActivity(intent);
			Information.this.finish();
		}
	}
	
	class LogOut implements OnClickListener {

		@Override
		public void onClick(View v) {
			LogOutService temp = new LogOutService();
			temp.start();
			Toast.makeText(getApplicationContext(), "成功登出", Toast.LENGTH_SHORT);

			Intent intent = new Intent(Information.this, iLift.class);
			startActivity(intent);

			finish();

		}

	}

	 public void startPhotoZoom(Uri uri) 
	 {  
	        Intent intent = new Intent("com.android.camera.action.CROP");  
	        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);  
	        intent.putExtra("crop", "true");
	        intent.putExtra("aspectX", 1);  
	        intent.putExtra("aspectY", 1);
	        intent.putExtra("outputX", 100);  
	        intent.putExtra("outputY", 100);  
	        intent.putExtra("return-data", true);  
	        startActivityForResult(intent, PHOTORESOULT);  
	    }  
	
	  @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data)
	    {
	        if (requestCode == COMPLETE) 
	        {  
	        	if (data != null)
	        	{
	        		Uri uri = data.getData();  
	        		if (uri != null) 
	        		{  
	        			portrait.setImageURI(uri);
	        			try 
	        			{
							Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
							ResourceService.setUserPortrait(bm);
						} 
	        			catch (FileNotFoundException e) 
	        			{
							e.printStackTrace();
						} 
	        			catch (IOException e) 
	        			{
							e.printStackTrace();
						}
	        		}
	        	}
	  
	        }  	
	       
	        super.onActivityResult(requestCode, resultCode, data);  
	    }
	
	class NewPhoto implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			 Intent intent = new Intent(Information.this, FlyingPhoto.class);
			 intent.putExtra("Source", "portrait");
			 startActivity(intent);
			 
			 new SingleThread().start();
			 
			 	
		}
	}
	
	private class SingleThread extends Thread
	{
		@Override
		public void run()
		{
			while(!ResourceService.isNewPhoto())
			{
			}
			handler.post(runnable);
		}
	}
	
	private Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			
			Bitmap bm = ResourceService.getUserPortrait();
			
			if (bm == null)
				return;
			
			portrait.setImageBitmap(bm);
			ResourceService.setNewPhoto(false);
		}
		
	};
	
	class BarOnTouch implements OnTouchListener {

        private int leftX;
        private int rightX;

        private int ownerX;
        private int passengerX;

        public BarOnTouch()
        {
            if (getWindowManager().getDefaultDisplay().getWidth() == 320)
            {
                leftX = 14;
                rightX = 306;

                ownerX = 83;
                passengerX = 255;
            }
            else if (getWindowManager().getDefaultDisplay().getWidth() == 480)
            {
                leftX = 30;
                rightX = 462;

                ownerX = 102;
                passengerX = 389;
            }
        }

		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			switch (event.getAction()) 
			{
			case MotionEvent.ACTION_DOWN:
				if (!IsFirst) {
					informationBarTop = informationBar.getTop();
					initialLeft = informationBarLeft = informationBar.getLeft();

					height = informationBar.getHeight();
					width = informationBar.getWidth();

					IsFirst = true;
				}
				lastX = (int) event.getRawX();
				break;
			case MotionEvent.ACTION_MOVE:
				if ((int) (event.getX() - lastX) != 0)
				{
					if (event.getRawX() - lastX + informationBarLeft <= leftX)
					{
						informationBar.layout(leftX, informationBarTop, width + leftX, informationBarTop + height);
					}
					else if (event.getRawX() - lastX + informationBarLeft + width >= rightX)
					{
						informationBar.layout(rightX - width, informationBarTop, rightX, informationBarTop + height);
					}
					else
					{
						informationBar.layout((int) (event.getRawX() - lastX) + informationBarLeft, informationBarTop, (int) (event.getRawX() - lastX) + informationBarLeft + width, informationBarTop + height);
					}
				}
				
				informationBarTop = informationBar.getTop();
				informationBarLeft = informationBar.getLeft();
				Log.d("ilift", "On Touch Move : " + (event.getRawX() - lastX + informationBarLeft));
				lastX = (int) event.getRawX();

				break;
			case MotionEvent.ACTION_UP:
				if (event.getRawX() - lastX + informationBarLeft <= ownerX) {

					Intent intent = new Intent(Information.this,Setting.class);

					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, leftX - (float)informationBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (0.41f * (rightX - (float)informationBar.getLeft() - leftX)));
					translateAnimation.setFillEnabled(true);

					intent.putExtra("Statue", 2);

					translateAnimation.setAnimationListener(new TranslateListener(leftX + informationBar.getWidth(), intent, null, informationBar, Information.this));

					informationBar.startAnimation(translateAnimation);
				}
				else if(event.getRawX() - lastX + informationBarLeft + width >= passengerX)
				{
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, rightX - (float)informationBar.getLeft() - (float)informationBar.getWidth(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (0.41f * (rightX - (float)informationBar.getLeft())));
					translateAnimation.setFillEnabled(true);

					Intent intent = new Intent(Information.this, Setting.class);

					intent.putExtra("Statue", 1);

					translateAnimation.setAnimationListener(new TranslateListener(rightX, intent, null, informationBar, Information.this));

					informationBar.startAnimation(translateAnimation);
				}
				else
				{
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, initialLeft - (float)informationBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (4.1f * Math.abs((initialLeft - (float)informationBar.getLeft()))));
					translateAnimation.setFillEnabled(true);

					translateAnimation.setAnimationListener(new TranslateListener(initialLeft + informationBar.getWidth(), null, null, informationBar, Information.this));

					informationBar.startAnimation(translateAnimation);
				}
				informationBarLeft = initialLeft;
				break;
			}
			return true;
		}
	}
	

	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK: {
			Intent intent = new Intent(Information.this, MainPage.class);
			startActivity(intent);
			Information.this.finish();
			
		}

		}
		return true;

	}

	class DriverSexSet implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			sexConfigure[1] = arg2;
			// Log.d("ilift", "Button " + arg2 + "tapped");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	class PassengerSexSet implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			sexConfigure[0] = arg2;
			// Log.d("ilift", "Button " + arg2 + "tapped");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	class NextPage implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Information.this, Setting.class);
			paras = new Bundle();
			//paras.putString("DistanceRange", distanceRange.getText().toString());
			//paras.putString("TimeRange", timeRange.getText().toString());
			paras.putString("PassengerSex", sex_array_en[sexConfigure[0]]);
			paras.putString("DriverSex", sex_array_en[sexConfigure[1]]);
			intent.putExtras(paras);
			startActivity(intent);
			finish();
		}

	}

	class MoreInfo implements OnClickListener {
		@Override
		public void onClick(View V) {
			Intent intent = new Intent(Information.this, detail.class);
			if (carType.getText().toString() != null) //TODO: Add the occupation
			{
				Bundle paras = new Bundle();
				paras.putString("CarType", carType.getText().toString());
				intent.putExtras(paras);
			}
			startActivity(intent);
			finish();
		}

	}

	class Cancel implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	}

	class Out implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			LogOutService logOut = new LogOutService();
			logOut.start();
			dialog.dismiss();

		}

	}

	class DialogOfLogOut implements OnClickListener {
		@Override
		public void onClick(View V) {
			System.out.println("LogOut!!");
			AlertDialog.Builder builder = new Builder(Information.this);
			builder.setMessage("Sure to logout?");
			builder.setTitle("Tip");
			builder.setPositiveButton("LogOut", new Out());
			builder.setNegativeButton("Cancel", new Cancel());
			builder.create().show();
		}
	}
	
	class SelectBarOnTouch implements OnTouchListener {
		
		private int selectLastX;
		private int selectHeight;
		private int selectWidth;
		private int selectBarTop;
		private int selectBarLeft;
		private int selectInitialLeft;
		private boolean selectIsFirst = false;
		private Button selectBar;

        private int leftX1;
        private int leftX2;
        private int leftX3;
        private int leftX4;

        private int x1;
        private int x2;
        private int x3;
        private int x4;

        private int leftX;
        private int rightX;

		
		public SelectBarOnTouch(Button selectBar)
		{
			this.selectBar = selectBar;

            if (getWindowManager().getDefaultDisplay().getWidth() == 320)
            {
                leftX1 = 2;
                leftX2 = 84;
                leftX3 = 160;
                leftX4 = 238;

                x1 = 16;
                x2 = 92;
                x3 = 166;
                x4 = 244;

                leftX = 2;
                rightX = 319;
            }
            else if (getWindowManager().getDefaultDisplay().getWidth() == 480)
            {
                leftX1 = 17;
                leftX2 = 133;
                leftX3 = 244;
                leftX4 = 347;

                x1 = 33;
                x2 = 140;
                x3 = 251;
                x4 = 359;

                leftX = 17;
                rightX = 464;
            }
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				if (!selectIsFirst) 
				{
					selectInitialLeft = selectBarLeft = selectBar.getLeft();

					Log.d("ilift", "Top : " + selectBarTop + ", Left : "+ selectBarLeft);

					selectHeight = selectBar.getHeight();
					selectWidth = selectBar.getWidth();
					
					selectBarTop = selectBar.getTop();
					
					Log.d("ilift", "Height && Width : " + selectHeight + ", "+ selectWidth);
					selectIsFirst = true;
				}
				selectLastX = (int) event.getRawX();
				Log.d("ilift", "On Touch Down : " + event.getX());
				
				break;
				
			case MotionEvent.ACTION_MOVE:
				if ((int) (event.getRawX() - selectLastX) != 0)
				{
					Log.d("ilift", "X : " + event.getRawX() + ", Y: " + event.getRawY());
					if (event.getRawX() - selectLastX + selectBarLeft <= leftX)
					{
						selectBar.layout(leftX, selectBarTop, leftX + selectWidth,selectBarTop + selectHeight);
					}
					else if (event.getRawX() - selectLastX + selectBarLeft + selectWidth >= rightX)
					{
						selectBar.layout(rightX - selectWidth, selectBarTop, rightX ,selectBarTop + selectHeight);
					}
					else
					{
						selectBar.layout((int) (event.getRawX() - selectLastX)+ selectBarLeft, selectBarTop,(int) (event.getRawX() - selectLastX)+ selectBarLeft + selectWidth,selectBarTop + selectHeight);
					}
				}
				selectBarLeft = selectBar.getLeft();
				Log.d("ilift", "On Touch Move : " + (event.getRawX() - selectLastX + selectBarLeft));
				selectLastX = (int) event.getRawX();
				
				break;
				
			case MotionEvent.ACTION_UP:
				Log.d("ilift", Float.toString((event.getRawX() - selectLastX) + selectBarLeft));
				if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) >= leftX2 && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) <= leftX3)
				{
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, x2 - (float)selectBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (1.0f * Math.abs((x2 - (float)selectBar.getLeft()))));
					translateAnimation.setFillEnabled(true);

					Intent intent = new Intent(Information.this, Setting.class);

					translateAnimation.setAnimationListener(new TranslateListener(x2 + selectBar.getWidth(), intent, null, selectBar, Information.this));

					selectBar.startAnimation(translateAnimation);

				}
				else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > leftX3 && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) <= leftX4)
				{
					Intent intent = new Intent(Information.this,status.class);

					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, x3 - (float)selectBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (1.0f * Math.abs((x3 - (float)selectBar.getLeft()))));
					translateAnimation.setFillEnabled(true);

					translateAnimation.setAnimationListener(new TranslateListener(x3 + selectBar.getWidth(), intent, null, selectBar, Information.this));

					selectBar.startAnimation(translateAnimation);
				}
//				else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > leftX4)
//				{
//					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, x4 - (float)selectBar.getLeft(), 0.0f, 0.0f);
//					translateAnimation.setDuration((long) (1.0f * Math.abs((x4 - (float)selectBar.getLeft()))));
//					translateAnimation.setFillEnabled(true);
//
//					translateAnimation.setAnimationListener(new TranslateListener(x4 + selectBar.getWidth(), null, null, selectBar, Information.this));
//
//					selectBar.startAnimation(translateAnimation);
//				}
				else
				{
					if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > leftX4)
						Toast.makeText(getApplicationContext(), "历史功能还在开发中", Toast.LENGTH_SHORT).show();

					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, selectInitialLeft - (float)selectBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (1.0f * Math.abs((selectInitialLeft - (float)selectBar.getLeft()))));
					translateAnimation.setFillEnabled(true);

					translateAnimation.setAnimationListener(new TranslateListener(selectInitialLeft + selectBar.getWidth(), null, null, selectBar, Information.this));

					selectBar.startAnimation(translateAnimation);
				}

				selectBarLeft = selectInitialLeft;
				break;
			}
			return true;
		}

	}

}
