package com.Sitp.iLift;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InviteInfoDetail extends RelativeLayout{

	private TextView occupation;
	private TextView carInfo;
	private TextView college;
	private TextView travelInfo;
	private TextView userName;
	private TextView departTime;
	
	private Animation scaleAndAlpha;
	
	private Context context;
	
	private int travelID;
	
	private RelativeLayout relativeLayout;
	private RelativeLayout mainView;
	
	private Button accept;
	private Button refuse;
	private Button close;
	private Button invite;
	
	private ImageView portrait;
	
	
	public InviteInfoDetail(Context context) {
		super(context);

		this.context = context;
		
		LayoutInflater.from(context).inflate(R.layout.invite_info_detail, this, true); 
		
		occupation = (TextView)findViewById(R.id.job);
		carInfo = (TextView)findViewById(R.id.privateCarInfo);
		college = (TextView)findViewById(R.id.college);
		travelInfo = (TextView)findViewById(R.id.travelInfo);
		userName = (TextView)findViewById(R.id.userName);
		departTime = (TextView)findViewById(R.id.departTime);
		
		accept = (Button)findViewById(R.id.accept);
		refuse = (Button)findViewById(R.id.refuse);
		invite = (Button)findViewById(R.id.invite);
		close = (Button)findViewById(R.id.close);
		
		portrait = (ImageView)findViewById(R.id.portrait);
		
		mainView = (RelativeLayout)findViewById(R.id.mainView);
		
		scaleAndAlpha = AnimationUtils.loadAnimation(context, R.anim.scale_alpha);
		
		this.setOnTouchListener(new OnTouch());
		
		mainView.startAnimation(scaleAndAlpha);

	}
	
	public void setPortrait(Bitmap bitmap)
	{
		portrait.setImageBitmap(bitmap);
	}
	
	public void setCloseListener(OnClickListener listener)
	{
		close.setOnClickListener(listener);

	}

	
	public void setParent(RelativeLayout relativeLayout)
	{
		this.relativeLayout = relativeLayout;
		
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				InviteInfoDetail.this.relativeLayout.setClickable(true);
				InviteInfoDetail.this.relativeLayout.removeView(InviteInfoDetail.this);
			}
			
		});
	}
	
	
	public class OnTouch implements OnTouchListener
	{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return true;
		}
		
		
	}
	
	public void setInviteListener(OnClickListener listener)
	{
		accept.setVisibility(View.GONE);
		refuse.setVisibility(View.GONE);
		invite.setVisibility(View.VISIBLE);
		
		invite.setOnClickListener(listener);
	}
	
	public void setTravelId(int id, String type) 
	{
		travelID = id;
		if (type.equals("goods"))
		{
			accept.setOnClickListener(new GoodsAccept());
			refuse.setOnClickListener(new GoodsRefuse());
		}
		else
		{
			accept.setOnClickListener(new Accept());
			refuse.setOnClickListener(new Refuse());
		}
		
		if (type.equals("InviteSent"))
		{
			accept.setVisibility(View.GONE);
			refuse.setVisibility(View.GONE);
		}

	
	}
	
	class GoodsAccept implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			JSONObject result;
			result = LogisticsInviteResponse.start(Integer.toString(travelID), "accept");
			try
			{
				boolean isSucceed = result.getBoolean("result");
				if (isSucceed)
				{
					Toast.makeText(context, "成功接受邀请", Toast.LENGTH_SHORT).show();
				}
				else
				{
					String message = result.getString("message");
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	class GoodsRefuse implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			JSONObject result;
			result = LogisticsInviteResponse.start(Integer.toString(travelID), "reject");
			try
			{
				boolean isSucceed = result.getBoolean("result");
				if (isSucceed)
				{
					Toast.makeText(context, "成功接受邀请", Toast.LENGTH_SHORT).show();
				}
				else
				{
					String message = result.getString("message");
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	class Accept implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			JSONObject result;
			
			responseInvitation rInvitation = new responseInvitation();
			result = rInvitation.start(Integer.toString(travelID), "accept");
			try {
				boolean isSucceed = result.getBoolean("result");
				if (isSucceed)
				{
					Toast.makeText(context, "成功接受邀请", Toast.LENGTH_SHORT).show();
				}
				else
				{
					String message = result.getString("message");
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}
				} catch (JSONException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	class Refuse implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			JSONObject result;
			
			responseInvitation rInvitation = new responseInvitation();
			result = rInvitation.start(Integer.toString(travelID), "refuse");
			try {
				
				boolean isSucceed = result.getBoolean("result");
				if (isSucceed)
				{
					Toast.makeText(context, "成功拒绝邀请", Toast.LENGTH_SHORT).show();
				}
				else
				{
					String message = result.getString("message");
					Toast.makeText(context, "发生错误" + message, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setJob(String job)
	{
		if (job.equals("0"))
			occupation.setText("教师");
		else if (job.equals("1"))
			occupation.setText("学生");
			
	}
	
	public void setCarInfo(String carInfo)
	{
		if (carInfo == null)
			this.carInfo.setText("无车");
		
		if (!carInfo.equals("none"))
			this.carInfo.setText(carInfo);
		else
			this.carInfo.setText("无车");
	}
	
	public void setCollege(String college)
	{
		this.college.setText(college);
	}
	
	public void setTravelInfo(String start, String end)
	{
		String temp = "";
		temp = "自" + start + "到" + end + " ";
		
		SpannableString sp = new SpannableString(temp);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 1 + start.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 2 + start.length(), temp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		
		travelInfo.setText(sp);
	}
	
	public void setUserName(String userName)
	{
		this.userName.setText(userName);
	}
	
	public void setDepartTime(String departTime)
	{
		this.departTime.setText(departTime);
	}
	

}
