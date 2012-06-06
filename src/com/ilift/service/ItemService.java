package com.ilift.service;

import com.ilift.entity.TGoodsInfo;
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

public class ItemService extends BaseService{

	public JSONObject start(TGoodsInfo goodsInfo, String operation) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("id", goodsInfo.getId().toString()));
		message.add(new BasicNameValuePair("name", goodsInfo.getName()));
		message.add(new BasicNameValuePair("quantity", goodsInfo.getQuantity().toString()));
		message.add(new BasicNameValuePair("description", goodsInfo.getDescription()));
		message.add(new BasicNameValuePair("operation", operation));

        return connect(ITEM_SERVICE, message);
    }
}
