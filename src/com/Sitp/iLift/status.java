package com.Sitp.iLift;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.RelativeLayout.LayoutParams;

import com.ilifting.dao.pojos.TInviteInfo;
import com.ilifting.dao.pojos.TLogisticsInfo;
import com.ilifting.dao.pojos.TLogisticsInviteInfo;
import com.ilifting.dao.pojos.TOwnerTravelInfo;
import com.ilifting.dao.pojos.TPassengerTravelInfo;

public class status extends Activity {
	private DisplayMetrics dm;

	private Button ObjectBar;

	
	private ViewFlipper statusFlipper;
	
	private ImageView objectButton;
	private ImageView inviteButton;
	
	private Handler handler;
	
	
	private Animation scaleAndAlpha;
	
	private RelativeLayout stateLayout;
	
	private RelativeLayout inviteReceived;
	private RelativeLayout inviteSent;
	private RelativeLayout statusLayout;	
	
	private String userName;
	private String source;
	
	private List<TInviteInfo> tInviteSentInfo = new ArrayList<TInviteInfo>();
	private List<TInviteInfo> tInviteReceivedInfo = new ArrayList<TInviteInfo>();
	private List<TPassengerTravelInfo> passengerTravelInfo = new ArrayList<TPassengerTravelInfo>();
	private List<TOwnerTravelInfo> ownerTravelInfo = new ArrayList<TOwnerTravelInfo>();
	
	private List<TLogisticsInviteInfo> tLogisticsInviteSent = new ArrayList<TLogisticsInviteInfo>();
	private List<TLogisticsInviteInfo> tLogisticsInviteReceived = new ArrayList<TLogisticsInviteInfo>();

	private int inviteSendCount = 0;
	private int inviteReceivedCount = 0;
	

	private TInviteInfo temp;
	private TLogisticsInviteInfo logisticsInviteTemp;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		objectButton = (ImageView)findViewById(R.id.statusImage);
		inviteButton = (ImageView)findViewById(R.id.inviteImage);
		
		objectButton.setOnClickListener(new ObjectButton());
		inviteButton.setOnClickListener(new InvitationButton());
		
		scaleAndAlpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_alpha);
		
		stateLayout = (RelativeLayout)findViewById(R.id.state);
		inviteReceived = (RelativeLayout)findViewById(R.id.inviteReceived);
		inviteSent = (RelativeLayout)findViewById(R.id.inviteSent);
		statusLayout = (RelativeLayout)findViewById(R.id.status);
		
		statusFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		ObjectBar = (Button) findViewById(R.id.selectBar);
		ObjectBar.setOnTouchListener(new SelectBarOnTouch(ObjectBar));

		ownerTravelInfo = resourceService.gettOwnerTravelInfo();
		passengerTravelInfo = resourceService.gettPassengerInfo();
		tInviteSentInfo = resourceService.gettInviteSentInfo();
		tInviteReceivedInfo = resourceService.gettInviteReceivedInfo();
		tLogisticsInviteSent = resourceService.gettLogisticsInviteSent();
		tLogisticsInviteReceived = resourceService.gettLogisticsInviteReceived();
		
		userName = resourceService.getUserInfo().getUsername();
		
		handler = new Handler();
		
		source = getIntent().getStringExtra("Source");
		
		handler.post(runnable);
		
		
			
		
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK: {
			Intent intent = new Intent(status.this, MainPage.class);
			startActivity(intent);
			status.this.finish();
			
		}

		}
		return true;

	}
	
	class ObjectButton implements OnClickListener{
		@Override
		public void onClick(View v){
			statusFlipper.showNext();
		}
	}
	
	class InvitationButton implements OnClickListener{
		@Override
		public void onClick(View v){
			statusFlipper.showPrevious();
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
				Log.d("iLift", Float.toString((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2)));
				if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) >= 2.0f && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) < 84.0f)
				{
					Intent intent = new Intent(status.this, Information.class);
					
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 16.0f - (float)selectBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (1.0f * Math.abs((16.0f - (float)selectBar.getLeft()))));
					translateAnimation.setFillEnabled(true);
					
					translateAnimation.setAnimationListener(new TranslateListener(16 + selectBar.getWidth(), intent, null, selectBar, status.this));
					
					selectBar.startAnimation(translateAnimation);
					
				} 
				else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) >= 84.0f && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) <= 160.0f)
				{
					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 92.0f - (float)selectBar.getLeft(), 0.0f, 0.0f);
					translateAnimation.setDuration((long) (1.0f * Math.abs((92.0f - (float)selectBar.getLeft()))));
					translateAnimation.setFillEnabled(true);
					
					Intent intent = new Intent(status.this, Setting.class);
					
					translateAnimation.setAnimationListener(new TranslateListener(92 + selectBar.getWidth(), intent, null, selectBar, status.this));
					
					selectBar.startAnimation(translateAnimation);
					
				} 
