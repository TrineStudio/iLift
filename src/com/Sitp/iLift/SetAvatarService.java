package com.Sitp.iLift;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SetAvatarService {

	public static JSONObject start(String ID, String type) {
		String url = "http://221.239.199.19:8080/ilifting/image/SetImageServlet";
		HttpPost request = new HttpPost(url);
		FileBody fileBody = new FileBody((new File(resourceService.getBitmapSrc())));
		
		try {
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			multipartEntity.addPart("file", fileBody);
			multipartEntity.addPart("id", new StringBody(ID));
			multipartEntity.addPart("type", new StringBody(type));
			
			
			request.setEntity(multipartEntity);
			HttpResponse response = resourceService.getHttpClient().execute(request);
			System.out.println("Response Clear");
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			JSONObject feedback = new JSONObject(convertStreamToString(content));
			
			return feedback;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("iLift", "Error");
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("iLift", "Error");
			e.printStackTrace();
			return null;
		}

	}

	private static String convertStreamToString(InputStream is) {
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
