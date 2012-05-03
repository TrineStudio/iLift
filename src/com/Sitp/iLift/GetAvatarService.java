package com.Sitp.iLift;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class GetAvatarService {

	public static Bitmap start(String ID, String type, String userName, boolean isOther)
	{
		String url = "http://221.239.199.19:8080/ilifting/image/GetImageServlet";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("id", ID));
		message.add(new BasicNameValuePair("type", type));

		try {
			request.setEntity(new UrlEncodedFormEntity(message, HTTP.UTF_8));
			HttpResponse response = resourceService.getHttpClient().execute(request);
			System.out.println("Response Clear");
			HttpEntity entity = response.getEntity();
			
			return getImage(entity, type, userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static Bitmap getImage(HttpEntity entity, String type, String userName)
	{
		
        File sd=Environment.getExternalStorageDirectory(); 
        String path=sd.getPath()+"/iLiftImages/" + userName; 
        File storeFile;
        
        storeFile = new File(path + "/temp.jpg");
		
		try {
			FileOutputStream outputStream = new FileOutputStream(storeFile);
			entity.writeTo(outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return BitmapFactory.decodeFile(path + "/temp.jpg");
	}
	
	public static boolean start(String ID, String type, String userName) {
		String url = "http://221.239.199.19:8080/ilifting/image/GetImageServlet";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("id", ID));
		message.add(new BasicNameValuePair("type", type));

		try {
			request.setEntity(new UrlEncodedFormEntity(message, HTTP.UTF_8));
			HttpResponse response = resourceService.getHttpClient().execute(request);
			System.out.println("Response Clear");
			HttpEntity entity = response.getEntity();
			
			return getImageFile(entity, type, userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private static boolean getImageFile(HttpEntity entity, String type, String userName)
	{
        File sd=Environment.getExternalStorageDirectory(); 
        String path=sd.getPath()+"/iLiftImages/" + userName; 
        File storeFile;
        if (type == "user")
        	storeFile = new File(path + "/portrait.jpg");
        else 
        	storeFile = new File(path + "/goods" + resourceService.gettGoodsInfo().size() + ".jpg");
		
		try {
			FileOutputStream outputStream = new FileOutputStream(storeFile);
			entity.writeTo(outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		Log.d("iLift", "Portrait loaded Successfully");
		resourceService.setBitmapSrc(path + "/goods" + resourceService.gettGoodsInfo().size() + ".jpg");
		return true;
		
	}

}
