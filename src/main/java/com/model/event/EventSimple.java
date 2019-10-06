package com.model.event;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.model.IPCamera;
import com.model.Match;

public class EventSimple implements Event,Serializable{

	private static final long serialVersionUID = 7630199873467233523L;
	
	private int priority;
	private IPCamera ipCamera;
	private String mensaje;
	private Date date;
	private String name;
	
	public EventSimple(IPCamera ipCamera, String mensaje) {
		this.ipCamera = ipCamera;
		this.mensaje = mensaje;
		this.priority = 1;
	}
	
	public EventSimple(IPCamera ipCamera) {
		this.ipCamera = ipCamera;
		this.priority = 1;
	}
	
	@Override
	public boolean isSuccesed(List<Match> personMatches) {
		if(personMatches.size()>0) {
			this.date = personMatches.get(0).getDate();
			Match ultimateMatch = personMatches.get(0);
			if(ultimateMatch.getIpCamera().getName().equals(ipCamera.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getPriority() {
		return this.priority;
	}

	@Override
	public String getMensaje() {
		return this.mensaje;
	}

	@Override
	public Date getDate() {
		return this.date;
	}
	
	@Override
	public String toString() {
		return mensaje;
	}
	
	@Override
	public JSONObject getJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mensaje", mensaje);
		jsonObject.put("date", date.getTime());
		return jsonObject;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}
}
