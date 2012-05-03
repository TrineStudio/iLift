package com.Sitp.iLift;


import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import com.ilifting.dao.pojos.TGoodsInfo;
import com.ilifting.dao.pojos.TInviteInfo;
import com.ilifting.dao.pojos.TLogisticsInfo;
import com.ilifting.dao.pojos.TLogisticsInviteInfo;
import com.ilifting.dao.pojos.TOwnerTravelInfo;
import com.ilifting.dao.pojos.TPassengerTravelInfo;
import com.ilifting.dao.pojos.TUserPathinfo;
import com.ilifting.dao.pojos.TUserinfo;

public class resourceService extends Service{
	
	private static TUserinfo userInfo;
	private static List<TUserPathinfo> tUserPath = new ArrayList<TUserPathinfo>();
	private static List<TInviteInfo> tInviteReceivedInfo = new ArrayList<TInviteInfo>();
	private static List<TInviteInfo> tInviteSentInfo = new ArrayList<TInviteInfo>();
	private static List<TInviteInfo> tResponseInfo = new ArrayList<TInviteInfo>();
	private static List<TGoodsInfo> tGoodsInfo = new ArrayList<TGoodsInfo>();
	private static List<TPassengerTravelInfo> tPassengerInfo = new ArrayList<TPassengerTravelInfo>();
	private static List<TOwnerTravelInfo> tOwnerTravelInfo = new ArrayList<TOwnerTravelInfo>();
	private static List<TLogisticsInfo> tLogisticsInfo = new ArrayList<TLogisticsInfo>();
	private static List<TLogisticsInviteInfo> tLogisticsInviteSent = new ArrayList<TLogisticsInviteInfo>();
	private static List<TLogisticsInviteInfo> tLogisticsInviteReceived = new ArrayList<TLogisticsInviteInfo>();

	private static TOwnerTravelInfo ownerTravelInfo;
	private static TPassengerTravelInfo passengerTravelInfo;
	private static TLogisticsInfo logisticsInfo;

	private static HttpClient httpClient;
	
	private static boolean isNewPhoto = false;
	
	private static ImageView imageView;
	
	private static Bitmap userPortrait = null;
	private static Bitmap goodsPortrait = null;

	private static String bitmapSrc;
	
	public static TLogisticsInfo getLogisticsInfo() {
		return logisticsInfo;
	}

	public static void setLogisticsInfo(TLogisticsInfo logisticsInfo) {
		resourceService.logisticsInfo = logisticsInfo;
	}

