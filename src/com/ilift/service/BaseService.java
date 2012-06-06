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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: GodBlessedMay
 * Date: 12-5-18
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class BaseService implements ServletName
{
    private String url = "http://221.239.197.91:8080/ilifting/";

    public Bitmap connect(String servletName, List<NameValuePair> message, String userName)
    {
        url = url + servletName;
        HttpPost request = new HttpPost(url);

        try {
            request.setEntity(new UrlEncodedFormEntity(message, HTTP.UTF_8));
            HttpResponse response = ResourceService.getHttpClient().execute(request);
            System.out.println("Response Clear");
            HttpEntity entity = response.getEntity();

            return getImage(entity, userName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public boolean connect(String servletName, List<NameValuePair> message, String type, String userName)
    {
        url = url + servletName;
        HttpPost request = new HttpPost(url);

        try {
            request.setEntity(new UrlEncodedFormEntity(message, HTTP.UTF_8));
            HttpResponse response = ResourceService.getHttpClient().execute(request);
            System.out.println("Response Clear");
            HttpEntity entity = response.getEntity();

            return getImageFile(entity, type, userName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }


    public JSONObject connect(String servletName, List<NameValuePair> message)
    {
        url = url + servletName;
        HttpPost request = new HttpPost(url);

        try
        {
            request.setEntity(new UrlEncodedFormEntity(message, HTTP.UTF_8));
            HttpResponse response = ResourceService.getHttpClient().execute(request);
            System.out.println("Response Clear");
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            String feedback = convertStreamToString(content);
            JSONObject object = null;

            try
            {
                object = new JSONObject(feedback);
            }
            catch (JSONException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return object;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
    }


}   public String connect(String servletName)
    {
        url = url + servletName;
        HttpPost request = new HttpPost(url);

        try {
            HttpResponse response = ResourceService.getHttpClient().execute(request);
            System.out.println("Response Clear");
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            String feedback = convertStreamToString(content);
            return feedback;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


    public JSONObject connect(String servletName, MultipartEntity multipartEntity)
    {
        url = url + servletName;
        HttpPost request = new HttpPost(url);

        try {
            request.setEntity(multipartEntity);
            HttpResponse response = ResourceService.getHttpClient().execute(request);
            System.out.println("Response Clear");
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            String feedback = convertStreamToString(content);
            JSONObject object = null;

            try
            {
                object = new JSONObject(feedback);
            }
            catch (JSONException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return object;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }


    }

private String convertStreamToString(InputStream is) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;
    try {
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return sb.toString();
}

    private static Bitmap getImage(HttpEntity entity, String userName)
    {

        File sd= Environment.getExternalStorageDirectory();
        String path=sd.getPath()+"/iLiftImages/" + userName;
        File storeFile;

        storeFile = new File(path + "/temp.jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(storeFile);
            entity.writeTo(outputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return BitmapFactory.decodeFile(path + "/temp.jpg");
    }

    private static boolean getImageFile(HttpEntity entity, String type, String userName)
    {
        File sd=Environment.getExternalStorageDirectory();
        String path=sd.getPath()+"/iLiftImages/" + userName;
        File storeFile;
        if (type == "user")
            storeFile = new File(path + "/portrait.jpg");
        else
            storeFile = new File(path + "/goods" + ResourceService.gettGoodsInfo().size() + ".jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(storeFile);
            entity.writeTo(outputStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        Log.d("ilift", "Portrait loaded Successfully");
        ResourceService.setBitmapSrc(path + "/goods" + ResourceService.gettGoodsInfo().size() + ".jpg");
        return true;

    }
}

