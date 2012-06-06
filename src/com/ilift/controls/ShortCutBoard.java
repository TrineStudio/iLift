package com.ilift.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import com.Sitp.ilift.R;

public class ShortCutBoard extends RelativeLayout{


	
	private int index = 0;
	
	private HorizontalScrollView horizontalScrollView;
	
	
	private RelativeLayout relativeLayout;

    private int marginLeft;

    private int itemWidth;

    public void setMarginLeft(int marginLeft)
    {
        this.marginLeft = marginLeft;
    }

    public void setItemWidth(int itemWidth)
    {
        this.itemWidth = itemWidth;
    }


	
	public ShortCutBoard(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.short_cut, this, true);
		
		
		relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
		
		horizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
		horizontalScrollView.setOnTouchListener(new OnTouch());
		horizontalScrollView.setSmoothScrollingEnabled(true);
		
	}
	
	private class OnTouch implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			synchronized(this)
			{
				switch(event.getAction())
				{
				case MotionEvent.ACTION_UP:
					int currentLocation = horizontalScrollView.getScrollX();
					int index = currentLocation / itemWidth;
					index += ((currentLocation - index * itemWidth) > itemWidth / 2 ? 1 : 0);
					
					horizontalScrollView.smoothScrollTo(index * itemWidth, 0);
					
					return true;
				default:
					return false;
				}
				
			}
		}
		
		
	}
	
	public void addItem(View shortCutItem, int type){
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		
		if (index != 0)
		{
			layoutParams.addRule(RelativeLayout.RIGHT_OF, index);
			layoutParams.addRule(RelativeLayout.ALIGN_TOP, index);
			
		}
		if (type == 1)
		{
			layoutParams.leftMargin = marginLeft;
			
		}
		
		index++;
		
		relativeLayout.addView(shortCutItem, layoutParams);
	}

	public void addView(View view, LayoutParams layoutParams){
		relativeLayout.addView(view, layoutParams);
	}

}
