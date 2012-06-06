package com.ilift.service;

import com.ilift.entity.TUserinfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SaveService extends BaseService{

	public JSONObject start(TUserinfo userInfo) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();

		message.add(new BasicNameValuePair("email", userInfo.getEmail()));
		message.add(new BasicNameValuePair("password", userInfo.getPassword()));
		message.add(new BasicNameValuePair("car_model", userInfo.getCarModel()));
		message.add(new BasicNameValuePair("name", userInfo.getName()));
		message.add(new BasicNameValuePair("job", userInfo.getJob()));
		message.add(new BasicNameValuePair("phone", userInfo.getPhone()));
		message.add(new BasicNameValuePair("gender", userInfo.getGender()));
		message.add(new BasicNameValuePair("no", userInfo.getNo()));
		message.add(new BasicNameValuePair("college", userInfo.getCollege()));

        return connect(SAVE_SERVICE, message);
	}
}
