package com.ilift.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.Sitp.ilift.R;
import com.ilift.entity.TUserinfo;
import com.ilift.service.ResourceService;
import com.ilift.service.SaveService;
import org.json.JSONException;
import org.json.JSONObject;

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
			String message = "未知错误";

			SaveService temp = new SaveService();
			
			if (! (newPassword.getText().toString().equals(newPasswordAck.getText().toString())))
			{
				Toast.makeText(getApplicationContext(), "输入的新密码不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			
			TUserinfo userInfo = ResourceService.getUserInfo();
			
			if (!(oldPassword.getText().toString().equals(userInfo.getPassword())))
			{
				Toast.makeText(getApplicationContext(), "输入的密码与原密码不符", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
				ResourceService.setUserInfo(userInfo);
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
