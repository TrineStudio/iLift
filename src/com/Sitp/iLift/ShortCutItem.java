package com.Sitp.iLift;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilifting.dao.pojos.TOwnerTravelInfo;
import com.ilifting.dao.pojos.TPassengerTravelInfo;

public class ShortCutItem extends RelativeLayout{

	private TextView message;
	private TextView startPos;
	private TextView endPos;
	private EditText departTime;
	private TextView amount;
	
	private String role;
	
	private Button go;
	private Button cancel;
	
	private Animation alphaDecreaseAnimation;
	
	public ShortCutItem(Context context) {
		
		super(context);
		LayoutInflater.from(context).inflate(R.layout.short_cut_item, this, true);
		
		message = (TextView)findViewById(R.id.message);
		startPos = (TextView)findViewById(R.id.startPos);
		endPos = (TextView)findViewById(R.id.endPos);
		departTime = (EditText)findViewById(R.id.departTime);
		amount = (TextView)findViewById(R.id.amount);
		
		go = (Button)findViewById(R.id.go);
		cancel = (Button)findViewById(R.id.cancel);
		
		
		alphaDecreaseAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_decrease);
		
	}
	
	public void setGoParams(Calendar c, Activity activity)
	{
		go.setOnClickListener(new Go(c, activity));
	}
	
	public class Go implements OnClickListener
	{
		private Calendar c;
		private Activity activity;
		
		public Go(Calendar c, Activity activity)
		{
			this.c = c;
			this.activity = activity;
		}
		
		@Override
		public void onClick(View v)
		{
			
			
			
			String origin;
			String destination;
			
			origin = startPos.getText().toString();
			destination = endPos.getText().toString();

			
			
			int type = -1;
			
			if (role.equals("乘客"))
				type = 1;
			else if (role.equals("车主"))
				type = 2;
			else if (role.equals("物流"))
				type = 0;
			
			
			Timestamp timeStamp;
			timeStamp = new Timestamp(c.getTime().getYear(), c.getTime().getMonth() - 1, c.getTime().getDay(), c.getTime().getHours(), c.getTime().getMinutes(), 0, 0);
			
			JSONObject result = null;
			JSONArray array = null;
			List<TOwnerTravelInfo> driverTravelInfo = new ArrayList<TOwnerTravelInfo>();
			List<TPassengerTravelInfo> passengerTravelInfo = new ArrayList<TPassengerTravelInfo>();
			
			if (type == 1)
			{
				passengerService temp = new passengerService();
				result = temp.start(origin, destination, timeStamp.toString(), departTime.getText().toString());
			}
			else if (type == 2)
			{
				driverService temp = new driverService();
				result = temp.start(origin, destination, timeStamp.toString(), departTime.getText().toString(), "1");
			}
			
			
			if (type == 1)
			{
				if (result != null)
				{
					try 
					{
						array = result.getJSONArray("owners");
					} 
					catch (JSONException e1) 
					{
						e1.printStackTrace();
					}
				}
				
				if (array.length() != 0) 
				{
					for (int i = 0; i != array.length(); i++) {
						try 
						{
							JSONObject object = array.getJSONObject(i);

							TOwnerTravelInfo tot = new TOwnerTravelInfo();
							tot = resourceService.getOwnerTravelInfo(object);

							driverTravelInfo.add(tot);
						} 
						catch (JSONException e) 
						{
							e.printStackTrace();
						}
					}
					
					resourceService.settOwnerTravelInfo(driverTravelInfo);
					
				}

			
			}
			else if (type == 2)
			{
				if (result != null)
				{
					try 
					{
						array = result.getJSONArray("passengers");
					} 
					catch (JSONException e1) 
					{
						e1.printStackTrace();
					}
				}
				if (array.length() != 0) 
				{
					for (int i = 0; i != array.length(); i++) {
						try 
						{
							JSONObject object = array.getJSONObject(i);

							TPassengerTravelInfo tot = new TPassengerTravelInfo();
							tot = resourceService.getPassengerTravelInfo(object);

							passengerTravelInfo.add(tot);
						} 
						catch (JSONException e) 
						{
							e.printStackTrace();
						}
					}
					
					resourceService.settPassengerInfo(passengerTravelInfo);
					
				}
			}
			Intent intent = new Intent(activity, status.class);
			
			activity.startActivity(intent);

			activity.finish();
		}
	}

	
	public void setParent(final RelativeLayout relativeLayout, final ShortCutBoard shortCutBoard){
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				alphaDecreaseAnimation.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationEnd(Animation animation) {
						relativeLayout.removeView(shortCutBoard);
						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
				});
				shortCutBoard.startAnimation(alphaDecreaseAnimation);
			}
		});
		
	}
	
	
	
	public void setStatus(String status){
		
		String textContent = "即将以" + status + "的身份为您展开搜索";
		
		role = status;
		
		SpannableString sp = new SpannableString(textContent);
		sp.setSpan(new ForegroundColorSpan(Color.WHITE), 0, textContent.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 3, 3 + status.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		
		message.setText(sp);
	}
	
	public void setStartPos(String startPos){
		
		this.startPos.setText(startPos);
	}
	
	public void setEndPos(String endPos){
		
		this.endPos.setText(endPos);
		
	}
	
	public void setDepartTime(String departTime){
		
		this.departTime.setText(departTime);
		
	}
	
	public void setAmount(String amount){
		
		this.amount.setText(amount);
		
	}
	
	

}
