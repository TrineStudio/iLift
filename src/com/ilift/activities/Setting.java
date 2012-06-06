package com.ilift.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.Sitp.ilift.R;
import com.ilift.controls.ShortCutData;
import com.ilift.entity.TOwnerTravelInfo;
import com.ilift.entity.TPassengerTravelInfo;
import com.ilift.service.DriverService;
import com.ilift.service.PassengerService;
import com.ilift.service.ResourceService;
import com.ilift.usage.TranslateListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Setting extends Activity {
//	private Button passengerMap;

	private Button go;

	private Button selectBar;
	private Button saveAsShortCut;
	
	private TextView setData;
	private TextView setTime;

	private int[] choice_select = { 0, 0 };
	private String[] choice = new String[] { "同济大学本部", "同济大学嘉定校区" };
	private String[] accurateChoise = new String[]{"月 ", "日 ", "时 ", "分 "};

	private Spinner startPos;
	private Spinner endPos;
	private Spinner accuracy;
	
	private List<TPassengerTravelInfo> passengerTravelInfo = new ArrayList<TPassengerTravelInfo>();
	private List<TOwnerTravelInfo> driverTravelInfo = new ArrayList<TOwnerTravelInfo>();

	private EditText passengerAmount; // TODO: Add this para
	private EditText timeFlow;

	
	private Calendar c;

	String userName = null;
	
	private JSONArray array = new JSONArray();
	private JSONObject result = new JSONObject();
	private JSONObject object = null;


	

	private DisplayMetrics dm;


	

	private int month;
	private int year;
	private int day;
	private int hour;
	private int minute;
	
	private final int DATA_PICKER_ID = 1;
	private final int TIME_PICKER_ID = 2;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passenger);

		c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);  
        month = c.get(Calendar.MONTH) + 1;  
        day = c.get(Calendar.DAY_OF_MONTH);  
        hour = c.get(Calendar.HOUR_OF_DAY);  
        minute = c.get(Calendar.MINUTE);  
        
		dm = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(dm);

		setData = (TextView)findViewById(R.id.setDate);
		setTime = (TextView)findViewById(R.id.setTime);
		
		setData.setOnClickListener(new SetData());
		setTime.setOnClickListener(new SetTime());
		
		selectBar = (Button)findViewById(R.id.selectBar);
		
		startPos = (Spinner) findViewById(R.id.startPos);
		endPos = (Spinner) findViewById(R.id.endPos);
		accuracy = (Spinner)findViewById(R.id.accurate);
		
		go = (Button) findViewById(R.id.go);
		saveAsShortCut = (Button)findViewById(R.id.saveShortCut);
		
		timeFlow = (EditText)findViewById(R.id.flowTime);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, choice);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, accurateChoise);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accuracy.setAdapter(spinnerAdapter);
		accuracy.setOnItemSelectedListener(new AccuracyChanged());
		
		startPos.setAdapter(adapter);
		endPos.setAdapter(adapter);

		startPos.setOnItemSelectedListener(new StartSet());
		endPos.setOnItemSelectedListener(new EndSet());

		selectBar.setOnTouchListener(new SelectBarOnTouch(selectBar));
		
		passengerAmount = (EditText) findViewById(R.id.passengerAmount);
		
		passengerAmount.setText("1");
		timeFlow.setText("0");
		
		go.setOnClickListener(new Go());
		saveAsShortCut.setOnClickListener(new SaveShortCut());
		
		setData.setText(year + "年" + month + "月" + day + "日");
		setTime.setText(hour + "时" + minute + "分");
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

            setData.setText(year + "年" + month + "月" + day + "日");
            setTime.setText(hour + "时" + minute + "分");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK: {
			Intent intent = new Intent(Setting.this, MainPage.class);
			startActivity(intent);
			Setting.this.finish();
			
		}

		}
		return true;

	}
	
	class SaveShortCut implements OnClickListener{
		@Override
		public void onClick(View v){
			
			Intent intent = Setting.this.getIntent();
			
			int type = intent.getIntExtra("Statue", -1);
			
			if (type == -1)
			{
				Toast.makeText(getApplicationContext(), "请先选择你是乘客还是车主.", Toast.LENGTH_SHORT).show();
				return;
			}

			if (startPos.getSelectedItemId() == endPos.getSelectedItemId())
			{
				Toast.makeText(getApplicationContext(), "请选择不同的起点与终点", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (passengerAmount.getText().toString().equals(""))
			{
				Toast.makeText(getApplicationContext(), "请输入人数", Toast.LENGTH_SHORT).show();
				return;
			}
			
			ShortCutData tmp = new ShortCutData(type);
			
			tmp.setAmount(Integer.parseInt(passengerAmount.getText().toString()));
			tmp.setEndPos(choice[(int) endPos.getSelectedItemId()]);
			tmp.setStartPos(choice[(int)startPos.getSelectedItemId()]);
			tmp.setDepartTime("15");
			
			try 
			{
				ShortCutData.WriteData(getApplicationContext(), tmp);
				Toast.makeText(getApplicationContext(), "捷径保存成功", Toast.LENGTH_SHORT).show();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
           Setting.this.year = year;
           Setting.this.month = month + 1;
           Setting.this.day = day;
           
           if(accuracy.getSelectedItemId() == 0)
           {
        	   Setting.this.day = 1;
        	   Setting.this.hour = 0;
        	   Setting.this.minute = 0;
           }

            setData.setText(Setting.this.year + "年" + Setting.this.month + "月" +  Setting.this.day + "日");
            setTime.setText(Setting.this.hour + "时" + Setting.this.minute + "分");
        }  
          
    };  
  
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

    	
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			  Setting.this.hour = hourOfDay;
			  Setting.this.minute = minute;
			  
			  if (accuracy.getSelectedItemId() == 1)
			  {
				  Setting.this.hour = 0;
				  Setting.this.minute = 0;
			  }
			  else if (accuracy.getSelectedItemId() == 2)
			  {
				  Setting.this.minute = 0;
			  }

            setData.setText(Setting.this.year + "年" + Setting.this.month + "月" +  Setting.this.day + "日");
            setTime.setText(Setting.this.hour + "时" + Setting.this.minute + "分");
		}  
  	  

    };  
    
    
    @Override  
    protected Dialog onCreateDialog(int id) {  
        // TODO Auto-generated method stub  
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

	class Go implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			if (passengerAmount.getText().toString() == "")
			{
				Toast.makeText(getApplicationContext(), "请输入出发的人数", Toast.LENGTH_SHORT).show();
				return;
			}
			
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
				
				
				int type = Setting.this.getIntent().getIntExtra("Statue", -1);

				if (type == -1)
				{
					Toast.makeText(getApplicationContext(), "请先选择身份", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Timestamp timeStamp;
				timeStamp = new Timestamp(year, month - 1, day, hour, minute, 0, 0);
				
				int timeLimit = 0;
				
				int timeAccuracy = Integer.parseInt(timeFlow.getText().toString());
				
				if (accuracy.getSelectedItemId() == 0)
				{
					timeLimit = timeAccuracy * 30 * 24 * 60 * 60 ;
				}
				else if (accuracy.getSelectedItemId() == 1)
				{
					timeLimit = timeAccuracy * 24 * 60 * 60;
				}
				else if (accuracy.getSelectedItemId() == 2)
				{
					timeLimit = timeAccuracy * 60 * 60;
				}
				else if (accuracy.getSelectedItemId() == 3)
				{
					timeLimit = timeAccuracy * 60;
				}
				
				if (type == 1)
				{
					PassengerService temp = new PassengerService();
					result = temp.start(origin, destination, timeStamp.toString(), Integer.toString(timeLimit));
				}
				else if (type == 2)
				{
					DriverService temp = new DriverService();
					result = temp.start(origin, destination, timeStamp.toString(), Integer.toString(timeLimit), passengerAmount.getText().toString());
				}
				
				
				if (type == 1)
				{
					if (result != null)
					{
						try 
						{
							array = result.getJSONArray("owners");
							object = result.getJSONObject("passenger_travel_info");
						} 
						catch (JSONException e1) 
						{
							e1.printStackTrace();
						}
					}
					
					if (array.length() != 0) 
					{
						for (int i = 0; i != array.length(); i++) {
							try 
							{
								JSONObject object = array.getJSONObject(i);
	
								TOwnerTravelInfo tot = new TOwnerTravelInfo();
								tot = ResourceService.getOwnerTravelInfo(object);
	
								driverTravelInfo.add(tot);
							} 
							catch (JSONException e) 
							{
								e.printStackTrace();
							}
						}
						
						ResourceService.settOwnerTravelInfo(driverTravelInfo);
						
					}
					
					ResourceService.setPassengerTravelInfo(ResourceService.getPassengerTravelInfo(object));
	
				
				}
				else if (type == 2)
				{
					if (result != null)
					{
						try 
						{
							array = result.getJSONArray("passengers");
							object = result.getJSONObject("owner_travel_info");
						} 
						catch (JSONException e1) 
						{
							e1.printStackTrace();
						}
					}
					if (array.length() != 0) 
					{
						for (int i = 0; i != array.length(); i++) {
							try 
							{
								JSONObject object = array.getJSONObject(i);
	
								TPassengerTravelInfo tot = new TPassengerTravelInfo();
								tot = ResourceService.getPassengerTravelInfo(object);
	
								passengerTravelInfo.add(tot);
							} 
							catch (JSONException e) 
							{
								e.printStackTrace();
							}
						}
						
						ResourceService.settPassengerInfo(passengerTravelInfo);
						ResourceService.setOwnerTravelInfo(ResourceService.getOwnerTravelInfo(object));
						
					}
				}
				Intent intent = new Intent(Setting.this, status.class);
				
				intent.putExtra("Source", "user");
				
				startActivity(intent);

				finish();
			}
		}

	}
	
class SelectBarOnTouch implements OnTouchListener {
		
		private int selectLastX;
		private int selectHeight;
		private int selectWidth;
		private int selectBarTop;
		private int selectBarLeft;
		private int selectInitialLeft;
		private boolean selectIsFirst = false;
		private Button selectBar;

    private int leftX1;
    private int leftX2;
    private int leftX3;
    private int leftX4;

    private int x1;
    private int x2;
    private int x3;
    private int x4;

    private int leftX;
    private int rightX;


    public SelectBarOnTouch(Button selectBar)
    {
        this.selectBar = selectBar;

        if (getWindowManager().getDefaultDisplay().getWidth() == 320)
        {
            leftX1 = 2;
            leftX2 = 84;
            leftX3 = 160;
            leftX4 = 238;

            x1 = 16;
            x2 = 92;
            x3 = 166;
            x4 = 244;

            leftX = 2;
            rightX = 319;
        }
        else if (getWindowManager().getDefaultDisplay().getWidth() == 480)
        {
            leftX1 = 17;
            leftX2 = 133;
            leftX3 = 244;
            leftX4 = 347;

            x1 = 33;
            x2 = 140;
            x3 = 251;
            x4 = 359;

            leftX = 17;
            rightX = 464;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (!selectIsFirst)
                {
                    selectInitialLeft = selectBarLeft = selectBar.getLeft();

                    Log.d("ilift", "Top : " + selectBarTop + ", Left : "+ selectBarLeft);

                    selectHeight = selectBar.getHeight();
                    selectWidth = selectBar.getWidth();

                    selectBarTop = selectBar.getTop();

                    Log.d("ilift", "Height && Width : " + selectHeight + ", "+ selectWidth);
                    selectIsFirst = true;
                }
                selectLastX = (int) event.getRawX();
                Log.d("ilift", "On Touch Down : " + event.getX());

                break;

            case MotionEvent.ACTION_MOVE:
                if ((int) (event.getRawX() - selectLastX) != 0)
                {
                    Log.d("ilift", "X : " + event.getRawX() + ", Y: " + event.getRawY());
                    if (event.getRawX() - selectLastX + selectBarLeft <= leftX)
                    {
                        selectBar.layout(leftX, selectBarTop, leftX + selectWidth,selectBarTop + selectHeight);
                    }
                    else if (event.getRawX() - selectLastX + selectBarLeft + selectWidth >= rightX)
                    {
                        selectBar.layout(rightX - selectWidth, selectBarTop, rightX ,selectBarTop + selectHeight);
                    }
                    else
                    {
                        selectBar.layout((int) (event.getRawX() - selectLastX)+ selectBarLeft, selectBarTop,(int) (event.getRawX() - selectLastX)+ selectBarLeft + selectWidth,selectBarTop + selectHeight);
                    }
                }
                selectBarLeft = selectBar.getLeft();
                Log.d("ilift", "On Touch Move : " + (event.getRawX() - selectLastX + selectBarLeft));
                selectLastX = (int) event.getRawX();

                break;

            case MotionEvent.ACTION_UP:
                Log.d("ilift", Float.toString((event.getRawX() - selectLastX) + selectBarLeft));
                if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) >= leftX1 && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) <= leftX2)
                {
                    TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, x1 - (float)selectBar.getLeft(), 0.0f, 0.0f);
                    translateAnimation.setDuration((long) (1.0f * Math.abs((x1 - (float)selectBar.getLeft()))));
                    translateAnimation.setFillEnabled(true);

                    Intent intent = new Intent(Setting.this, Information.class);

                    translateAnimation.setAnimationListener(new TranslateListener(x1 + selectBar.getWidth(), intent, null, selectBar, Setting.this));

                    selectBar.startAnimation(translateAnimation);

                }
                else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > leftX3 && (event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) <= leftX4)
                {
                    Intent intent = new Intent(Setting.this,status.class);

                    TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, x3 - (float)selectBar.getLeft(), 0.0f, 0.0f);
                    translateAnimation.setDuration((long) (1.0f * Math.abs((x3 - (float)selectBar.getLeft()))));
                    translateAnimation.setFillEnabled(true);

                    translateAnimation.setAnimationListener(new TranslateListener(x3 + selectBar.getWidth(), intent, null, selectBar, Setting.this));

                    selectBar.startAnimation(translateAnimation);
                }
