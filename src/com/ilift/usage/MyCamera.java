package com.ilift.usage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ZoomControls;
import com.ilift.service.ResourceService;
import com.ilift.service.SetAvatarService;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;

public class MyCamera implements SurfaceHolder.Callback{
	private SurfaceView surface;
	
	private SurfaceHolder surfaceHolder;
	
	private Camera camera;
	
	private ZoomControls zoomControl;
	
	private Activity activity;
	
	private SeekBar seekBar;
	
	private RelativeLayout relativeLayout;
	
	private String type;
	
	private int zoomLevel = 0;
	private int maxZoomLevel = 0;
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public MyCamera(SurfaceView surface, RelativeLayout relativeLayout, Activity activity)
	{
		this.surface = surface;
		this.relativeLayout = relativeLayout;
		this.activity = activity;
	}
	
	
	public void initCamera()
    {
    	surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
    	surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    	
    	zoomControl = new ZoomControls(activity);
    	

    }
    
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
		Parameters p = camera.getParameters();
	       

        camera.getParameters().setRotation(90);

        Camera.Size s = p.getSupportedPreviewSizes().get(0);
        p.setPreviewSize( s.width, s.height );
        p.setPictureSize(s.width, s.height);
        p.setPictureFormat(PixelFormat.JPEG);
        p.set("flash-mode", "auto");
        