//				else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > 238.0f) 
//				{
//					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 244.0f - (float)selectBar.getLeft(), 0.0f, 0.0f);
//					translateAnimation.setDuration((long) (1.0f * Math.abs((244.0f - (float)selectBar.getLeft()))));
//					translateAnimation.setFillEnabled(true);
//					
//					translateAnimation.setAnimationListener(new TranslateListener(244 + selectBar.getWidth(), null, null, selectBar, status.this));
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
					
					translateAnimation.setAnimationListener(new TranslateListener(selectInitialLeft + selectBar.getWidth(), null, null, selectBar, status.this));
					
					selectBar.startAnimation(translateAnimation);
				}

				selectBarLeft = selectInitialLeft;
				break;
			}
			return true;
		}

	}
	
	public void ClearState()
	{
		stateLayout.removeAllViews();
	}
	
	public void Refresh()
	{
		ClearState();
		handler.post(runnable);
	}

	public String getTime(Timestamp time){
		return (time.getHours() + " : " + time.getMinutes());
	}
	
	private Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			// Statue
			
			int count = 0;
			
			if (passengerTravelInfo.size() != 0) {
				for (int i = 0; i != passengerTravelInfo.size(); i++, count++)
				{
					final TPassengerTravelInfo temp = passengerTravelInfo.get(i);
					final int index = i;
					
					if (count % 2 == 0)
					{
						InviteMessageLeft singleMessage = new InviteMessageLeft(status.this);
						if (source.equals("user"))
							singleMessage.setInviteListener(new OnInvite(temp.getPassenger().getId(), resourceService.getOwnerTravelInfo().getId(), temp.getId(), "owner", i));
						else if (source.equals("goods"))
							singleMessage.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getPassenger().getId(), "passenger", temp.getId()));
						singleMessage.setName(temp.getPassenger().getName());
						singleMessage.setTime(temp.getStartTime());
						singleMessage.setStartAndEnd(temp.getOrigin(), temp.getDestination());
						singleMessage.setOccupation("passenger");
						singleMessage.setJob(temp.getPassenger().getJob());
						singleMessage.setId(count + 1);
						
						
						
						singleMessage.setMoreInfoListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
								inviteInfoDetail.setUserName(temp.getPassenger().getName());
								inviteInfoDetail.setTravelInfo(temp.getOrigin(), temp.getDestination());
								inviteInfoDetail.setDepartTime(getTime(temp.getStartTime()));
								inviteInfoDetail.setCollege(temp.getPassenger().getCollege());
								inviteInfoDetail.setParent(stateLayout);
								inviteInfoDetail.setDepartTime((temp.getStartTime().getMonth() + 1) + "月" + temp.getStartTime().getDay() + "日 " + temp.getStartTime().getHours() + "点" + temp.getStartTime().getMinutes() + "分"); 
								inviteInfoDetail.setCarInfo(temp.getPassenger().getCarModel());
								inviteInfoDetail.setPortrait(GetAvatarService.start(temp.getPassenger().getId().toString(), "user", userName, true));
								if (source.equals("user"))
									inviteInfoDetail.setInviteListener(new OnInvite(temp.getPassenger().getId(), resourceService.getOwnerTravelInfo().getId(), temp.getId(), "owner", index));
								else if (source.equals("goods"))
									inviteInfoDetail.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getPassenger().getId(), "passenger", temp.getId()));
								stateLayout.addView(inviteInfoDetail);
								
								
							}
							
						}
						);
						
						
						LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
						if (count != 0)
							layoutParams.addRule(RelativeLayout.BELOW, count);
						
						stateLayout.addView(singleMessage, layoutParams);
					}
					else
					{
						InviteMessageRight singleMessage = new InviteMessageRight(status.this);
						if (source.equals("user"))
							singleMessage.setInviteListener(new OnInvite(temp.getPassenger().getId(), resourceService.getOwnerTravelInfo().getId(), temp.getId(), "owner", i));
						else if (source.equals("goods"))
							singleMessage.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getPassenger().getId(), "passenger", temp.getId()));
						singleMessage.setName(temp.getPassenger().getName());
						singleMessage.setTime(temp.getStartTime());
						singleMessage.setStartAndEnd(temp.getOrigin(), temp.getDestination());
						singleMessage.setOccupation("passenger");
						singleMessage.setJob(temp.getPassenger().getJob());
						singleMessage.setId(count + 1);
						
						singleMessage.setMoreInfoListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
								inviteInfoDetail.setUserName(temp.getPassenger().getName());
								inviteInfoDetail.setTravelInfo(temp.getOrigin(), temp.getDestination());
								inviteInfoDetail.setDepartTime(getTime(temp.getStartTime()));
								inviteInfoDetail.setCollege(temp.getPassenger().getCollege());
								inviteInfoDetail.setParent(stateLayout);
								inviteInfoDetail.setDepartTime((temp.getStartTime().getMonth() + 1) + "月" + temp.getStartTime().getDay() + "日 " + temp.getStartTime().getHours() + "点" + temp.getStartTime().getMinutes() + "分");
								inviteInfoDetail.setCarInfo(temp.getPassenger().getCarModel());
								inviteInfoDetail.setPortrait(GetAvatarService.start(temp.getPassenger().getId().toString(), "user", userName, true));
								if (source.equals("user"))
									inviteInfoDetail.setInviteListener(new OnInvite(temp.getPassenger().getId(), resourceService.getOwnerTravelInfo().getId(), temp.getId(), "owner", index));
								else
									inviteInfoDetail.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getPassenger().getId(), "passenger", temp.getId()));
								stateLayout.addView(inviteInfoDetail);
								
								
							}
							
						}
						);
						
						LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
						layoutParams.addRule(RelativeLayout.BELOW, count);
						
						stateLayout.addView(singleMessage, layoutParams);
					}
					
					
				}
			} 
			if (ownerTravelInfo.size() != 0) {
				for (int i = 0; i != ownerTravelInfo.size(); i++, count++) {
					final TOwnerTravelInfo temp = ownerTravelInfo.get(i);
					final int index = i;
					
					if (count % 2 == 0)
					{
						InviteMessageLeft singleMessage = new InviteMessageLeft(status.this);
						if (source.equals("user"))
							singleMessage.setInviteListener(new OnInvite(temp.getOwner().getId(), resourceService.getPassengerTravelInfo().getId(), temp.getId(), "passenger", i));
						else if (source.equals("goods"))
							singleMessage.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getOwner().getId(), "owner", temp.getId()));
						singleMessage.setName(temp.getOwner().getName());
						singleMessage.setTime(temp.getStartTime());
						singleMessage.setStartAndEnd(temp.getOrigin(), temp.getDestination());
						singleMessage.setOccupation("owner");
						singleMessage.setJob(temp.getOwner().getJob());
						singleMessage.setId(count + 1);
						
					
						
						singleMessage.setMoreInfoListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
								inviteInfoDetail.setUserName(temp.getOwner().getName());
								inviteInfoDetail.setTravelInfo(temp.getOrigin(), temp.getDestination());
								inviteInfoDetail.setDepartTime(getTime(temp.getStartTime()));
								inviteInfoDetail.setCollege(temp.getOwner().getCollege());
								inviteInfoDetail.setParent(stateLayout);
								inviteInfoDetail.setDepartTime((temp.getStartTime().getMonth() + 1) + "月" + temp.getStartTime().getDay() + "日 " + temp.getStartTime().getHours() + "点" + temp.getStartTime().getMinutes() + "分");
								inviteInfoDetail.setJob(temp.getOwner().getJob());
								inviteInfoDetail.setCarInfo(temp.getOwner().getCarModel());
								inviteInfoDetail.setPortrait(GetAvatarService.start(temp.getOwner().getId().toString(), "user", userName, true));
								if (source.equals("user"))
									inviteInfoDetail.setInviteListener(new OnInvite(temp.getOwner().getId(), resourceService.getOwnerTravelInfo().getId(), temp.getId(), "passenger", index));
								else if (source.equals("goods"))
									inviteInfoDetail.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getOwner().getId(), "owner", temp.getId()));
								stateLayout.addView(inviteInfoDetail);
								
								
							}
							
						}
						);
						
						LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
						if (count != 0)
							layoutParams.addRule(RelativeLayout.BELOW, count);
						
						stateLayout.addView(singleMessage, layoutParams);
					}
					else
					{
						InviteMessageRight singleMessage = new InviteMessageRight(status.this);
						if (source.equals("user"))
							singleMessage.setInviteListener(new OnInvite(temp.getOwner().getId(), resourceService.getPassengerTravelInfo().getId(), temp.getId(), "passenger", i));
						else if (source.equals("goods"))
							singleMessage.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getOwner().getId(), "owner", temp.getId()));
						singleMessage.setName(temp.getOwner().getName());
						singleMessage.setTime(temp.getStartTime());
						singleMessage.setStartAndEnd(temp.getOrigin(), temp.getDestination());
						singleMessage.setOccupation("owner");
						singleMessage.setJob(temp.getOwner().getJob());
						singleMessage.setId(count + 1);
						
						
						singleMessage.setMoreInfoListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
								inviteInfoDetail.setUserName(temp.getOwner().getName());
								inviteInfoDetail.setTravelInfo(temp.getOrigin(), temp.getDestination());
								inviteInfoDetail.setDepartTime(getTime(temp.getStartTime()));
								inviteInfoDetail.setCollege(temp.getOwner().getCollege());
								inviteInfoDetail.setParent(stateLayout);
								inviteInfoDetail.setDepartTime((temp.getStartTime().getMonth() + 1) + "月" + temp.getStartTime().getDay() + "日 " + temp.getStartTime().getHours() + "点" + temp.getStartTime().getMinutes() + "分");
								inviteInfoDetail.setCarInfo(temp.getOwner().getCarModel());
								inviteInfoDetail.setPortrait(GetAvatarService.start(temp.getOwner().getId().toString(), "user", userName, true));
								if (source.equals("user"))
									inviteInfoDetail.setInviteListener(new OnInvite(temp.getOwner().getId(), resourceService.getOwnerTravelInfo().getId(), temp.getId(), "passenger", index));
								else if (source.equals("goods"))
									inviteInfoDetail.setInviteListener(new LogisticsOnInvite(resourceService.getLogisticsInfo().getId(), temp.getOwner().getId(), "owner", temp.getId()));
								
								stateLayout.addView(inviteInfoDetail);
							}
							
						}
						);
						
						LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
						layoutParams.addRule(RelativeLayout.BELOW, count);
						
						stateLayout.addView(singleMessage, layoutParams);
					}
				}
			}
			
			// Invite 
			
			if (tInviteSentInfo.size() != 0)
				addInviteView(tInviteSentInfo, inviteSent, "InviteSent");
		
			if (tInviteReceivedInfo.size() != 0)
				addInviteView(tInviteReceivedInfo, inviteReceived, "InviteReceived");
			
			if (tLogisticsInviteSent.size() != 0)
				addLogisticsInviteView(tLogisticsInviteSent, inviteSent, "inviteSent");
			
			if (tLogisticsInviteReceived.size() != 0)
				addLogisticsInviteView(tLogisticsInviteReceived, inviteReceived, "inviteReceived");
		}
		
	};
	
	private class LogisticsOnInvite implements OnClickListener
	{
		private int logisticsId;
		private int senderId;
		private String senderType;
		private int travelId;
		
		LogisticsOnInvite(int logisticsId, int senderId, String senderType, int travelId)
		{
			this.logisticsId = logisticsId;
			this.senderId = senderId;
			this.senderType = senderType;
			this.travelId = travelId;
		}
		
		public void onClick(View v)
		{
			JSONObject object;
			
			object = LogisticsInvite.start(Integer.toString(logisticsId), Integer.toString(senderId), senderType, Integer.toString(travelId));
			
			try
			{
				boolean result = object.getBoolean("result");
				
				if (!result)
				{
					String message = object.getString("message");
					Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				}
				else
				{
					JSONObject logisticsInfoObject = object.getJSONObject("logistics_invite_info");
					
					TLogisticsInfo temp = resourceService.getLogisticsInfo(logisticsInfoObject);
					
					resourceService.gettLogisticsInfo().add(temp);
					
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class OnInvite implements OnClickListener
	{
	
		private int inviteID;
		private int driverID;
		private int passengerID;
		private String identity;
		
		private int index;
		
		public OnInvite(int inviteID, int driverID, int passengerID, String identity, int index)
		{
			this.inviteID = inviteID;
			this.driverID = driverID;
			this.passengerID = passengerID;
			this.identity = identity;
			this.index = index;
		}
		
		@Override
		public void onClick(View v) 
		{
			JSONObject result;
			
			inviteService iService = new inviteService();
			result = iService.start(Integer.toString(inviteID), Integer.toString(driverID), Integer.toString(passengerID), identity);
			try {
				
				boolean isSucceed = result.getBoolean("result");
				if (isSucceed)
				{
					Toast.makeText(getApplicationContext(), "成功邀请他人，请等待回应", Toast.LENGTH_SHORT).show();
					if (identity.equals("passenger"))
					{
						resourceService.RemoveOwnerTravelItem(index);
						status.this.Refresh();
					}
				}
				else
				{
					String message = result.getString("message");
					Toast.makeText(getApplicationContext(), "发生错误" + message, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addInviteView(List<TInviteInfo> inviteInfo, RelativeLayout layout, final String type)
	{
		for (int i = 0; i != inviteInfo.size(); i++) 
		{
			if (type.equals("InviteSent"))
				inviteSendCount++;
			else if(type.equals("InviteReceived"))
				inviteReceivedCount++;
			
			temp = inviteInfo.get(i);
			
			SingleInviteInfo singleInviteInfo = new SingleInviteInfo(status.this);
			singleInviteInfo.setId(i + 1);
			
			if (userName.equals(temp.getOwner().getUsername()))
			{
				Timestamp timeStamp = temp.getTPassengerTravelInfo().getStartTime();
				
				singleInviteInfo.setTravelInfo(temp.getTPassengerTravelInfo().getOrigin(), temp.getTPassengerTravelInfo().getDestination());
				singleInviteInfo.setUserName(temp.getPassenger().getName());
				singleInviteInfo.setDepartTime((timeStamp.getMonth() + 1) + "月" + timeStamp.getDay() + "日" + timeStamp.getHours() + "点" + timeStamp.getMinutes()+ "分");
				
				singleInviteInfo.setMoreInfoListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
						inviteInfoDetail.setUserName(temp.getPassenger().getUsername());
						inviteInfoDetail.setTravelInfo(temp.getTPassengerTravelInfo().getOrigin(), temp.getTPassengerTravelInfo().getDestination());
						
						Timestamp timeStamp = temp.getTPassengerTravelInfo().getStartTime();
						
						inviteInfoDetail.setDepartTime(timeStamp.getYear() + "-" + (timeStamp.getMonth() + 1) + "-" + timeStamp.getDay() + " " + timeStamp.getHours() + ":" + timeStamp.getMinutes());
						inviteInfoDetail.setCollege(temp.getPassenger().getCollege());
						inviteInfoDetail.setJob(temp.getPassenger().getJob());
						inviteInfoDetail.setCarInfo(temp.getPassenger().getCarModel());
						inviteInfoDetail.setTravelId(temp.getId(), type);
						inviteInfoDetail.setParent(statusLayout);
						inviteInfoDetail.setPortrait(GetAvatarService.start(temp.getPassenger().getId().toString(), "user", userName, true));
						
						
						statusLayout.addView(inviteInfoDetail);
						inviteInfoDetail.startAnimation(scaleAndAlpha);
						
					}
					
				}
				);
				
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				if (i != 0)
				{
					params.addRule(RelativeLayout.BELOW, i);
				}
				
				layout.addView(singleInviteInfo, params);
				params.topMargin = 5;
				
			}
			else
			{
				Timestamp timeStamp = temp.getTOwnerTravelInfo().getStartTime();
				
				singleInviteInfo.setTravelInfo(temp.getTOwnerTravelInfo().getOrigin(), temp.getTOwnerTravelInfo().getDestination());
				singleInviteInfo.setUserName(temp.getOwner().getName());
				singleInviteInfo.setDepartTime((timeStamp.getMonth() + 1) + "月" + timeStamp.getDay() + "日" + timeStamp.getHours() + "点" + timeStamp.getMinutes()+ "分");

				
				singleInviteInfo.setMoreInfoListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
						inviteInfoDetail.setUserName(temp.getOwner().getUsername());
						inviteInfoDetail.setTravelInfo(temp.getTOwnerTravelInfo().getOrigin(), temp.getTOwnerTravelInfo().getDestination());
						Timestamp timeStamp = temp.getTPassengerTravelInfo().getStartTime();
						
						inviteInfoDetail.setDepartTime(timeStamp.getYear() + "-" + (timeStamp.getMonth() + 1) + "-" + timeStamp.getDay() + " " + timeStamp.getHours() + ":" + timeStamp.getMinutes());
						inviteInfoDetail.setCollege(temp.getOwner().getCollege());
						inviteInfoDetail.setJob(temp.getOwner().getJob());
						inviteInfoDetail.setCarInfo(temp.getOwner().getCarModel());
						inviteInfoDetail.setTravelId(temp.getId(), type);
						inviteInfoDetail.setParent(statusLayout);
						inviteInfoDetail.setPortrait(GetAvatarService.start(temp.getOwner().getId().toString(), "user", userName, true));
						
						statusLayout.addView(inviteInfoDetail);
						inviteInfoDetail.startAnimation(scaleAndAlpha);
					}
					
				}
				);
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				if (i != 0)
				{
					params.addRule(RelativeLayout.BELOW, i);
					params.topMargin = 5;
				}
				
				layout.addView(singleInviteInfo, params);
			}
		}
	}

	public void addLogisticsInviteView(List<TLogisticsInviteInfo> logisticsInviteInfo, RelativeLayout layout, String type)
	{
		for (int i = 0; i != logisticsInviteInfo.size(); i++) 
		{
			if (type.equals("InviteSent"))
				inviteSendCount++;
			else if(type.equals("InviteReceived"))
				inviteReceivedCount++;
			
			logisticsInviteTemp = logisticsInviteInfo.get(i);
			
			SingleInviteInfo singleInviteInfo = new SingleInviteInfo(status.this);
			singleInviteInfo.setId(i + 1);
			
			if (userName.equals(logisticsInviteTemp.getSender().getName()))			// Current user is the sender
			{
				Timestamp timeStamp = logisticsInviteTemp.getTime();
				
				singleInviteInfo.setTravelInfo(logisticsInviteTemp.getTLogisticsInfo().getOrigin(), logisticsInviteTemp.getTLogisticsInfo().getDestination());
				singleInviteInfo.setUserName(logisticsInviteTemp.getUser().getName());
				singleInviteInfo.setDepartTime((timeStamp.getMonth() + 1) + "月" + timeStamp.getDay() + "日" + timeStamp.getHours() + "点" + timeStamp.getMinutes()+ "分");
				
				singleInviteInfo.setMoreInfoListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
						inviteInfoDetail.setUserName(logisticsInviteTemp.getUser().getUsername());
						inviteInfoDetail.setTravelInfo(logisticsInviteTemp.getTLogisticsInfo().getOrigin(), logisticsInviteTemp.getTLogisticsInfo().getDestination());
						
						Timestamp timeStamp = logisticsInviteTemp.getTime();
						
						inviteInfoDetail.setDepartTime(timeStamp.getYear() + "-" + (timeStamp.getMonth() + 1) + "-" + timeStamp.getDay() + " " + timeStamp.getHours() + ":" + timeStamp.getMinutes());
						inviteInfoDetail.setCollege(logisticsInviteTemp.getUser().getCollege());
						inviteInfoDetail.setJob(logisticsInviteTemp.getUser().getJob());
						inviteInfoDetail.setCarInfo(logisticsInviteTemp.getUser().getCarModel());
						inviteInfoDetail.setTravelId(logisticsInviteTemp.getTravelId(), "goods");
						inviteInfoDetail.setParent(statusLayout);
						
						statusLayout.addView(inviteInfoDetail);
						inviteInfoDetail.startAnimation(scaleAndAlpha);
						
					}
					
				}
				);
				
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				if (inviteSendCount != 0)
				{
					params.addRule(RelativeLayout.BELOW, inviteSendCount);
				}
				
				layout.addView(singleInviteInfo, params);
				params.topMargin = 5;
				
			}
			else																// Current user is the receiver
			{
				Timestamp timeStamp = logisticsInviteTemp.getTime();
				
				singleInviteInfo.setTravelInfo(logisticsInviteTemp.getTLogisticsInfo().getOrigin(), logisticsInviteTemp.getTLogisticsInfo().getDestination());
				singleInviteInfo.setUserName(logisticsInviteTemp.getSender().getName());
				singleInviteInfo.setDepartTime((timeStamp.getMonth() + 1) + "月" + timeStamp.getDay() + "日" + timeStamp.getHours() + "点" + timeStamp.getMinutes()+ "分");
				
				singleInviteInfo.setMoreInfoListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						InviteInfoDetail inviteInfoDetail = new InviteInfoDetail(status.this);
						inviteInfoDetail.setUserName(logisticsInviteTemp.getSender().getUsername());
						inviteInfoDetail.setTravelInfo(logisticsInviteTemp.getTLogisticsInfo().getOrigin(), logisticsInviteTemp.getTLogisticsInfo().getDestination());
						
						Timestamp timeStamp = logisticsInviteTemp.getTime();
						
						inviteInfoDetail.setDepartTime(timeStamp.getYear() + "-" + (timeStamp.getMonth() + 1) + "-" + timeStamp.getDay() + " " + timeStamp.getHours() + ":" + timeStamp.getMinutes());
						inviteInfoDetail.setCollege(logisticsInviteTemp.getSender().getCollege());
						inviteInfoDetail.setJob(logisticsInviteTemp.getSender().getJob());
						inviteInfoDetail.setCarInfo(logisticsInviteTemp.getSender().getCarModel());
						inviteInfoDetail.setTravelId(logisticsInviteTemp.getTravelId(), "goods");
						inviteInfoDetail.setParent(statusLayout);
						
						statusLayout.addView(inviteInfoDetail);
						inviteInfoDetail.startAnimation(scaleAndAlpha);
						
					}
					
				}
				);
				
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				if (inviteReceivedCount != 0)
				{
					params.addRule(RelativeLayout.BELOW, inviteReceivedCount);
				}
				
				layout.addView(singleInviteInfo, params);
				params.topMargin = 5;
			}
		}
	}
	
	class DeleteFromParent implements OnClickListener
	{
		private RelativeLayout view;
		
		public DeleteFromParent(RelativeLayout view)
		{
			this.view = view;
		}
		
		@Override
		public void onClick(View v)
		{
			statusLayout.removeView(view);
		}
	}
}
