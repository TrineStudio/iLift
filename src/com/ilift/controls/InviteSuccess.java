package com.ilift.controls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Sitp.ilift.R;

public class InviteSuccess extends RelativeLayout{

	private TextView text;
	
	private Button phone;
	private Button close;
	
	private ImageView portrait;
	
	private String phoneNumber;
	
	private RelativeLayout relativeLayout;

    private Activity activity;
	
	public InviteSuccess(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.invite_success, this, true);
		
		text = (TextView)findViewById(R.id.text);
		
		portrait = (ImageView)findViewById(R.id.portrait);
		
		phone = (Button)findViewById(R.id.phoneButton);
		close = (Button)findViewById(R.id.close);
		
	}

    public void setActivity(Activity activity){
        this.activity = activity;
    }

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber.toString();
		
		phone.setOnClickListener(new PhoneClick());
	}
	
	public void setUserName(String userName)
	{
		String hints = "" + userName + "用户已经接受您的搭车邀请，您可以点击按钮和对方联系~";
		
		SpannableString sp = new SpannableString(hints);
		sp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		
		text.setText(sp);
	}
	
	public void setPortrait(Bitmap image)
	{
		portrait.setImageBitmap(image);
	}
	
	public class PhoneClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNumber));
            activity.startActivity(intent);
		}
		
	}
	
	
	public void setParent(RelativeLayout relativeLayout)
	{
		this.relativeLayout = relativeLayout;
		
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				InviteSuccess.this.relativeLayout.setClickable(true);
				InviteSuccess.this.relativeLayout.removeView(InviteSuccess.this);
			}
			
		});
	}
	
	
	

}
