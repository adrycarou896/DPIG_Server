package com.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.utils.Util;

public class ReadProperties {
	
	private Map<String, String> events;
	private Map<String,String> alerts;
	
	private BufferedReader bf;
	
	public Map<String,Object> readPropertiesFile() throws FileNotFoundException, IOException {
		Map<String,Object> data = new HashMap<String, Object>();
		
		int totalProperties = numberOfLines();
		events = new HashMap<String, String>();
		alerts = new HashMap<String, String>();
		
		try (InputStream input = new FileInputStream(Util.RULES_FILE_PATH)) {
			Properties prop = new Properties();
            prop.load(input);
            
            for (int i = 1; i <= totalProperties; i++) {
            	String propertyName = "event"+i;
   			 	String event = prop.getProperty(propertyName);
   			 	if(event!=null) {
   			 		events.put(propertyName,event);
   			 	}
   			 	propertyName = "alert"+i;
   			 	String alert = prop.getProperty(propertyName);
   			 	if(alert!=null){
   			 		alerts.put(propertyName, alert);
   			 	}
   			}
		}
		data.put("events", events);
		data.put("alerts", alerts);
		return data;
	}
	
	private int numberOfLines() throws IOException {
		
		FileReader fr = new FileReader(Util.RULES_FILE_PATH);
		bf = new BufferedReader(fr);
		int lNumeroLineas = 0;
		while ((bf.readLine())!=null) {
		  lNumeroLineas++;
		}
		
		return lNumeroLineas;
	}
}
