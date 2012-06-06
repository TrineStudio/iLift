package com.ilift.service;

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

public class GetUserInfo extends BaseService{

	public JSONObject start(String userName) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("username",userName));

        return connect(GET_USER_INFO, message);

	}

}
