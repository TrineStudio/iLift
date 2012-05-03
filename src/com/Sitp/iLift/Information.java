package com.Sitp.iLift;

import java.io.FileNotFoundException;
import java.io.IOException;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ilifting.dao.pojos.TUserinfo;

public class Information extends Activity {
	/** Called when the activity is first created. */

	public static int[] sexConfigure = { 0, 0 };
	
	public static final String IMAGE_UNSPECIFIED = "image/*";  
	
	public static final int COMPLETE = 0; // 缩放 
	public static final int PHOTOHRAPH = 1;// 拍照  
    public static final int PHOTOZOOM = 2; // 缩放  
    public static final int PHOTORESOULT = 3;// 结果  
    
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

	private static final String[] sex_array_en = { "male", "famale" };
	
	
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
		
		tUserInfo = (TUserinfo) resourceService.getUserInfo();

		carType.setText(tUserInfo.getCarModel());
		userName.setText(tUserInfo.getUsername());
		userCollege.setText(tUserInfo.getCollege());
		if (carType.getText().toString().equals("none"))
			carType.setText("无");
			
		if (!tUserInfo.getJob().equals("0"))
		{
			occupation.setImageDrawable(getResources().getDrawable(R.drawable.teacher));
		}
		
		if (resourceService.getUserPortrait() != null)
		{
			portrait.setImageBitmap(resourceService.getUserPortrait());
			Log.d("iLift", "Portrait Loaded!");
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
	        // aspectX aspectY 是宽高的比例  
	        intent.putExtra("aspectX", 1);  
	        intent.putExtra("aspectY", 1);  
	        // outputX outputY 是裁剪图片宽高  
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
							resourceService.setUserPortrait(bm);
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
			while(!resourceService.isNewPhoto())
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
			
			Bitmap bm = resourceService.getUserPortrait();
			
			if (bm == null)
				return;
			
			portrait.setImageBitmap(bm);
			resourceService.setNewPhoto(false);
		}
		
	};
	
	class BarOnTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			switch (event.getAction()) 
			{
			case MotionEvent.ACTION_DOWN:
				if (!IsFirst) {
					informationBarTop = informationBar.getTop();
					initialLeft = informationBarLeft = informationBar.getLeft();

					Log.d("iLift", "Top : " + informationBarTop + ", Left : "
							+ informationBarLeft);

					height = informationBar.getHeight();
					width = informationBar.getWidth();

					Log
							.d("iLift", "Height && Width : " + height + ", "
									+ width);
					IsFirst = true;
				}
				lastX = (int) event.getRawX();
				Log.d("iLift", "On Touch Down : " + event.getX());
				break;
			case MotionEvent.ACTION_MOVE:
				if ((int) (event.getX() - lastX) != 0)
				{
					Log.d("iLift", "X : " + event.getRawX() + ", Y: " + event.getRawY());
					if (event.getRawX() - lastX + informationBarLeft <= 14.0f)
					{
						informationBar.layout(14, informationBarTop, width + 14, informationBarTop + height);
					}
					else if (event.getRawX() - lastX + informationBarLeft + width >= 306.0f)
					{
						informationBar.layout(306 - width, informationBarTop, 306, informationBarTop + height);
					} 
					else
					{
						informationBar.layout((int) (event.getRawX() - lastX) + informationBarLeft, informationBarTop, (int) (event.getRawX() - lastX) + informationBarLeft + width, informationBarTop + height);
					}
				}
				
				informationBarTop = informationBar.getTop();
				informationBarLeft = informationBar.getLeft();
				Log.d("iLift", "On Touch Move : " + event.getX());
				lastX = (int) event.getRawX();

				break;
			case MotionEvent.ACTION_UP:
				if (event.getRawX() - lastX + informationBarLeft <= 83.0f) {

					Intent intent = new Intent(Information.this,Setting.class);

					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 14.0f - (float)informationBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (0.41f * (306.0f - (float)informationBar.getLeft() - 14.0f)));
					translateAnimation.setFillEnabled(true);
					
					intent.putExtra("Statue", 2);
					
					translateAnimation.setAnimationListener(new TranslateListener(14 + informationBar.getWidth(), intent, null, informationBar, Information.this));
					
					informationBar.startAnimation(translateAnimation);
				}
				else if(event.getRawX() - lastX + informationBarLeft + width >= 255)
				{ 
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 306.0f - (float)informationBar.getLeft() - (float)informationBar.getWidth(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (0.41f * (306.0f - (float)informationBar.getLeft())));
					translateAnimation.setFillEnabled(true);
					
					
					
					
					Intent intent = new Intent(Information.this, Setting.class);
					
					intent.putExtra("Statue", 1);
					
					translateAnimation.setAnimationListener(new TranslateListener(306, intent, null, informationBar, Information.this));
					
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
			// Log.d("iLift", "Button " + arg2 + "tapped");
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
			// Log.d("iLift", "Button " + arg2 + "tapped");
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
		
		public SelectBarOnTouch(Button selectBar)
		{
			this.selectBar = selectBar;
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				if (!selectIsFirst) 
				{
					selectInitialLeft = selectBarLeft = selectBar.getLeft();

					Log.d("iLift", "Top : " + selectBarTop + ", Left : "+ selectBarLeft);

					selectHeight = selectBar.getHeight();
					selectWidth = selectBar.getWidth();
					
					selectBarTop = selectBar.getTop();
					
					Log.d("iLift", "Height && Width : " + selectHeight + ", "+ selectWidth);
					selectIsFirst = true;
				}
				selectLastX = (int) event.getRawX();
				Log.d("iLift", "On Touch Down : " + event.getX());
				
				break;
				
			case MotionEvent.ACTION_MOVE:
				if ((int) (event.getRawX() - selectLastX) != 0) 
				{
					Log.d("iLift", "X : " + event.getRawX() + ", Y: " + event.getRawY());
					if (event.getRawX() - selectLastX + selectBarLeft <= 2.0f) 
					{
						selectBar.layout(2, selectBarTop, 2 + selectWidth,selectBarTop + selectHeight);
					} 
					else if (event.getRawX() - selectLastX + selectBarLeft + selectWidth >= 319)
					{
						selectBar.layout(254, selectBarTop, 254 + selectWidth ,selectBarTop + selectHeight);
					} 
					else 
					{
						selectBar.layout((int) (event.getRawX() - selectLastX)+ selectBarLeft, selectBarTop,(int) (event.getRawX() - selectLastX)+ selectBarLeft + selectWidth,selectBarTop + selectHeight);
					}
				}
				selectBarLeft = selectBar.getLeft();
				Log.d("iLift", "On Touch Move : " + (event.getRawX() - selectLastX + selectBarLeft + selectWidth));
				selectLastX = (int) event.getRawX();
				
				break;
				
			case MotionEvent.ACTION_UP:
				Log.d("iLift", Float.toString((event.getRawX() - selectLastX) + selectBarLeft));
				if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) >= 84.0f && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) <= 160.0f)
				{
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 92.0f - (float)selectBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (1.0f * Math.abs((92.0f - (float)selectBar.getLeft()))));
					translateAnimation.setFillEnabled(true);
					
					Intent intent = new Intent(Information.this, Setting.class);
					
					translateAnimation.setAnimationListener(new TranslateListener(92 + selectBar.getWidth(), intent, null, selectBar, Information.this));
					
					selectBar.startAnimation(translateAnimation);
					
				} 
				else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > 160.0f && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) <= 238.0f)
				{
					Intent intent = new Intent(Information.this,status.class);
					
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 166.0f - (float)selectBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (1.0f * Math.abs((166.0f - (float)selectBar.getLeft()))));
					translateAnimation.setFillEnabled(true);
					
					translateAnimation.setAnimationListener(new TranslateListener(166 + selectBar.getWidth(), intent, null, selectBar, Information.this));
					
					selectBar.startAnimation(translateAnimation);
				} 
//				else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > 238.0f) 
//				{
//					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 244.0f - (float)selectBar.getLeft(), 0.0f, 0.0f);
//					translateAnimation.setDuration((long) (1.0f * Math.abs((244.0f - (float)selectBar.getLeft()))));
//					translateAnimation.setFillEnabled(true);
//					
//					translateAnimation.setAnimationListener(new TranslateListener(244 + selectBar.getWidth(), null, null, selectBar, Information.this));
//					
//					selectBar.startAnimation(translateAnimation);
//				} 
				else 
				{
					if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > 238.0f) 
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
