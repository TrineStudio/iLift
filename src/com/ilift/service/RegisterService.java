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

public class RegisterService extends BaseService{

	public JSONObject start(TUserinfo tUserInfo) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();

		message.add(new BasicNameValuePair("username", tUserInfo.getUsername()));
		message.add(new BasicNameValuePair("password", tUserInfo.getPassword()));
		message.add(new BasicNameValuePair("name", tUserInfo.getUsername()));
		message.add(new BasicNameValuePair("gender", tUserInfo.getGender()));
		message.add(new BasicNameValuePair("colledge", tUserInfo.getCollege()));
		message.add(new BasicNameValuePair("email", tUserInfo.getEmail()));
		message.add(new BasicNameValuePair("job", tUserInfo.getJob()));
		message.add(new BasicNameValuePair("no", tUserInfo.getNo()));
		message.add(new BasicNameValuePair("car_model", tUserInfo.getCarModel()));
		message.add(new BasicNameValuePair("phone", tUserInfo.getPhone()));

        return connect(REGISTER_SERVICE, message);
	}
}
