package com.ilift.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.Sitp.ilift.R;
import com.ilift.controls.ShortCutData;
import com.ilift.entity.TGoodsInfo;
import com.ilift.entity.TLogisticsInfo;
import com.ilift.entity.TOwnerTravelInfo;
import com.ilift.entity.TPassengerTravelInfo;
import com.ilift.service.ItemTripService;
import com.ilift.service.ResourceService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ItemTrip extends Activity{
	
	private Button go;
	private Button saveShortCut;
	
	private TextView setData;
	private TextView setTime;
	
	private EditText timeFlow;

	private int[] choice_select = { 0, 0 };
	
	private String[] choice = new String[] { "同济大学本部", "同济大学嘉定校区" };
	private String[] accurateChoise = new String[]{"月 ", "日 ", "时 ", "分 "};
	
	private Spinner startPos;
	private Spinner endPos;
	private Spinner accuracy;
	
	private Calendar c;

	String userName = null;
	
	private DisplayMetrics dm;

	private int index;
	
	private int month;
	private int year;
	private int day;
	private int hour;
	private int minute;
	
	private final int DATA_PICKER_ID = 1;
	private final int TIME_PICKER_ID = 2;
	
	private List<TPassengerTravelInfo> passengerTravelInfo = new ArrayList<TPassengerTravelInfo>();
	private List<TOwnerTravelInfo> driverTravelInfo = new ArrayList<TOwnerTravelInfo>();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_trip);
		
		index = getIntent().getIntExtra("Index", -1);
		
		if (index == -1)
		{
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
		}
		
		c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);  
        month = c.get(Calendar.MONTH);  
        day = c.get(Calendar.DAY_OF_MONTH);  
        hour = c.get(Calendar.HOUR_OF_DAY);  
        minute = c.get(Calendar.MINUTE);  
        
		dm = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(dm);

		setData = (TextView)findViewById(R.id.setDate);
		setTime = (TextView)findViewById(R.id.setTime);
		
		accuracy = (Spinner)findViewById(R.id.accurate);
		
		timeFlow = (EditText)findViewById(R.id.flowTime);
		
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, accurateChoise);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accuracy.setAdapter(spinnerAdapter);
		accuracy.setOnItemSelectedListener(new AccuracyChanged());
		
		setData.setOnClickListener(new SetData());
		setTime.setOnClickListener(new SetTime());
		
		
		startPos = (Spinner) findViewById(R.id.startPos);
		endPos = (Spinner) findViewById(R.id.endPos);
		
		go = (Button) findViewById(R.id.go);
		saveShortCut = (Button)findViewById(R.id.saveShortCut);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, choice);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		startPos.setAdapter(adapter);
		endPos.setAdapter(adapter);

		startPos.setOnItemSelectedListener(new StartSet());
		endPos.setOnItemSelectedListener(new EndSet());

		go.setOnClickListener(new Go());
		saveShortCut.setOnClickListener(new SaveShortCut());
		
		setData.setText(year + "-" + month + "-" + day);
		setTime.setText(hour + "-" + minute);
		
		timeFlow.setText("0");
		
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK: {
			Intent intent = new Intent(ItemTrip.this, itemDrawer.class);
			startActivity(intent);
			ItemTrip.this.finish();
		}

		}
		return true;

	}
	
	class AccuracyChanged implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (arg2 == 0)
			{
				day = 1;
	        	hour = 0;
	        	minute = 0;
			}
			else if (arg2 == 1)
			{
				hour = 0;
				minute = 0;
			}
			else if (arg2 == 2)
			{
				minute = 0;
			}
			
	        setData.setText(ItemTrip.this.year + "年" + ItemTrip.this.month + "月" +  ItemTrip.this.day + "日");
	        setTime.setText(ItemTrip.this.hour + "时" + ItemTrip.this.minute + "分");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
	
	
	class SaveShortCut implements OnClickListener{
		@Override
		public void onClick(View v){
		
			TGoodsInfo goodInfo = ResourceService.gettGoodsInfo().get(index);
			
			ShortCutData shortCutData = new ShortCutData(3); // 3 is the index of goods
			
			double amount = goodInfo.getQuantity();
			int amountInt = (int)amount;
			
			if (endPos.getSelectedItemId() == startPos.getSelectedItemId())
			{
				Toast.makeText(getApplicationContext(), "请选择不同的起点与终点", Toast.LENGTH_SHORT).show();
				return;
			}
			
			shortCutData.setAmount(amountInt);
			shortCutData.setDescription(goodInfo.getDescription());
			shortCutData.setEndPos(choice[(int) endPos.getSelectedItemId()]);
			shortCutData.setStartPos((choice[(int) startPos.getSelectedItemId()]));
			shortCutData.setGoodsName(goodInfo.getName());
			shortCutData.setDepartTime("15");
			
			try {
				ShortCutData.WriteData(getApplicationContext(), shortCutData);
				Toast.makeText(getApplicationContext(), "捷径保存成功", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	class Go implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (timeFlow.getText().toString() == "")
			{
				Toast.makeText(getApplicationContext(), "请输入浮动时间", Toast.LENGTH_SHORT).show();
				return;
			}
			
	
			if (choice_select[0] == choice_select[1])
				Toast.makeText(getApplicationContext(), "请不要选择相同的起点和终点",Toast.LENGTH_SHORT).show();
			else 
			{
				c.set(year, month - 1, day, hour, minute);
				
				String origin;
				String destination;
				
				if (choice_select[0] == 0) 
				{
					origin = "同济大学本部";
					destination = "嘉定校区";
				} 
				else 
				{
					origin = "嘉定校区";
					destination = "同济大学本部";
				}
				
				
				
				Timestamp timeStamp;
				timeStamp = new Timestamp(year, month - 1, day, hour, minute, 0, 0);
				
				int timeLimit = 0;
				
				int timeAccuracy = Integer.parseInt(timeFlow.getText().toString());
				
				if (accuracy.getSelectedItemId() == 0)
				{
					timeLimit = timeAccuracy * 30 * 24 * 60 ;
				}
				else if (accuracy.getSelectedItemId() == 1)
				{
					timeLimit = timeAccuracy * 24 * 60;
				}
				else if(accuracy.getSelectedItemId() == 2)
				{
					timeLimit = timeAccuracy * 60;
				}
				
				TGoodsInfo goodInfo = ResourceService.gettGoodsInfo().get(index);
				
				TLogisticsInfo logisticGoods = new TLogisticsInfo();
				logisticGoods.setTGoodsInfo(goodInfo);
				logisticGoods.setOrigin(origin);
				logisticGoods.setDestination(destination);
				logisticGoods.setTimeLimit((double)timeLimit);
				logisticGoods.setStartTime(timeStamp);
				logisticGoods.setMoney(goodInfo.getMoney());
				
				JSONObject object = new ItemTripService().start(logisticGoods);
				
				try 
				{
					JSONArray owners = object.getJSONArray("owners");
					JSONArray passengers = object.getJSONArray("passengers");
					JSONObject logisticsInfo = object.getJSONObject("logistics");
					
					if (owners != null)
					{
						for (int i = 0; i != owners.length(); i++)
						{
							object = owners.getJSONObject(i);
							
							TOwnerTravelInfo ownerTravelInfo = new TOwnerTravelInfo();
							ownerTravelInfo = ResourceService.getOwnerTravelInfo(object);
							
							driverTravelInfo.add(ownerTravelInfo);
						}
					}
					
					if (passengers != null)
					{
						for (int i = 0; i != passengers.length(); i++)
						{
							object = passengers.getJSONObject(i);
							
							TPassengerTravelInfo tPassengerTravelInfo = new TPassengerTravelInfo();
							tPassengerTravelInfo = ResourceService.getPassengerTravelInfo(object);
							
							passengerTravelInfo.add(tPassengerTravelInfo);
						}
					}
					
					if (logisticsInfo != null)
					{
						TLogisticsInfo temp = ResourceService.getLogisticsInfo(logisticsInfo);
						
						ResourceService.setLogisticsInfo(temp);
					}
					
					ResourceService.settOwnerTravelInfo(driverTravelInfo);
					ResourceService.settPassengerInfo(passengerTravelInfo);
					
					Intent intent = new Intent(ItemTrip.this, status.class);
					
					intent.putExtra("Source", "goods");
					
					startActivity(intent);
					
					ItemTrip.this.finish();
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}	
			}
		}
		
	}

	class StartSet implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			choice_select[0] = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	class EndSet implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			choice_select[1] = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	class SetTime implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			showDialog(TIME_PICKER_ID);
		}
	}
	
	   @Override  
	    protected Dialog onCreateDialog(int id) {  
	        switch(id){  
	        case DATA_PICKER_ID:  
	        { 	

	            
	            return new DatePickerDialog(this,onDateSetListener,year, month - 1, day);
	        }
	        case TIME_PICKER_ID:
	        {
	            
	            return new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
	        }
	        }  
	        return super.onCreateDialog(id);  
	    }  


	class SetData implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			showDialog(DATA_PICKER_ID); 
		}
	}
	
	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener(){  
  	  
        @Override  
        public void onDateSet(DatePicker view, int year, int month, int day) {  
           ItemTrip.this.year = year;
           ItemTrip.this.month = month + 1;
           ItemTrip.this.day = day;
           
           if(accuracy.getSelectedItemId() == 0)
           {
        	   ItemTrip.this.day = 1;
        	   ItemTrip.this.hour = 0;
        	   ItemTrip.this.minute = 0;
           }
           
           setData.setText(ItemTrip.this.year + "年" + ItemTrip.this.month + "月" +  ItemTrip.this.day + "日");
           setTime.setText(ItemTrip.this.hour + "时" + ItemTrip.this.minute + "分");
        }  
          
    };  
  
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			ItemTrip.this.hour = hourOfDay;
			ItemTrip.this.minute = minute;
			  
			  if (accuracy.getSelectedItemId() == 1)
			  {
				  ItemTrip.this.hour = 0;
				  ItemTrip.this.minute = 0;
			  }
			  else if (accuracy.getSelectedItemId() == 2)
			  {
				  ItemTrip.this.minute = 0;
			  }

            setData.setText(ItemTrip.this.year + "年" + ItemTrip.this.month + "月" +  ItemTrip.this.day + "日");
            setTime.setText(ItemTrip.this.hour + "时" + ItemTrip.this.minute + "分");
		}  
  	  

    };  
    
    
 
	
}
