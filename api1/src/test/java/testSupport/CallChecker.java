/*
 *  testSupport.CallChecker
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package testSupport;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * This is a helper class that can be used to see if a method was called during a test.
 * @author d116773
 */
public class CallChecker implements Answer<Void> {

	private boolean methodCalled;

	public CallChecker() {
		this.methodCalled = false;
	}

	@Override
	public Void answer(InvocationOnMock invocation) throws Throwable {
		this.methodCalled = true;
		return null;
	}

	public boolean isMethodCalled() {
		return methodCalled;
	}
}
