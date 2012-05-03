package com.Sitp.iLift;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ilifting.dao.pojos.TUserinfo;

public class detail extends Activity {
	
	private EditText oldPassword;
	private EditText newPassword;
	private EditText newPasswordAck;
	private EditText newEmail;
	private EditText privateCarInfo;
	
	private CheckBox noPrivateCar;
	
	private Button saveChange;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		oldPassword = (EditText)findViewById(R.id.oldPassword);
		newPassword = (EditText)findViewById(R.id.newPassword);
		newPasswordAck = (EditText)findViewById(R.id.newPasswordAck);
		newEmail = (EditText)findViewById(R.id.newMail);
		privateCarInfo = (EditText)findViewById(R.id.userPrivateCar);
		
		noPrivateCar = (CheckBox)findViewById(R.id.noPrivateCar);

		saveChange = (Button) findViewById(R.id.save);

		saveChange.setOnClickListener(new SaveChange());

		noPrivateCar.setOnCheckedChangeListener(new NoCar());
	}

	class NoCar implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked)
			{
				privateCarInfo.setEnabled(true);
			}
			else
			{
				privateCarInfo.setEnabled(false);
			}
			
		}

	}

	class SaveChange implements OnClickListener {
		@Override
		public void onClick(View v) {

			JSONObject object;
			boolean result = false;
			String message = "δ֪����";

			saveService temp = new saveService();
			
			if (! (newPassword.getText().toString().equals(newPasswordAck.getText().toString())))
			{
				Toast.makeText(getApplicationContext(), "����������벻һ��", Toast.LENGTH_SHORT).show();
				return;
			}
			
			TUserinfo userInfo = resourceService.getUserInfo();
			
			if (!(oldPassword.getText().toString().equals(userInfo.getPassword())))
			{
				Toast.makeText(getApplicationContext(), "�����������ԭ���벻��", Toast.LENGTH_SHORT).show();
				return;
			}
			
			userInfo.setPassword(newPassword.getText().toString());
			userInfo.setCarModel(privateCarInfo.getText().toString());
			userInfo.setEmail(newEmail.getText().toString());
			
			object = temp.start(userInfo);

			if (object != null) {
				try 
				{
					result = object.getBoolean("result");
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}

			if (result) 
			{
				Toast.makeText(getApplicationContext(), "����ɹ�", Toast.LENGTH_SHORT).show();
				resourceService.setUserInfo(userInfo);
				finish();
			} 
			else
			{
				try 
				{
					message = object.getString("message");
					Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
				
			}
				

		}

	}
}
