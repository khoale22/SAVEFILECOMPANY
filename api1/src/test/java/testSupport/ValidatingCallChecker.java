/*
 *  ValidatingCallChecker
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package testSupport;

import org.junit.Assert;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Helper class that can be used to mock up function calls that take one parameter that you want verified and will
 * return an object you supply. It tracks whether the method was called, which can be checked by calling isMethodCalled.
 *
 * @param <I> The type of the parameter to the method.
 * @param <T> The return type of the function.
 *
 * @author d116773
 * @since 2.0.3
 */
public class ValidatingCallChecker<I, T> implements Answer<T> {

	private T toReturn;
	private I toValidate;
	private boolean methodCalled;

	/**
	 * Constructs a new ValidatingCallChecker.
	 *
	 * @param toValidate The value to look for being passed to the function.
	 * @param toReturn What to return from the function.
	 */
	public ValidatingCallChecker(I toValidate, T toReturn) {
		this.toValidate = toValidate;
		this.toReturn = toReturn;
		this.methodCalled = false;
	}

	/**
	 * Returns whether or not the mocked function was called.
	 *
	 * @return True if the function was called and false otherwise.
	 */
	public boolean isMethodCalled() {
		return this.methodCalled;
	}

	/**
	 * Mocks up the function call.
	 *
	 * @param invocation The call of the method from Mockito.
	 * @return Returns the object passed to the constructor.
	 * @throws Throwable
	 */
	@Override
	public T answer(InvocationOnMock invocation) throws Throwable {
		Assert.assertEquals(this.toValidate, invocation.getArguments()[0]);
		this.methodCalled = true;
		return this.toReturn;
	}
}
