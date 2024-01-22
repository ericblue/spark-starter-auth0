package com.ericblue.spark.auth0.model;

import io.swagger.annotations.ApiModelProperty;

public class Message extends BaseDomainObject {

	@ApiModelProperty(value = "Sample message to send/receive")
	private String text;

	public Message() {
	}

	public Message(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text=text;
	}


}
