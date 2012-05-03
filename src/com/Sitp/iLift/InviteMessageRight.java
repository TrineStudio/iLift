package com.Sitp.iLift;

import java.sql.Timestamp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InviteMessageRight extends RelativeLayout
{
	private TextView name;
	private TextView occupation;
	private TextView job;
	private TextView startAndEnd;
	private TextView time;
	
	private Button moreInfo;
	private Button invite;
	
	private ImageView portrait;
	
	public InviteMessageRight(Context context) 
	{
		super(context);
		
		
		LayoutInflater.from(context).inflate(R.layout.status_message_right, this, true); 
		
		name =(TextView)findViewById(R.id.name);
		occupation =(TextView)findViewById(R.id.occupation);
		job =(TextView)findViewById(R.id.job);
		startAndEnd =(TextView)findViewById(R.id.startAndEnd);
		time =(TextView)findViewById(R.id.time);
		
		moreInfo =(Button)findViewById(R.id.moreInfo);
		invite =(Button)findViewById(R.id.invite);		
		
		portrait = (ImageView)findViewById(R.id.portrait);
	}
	
	public void setOccupation(String occupation)
	{
		if (occupation.equals("passenger"))
		{
			this.occupation.setText("�˿� ");
		}
		else
			this.occupation.setText("���� ");
	}
	
	public void setName(String name)
	{
		this.name.setText(name);
	}
	
	public void setJob(String job)
	{
		if (job.equals("0"))
			this.job.setText("��ʦ");
		else if (job.equals("1"))
			this.job.setText("ѧ��");
	}
	
	public void setMoreInfoListener(OnClickListener listener)
	{
		moreInfo.setOnClickListener(listener);
	}
	
	public void setStartAndEnd(String start, String end)
	{
		String temp = "";
		temp = "��" + start + "��" + end + " ";
		
		SpannableString sp = new SpannableString(temp);
		sp.setSpan(new ForegroundColorSpan(Color.GRAY), 0, temp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 1 + start.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 2 + start.length(), temp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		
		
		startAndEnd.setText(sp);
	}
	
	public void setTime(Timestamp time)
	{
		this.time.setText((time.getMonth() + 1) + "��" + time.getDay() + "��");
	}
	
	public void setPortrait(Bitmap bm)
	{
		portrait.setImageBitmap(bm);
	}
	
	public void setInviteListener(OnClickListener listener)
	{
		invite.setOnClickListener(listener);
	}
	
	
}
