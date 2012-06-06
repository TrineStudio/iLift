package com.ilift.service;

import com.ilift.entity.TLogisticsInfo;
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

public class ItemTripService extends BaseService{

	public JSONObject start(TLogisticsInfo logisticsInfo) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("goods_id", logisticsInfo.getTGoodsInfo().getId().toString()));
		message.add(new BasicNameValuePair("origin", logisticsInfo.getOrigin()));
		message.add(new BasicNameValuePair("destination", logisticsInfo.getDestination()));
		message.add(new BasicNameValuePair("start_time", logisticsInfo.getStartTime().toString()));
		message.add(new BasicNameValuePair("time_limit", logisticsInfo.getTimeLimit().toString()));
		message.add(new BasicNameValuePair("origin_limit", "10000"));
		message.add(new BasicNameValuePair("money", logisticsInfo.getMoney().toString()));

		return connect(ITEM_TRIP_SERVICE, message);
	}

}
