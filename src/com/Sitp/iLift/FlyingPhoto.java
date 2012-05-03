package com.Sitp.iLift;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class FlyingPhoto extends Activity
{
	private SurfaceView surface;
	private Handler handler;
	
	private RelativeLayout relativeLayout;
	
	private MyCamera myCamera;
	
	private Runnable runnable = new Runnable(){

		@Override
		public void run() 
		{
			
			myCamera = new MyCamera(surface, relativeLayout, FlyingPhoto.this);
    		myCamera.initCamera();
    		myCamera.setType(getIntent().getStringExtra("Source"));
    		LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    		
    		relativeLayout.addView(surface, params);
		}
		
	};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_photo);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// È«ÆÁ  
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// ºáÆÁ  
        
        relativeLayout = (RelativeLayout)findViewById(R.id.flyingPhoto);
        
        surface = new SurfaceView(this);
        
        handler = new Handler();
        handler.post(runnable);
        
    }
    
    @Override  
    public void onAttachedToWindow()  
    {  
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);  
        super.onAttachedToWindow();  
    }  
    
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_HOME: 
		{
			if (myCamera != null)
			{
				myCamera.takePhoto();
				
				try {
					Thread.sleep(1000);
					this.finish();
					break;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		}
		
		default:
			super.onKeyDown(KeyCode, event);

		}
		
		return true;
	}
    

    
}