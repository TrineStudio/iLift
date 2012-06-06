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

public class LogisticsInvite extends BaseService{

	public JSONObject start(String logistics_id, String sender_id, String sender_type, String travel_id) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("logistics_id", logistics_id));
		message.add(new BasicNameValuePair("sender_id", sender_id));
		message.add(new BasicNameValuePair("sender_type", sender_type));
		message.add(new BasicNameValuePair("travel_id", travel_id));

        return connect(LOGISTICS_INVITE, message);

	}
}
