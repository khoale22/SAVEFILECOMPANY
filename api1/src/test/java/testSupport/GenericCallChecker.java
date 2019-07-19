package testSupport;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author d116773
 */
public class GenericCallChecker <T> implements Answer<T> {

	private boolean methodCalled;
	private T returnValue;

	public GenericCallChecker(T returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public T answer(InvocationOnMock invocation) throws Throwable {
		this.methodCalled = true;
		return this.returnValue;
	}

	public boolean isMethodCalled() {
		return this.methodCalled;
	}
}
