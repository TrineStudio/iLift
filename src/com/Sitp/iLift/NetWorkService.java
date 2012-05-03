package com.Sitp.iLift;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.ilifting.dao.pojos.TInviteInfo;
import com.ilifting.dao.pojos.TLogisticsInviteInfo;

public class NetWorkService extends Service {

	private HttpResponse response;
	private Context context;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	
	@Override
	public void onCreate()
	{
		String url = "http://221.239.199.19:8080/ilifting/servlet/PushServlet";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> message = new ArrayList<NameValuePair>();
		
		try
		{
			request.setEntity(new UrlEncodedFormEntity(message, HTTP.UTF_8));
			response = resourceService.getHttpClient().execute(request);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void onStart(Intent intent, int stratId){
		while(true){
			try {
				Thread.sleep(5000);
				
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					String feedback = convertStreamToString(content);
					JSONObject object = new JSONObject(feedback);
					
					JSONArray result;
					
					if ( (result = object.getJSONArray("invite_infos")) != null)
					{
						for (int i = 0; i != result.length(); i++) 
						{
							JSONObject jsonObject = result.getJSONObject(i);
							TInviteInfo temp = new TInviteInfo();

							temp = resourceService.getInviteInfo(jsonObject);
					
							resourceService.gettInviteReceivedInfo().add(temp);
						}
						
						NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);               
						Notification n = new Notification(R.drawable.icon, "收到新的邀请", System.currentTimeMillis());             
						n.flags = Notification.FLAG_AUTO_CANCEL;                
						Intent i = new Intent(context, status.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);           
						//PendingIntent
						PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
						                 
						n.setLatestEventInfo(context, "收到新的邀请", "收到新的邀请", contentIntent);
						nm.notify(0, n);
						
					}
					if ((result = object.getJSONArray("response_infos")) != null)
					{
						for (int i = 0; i != result.length(); i++) 
						{
							JSONObject jsonObject = result.getJSONObject(i);
							TInviteInfo temp = new TInviteInfo();

							temp = resourceService.getInviteInfo(jsonObject);
					
							resourceService.gettResponseInfo().add(temp);
						}
						
						NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);               
						Notification n = new Notification(R.drawable.icon, "邀请收到回复", System.currentTimeMillis());             
						n.flags = Notification.FLAG_AUTO_CANCEL;                
						Intent i = new Intent(context, status.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);           
						//PendingIntent
						PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
						                 
						n.setLatestEventInfo(context, "邀请收到回复", "邀请收到回复", contentIntent);
						nm.notify(0, n);
					}
					if ((result = object.getJSONArray("logistics_invite_infos")) != null)
					{
						for (int i = 0; i != result.length(); i++) 
						{
							JSONObject jsonObject = result.getJSONObject(i);
							TLogisticsInviteInfo temp = new TLogisticsInviteInfo();

							temp = resourceService.getLogisticsInviteInfo(jsonObject);
					
							resourceService.gettLogisticsInviteReceived().add(temp);
						}
						
						NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);               
						Notification n = new Notification(R.drawable.icon, "收到新的邀请", System.currentTimeMillis());             
						n.flags = Notification.FLAG_AUTO_CANCEL;                
						Intent i = new Intent(context, status.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);           
						//PendingIntent
						PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
						                 
						n.setLatestEventInfo(context, "收到新的邀请", "收到新的邀请", contentIntent);
						nm.notify(0, n);
					}		
					if ((result = object.getJSONArray("response_logistics_infos")) != null)
					{
						for (int i = 0; i != result.length(); i++) 
						{
							JSONObject jsonObject = result.getJSONObject(i);
							TLogisticsInviteInfo temp = new TLogisticsInviteInfo();

							temp = resourceService.getLogisticsInviteInfo(jsonObject);
					
							resourceService.gettLogisticsInviteSent().add(temp);
						}
						
						NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);               
						Notification n = new Notification(R.drawable.icon, "收到新的回复", System.currentTimeMillis());             
						n.flags = Notification.FLAG_AUTO_CANCEL;                
						Intent i = new Intent(context, status.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);           
						//PendingIntent
						PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
						                 
						n.setLatestEventInfo(context, "收到新的回复", "收到新的回复", contentIntent);
						nm.notify(0, n);
					}						
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
