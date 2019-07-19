package com.heb.util.ws.scaffolding.converterClasses;

import com.heb.util.ws.MessageField;

/**
 * Created by d116773 on 4/26/2016.
 */
public class InnerDestinationClass {

	@MessageField(sourceField = "StringValue")
	private String stringValue;

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
}
