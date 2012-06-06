package com.ilift.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.Sitp.ilift.R;

public class SingleShelf extends RelativeLayout{

	int count = 0;
    private int offset;
	
	public SingleShelf(Context context) 
	{
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.single_shelf, this, true);
	}

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

	
	public void AddItem(SingleItem singleItem, int sw, int w, int margin, int width)
	{

		if (count == 0)
		{							
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(sw / 3 - width, margin, 0, 0);
				
				this.addView(singleItem, params);
				
				count ++;
		}
		
		else 
		{
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				if (count % 3 == 0)
				{
					params.addRule(RelativeLayout.BELOW, count - 2);
					params.addRule(RelativeLayout.ALIGN_LEFT, count - 2);
					params.setMargins(0, margin + offset, 0, 0);
				}
				else
				{
					params.setMargins(w, 0, 0, 0);
					params.addRule(RelativeLayout.RIGHT_OF, count);
					params.addRule(RelativeLayout.ALIGN_TOP, count);
				}
				
				this.addView(singleItem, params);
				
				count ++;
				
			}
		}
	}
	
