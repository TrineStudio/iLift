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

public class ResponseInvitation extends BaseService{

	public JSONObject start(String id, String result) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();

		message.add(new BasicNameValuePair("invited_id", id));
		message.add(new BasicNameValuePair("result", result));

        return connect(RESPONSE_INVITATION, message);
	}

}