        camera.setParameters(p);
        
        
        if (p.isZoomSupported())
        {
        	seekBar = new SeekBar(activity);
        	
        	maxZoomLevel = p.getMaxZoom();
        	
        	zoomControl.setIsZoomInEnabled(true);
        	zoomControl.setIsZoomOutEnabled(true);
        	zoomControl.setOnZoomOutClickListener(new OnZoomOut());
        	zoomControl.setOnZoomInClickListener(new OnZoomIn());
        	


        	RelativeLayout.LayoutParams seekBarParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        	seekBarParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        	seekBarParams.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
        	
        	seekBar.setMax(maxZoomLevel);
        	seekBar.setOnSeekBarChangeListener(new SeekBarOnChange());
        	seekBar.setVisibility(View.GONE);
        	
        	relativeLayout.addView(seekBar, seekBarParams);
        	
        	//relativeLayout.setOnClickListener(new RelativeLayoutOnClick());
        	relativeLayout.setOnTouchListener(new TouchZoom());
        }
        
		
	}
	
	
	public void takePhoto()
	{
		camera.takePicture(new shutter(), null, new pictureTake());
	}
	
	private class pictureTake implements PictureCallback
	{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) 
		{
			if (data != null)
			{
				try
				{
					
					
			        File sd=Environment.getExternalStorageDirectory();
			        String path = sd.getPath()+"/iLiftImages/";
			        if (type.equals("portrait"))
			        	path += ResourceService.getUserInfo().getUsername();
			        else
			        	path += "goods";
			        File file=new File(path); 
			        if(!file.exists()) 
			         file.mkdir();  
					
					
					
					Date today = new Date();
					String fileName = Integer.toString(today.getYear()) + Integer.toString(today.getMonth()) + Integer.toString(today.getDay()) + 
					Integer.toString(today.getHours()) + Integer.toString(today.getMinutes()) + Integer.toString(today.getSeconds());
					
					Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
					
					float width = 1.0f;
					float hegith = 1.0f;

					Matrix matrix = new Matrix();

					matrix.postScale(width,hegith);

					matrix.postRotate(90);
					
					bm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);
					
                    
                    
                    
                 
                    CompressFormat format= Bitmap.CompressFormat.JPEG;  
                    int quality = 100;  
                    OutputStream stream = null;  
                    try {  
                            stream = new FileOutputStream(path + "/" + fileName);  
                    } catch (FileNotFoundException e) {  
                            // TODO Auto-generated catch block  
                            e.printStackTrace();  
                    }  
                    
                    boolean result =  bm.compress(format, quality, stream);
             
                    Log.d("ilift", Boolean.toString(result));
                    
                    if (type.equals("portrait"))
                    {
                    	ResourceService.setUserPortrait(bm);
                    	ResourceService.setBitmapSrc(path + "/" + fileName);
                    	
                    	JSONObject object = new SetAvatarService().start(ResourceService.getUserInfo().getId().toString(), "user");
                    
                    	result = object.getBoolean("result");
                   
	                   if (!result)
	                   {
	                	   String message = object.getString("message");
	                	   Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	                   }
                    }
                    else if (type.equals("goods"))
                    {
                    	ResourceService.setGoodsPortrait(bm);
                    	ResourceService.setBitmapSrc(path + "/" + fileName);
                    }
                    
                    ResourceService.setNewPhoto(true);
	                    
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
			}
			
		}
		
	}
	
	private class TouchZoom implements OnTouchListener
	{
		
		private boolean isTouch = false;
		private float lastDistance = -1.0f;
		private float curDistance = -1.0f;
		private int zoomDepth = 0;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			Log.d("FlyingPhoto", Integer.toString(event.getAction()));
			switch(event.getAction() & MotionEvent.ACTION_MASK)
			{
			case MotionEvent.ACTION_POINTER_UP:
				lastDistance = -1.0f;
				curDistance = -1.0f;
				isTouch = false;
				MyCamera.this.seekBar.setVisibility(View.GONE);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				isTouch = true;
				lastDistance = space(event);
				camera.autoFocus(null);
				break;
			case MotionEvent.ACTION_MOVE:
			if (isTouch)
			{
				MyCamera.this.seekBar.setVisibility(View.VISIBLE);
				
				curDistance = space(event);
				int depth = Math.abs((int)(curDistance - lastDistance) / 3);
				Parameters p = camera.getParameters();
					
				camera.getParameters().setRotation(90);

				Camera.Size s = p.getSupportedPreviewSizes().get(0);
				p.setPreviewSize( s.width, s.height );
				p.setPictureSize(s.width, s.height);
				p.setPictureFormat(PixelFormat.JPEG);
				p.set("flash-mode", "auto");
				
				if (depth == 0)
					return true;
				
				if (curDistance > lastDistance)
					zoomDepth += depth;
				else
					zoomDepth -= depth;
				
				if (zoomDepth > maxZoomLevel)
					zoomDepth = maxZoomLevel;
				else if (zoomDepth <= 0)
					zoomDepth = 0;
					
					p.setZoom(zoomDepth);
						
				camera.setParameters(p);
				lastDistance = curDistance;
				
				MyCamera.this.seekBar.setProgress(zoomDepth);
			}
			
			}
			
			return true;
		}
		
		private float space(MotionEvent event)
		{
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
		    return FloatMath.sqrt(x * x + y * y);
		}
		
	}
	
	private class shutter implements ShutterCallback
	{

		@Override
		public void onShutter() {
			MediaPlayer sound = MediaPlayer.create(activity.getApplicationContext(), Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
			sound.start();
		}
		
	};
	
	public class RelativeLayoutOnClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (seekBar.getVisibility() == View.GONE)
				seekBar.setVisibility(View.VISIBLE);
			else
				seekBar.setVisibility(View.GONE);
				
		}
	}
	
	public class SeekBarOnChange implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			Parameters p = camera.getParameters();
		       

	        camera.getParameters().setRotation(90);

	        Camera.Size s = p.getSupportedPreviewSizes().get(0);
	        p.setPreviewSize( s.width, s.height );
	        p.setPictureSize(s.width, s.height);
	        p.setPictureFormat(PixelFormat.JPEG);
	        p.set("flash-mode", "auto");
	        p.setZoom(progress);
				
			camera.setParameters(p);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
	
	}
	
	public class OnZoomIn implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			if (zoomLevel < maxZoomLevel)
			{
				zoomLevel++;
				
				Parameters p = camera.getParameters();
			       

		        camera.getParameters().setRotation(90);

		        Camera.Size s = p.getSupportedPreviewSizes().get(0);
		        p.setPreviewSize( s.width, s.height );
		        p.setPictureSize(s.width, s.height);
		        p.setPictureFormat(PixelFormat.JPEG);
		        p.set("flash-mode", "auto");
		        p.setZoom(zoomLevel);
				
				Log.d("FlyingPhoto", Integer.toString(zoomLevel));
				
				camera.setParameters(p);
			}
			
		}	
	}
	
	public class OnZoomOut implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (zoomLevel > 0)
			{
				zoomLevel --;
				Parameters p = camera.getParameters();
			       

		        camera.getParameters().setRotation(90);

		        Camera.Size s = p.getSupportedPreviewSizes().get(0);
		        p.setPreviewSize( s.width, s.height );
		        p.setPictureSize(s.width, s.height);
		        p.setPictureFormat(PixelFormat.JPEG);
		        p.set("flash-mode", "auto");
		        p.setZoom(zoomLevel);
				
				Log.d("FlyingPhoto", Integer.toString(zoomLevel));
				
				camera.setParameters(p);
			}
		}
	}
	
	

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera = Camera.open();
		
		try {
			camera.setPreviewDisplay(surfaceHolder);
			camera.startPreview();;
		} catch (IOException e) {
	          camera.release();
	          camera = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (camera != null) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
	        camera.release();
	        camera = null;
	    }
	}

}