	public static TGoodsInfo getGoodsInfo(JSONObject object)
	{
		TGoodsInfo goodsInfo = new TGoodsInfo();
		
		try 
		{
			goodsInfo.setId(object.getInt("id"));
			goodsInfo.setDescription(object.getString("description"));
			goodsInfo.setQuantity(object.getDouble("quantity"));
			goodsInfo.setName(object.getString("name"));
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

		return goodsInfo;
	}
	
	public static TLogisticsInviteInfo getLogisticsInviteInfo(JSONObject object)
	{
		TLogisticsInviteInfo temp = new TLogisticsInviteInfo();
		
		try 
		{
			temp.setId(object.getInt("id"));
			
			TUserinfo user = new TUserinfo();
			TUserinfo sender = new TUserinfo();
			
			JSONObject userObject = object.getJSONObject("user");
			JSONObject logistics = object.getJSONObject("logistics");
			
			TLogisticsInfo logisticsInfo = new TLogisticsInfo();
			
			user = resourceService.getUserInfo(userObject);
			logisticsInfo = resourceService.getLogisticsInfo(logistics);
			
			sender.setId(object.getInt("sender_id"));
			sender.setName(object.getString("sender_name"));
			
			temp.setUser(user);
			temp.setSender(sender);
			temp.setTLogisticsInfo(logisticsInfo);
			
			temp.setTime(new Timestamp(object.getLong("time")));
			temp.setSenderType(object.getString("sender_type"));
			temp.setStatus(object.getString("status"));
			temp.setTravelId(object.getInt("travel_id"));
			temp.setResponseChecked(object.getBoolean("reponse_checked"));
			
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		
		return temp;
	}
	
	public static TLogisticsInfo getLogisticsInfo(JSONObject object)
	{
		TLogisticsInfo temp = new TLogisticsInfo();
		
		try 
		{
			temp.setId(object.getInt("id"));
			temp.setStatus(object.getString("status"));
			temp.setMoney(object.getDouble("money"));
			temp.setOrigin(object.getString("origin"));
			temp.setDestination(object.getString("destination"));
			temp.setStartTime(new Timestamp(object.getLong("start_time")));
			temp.setTimeLimit(object.getDouble("time_limit"));
			
			TGoodsInfo goodInfo = new TGoodsInfo();
			goodInfo.setId(object.getInt("goods_id"));
			
			temp.setTGoodsInfo(goodInfo);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		
		
		return temp;
	}
	
	
	public static List<TLogisticsInviteInfo> gettLogisticsInviteSent() {
		return tLogisticsInviteSent;
	}

	public static void settLogisticsInviteSent(List<TLogisticsInviteInfo> tLogisticsInviteSent) {
		resourceService.tLogisticsInviteSent = tLogisticsInviteSent;
	}

	public static List<TLogisticsInviteInfo> gettLogisticsInviteReceived() {
		return tLogisticsInviteReceived;
	}

	public static void settLogisticsInviteReceived(List<TLogisticsInviteInfo> tLogisticsInviteReceived) {
		resourceService.tLogisticsInviteReceived = tLogisticsInviteReceived;
	}

	public static List<TLogisticsInfo> gettLogisticsInfo() {
		return tLogisticsInfo;
	}

	public static void settLogisticsInfo(List<TLogisticsInfo> tLogisticsInfo) {
		resourceService.tLogisticsInfo = tLogisticsInfo;
	}

	
	public static Bitmap getGoodsPortrait() {
		return goodsPortrait;
	}

	public static void setGoodsPortrait(Bitmap goodsPortrait) {
		resourceService.goodsPortrait = goodsPortrait;
	}

	
	public static void RemovePassengerTravelItem(int index)
	{
		tPassengerInfo.remove(index);
	}
	
	public static void RemoveOwnerTravelItem(int index)
	{
		tOwnerTravelInfo.remove(index);
	}
	
	public static String getBitmapSrc() {
		return bitmapSrc;
	}
	
	public static TOwnerTravelInfo getOwnerTravelInfo() {
		return ownerTravelInfo;
	}

	public static void setOwnerTravelInfo(TOwnerTravelInfo ownerTravelInfo) {
		resourceService.ownerTravelInfo = ownerTravelInfo;
	}

	public static void setBitmapSrc(String bitmapSrc) {
		resourceService.bitmapSrc = bitmapSrc;
	}

	public static void setHttpClient(HttpClient httpClient){
		resourceService.httpClient = httpClient;
	}
	
	public static HttpClient getHttpClient(){
		return resourceService.httpClient;
	}
	
	public static ImageView getImageView() {
		return imageView;
	}

	public static void setImageView(ImageView imageView) {
		resourceService.imageView = imageView;
	}

	public static boolean isNewPhoto() {
		return isNewPhoto;
	}

	public static void setNewPhoto(boolean isNewPhoto) {
		resourceService.isNewPhoto = isNewPhoto;
	}

	public static TInviteInfo getInviteInfo(JSONObject jsonObject)
	{
		TInviteInfo temp = new TInviteInfo();

		// Common attribute
		
		try {

		temp.setId(jsonObject.getInt("id"));
		temp.setTime(new Timestamp(jsonObject.getLong("time")));
		temp.setType(jsonObject.getString("type"));
		temp.setStatus(jsonObject.getString("status"));
		
		// Passenger and owner attribute
		
		JSONObject tPassengerInfo = jsonObject.getJSONObject("passenger");
		JSONObject tOwnerInfo = jsonObject.getJSONObject("owner");
		
		TUserinfo tUserPassneger = resourceService.getUserInfo(tPassengerInfo);
		TUserinfo tUserOwner = resourceService.getUserInfo(tOwnerInfo);
		
		temp.setPassenger(tUserPassneger);
		temp.setOwner(tUserOwner);

		// TravelInfo attribute
		
		JSONObject tPassengerTravelInfo = jsonObject.getJSONObject("passenger_travel");
		JSONObject tOwnerTravelInfo = jsonObject.getJSONObject("owner_travel");
		
		temp.setTPassengerTravelInfo(resourceService.getPassengerTravelInfo(tPassengerTravelInfo));
		temp.setTOwnerTravelInfo(resourceService.getOwnerTravelInfo(tOwnerTravelInfo));
		
		
		return temp;
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void AddResponseInfo(TInviteInfo tInviteInfo){
		tResponseInfo.add(tInviteInfo);
	}
	
	public static void AddInvite(TInviteInfo tInvite, int type){
		if (type == 1)
		{
			tInviteSentInfo.add(tInvite);
		}
		else if (type == 2)
		{
			tInviteReceivedInfo.add(tInvite);
		}
	}
	
	public static List<TInviteInfo> gettResponseInfo() {
		return tResponseInfo;
	}


	public static Bitmap getUserPortrait() {
		return userPortrait;
	}

	public static void setUserPortrait(Bitmap userPortrait) {
		resourceService.userPortrait = userPortrait;
	}

	public static void settResponseInfo(List<TInviteInfo> tResponseInfo) {
		resourceService.tResponseInfo = tResponseInfo;
	}

	
	public static int getDriverID() {
		return driverID;
	}

	public static void setDriverID(int driverID) {
		resourceService.driverID = driverID;
	}

	public static int getPassengerID() {
		return passengerID;
	}

	public static void setPassengerID(int passengerID) {
		resourceService.passengerID = passengerID;
	}

	private static int driverID;
	private static int passengerID;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TUserinfo getUserInfo() {
		
		TUserinfo user = new TUserinfo();
		user.setAvatar(userInfo.getAvatar());
		user.setCarModel(userInfo.getCarModel());
		user.setCollege(userInfo.getCollege());
		user.setEmail(userInfo.getEmail());
		user.setId(userInfo.getId());
		user.setJob(userInfo.getJob());
		user.setNo(userInfo.getNo());
		user.setPassword(userInfo.getPassword());
		user.setUsername(userInfo.getUsername());
		user.setPhone(userInfo.getPhone());
		
		return user;
	}

	public static TUserinfo getUserInfo(JSONObject object)
	{
		TUserinfo userInfo = new TUserinfo();
		
		try 
		{
			userInfo.setId(object.getInt("id"));
			userInfo.setUsername(object.getString("username"));
			userInfo.setJob(object.getString("job"));
			userInfo.setPhone(object.getString("phone"));
			userInfo.setCarModel(object.getString("car_model"));
			userInfo.setCollege(object.getString("college"));
			userInfo.setEmail(object.getString("email"));
			userInfo.setNo(object.getString("no"));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		
		return userInfo;
	}
	
	public static TPassengerTravelInfo getPassengerTravelInfo(JSONObject object)
	{
		TPassengerTravelInfo travelInfo = new TPassengerTravelInfo();
		
		try 
		{
			travelInfo.setId(object.getInt("id"));
			travelInfo.setOrigin(object.getString("origin"));
			travelInfo.setDestination(object.getString("destination"));
			travelInfo.setStartTime(new Timestamp(object.getLong("start_time")));
			travelInfo.setTimeLimit(object.getDouble("time_limit"));
			
			TUserinfo passenger = new TUserinfo();
			passenger.setName(object.getString("passenger_name"));
			passenger.setId(object.getInt("passenger_id"));
			passenger.setJob(object.getString("job"));
			passenger.setCollege(object.getString("college"));
			passenger.setCarModel(object.getString("car_model"));
			
			travelInfo.setPassenger(passenger);
			
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		
		return travelInfo;
	}
	
	public static TOwnerTravelInfo getOwnerTravelInfo(JSONObject object)
	{
		TOwnerTravelInfo travelInfo = new TOwnerTravelInfo();
		
		try 
		{
			travelInfo.setId(object.getInt("id"));
			travelInfo.setPassengerNum(object.getInt("passenger_num"));
			travelInfo.setOrigin(object.getString("origin"));
			travelInfo.setDestination(object.getString("destination"));
			travelInfo.setStartTime(new Timestamp(object.getLong("start_time")));
			travelInfo.setTimeLimit(object.getDouble("time_limit"));
			
			TUserinfo owner = new TUserinfo();
			owner.setName(object.getString("owner_name"));
			owner.setId(object.getInt("owner_id"));
			owner.setJob(object.getString("job"));
			owner.setCollege(object.getString("college"));
			owner.setCarModel(object.getString("car_model"));
			
			travelInfo.setOwner(owner);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		
		return travelInfo;
	}
	
	public static void getUserPortraitBitmap(String userName)
	{
        File sd=Environment.getExternalStorageDirectory(); 
        String path=sd.getPath()+"/iLiftImages/" + userName + "/portrait.jpg"; 
       
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		resourceService.setUserPortrait(bitmap);
		if (resourceService.getUserPortrait() != null)
			Log.d("iLift", "Portrait not null!");
	}
	
	public static void setUserInfo(TUserinfo userInfo) {
		resourceService.userInfo = userInfo;
	}

	public static List<TUserPathinfo> gettUserPath() {
		return tUserPath;
	}

	public static void settUserPath(List<TUserPathinfo> tUserPath) {
		resourceService.tUserPath = tUserPath;
	}

	public static List<TInviteInfo> gettInviteReceivedInfo() {
		return tInviteReceivedInfo;
	}

	public static void settInviteReceivedInfo(List<TInviteInfo> tInviteReceivedInfo) {
		resourceService.tInviteReceivedInfo = tInviteReceivedInfo;
	}

	public static List<TInviteInfo> gettInviteSentInfo() {
		return tInviteSentInfo;
	}

	public static void settInviteSentInfo(List<TInviteInfo> tInviteSentInfo) {
		resourceService.tInviteSentInfo = tInviteSentInfo;
	}

	public static List<TGoodsInfo> gettGoodsInfo() {
		return tGoodsInfo;
	}

	public static void settGoodsInfo(List<TGoodsInfo> tGoodsInfo) {
		resourceService.tGoodsInfo = tGoodsInfo;
	}

	public static List<TPassengerTravelInfo> gettPassengerInfo() {
		return tPassengerInfo;
	}

	public static void settPassengerInfo(List<TPassengerTravelInfo> tPassengerInfo) {
		resourceService.tPassengerInfo = tPassengerInfo;
	}

	public static List<TOwnerTravelInfo> gettOwnerTravelInfo() {
		return tOwnerTravelInfo;
	}

	public static void settOwnerTravelInfo(List<TOwnerTravelInfo> tOwnerTravelInfo) {
		resourceService.tOwnerTravelInfo = tOwnerTravelInfo;
	}
	
	public static void addGoodsInfo(TGoodsInfo goodInfo){
		tGoodsInfo.add(goodInfo);
	}

	public static TPassengerTravelInfo getPassengerTravelInfo() {
		return passengerTravelInfo;
	}

	public static void setPassengerTravelInfo(
			TPassengerTravelInfo passengerTravelInfo) {
		resourceService.passengerTravelInfo = passengerTravelInfo;
	}

	
}
