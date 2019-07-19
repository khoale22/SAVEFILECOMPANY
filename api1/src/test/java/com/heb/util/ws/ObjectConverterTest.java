package com.heb.util.ws;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by d116773 on 4/25/2016.
 */
public class ObjectConverterTest {

	private class ObjectConverterDelegateImpl implements ObjectConverterDelegate {

		public boolean calledProcessClass = false;
		public boolean calledSetListConstrucror = false;
		public Object objectToCopy;

		@Override
		public void processClass(Object source, Object destination) {
			this.objectToCopy = source;
			this.calledProcessClass = true;
		}

		@Override
		public void setListConstructor(Supplier<? extends List> listConstructor) {
			this.calledSetListConstrucror = true;
		}
	}

	private ObjectConverterDelegateImpl converterDelegate = new ObjectConverterDelegateImpl();
	private ObjectConverter<String, String> objectConverter = new ObjectConverter<>(String::new, this.converterDelegate);


	@Test
	public void testConvertPassesCorrectObject() {
		this.converterDelegate.objectToCopy = null;
		String sourceString = "This is a test string.";

		this.objectConverter.convert(sourceString);
		Assert.assertEquals("Wrong object passed to process", sourceString, this.converterDelegate.objectToCopy);
	}

	@Test
	public void testNullPassedReturnsNull() {
		String returned = this.objectConverter.convert(null);
		Assert.assertNull("Passed null does not return null.", returned);
	}

	@Test
	public void testDelegateCalled() {

		this.converterDelegate.calledProcessClass = false;
		String sourceString = "This is a test string.";

		this.objectConverter.convert(sourceString);
		Assert.assertTrue("Delegate not called", this.converterDelegate.calledProcessClass);
	}

	@Test
	public void testSetListConstructorCalled() {
		this.converterDelegate.calledSetListConstrucror = false;
		this.objectConverter.setListConstructor(ArrayList::new);
		Assert.assertTrue("Set list constructor not called", this.converterDelegate.calledSetListConstrucror);
	}

	@Test
	public void testProcessReturnsCorrectObject() {
		String sourceString = "This is a test string.";
		String s = this.objectConverter.convert(sourceString);

		Assert.assertNotEquals("Returned same object", s, sourceString);
		Assert.assertNotNull("Null object", s);
	}

	@Test
	public void testSetListConstructorHandlesNull() {
		this.converterDelegate.calledSetListConstrucror = false;
		this.objectConverter.setListConstructor(null);
		Assert.assertFalse("Called set list constructor with null", this.converterDelegate.calledSetListConstrucror);
	}
}
