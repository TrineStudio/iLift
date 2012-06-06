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

public class PassengerService extends BaseService{

	public JSONObject start(String origin, String destination, String start_time, String time_limit) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("origin", origin));
		message.add(new BasicNameValuePair("destination", destination));
		message.add(new BasicNameValuePair("start_time", start_time));
		message.add(new BasicNameValuePair("time_limit", time_limit));
		message.add(new BasicNameValuePair("origin_limit", "9999"));
		message.add(new BasicNameValuePair("owner_gender", "none"));

		return connect(PASSENGER_SERVICE, message);
	}
}
