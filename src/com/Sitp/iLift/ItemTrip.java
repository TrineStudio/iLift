package com.Sitp.iLift;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ilifting.dao.pojos.TGoodsInfo;
import com.ilifting.dao.pojos.TLogisticsInfo;
import com.ilifting.dao.pojos.TOwnerTravelInfo;
import com.ilifting.dao.pojos.TPassengerTravelInfo;

public class ItemTrip extends Activity{
	
	private Button go;
	private Button saveShortCut;
	
	private TextView setData;
	private TextView setTime;
	
	private EditText timeFlow;

	private int[] choice_select = { 0, 0 };
	
	private String[] choice = new String[] { "ͬ�ô�ѧ����", "ͬ�ô�ѧ�ζ�У��" };
	private String[] accurateChoise = new String[]{"�� ", "�� ", "ʱ ", "�� "};
	
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
			
	        setData.setText(ItemTrip.this.year + "��" + ItemTrip.this.month + "��" +  ItemTrip.this.day + "��");
	        setTime.setText(ItemTrip.this.hour + "ʱ" + ItemTrip.this.minute + "��");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}
	
	
	class SaveShortCut implements OnClickListener{
		@Override
		public void onClick(View v){
		
			TGoodsInfo goodInfo = resourceService.gettGoodsInfo().get(index);
			
			ShortCutData shortCutData = new ShortCutData(3); // 3 is the index of goods
			
			double amount = goodInfo.getQuantity();
			int amountInt = (int)amount;
			
			if (endPos.getSelectedItemId() == startPos.getSelectedItemId())
			{
				Toast.makeText(getApplicationContext(), "��ѡ��ͬ��������յ�", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getApplicationContext(), "�ݾ�����ɹ�", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getApplicationContext(), "�����븡��ʱ��", Toast.LENGTH_SHORT).show();
				return;
			}
			
	
			if (choice_select[0] == choice_select[1])
				Toast.makeText(getApplicationContext(), "�벻Ҫѡ����ͬ�������յ�",Toast.LENGTH_SHORT).show();
			else 
			{
				c.set(year, month - 1, day, hour, minute);
				
				String origin;
				String destination;
				
				if (choice_select[0] == 0) 
				{
					origin = "ͬ�ô�ѧ����";
					destination = "�ζ�У��";
				} 
				else 
				{
					origin = "�ζ�У��";
					destination = "ͬ�ô�ѧ����";
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
				
				TGoodsInfo goodInfo = resourceService.gettGoodsInfo().get(index);
				
				TLogisticsInfo logisticGoods = new TLogisticsInfo();
				logisticGoods.setTGoodsInfo(goodInfo);
				logisticGoods.setOrigin(origin);
				logisticGoods.setDestination(destination);
				logisticGoods.setTimeLimit((double)timeLimit);
				logisticGoods.setStartTime(timeStamp);
				logisticGoods.setMoney(goodInfo.getMoney());
				
				JSONObject object = ItemTripService.start(logisticGoods);
				
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
							ownerTravelInfo = resourceService.getOwnerTravelInfo(object);
							
							driverTravelInfo.add(ownerTravelInfo);
						}
					}
					
					if (passengers != null)
					{
						for (int i = 0; i != passengers.length(); i++)
						{
							object = passengers.getJSONObject(i);
							
							TPassengerTravelInfo tPassengerTravelInfo = new TPassengerTravelInfo();
							tPassengerTravelInfo = resourceService.getPassengerTravelInfo(object);
							
							passengerTravelInfo.add(tPassengerTravelInfo);
						}
					}
					
					if (logisticsInfo != null)
					{
						TLogisticsInfo temp = resourceService.getLogisticsInfo(logisticsInfo);
						
						resourceService.setLogisticsInfo(temp);
					}
					
					resourceService.settOwnerTravelInfo(driverTravelInfo);
					resourceService.settPassengerInfo(passengerTravelInfo);
					
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
           
           setData.setText(ItemTrip.this.year + "��" + ItemTrip.this.month + "��" +  ItemTrip.this.day + "��");
           setTime.setText(ItemTrip.this.hour + "ʱ" + ItemTrip.this.minute + "��");
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
			  	
			  setData.setText(ItemTrip.this.year + "��" + ItemTrip.this.month + "��" +  ItemTrip.this.day + "��");
			  setTime.setText(ItemTrip.this.hour + "ʱ" + ItemTrip.this.minute + "��");
		}  
  	  

    };  
    
    
 
	
}
