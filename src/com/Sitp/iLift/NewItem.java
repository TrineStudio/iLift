package com.Sitp.iLift;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ilifting.dao.pojos.TGoodsInfo;



public class NewItem extends Activity{
	
	public static final String IMAGE_UNSPECIFIED = "image/*";  
	
	public static final int COMPLETE = 0; // 缩放 
	public static final int PHOTOHRAPH = 1;// 拍照  
    public static final int PHOTOZOOM = 2; // 缩放  
    public static final int PHOTORESOULT = 3;// 结果  
	
	private Button putIn;
	private Button newPhoto;
	private Button selectPhoto;
	
	private ImageView itemPortrait;
	
	private CheckBox itemFragile;
	private CheckBox itemHot;
	private CheckBox itemFolderable;
	
	private EditText itemDescription;
	private EditText itemName;
	private EditText itemAmount;
	private EditText itemPrice;
	
	private Handler handler;
	
	private TGoodsInfo good = new TGoodsInfo();
	
	private String[] descriptionText = {"易碎", "烫", "易漏"};
	
	private int[] descriptions = {0, 0, 0};
	
	private Bitmap bitmap;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_item);
		
		handler = new Handler();
		
		itemDescription = (EditText)findViewById(R.id.descriptor);
		itemName = (EditText)findViewById(R.id.itemName);
		itemAmount = (EditText)findViewById(R.id.itemAmount); 
		itemPrice = (EditText)findViewById(R.id.itemPrice);
		
		itemFragile = (CheckBox)findViewById(R.id.fragile);
		itemHot = (CheckBox)findViewById(R.id.hot);
		itemFolderable = (CheckBox)findViewById(R.id.folderable);
		
		putIn = (Button)findViewById(R.id.finish);
		
		newPhoto = (Button)findViewById(R.id.newPhoto);
		selectPhoto = (Button)findViewById(R.id.selectPhoto);
		itemPortrait = (ImageView)findViewById(R.id.portrait);
		
		newPhoto.setOnClickListener(new NewPhoto());
		selectPhoto.setOnClickListener(new SelectPhoto());
		
		putIn.setOnClickListener(new PutIn());
		
		itemFragile.setOnCheckedChangeListener(new ItemFragile());
		itemHot.setOnCheckedChangeListener(new ItemHot());
		itemFolderable.setOnCheckedChangeListener(new itemFolderable());
		
		itemName.setText("新货物" + Integer.toString(resourceService.gettGoodsInfo().size() + 1));
		itemAmount.setText("0");
		itemDescription.setText("无");
		itemPrice.setText("5");
	}

	class ItemFragile implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if(arg1)
			{
				descriptions[0] = 1;
			}
			else
			{
				descriptions[0] = 0;
			}
			
		}
	}
	

	class ItemHot implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if(arg1)
			{
				descriptions[1] = 1;
			}
			else
			{
				descriptions[1] = 0;
			}
			
		}
	}
	
	class itemFolderable implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if(arg1)
			{
				descriptions[2] = 1;
			}
			else
			{
				descriptions[2] = 0;
			}
			
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
	        			itemPortrait.setImageURI(uri);
	        			try 
	        			{
							bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

							resourceService.setUserPortrait(bitmap);
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
				 Intent intent = new Intent(NewItem.this, FlyingPhoto.class);
				 intent.putExtra("Source", "goods");
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
				
				bitmap = resourceService.getGoodsPortrait();

				if (bitmap == null)
					return;

				itemPortrait.setImageBitmap(bitmap);
				resourceService.setNewPhoto(false);
			}
			
		};
	 
	class PutIn implements OnClickListener{
		@Override
		public void onClick(View v){
			String details;
			
			details = itemDescription.getText().toString();
			
			for (int i = 0; i != 3; i++)
			{
				if (descriptions[i] == 1)
					details += descriptionText[i];
			}
			
			good.setDescription(details);
			good.setName(itemName.getText().toString());
			good.setQuantity((double)Integer.parseInt(itemAmount.getText().toString()));
			good.setShow(true);
			good.setMoney(Double.parseDouble(itemPrice.getText().toString()));			
			if (bitmap != null)
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	  
				good.setAvatar(baos.toByteArray());
			}
			else
			{
				Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.new_item_no_pic);;
				bitmap = Bitmap.createBitmap(  
                                drawable.getIntrinsicWidth(),  
                                drawable.getIntrinsicHeight(),  
                                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);  
				Canvas canvas = new Canvas(bitmap);  

				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
				drawable.draw(canvas);  
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	  
				good.setAvatar(baos.toByteArray());
			}
			
        	boolean result;
        	
        	JSONObject object ;
        	
        	String message;
        	
        	if (getIntent().getStringExtra("Source").equals("New"))
        	{
        		good.setId(0);
        		
        		object = ItemService.start(good, "add");
        		
        		try
        		{
					result = object.getBoolean("result");
					
	        		if (result)
	        		{
	        			JSONObject singleGoodsInfo = object.getJSONObject("goods_info");
	        			good.setId(singleGoodsInfo.getInt("id"));
	        		}
	        		else
	        		{
	        			message = object.getString("message");
	        			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	        		}
	        		
				} 
        		catch (JSONException e)
        		{
					e.printStackTrace();
				}
        	}
        	
        	object = SetAvatarService.start(good.getId().toString(), "goods");
        
        	try
        	{
				result = object.getBoolean("result");
				 if (!result)
		           {
		        	   message = object.getString("message");
		        	   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		           }
			} 
        	catch (JSONException e) 
        	{
				e.printStackTrace();
			}
        	
			resourceService.addGoodsInfo(good);
			
			Intent intent = new Intent(NewItem.this, ItemTrip.class);
			intent.putExtra("Index", resourceService.gettGoodsInfo().size() - 1);
			startActivity(intent);
			NewItem.this.finish();
		}
		
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK: {
			Intent intent = new Intent(NewItem.this, itemDrawer.class);
			startActivity(intent);
			NewItem.this.finish();
			
		}

		}
		return true;

	}
	
}
