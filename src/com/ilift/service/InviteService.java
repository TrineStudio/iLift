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

public class InviteService extends BaseService{

	public JSONObject start(String invitePersonID, String driverID, String passengerID, String inviteType) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("invited_id", invitePersonID));
		message.add(new BasicNameValuePair("owner_travel_id", driverID));
		message.add(new BasicNameValuePair("passenger_travel_id", passengerID));
		message.add(new BasicNameValuePair("invite_type", inviteType));

        return connect(INVITE_SERVICE, message);
    }
}
