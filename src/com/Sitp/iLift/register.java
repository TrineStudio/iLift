package com.Sitp.iLift;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ilifting.dao.pojos.TUserinfo;

public class register extends Activity {
	private Button submit;
	private Button nextStep;
	
	private EditText userId;
	private EditText userPassword;
	private EditText userPasswordAck;
	private EditText userCollege;
	private EditText userGender;
	private EditText userNumber;
	private EditText userEmail;
	private EditText carType;
	
	private ImageView step1;
	private ImageView step2;
	
	private EditText phoneNumber;
	
	private CheckBox userOccupation;
	private CheckBox noCar;
	
	private ViewFlipper viewFlipper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		userId = (EditText) findViewById(R.id.userId);
		userPassword = (EditText) findViewById(R.id.userPassword);
		userPasswordAck = (EditText) findViewById(R.id.userPasswordAck);
		userCollege = (EditText) findViewById(R.id.userCollege);
		userGender = (EditText) findViewById(R.id.userGender);
		userEmail = (EditText)findViewById(R.id.email);
		carType = (EditText)findViewById(R.id.userPrivateCar);
		noCar = (CheckBox)findViewById(R.id.noPrivateCar);
		phoneNumber = (EditText)findViewById(R.id.phoneNumber);
		userOccupation = (CheckBox)findViewById(R.id.userOccupation);
		userNumber = (EditText)findViewById(R.id.userNumber);
		nextStep = (Button)findViewById(R.id.nextStep);
		submit = (Button) findViewById(R.id.register);
		viewFlipper = (ViewFlipper)findViewById(R.id.registerFlipper);
		
		noCar.setOnCheckedChangeListener(new OnCheckChange());
		
		step1 = (ImageView)findViewById(R.id.step1Gray);
		step2 = (ImageView)findViewById(R.id.step2Gray);
		
		step1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				viewFlipper.showPrevious();
			}
		});
		
		step2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				viewFlipper.showNext();
			}
		});
		
		nextStep.setOnClickListener(new NextStep());

		submit.setOnClickListener(new Register());
		
		getPhoneNumber();

	}

	public class OnCheckChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (!isChecked)
			{
				carType.setEnabled(false);
				carType.setText("");
			}
			else
			{
				carType.setEnabled(true);
			}
			
		}
		
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK: 
		{
			Intent intent = new Intent(register.this, iLift.class);
			startActivity(intent);
			finish();
		}

		}
		return true;

	}

	class NextStep implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			viewFlipper.showNext();
		}
	}
	
	class Register implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.d("iLift", userPassword.getText().toString());
			Log.d("iLift", userPasswordAck.getText().toString());

			String key = userPassword.getText().toString();
			String keyAck = userPasswordAck.getText().toString();
			if (userPassword.getText().toString().equals("")|| userPasswordAck.getText().toString().equals("") || userNumber.getText().toString().equals("") || 
				userId.getText().toString().equals("") || userGender.getText().toString().equals("") || userCollege.getText().toString().equals("") || 
				userEmail.getText().toString().equals("") )
				Toast.makeText(getApplicationContext(), "请输入完整信息", Toast.LENGTH_SHORT).show();
			else if (key.equals(keyAck))
			{
				TUserinfo tUserInfo = new TUserinfo();
				
				registerService rs = new registerService();
				
				tUserInfo.setUsername(userId.getText().toString());
				tUserInfo.setPassword(userPassword.getText().toString());
				tUserInfo.setGender(userGender.getText().toString());
				tUserInfo.setNo(userNumber.getText().toString());
				tUserInfo.setCollege(userCollege.getText().toString());
				tUserInfo.setEmail(userEmail.getText().toString());
				
				String phone = phoneNumber.getText().toString();
				
				for(int i = 0; i != phone.length(); i++)
					if (phone.charAt(i) < '0' && phone.charAt(i) > '9')
					{
						Toast.makeText(getApplicationContext(), "请正确填写手机号码", Toast.LENGTH_SHORT).show();
						return;
					}
				
				if (phone.length() != 11)
				{
					Toast.makeText(getApplicationContext(), "请正确填写手机号码", Toast.LENGTH_SHORT).show();
					return;
				}
				
				tUserInfo.setPhone(phoneNumber.getText().toString());
				
				if (userOccupation.isChecked())
					tUserInfo.setJob("1");
				else
					tUserInfo.setJob("0");
				
				if (carType.getText().toString().equals(""))
					tUserInfo.setCarModel("none");
				else
					tUserInfo.setCarModel(carType.getText().toString());
				
				JSONObject object = rs.start(tUserInfo);
				if (object != null) {
					try {
						boolean result = object.getBoolean("result");
						if (result) {
							Toast.makeText(getApplicationContext(), "注册成功,现在返回登入界面", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(register.this,
									iLift.class);
							startActivity(intent);
							finish();
						} else
							Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "请输入相同的密码", Toast.LENGTH_SHORT).show();
			}

		}
	}
	
	private void getPhoneNumber(){
		 TelephonyManager telephonyMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE); 
	     String tel = null;
	     tel = telephonyMgr.getLine1Number();
	     
	     if (tel == null)
	    	 {
	    	 	Toast.makeText(getApplicationContext(), "手机号码获取失败请自行进行设定", Toast.LENGTH_SHORT).show();
	    	 	return;
	    	 }
	     else
	    	 for(int i = 0; i != tel.length(); i++)
	    		 if (tel.charAt(i) == '*')
	    		 {
	    			 Toast.makeText(getApplicationContext(), "手机号码获取失败请自行进行设定", Toast.LENGTH_SHORT).show();
	    			 return;
	    		 }
	     phoneNumber.setText(tel);
	}

	class BackLog implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(register.this, iLift.class);
			startActivity(intent);
			finish();
		}

	}
}
