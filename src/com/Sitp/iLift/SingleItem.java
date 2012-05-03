package com.Sitp.iLift;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class SingleItem extends RelativeLayout{

	private ImageView itemPortrait;
	
	private int index;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public SingleItem(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.single_item, this, true); 
		
		itemPortrait = (ImageView)findViewById(R.id.itemPortrait);
	}
	
	public void setItemPortrait(Bitmap bm, int w, int h)
	{
		itemPortrait.setImageBitmap(bm);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		
		itemPortrait.setScaleType(ScaleType.FIT_XY);
		itemPortrait.setLayoutParams(params);
	}
	
	public void resetImage()
	{
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(48, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		
		itemPortrait.setLayoutParams(params);
	}
	
	public void setClickListener(OnClickListener listener)
	{
		itemPortrait.setOnClickListener(listener);
	}

}
