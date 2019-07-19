package com.heb.util.ws.scaffolding.converterClasses;

import com.heb.util.ws.MessageField;

import java.util.List;

/**
 * Created by d116773 on 4/26/2016.
 */
public class DestinationClass {

	@MessageField(sourceField = "IntBigInteger")
	private int integerFromBigInteger;

	@MessageField(sourceField = "LongBigInteger")
	private long longFromBigInteger;

	@MessageField(sourceField = "NumericString")
	private int integerFromString;

	@MessageField(sourceField = "NumericString")
	private long longFromString;

	@MessageField(sourceField = "RegularString")
	private String stringFromString;

	@MessageField(sourceField = "StringList", innerType = InnerDestinationClass.class)
	private List<InnerDestinationClass> stringList;


	public boolean compareTo(SourceClass sc) {
		if (sc.getIntBigInteger().intValue() != this.integerFromBigInteger) {
			return false;
		}
		if (sc.getLongBigInteger().longValue() != this.longFromBigInteger) {
			return false;
		}
		if (Integer.parseInt(sc.getNumericString()) != this.integerFromString) {
			return false;
		}
		if (Long.parseLong(sc.getNumericString()) != this.longFromString) {
			return false;
		}
		if (!this.stringFromString.equals(sc.getRegularString())) {
			return false;
		}
		return this.compareLists(sc);
	}

	public boolean compareLists(SourceClass sc) {
		if (this.stringList.size() != sc.getStringList().size()) {
			return false;
		}
		for (int i = 0; i < this.stringList.size(); i++) {
			InnerDestinationClass idc = stringList.get(i);
			InnerSourceClass isc = sc.getStringList().get(i);
			if (!idc.getStringValue().equals(isc.getStringValue())) {
				return false;
			}
		}
		return true;
	}
	public int getIntegerFromBigInteger() {
		return integerFromBigInteger;
	}

	public void setIntegerFromBigInteger(int integerFromBigInteger) {
		this.integerFromBigInteger = integerFromBigInteger;
	}

	public int getIntegerFromString() {
		return integerFromString;
	}

	public void setIntegerFromString(int integerFromString) {
		this.integerFromString = integerFromString;
	}

	public String getStringFromString() {
		return stringFromString;
	}

	public void setStringFromString(String stringFromString) {
		this.stringFromString = stringFromString;
	}

	public long getLongFromBigInteger() {
		return longFromBigInteger;
	}

	public void setLongFromBigInteger(long longFromBigInteger) {
		this.longFromBigInteger = longFromBigInteger;
	}

	public long getLongFromString() {
		return longFromString;
	}

	public void setLongFromString(long longFromString) {
		this.longFromString = longFromString;
	}

	public List<InnerDestinationClass> getStringList() {
		return stringList;
	}

	public void setStringList(List<InnerDestinationClass> stringList) {
		this.stringList = stringList;
	}
}
