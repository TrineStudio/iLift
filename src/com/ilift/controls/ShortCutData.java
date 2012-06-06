package com.ilift.controls;

import android.content.Context;
import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShortCutData {

	private final int driver = 1;
	private final int passenger = 2;
	private final static int goods = 3;
	
	private String startPos;
	private String endPos;
	private String departTime;
	private String description;
	private String goodsName;
	
	private boolean oldest = false;
	
	private int amount;
	private int type;
	private int price;
	
	public ShortCutData(int type){
		this.type = type;
	}
	
	public String getStartPos() {
		return startPos;
	}
	
	public void setStartPos(String startPos) {
		this.startPos = startPos;
	}
	
	public String getEndPos() {
		return endPos;
	}
	
	public String getRole(){
		switch(type)
		{
		case 1:
			return "乘客";
		case 2:
			return "车主";
		case 3:
			return "物流";
		}
		
		return null;
	}
	
	public void setEndPos(String endPos) {
		this.endPos = endPos;
	}
	
	public String getDepartTime() {
		return departTime;
	}
	
	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getGoodsName() {
		return goodsName;
	}
	
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getDriver() {
		return driver;
	}
	
	public int getPassenger() {
		return passenger;
	}
	
	public int getGoods() {
		return goods;
	}

	public static void WriteData(Context context, ShortCutData data) throws IOException
	{
		ArrayList<ShortCutData> shortCutData = ShortCutData.ReadData(context);
		
		String FILENAME = "ShortCutFile";
		
		FileOutputStream fos;
		
		fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		
		writeSingleData(fos, data);
		
		for(int i = 0; i < 2 && i < shortCutData.size(); i++)
			writeSingleData(fos, shortCutData.get(i));
		
		fos.close();
		
	}
	
	public static void writeSingleData(FileOutputStream fos, ShortCutData shortCutData) throws IOException
	{
		try {
			fos.write((Integer.toString(shortCutData.getType()) + " ").getBytes());
			
			if (shortCutData.getType() == goods)
			{
				fos.write((shortCutData.getGoodsName() + " " + shortCutData.getAmount() + " " + shortCutData.getPrice() + " " + shortCutData.getDescription() + 
						" " + shortCutData.getStartPos() + " " + shortCutData.getEndPos() + " " + shortCutData.getDepartTime() + "\n").getBytes());
			}
			else
				fos.write((shortCutData.getStartPos() + " " + shortCutData.getEndPos() + " " + shortCutData.getAmount() + " " + shortCutData.getDepartTime() + "\n").getBytes());
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<ShortCutData> ReadData(Context context)
	{
		ArrayList<ShortCutData> shortCutData = new ArrayList<ShortCutData>();
		
		String FileName = "ShortCutFile";
		String res = "";
		
		FileInputStream fls = null;
		try {
			fls = context.openFileInput(FileName);
			
			if (fls == null)
				return shortCutData;
			
			int length = fls.available();       
			byte [] buffer = new byte[length];        
			fls.read(buffer);         
			res = EncodingUtils.getString(buffer, "UTF-8");
			
			getData(res, shortCutData);
			
			fls.close();
			
		} catch (FileNotFoundException e) {
			return shortCutData;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return shortCutData;
	}
	
	
	private static void getData(String res, ArrayList<ShortCutData> shortCutData)
	{
		int i = 0;
		int start = 0;
		
		while(i < res.length())
		{
		
			start = i;
			
			int type;
			
			while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				
			type = Integer.parseInt(res.substring(start, i));
			
			ShortCutData tmp = new ShortCutData(type);
			
			i++;
			start = i;
			
			
			if (type == goods)
			{
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setGoodsName(res.substring(start, i));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setAmount(Integer.parseInt(res.substring(start, i)));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setPrice(Integer.parseInt(res.substring(start, i)));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setDescription(res.substring(start, i));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setStartPos(res.substring(start, i));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setEndPos(res.substring(start, i));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setDepartTime(res.substring(start, i));
			}
			else
			{
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setStartPos(res.substring(start, i));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setEndPos(res.substring(start, i));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setAmount(Integer.parseInt(res.substring(start, i)));
				
				i++;
				start = i;
				
				while(i != res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
				tmp.setDepartTime(res.substring(start, i));
			}
			
			while(i < res.length() && res.charAt(i) != ' ' && res.charAt(i) != '\n' && res.charAt(i) != '\r') i++;
			
			i++;
			
			shortCutData.add(tmp);
		}
	}

	public void setOldest(boolean oldest) {
		this.oldest = oldest;
	}

	public boolean isOldest() {
		return oldest;
	}


}
