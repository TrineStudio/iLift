package com.Sitp.iLift;

import android.content.Context;
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

public class ShortCutGoodsItem extends RelativeLayout{

	private TextView message;
	private TextView startPos;
	private TextView endPos;
	private TextView amount;
	private TextView price;
	private TextView name;
	
	private EditText departTime;
	
	
	
	private Button go;
	private Button cancel;
	
	private Animation alphaDecreaseAnimation;
	
	
	public ShortCutGoodsItem(Context context) {
		
		super(context);
		LayoutInflater.from(context).inflate(R.layout.short_cut_goods_item, this, true);
		
		message = (TextView)findViewById(R.id.message);
		startPos = (TextView)findViewById(R.id.startPos);
		endPos = (TextView)findViewById(R.id.endPos);
		price = (TextView)findViewById(R.id.price);
		name = (TextView)findViewById(R.id.name);
		amount = (TextView)findViewById(R.id.amount);
		
		departTime = (EditText)findViewById(R.id.departTime);
		
		go = (Button)findViewById(R.id.go);
		cancel = (Button)findViewById(R.id.cancel);
		
		alphaDecreaseAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_decrease);
		
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
	
	public void setName(String name){
		this.name.setText(name);
	}
	
	public void setPrice(String price){
		this.price.setText(price);
	}
	
	public void setStatus(String status){
		
		String textContent = "即将以" + status + "的身份为您展开搜索";
		
		
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
