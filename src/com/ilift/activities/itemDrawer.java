package com.ilift.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.Sitp.ilift.R;
import com.ilift.controls.SingleItem;
import com.ilift.controls.SingleShelf;
import com.ilift.entity.TGoodsInfo;
import com.ilift.service.ResourceService;

import java.util.ArrayList;
import java.util.List;

public class itemDrawer extends Activity{
	
	private List<TGoodsInfo> tGoodsInfo = new ArrayList<TGoodsInfo>();
	
	private ViewFlipper drawer;

	private RelativeLayout drawerParent;
	
	private Handler handler;
	
	private Button pack;
	
	private SingleItem singleItem;
	
	private int screenHeight;
    private int shelfWidth;
	private int itemWidth = 0;
	private int w;
	private int h;
    private int width;
	
	private Runnable renewView = new Runnable()
	{

		@Override
		public void run() {
			int marginTop = 0;
			
			SingleShelf singleShelf = new SingleShelf(itemDrawer.this);
			
			
			if (screenHeight == 480)
			{
				marginTop = 30;
				itemWidth = 12;
				w = 63;
				h = 84;
                shelfWidth = 261;
                width = 70;

                singleShelf.setOffset(7);
			}
            else if (screenHeight == 854)
            {
                marginTop = 70;
                itemWidth = 24;
                w = 93;
                h = 119;
                shelfWidth = 451;
                width = 90;

                singleShelf.setOffset(14);
            }
			
			
			
			if (tGoodsInfo.size() == 0)
			{				
					singleItem = new SingleItem(itemDrawer.this);				
					
					singleItem.setClickListener(new NewItemClick());
					
					singleShelf.AddItem(singleItem, shelfWidth, itemWidth, marginTop, width);
					
					drawer.addView(singleShelf);
			}
			
			else 
			{
				for(int i = 0; i != tGoodsInfo.size(); i++)
				{
					if (i % 9 == 0 && i != 0)
					{
						drawer.addView(singleShelf);
						singleShelf = new SingleShelf(itemDrawer.this);
					}
					
					singleItem = new SingleItem(itemDrawer.this);

                    try
                    {
					    singleItem.setItemPortrait(BitmapFactory.decodeByteArray(tGoodsInfo.get(i).getAvatar(), 0, tGoodsInfo.get(i).getAvatar().length), w, h);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "获取物品图片失败", Toast.LENGTH_SHORT).show();
                    }

					singleItem.setId(i % 9 + 1);
                    singleItem.setIndex(i);
                    singleItem.setActivity(itemDrawer.this);
					
					singleItem.setClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(itemDrawer.this, ItemTrip.class);

                            intent.putExtra("Index", singleItem.getIndex());

                            startActivity(intent);
                            itemDrawer.this.finish();
                        }
                    });
					
					singleShelf.AddItem(singleItem, shelfWidth, itemWidth, marginTop, width);
					
			   }			
			   if (tGoodsInfo.size() % 9 == 0)
			   {
					drawer.addView(singleShelf);
					singleShelf = new SingleShelf(itemDrawer.this);
			   }
			
			   singleItem = new SingleItem(itemDrawer.this);				
				
			   singleItem.setClickListener(new NewItemClick());
				
			   singleShelf.AddItem(singleItem, shelfWidth, itemWidth, marginTop, width);
			   
			   drawer.addView(singleShelf);
			}
			
		}
		
	};
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK: {
			Intent intent = new Intent(itemDrawer.this, MainPage.class);
			startActivity(intent);
			itemDrawer.this.finish();
			
		}

		}
		return true;

	}
	
	public class DrawerScroll implements OnTouchListener
	{
		private int startX;
		private int endX;
		
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			switch(event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				startX = (int)event.getRawX();
				break;
			case MotionEvent.ACTION_UP:
				endX = (int)event.getRawX();
				if (endX - startX > 0) // Move Right
					drawer.showNext();
				else if (endX - startX < 0) // Move Left
					drawer.showPrevious();
				break;
					
			}
			return true;
		}
		
	}
	
	public class NewItemClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(itemDrawer.this, NewItem.class);
			startActivity(intent);
			itemDrawer.this.finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer);
	
		handler = new Handler();
		
		pack = (Button)findViewById(R.id.pack);
		
		drawerParent = (RelativeLayout)findViewById(R.id.drawerParent);
		
		drawer = (ViewFlipper)findViewById(R.id.drawer);
		
		drawerParent.setOnTouchListener(new DrawerScroll());
		
		pack.setOnClickListener(new Pack());
		
		tGoodsInfo = ResourceService.gettGoodsInfo();
		
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
	    screenHeight = dm.heightPixels;
	    
	    handler.post(renewView);
	   
	    }
	
	public class Back implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(itemDrawer.this, MainPage.class);
			startActivity(intent);
			
			itemDrawer.this.finish();
		}
		
	}
	
	public class Pack implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			 
		}
	}
	
	

}
