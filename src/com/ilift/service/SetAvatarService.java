package com.ilift.service;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class SetAvatarService extends BaseService{

	public JSONObject start(String ID, String type) {

		FileBody fileBody = new FileBody((new File(ResourceService.getBitmapSrc())));

		try
        {
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			multipartEntity.addPart("file", fileBody);
			multipartEntity.addPart("id", new StringBody(ID));
			multipartEntity.addPart("type", new StringBody(type));

            return connect(SET_AVATAR_SERVICE, multipartEntity);
		}
        catch (IOException e)
        {
			e.printStackTrace();
			return null;
		}

	}
}
