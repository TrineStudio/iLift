package com.ilift.service;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DriverService extends BaseService{

	public JSONObject start(String origin, String destination, String start_time, String time_limit, String passengerNum) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();

		message.add(new BasicNameValuePair("origin", origin));
		message.add(new BasicNameValuePair("destination", destination));
		message.add(new BasicNameValuePair("start_time", start_time));
		message.add(new BasicNameValuePair("time_limit", time_limit));
		message.add(new BasicNameValuePair("origin_limit", "9999"));
		message.add(new BasicNameValuePair("passenger_gender", "none"));
		message.add(new BasicNameValuePair("passenger_num", passengerNum));

	    return connect(DRIVER_SERVICE, message);
	}
}
