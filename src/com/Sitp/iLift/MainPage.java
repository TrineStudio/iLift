package com.Sitp.iLift;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainPage extends Activity{
	
	private ImageView lift;
	private ImageView good;
	private ImageView shortCut;
	private ImageView around;
	
	private RelativeLayout relativeLayout;
	
	private Animation alphaGrowAnimation;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);
		
		lift = (ImageView)findViewById(R.id.lift);
		good = (ImageView)findViewById(R.id.goods);
		shortCut = (ImageView)findViewById(R.id.shortCut);
		around = (ImageView)findViewById(R.id.around);
		
		relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
		
		alphaGrowAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_grow);
		
		
		lift.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainPage.this, Information.class);
				startActivity(intent);
				MainPage.this.finish();
			}
		});
		
		good.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainPage.this, itemDrawer.class);
				startActivity(intent);
				MainPage.this.finish();
			}
		});
		
		shortCut.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				ArrayList<ShortCutData> shortCutData = new ArrayList<ShortCutData>();
				shortCutData = ShortCutData.ReadData(getApplicationContext());
					
				if (shortCutData == null || shortCutData.size() == 0)
				{
					Toast.makeText(getApplicationContext(), "请先设置一个捷径", Toast.LENGTH_SHORT).show();
					return;
				}
				
				ShortCutBoard shortCutBoard = new ShortCutBoard(MainPage.this);
				
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				relativeLayout.addView(shortCutBoard, layoutParams);
					
				for(int i = 0; i != shortCutData.size(); i++)
				{
					ShortCutData data = shortCutData.get(i);
					
					if (data.getType() != 3)
					{
						ShortCutItem tmp = new ShortCutItem(MainPage.this);
						tmp.setId(i + 1);
						tmp.setAmount(Integer.toString(data.getAmount()));
						tmp.setParent(relativeLayout, shortCutBoard);
						tmp.setStatus(data.getRole());
						tmp.setStartPos(data.getStartPos());
						tmp.setEndPos(data.getEndPos());
						tmp.setDepartTime("15");
						tmp.setGoParams(Calendar.getInstance(), MainPage.this);
						
						if (i == 0)
							shortCutBoard.addItem(tmp, 1);
						else
							shortCutBoard.addItem(tmp, 2);
					}
					else 
					{
						ShortCutGoodsItem tmp = new ShortCutGoodsItem(MainPage.this);
						tmp.setId(i + 1);
						tmp.setAmount(Integer.toString(data.getAmount()));
						tmp.setParent(relativeLayout, shortCutBoard);
						tmp.setPrice(Integer.toString(data.getPrice()));
						tmp.setName(data.getGoodsName());
						tmp.setStatus(data.getRole());
						tmp.setStartPos(data.getStartPos());
						tmp.setEndPos(data.getEndPos());
						tmp.setDepartTime("15");
						//tmp.setGoParams(Calendar.getInstance(), MainPage.this);
						
						if (i == 0)
							shortCutBoard.addItem(tmp, 1);
						else
							shortCutBoard.addItem(tmp, 2);
					}
				}
					
				ImageView tmp = new ImageView(MainPage.this);
					
				RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(40, 80);
					
				layoutParam.addRule(RelativeLayout.RIGHT_OF, shortCutData.size());
				layoutParam.addRule(RelativeLayout.ALIGN_TOP, shortCutData.size());
					
				shortCutBoard.addView(tmp, layoutParam);
				
				shortCutBoard.startAnimation(alphaGrowAnimation);
			}
			
		});
		
		around.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Toast.makeText(getApplicationContext(), "功能还在开发中", Toast.LENGTH_SHORT).show();
			}
			
		});
		
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
			case KeyEvent.KEYCODE_BACK: 
			{
				
				ExitDialog exit = new ExitDialog();
				exit.ShowDialog(this);
				
				LogOutService logOut = new LogOutService();
				logOut.start();
				
				File sd=Environment.getExternalStorageDirectory();
			    String path = sd.getPath()+"/iLiftImages/";
			    
			    File file=new File(path); 
			    
			    if (file.exists())
			    	file.delete();
			    
				return true;
			}
			default:
				super.onKeyDown(KeyCode, event);
				return false;
		}
		

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
			
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		
		return true;
   
	}
	
    /*处理菜单事件*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	
		int item_id=item.getItemId();//得到当前选中MenuItem的ID
	
		switch(item_id)
		{
			case R.id.modifyPersonalInfo:
			{
				Intent intent = new Intent(MainPage.this, detail.class);
				startActivity(intent);
			}
		}
		return true;
	    }

}