//				else if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > leftX4)
//				{
//					TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, x4 - (float)selectBar.getLeft(), 0.0f, 0.0f);
//					translateAnimation.setDuration((long) (1.0f * Math.abs((x4 - (float)selectBar.getLeft()))));
//					translateAnimation.setFillEnabled(true);
//
//					translateAnimation.setAnimationListener(new TranslateListener(x4 + selectBar.getWidth(), null, null, selectBar, Information.this));
//
//					selectBar.startAnimation(translateAnimation);
//				}
                else
                {
                    if ((event.getRawX() - selectLastX + selectBarLeft + selectWidth / 2) > leftX4)
                        Toast.makeText(getApplicationContext(), "历史功能还在开发中", Toast.LENGTH_SHORT).show();

                    TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, selectInitialLeft - (float)selectBar.getLeft(), 0.0f, 0.0f);
                    translateAnimation.setDuration((long) (1.0f * Math.abs((selectInitialLeft - (float)selectBar.getLeft()))));
                    translateAnimation.setFillEnabled(true);

                    translateAnimation.setAnimationListener(new TranslateListener(selectInitialLeft + selectBar.getWidth(), null, null, selectBar, Setting.this));

                    selectBar.startAnimation(translateAnimation);
                }

                selectBarLeft = selectInitialLeft;
                break;
        }
        return true;
    }

	}
	

	

	class StartSet implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			choice_select[0] = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	class EndSet implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			choice_select[1] = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

}
