package com.poiin.yourown.people.message;

import java.io.Serializable;

import com.poiin.yourown.model.JsonStringSupport;

public class UserMessage implements Serializable, JsonStringSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private String from;
	private String fromFacebookId;
	private String fromTwitterId;
	private String to;
	private String id;

	public UserMessage(String content) {
		this.content = content;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromFacebookId() {
		return fromFacebookId;
	}

	public void setFromFacebookId(String fromFacebookId) {
		this.fromFacebookId = fromFacebookId;
	}

	public String getFromTwitterId() {
		return fromTwitterId;
	}

	public void setFromTwitterId(String fromTwitterId) {
		this.fromTwitterId = fromTwitterId;
	}

	@Override
	public String toJsonString() {
		return "{message: " + content + ",from:" + from + ",to:" + to
				+ ",from_twitter_id:" + fromTwitterId + ",from_facebook_id:"
				+ fromFacebookId + "}";
	}

}