package com.ilift.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.Sitp.ilift.R;
import com.ilift.entity.*;
import com.ilift.service.GetAvatarService;
import com.ilift.service.LogService;
import com.ilift.service.ResourceService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class iLift extends Activity {
	private Button logIn;
	private Button registerButton;
	
	private CheckBox rememberMe;
	private CheckBox autoLogIn;
	
	private Animation topDown;
	
	private String userName = "";

	private RelativeLayout relativeLayout;
	
	private boolean userNameClicked = false;
	private boolean userPasswordClicked = false;
	
	private TUserinfo tUserInfo;
	private List<TUserPathinfo> tUserPathInfo = new ArrayList<TUserPathinfo>();
	private List<TInviteInfo> tInviteSentInfo = new ArrayList<TInviteInfo>();
	private List<TInviteInfo> tInviteReceivedInfo = new ArrayList<TInviteInfo>();
	private List<TLogisticsInviteInfo> tLogisticsInviteSent = new ArrayList<TLogisticsInviteInfo>();
	private List<TLogisticsInviteInfo> tLogisticsInviteReceived = new ArrayList<TLogisticsInviteInfo>();


	private EditText userId;
	private EditText userPassword;

        private List<TGoodsInfo> tGoodsInfo = new ArrayList<TGoodsInfo>();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.log);

            try
            {
                ReadAccount();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            HttpClient httpClient = new DefaultHttpClient();
            ResourceService.setHttpClient(httpClient);
		
		logIn = (Button) findViewById(R.id.logIn);
		registerButton = (Button) findViewById(R.id.register);

		topDown = AnimationUtils.loadAnimation(this,R.anim.top_down);
		topDown.setAnimationListener(new AnimationDown());
		
		userId = (EditText) findViewById(R.id.userName);
		userPassword = (EditText) findViewById(R.id.password);

		userId.setOnClickListener(new UserNameClicked());
		userPassword.setOnClickListener(new UserPasswordClicked());
		
		rememberMe = (CheckBox)findViewById(R.id.rememberMe);
		autoLogIn = (CheckBox)findViewById(R.id.autoLogIn);
		
		registerButton.setOnClickListener(new Register());
		logIn.setOnClickListener(new LogIn());
		
		relativeLayout = (RelativeLayout)findViewById(R.id.whiteBoard);
		
        File sd=Environment.getExternalStorageDirectory(); 
        String path=sd.getPath()+"/iLiftImages"; 
        File file=new File(path); 
        if(!file.exists()) 
         file.mkdir(); 
		
		if (userName != "")
		{
			userId.setText(userName);
			userId.setTextColor(0xff000000);
			userPassword.setText("");
			rememberMe.setChecked(true);
		}

	}

	public class Register implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(iLift.this, register.class);
			
			startActivity(intent);
			finish();

		}

	}

	private class AnimationDown implements Animation.AnimationListener
	{

		@Override
		public void onAnimationEnd(Animation animation) {
			relativeLayout.layout(relativeLayout.getLeft(), 0 - relativeLayout.getHeight(), relativeLayout.getWidth() + relativeLayout.getLeft(), 0);
			
			WriteAccount();
				
			Intent intent = new Intent(iLift.this, MainPage.class);
			startActivity(intent);
			
			overridePendingTransition(R.anim.empty, R.anim.empty);
			
			iLift.this.finish();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			
		}
	
	}
	
	public class LogIn implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			if (userId.getText().toString().equals("") || userPassword.getText().toString().equals(""))
				Toast.makeText(getApplicationContext(), "请输入完整信息", Toast.LENGTH_SHORT).show();
			else 
			{

				
				LogService ls = new LogService();
				JSONObject object = ls.start(userId.getText().toString(), userPassword.getText().toString());
				if (object != null) {
					try {
						
						Intent service = new Intent();
						service.setClass(iLift.this, ResourceService.class);
						startService(service);
						
						boolean result = object.getBoolean("result");
						
						JSONObject userInfo = object.getJSONObject("user");
						JSONArray pathInfo = object.getJSONArray("path");
						JSONArray inviteInfo = object.getJSONArray("invite");
						JSONArray goodsInfo = object.getJSONArray("goods_info");
						JSONArray logisticsInviteInfo = object.getJSONArray("logistics_invite_info");
						
						if (userInfo != null) {
							tUserInfo = new TUserinfo();
							
							tUserInfo.setCarModel(userInfo.getString("car_model"));
							tUserInfo.setCollege(userInfo.getString("college"));
							tUserInfo.setId(userInfo.getInt("id"));
							tUserInfo.setJob(userInfo.getString("job"));
							tUserInfo.setPhone(userInfo.getString("phone"));
							tUserInfo.setUsername(userInfo.getString("username"));
							tUserInfo.setEmail(userInfo.getString("email"));
							tUserInfo.setPassword(userInfo.getString("password"));
							
							File sd=Environment.getExternalStorageDirectory();
					        String path = sd.getPath()+"/iLiftImages/";
					        
					        File file=new File(path + tUserInfo.getUsername()); 
					        if(!file.exists()) 
						         file.mkdir();  
					        
					        file=new File(path + "goods"); 
					        if(!file.exists()) 
					         file.mkdir();  
							
							if (new GetAvatarService().start(tUserInfo.getId().toString(), "user", tUserInfo.getUsername()))
							{
								ResourceService.getUserPortraitBitmap(tUserInfo.getUsername());
								Log.d("ilift", "Portrait setting up!");
							}
						}
						
						if (goodsInfo != null)
						{
							for(int i = 0;i != goodsInfo.length(); i++)
							{
								JSONObject temp = goodsInfo.getJSONObject(i);
								TGoodsInfo goodInfo = new TGoodsInfo();
								goodInfo = ResourceService.getGoodsInfo(temp);
								
								boolean getBitmap = new GetAvatarService().start(goodInfo.getId().toString(), "goods", tUserInfo.getUsername());
								if (getBitmap)
								{
                                    try
                                    {
                                        Bitmap bm = BitmapFactory.decodeFile(ResourceService.getBitmapSrc());
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                        goodInfo.setAvatar(baos.toByteArray());
                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(getApplicationContext(), "获取图片失败", Toast.LENGTH_SHORT).show();
                                    }
								}
								
								tGoodsInfo.add(goodInfo);
							}
						}
						
						if (pathInfo != null) {
							for (int i = 0; i != pathInfo.length(); i++) {
								JSONObject jsonObject = pathInfo
										.getJSONObject(i);
								TUserPathinfo temp = new TUserPathinfo();
								temp.setDestination(jsonObject
										.getString("destination"));
								temp.setId(jsonObject.getInt("id"));
								temp.setOrigin(jsonObject.getString("origin"));
								temp.setStartTime(Timestamp.valueOf(jsonObject
										.getString("startTime")));
								temp.setTUserinfo(null);
								temp.setType(jsonObject.getString("type"));

								tUserPathInfo.add(temp);
							}
						}

						if (inviteInfo != null) 
						{
							for (int i = 0; i != inviteInfo.length(); i++) 
							{
								JSONObject jsonObject = inviteInfo.getJSONObject(i);
								TInviteInfo temp = new TInviteInfo();

								temp = ResourceService.getInviteInfo(jsonObject);

//                                if (!temp.getStatus().equals("unhandle"))
//                                    continue;

								
								if (temp.getType().equals("owner") && temp.getPassenger().getUsername().equals(tUserInfo.getUsername()) || temp.getType().equals("passenger") && temp.getOwner().getUsername().equals(tUserInfo.getUsername()))
								{
									tInviteReceivedInfo.add(temp);
								}
								else
									tInviteSentInfo.add(temp);
							}

						}
						
						if (logisticsInviteInfo != null)
						{
							for (int i = 0; i != logisticsInviteInfo.length(); i++)
							{
								JSONObject jsonObject = logisticsInviteInfo.getJSONObject(i);
								
								TLogisticsInviteInfo temp = new TLogisticsInviteInfo();
								
								temp = ResourceService.getLogisticsInviteInfo(jsonObject);
								
								if (temp.getSender().getName().equals(tUserInfo.getName())) // The sender is the user
								{
									tLogisticsInviteSent.add(temp);
								}
								else
									tLogisticsInviteReceived.add(temp);
							}
						}

						if (result)
						{
							

							
							ResourceService.settGoodsInfo(tGoodsInfo);
							ResourceService.settInviteReceivedInfo(tInviteReceivedInfo);
							ResourceService.settInviteSentInfo(tInviteSentInfo);
							ResourceService.settUserPath(tUserPathInfo);
							ResourceService.setUserInfo(tUserInfo);
							ResourceService.settLogisticsInviteReceived(tLogisticsInviteReceived);
							ResourceService.settLogisticsInviteSent(tLogisticsInviteSent);
							
							relativeLayout.startAnimation(topDown);
						} 
						else
						{
							String message = object.getString("message");
							Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
						}

					} 
					catch (JSONException e) 
					{
						Log.d("ilift", e.getMessage());
					}

				}
				
			}
		}

	}
	
	

	
	public void WriteAccount()
	{
		String FILENAME = "account";
		
		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

			if (rememberMe.isChecked())
			{
				String string = userId.getText().toString(); 
				fos.write(string.getBytes());	
			}
			fos.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	private class UserNameClicked implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			if (!userNameClicked)
			{
				userId.setText("");
				userId.setTextColor(0xff000000);
				userNameClicked = true;
			}
		}
	}
	
	private class UserPasswordClicked implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			if (!userPasswordClicked)
			{
				userPassword.setText("");
				userPassword.setTextColor(0xff000000);
				userPasswordClicked = true;
				userPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
		}
	}
	

	
	
	public void ReadAccount() throws IOException
	{
		String FileName = "account";
		
		FileInputStream fls;
		fls = openFileInput(FileName);
		
		if (fls == null)
			return;
		
		int length = fls.available();       
		byte [] buffer = new byte[length];        
		fls.read(buffer);         
		userName = EncodingUtils.getString(buffer, "UTF-8");
		
		fls.close();
	}
	
	
}
