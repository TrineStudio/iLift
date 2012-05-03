package com.Sitp.iLift;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.ilifting.dao.pojos.TGoodsInfo;

public class ItemService {

	public static JSONObject start(TGoodsInfo goodsInfo, String operation) {
		String url = "http://221.239.199.19:8080/ilifting/goods.do";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("id", goodsInfo.getId().toString()));
		message.add(new BasicNameValuePair("name", goodsInfo.getName()));
		message.add(new BasicNameValuePair("quantity", goodsInfo.getQuantity().toString()));
		message.add(new BasicNameValuePair("description", goodsInfo.getDescription()));
		message.add(new BasicNameValuePair("operation", operation));


		System.out.println("Post Clear");
		// message.add(new BasicNameValuePair("s", ID));
		try {
			request.setEntity(new UrlEncodedFormEntity(message, HTTP.UTF_8));
			HttpResponse response = resourceService.getHttpClient().execute(request);
			System.out.println("Response Clear");
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			String feedback = convertStreamToString(content);
			JSONObject object = new JSONObject(feedback);
			return object;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
