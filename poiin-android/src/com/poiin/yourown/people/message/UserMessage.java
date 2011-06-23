package com.poiin.yourown.people.message;

import java.io.Serializable;

import com.poiin.yourown.model.JsonStringSupport;

public class UserMessage implements Serializable,JsonStringSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private String from;
	private String to;
	
	public UserMessage(String content){
		this.content=content;
	}

	public String getContent() {
		return content;
	}
	

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toJsonString() {
		return "{message: "+content+",from:"+from+",to:"+to+"}";
	}
	
}