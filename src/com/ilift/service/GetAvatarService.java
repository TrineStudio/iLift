package com.ilift.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetAvatarService extends BaseService{

	public Bitmap start(String ID, String type, String userName, boolean isOther)
	{

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("id", ID));
		message.add(new BasicNameValuePair("type", type));

        return connect(GET_AVATAR_SERVICE, message, userName);
	}

	
	public boolean start(String ID, String type, String userName) {

		List<NameValuePair> message = new ArrayList<NameValuePair>();
		message.add(new BasicNameValuePair("id", ID));
		message.add(new BasicNameValuePair("type", type));

        return connect(GET_AVATAR_SERVICE, message, type, userName);

	}

}
