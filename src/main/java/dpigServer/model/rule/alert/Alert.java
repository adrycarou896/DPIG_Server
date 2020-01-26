package dpigServer.model.rule.alert;

import java.util.Date;

import org.json.JSONObject;

import dpigServer.model.rule.Rule;
import dpigServer.model.rule.event.Event;

public class Alert implements Rule{
	
	private String name;
	private Event event;
	private String operator;
	private Date dateAlert;
	
	public Alert(String name, Event event, String operator, Date dateAlert) {
		this.name = name;
		this.event=event;
		this.operator=operator;
		this.dateAlert = dateAlert;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public JSONObject getJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("event", event);
		jsonObject.put("operator", operator);
		jsonObject.put("dateAlert", getDate());
		return jsonObject;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public Date getDate() {
		return this.dateAlert;
	}

	@Override
	public void setDate(Date date) {
		this.dateAlert = date;
	}

	@Override
	public String getType() {
		return "alert";
	}
	
	
}