package com.ilift.controls;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.Sitp.ilift.R;

public class SingleInviteInfo extends RelativeLayout{

	private TextView userName;
	private TextView travelInfo;
	private TextView departTime;
	
	private ImageView statusImage;
	
	
	public SingleInviteInfo(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.single_invite_info, this, true);
		
		userName = (TextView)findViewById(R.id.userName);
		travelInfo = (TextView)findViewById(R.id.travelInfo);
		departTime = (TextView)findViewById(R.id.departTime);
		
		statusImage = (ImageView)findViewById(R.id.statusImage);
		
	}
	
	public void setUserName(String  userName)
	{
		this.userName.setText(userName);
	}
	
	public void setStatus(int type)
	{
		switch(type)
		{
		case 1:	// driver
			statusImage.setImageDrawable(getResources().getDrawable(R.drawable.driver_icon));
			break;
		case 2: // passenger
			statusImage.setImageDrawable(getResources().getDrawable(R.drawable.passenger_icon));
			break;
		case 3: // goods
			statusImage.setImageDrawable(getResources().getDrawable(R.drawable.goods_icon));
			break;
		}
	}
	
	public void setTravelInfo(String start, String end)
	{
		String info = "自";
		info += start + "到" + end + " ";
		
		SpannableString sp = new SpannableString(info);
		
		sp.setSpan(new ForegroundColorSpan(Color.GRAY), 0, info.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 1 + start.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 2 + start.length(), info.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		travelInfo.setText(sp);
	}

    public void setDepartTime(String departTime)
    {
        this.departTime.setText(departTime);
    }

    public void setMoreInfoListener(OnClickListener listener)
    {
        this.setOnClickListener(listener);
	}
	

}
