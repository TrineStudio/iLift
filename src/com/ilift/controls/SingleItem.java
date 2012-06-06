package com.ilift.controls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.Sitp.ilift.R;
import com.ilift.activities.NewItem;
import com.ilift.service.ItemService;
import com.ilift.service.ResourceService;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleItem extends RelativeLayout{

	private ImageView itemPortrait;
	
	private int index;

    private ImageView modify;
    private ImageView delete;

    private Activity activity;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;

        delete.setOnClickListener(new Delete());
        modify.setOnClickListener(new Modify());
	}

	public SingleItem(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.single_item, this, true);
		
		itemPortrait = (ImageView)findViewById(R.id.itemPortrait);

        modify = (ImageView)findViewById(R.id.modify);
        delete = (ImageView)findViewById(R.id.delete);

	}

    public void setActivity(Activity activity){
        this.activity = activity;
    }


    private class Delete implements OnClickListener{

        @Override
        public void onClick(View v)
        {
            ResourceService.gettGoodsInfo().remove(index);

            JSONObject object = new ItemService().start(ResourceService.gettGoodsInfo().get(index), "delete");

            boolean result = false;
            try
            {
                result = object.getBoolean("result");
                if (result)
                {
                    Toast.makeText(activity.getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity.getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }
    }

    private class Modify implements OnClickListener{
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(activity, NewItem.class);

            intent.putExtra("Index", index);

            activity.startActivity(intent);
            activity.finish();


        }
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
