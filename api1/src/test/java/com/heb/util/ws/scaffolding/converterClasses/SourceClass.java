package com.heb.util.ws.scaffolding.converterClasses;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by d116773 on 4/26/2016.
 */
public class SourceClass {

	public static final String LIST_PREFIX_STRING = "message ";
	private static final int TEN_TO_THE_FIFTH = 100000;

	private int integer;
	private BigInteger intBigInteger;
	private BigInteger longBigInteger;
	private String numericString;
	private String regularString = "Test String";
	private List<InnerSourceClass> stringList;

	public SourceClass() {
		this.integer = (int)(Math.random() * SourceClass.TEN_TO_THE_FIFTH);
		this.intBigInteger = BigInteger.valueOf(this.integer);
		this.longBigInteger = BigInteger.valueOf(Integer.MAX_VALUE + this.integer);
		this.numericString = Integer.toString(this.integer);
		this.stringList = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			InnerSourceClass isc = new InnerSourceClass();
			isc.setStringValue(SourceClass.LIST_PREFIX_STRING + i);
			this.stringList.add(isc);
		}
	}


	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}

	public BigInteger getIntBigInteger() {
		return intBigInteger;
	}

	public void setIntBigInteger(BigInteger intBigInteger) {
		this.intBigInteger = intBigInteger;
	}

	public BigInteger getLongBigInteger() {
		return longBigInteger;
	}

	public void setLongBigInteger(BigInteger longBigInteger) {
		this.longBigInteger = longBigInteger;
	}

	public String getNumericString() {
		return numericString;
	}

	public void setNumericString(String numericString) {
		this.numericString = numericString;
	}

	public String getRegularString() {
		return regularString;
	}

	public void setRegularString(String regularString) {
		this.regularString = regularString;
	}

	public List<InnerSourceClass> getStringList() {
		return stringList;
	}

	public void setStringList(List<InnerSourceClass> stringList) {
		this.stringList = stringList;
	}
}
