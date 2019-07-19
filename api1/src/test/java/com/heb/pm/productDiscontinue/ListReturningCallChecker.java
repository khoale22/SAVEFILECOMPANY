package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ProductDiscontinue;
import org.junit.Assert;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Mocks up a function that will return a List of ProductDiscontinues and check for valid inputs on the call.
 *
 * @author d116773
 * @since 2.0.3
 */
public class ListReturningCallChecker implements Answer<List<ProductDiscontinue>> {

	private boolean methodCalled;
	private int page;
	private int pageSize;
	private Object toCheck;

	/**
	 * Returns whether or not the method was called.
	 *
	 * @return True if the method was called and false otherwise.
	 */
	public boolean isMethodCalled() {
		return this.methodCalled;
	}

	/**
	 * Constructs a new ListReturningCallChecker.
	 *
	 * @param page The page the function should look for.
	 * @param pageSize The page size the function should look for.
	 * @param toCheck What will be the first parameter to the mocked call. If null, then the page request
	 * will be treated as the first argument.
	 */
	public ListReturningCallChecker(int page, int pageSize, Object toCheck) {
		this.page = page;
		this.pageSize = pageSize;
		this.toCheck = toCheck;
	}

	/**
	 * Mocks the call to a function on the repository.
	 *
	 * @param invocation The method call from Mockito.
	 * @return An empty list of ProductDiscontinue objects.
	 * @throws Throwable
	 */
	@Override
	public List<ProductDiscontinue> answer(InvocationOnMock invocation) throws Throwable {
		this.methodCalled = true;

		int pageIndex = 0;

		if (this.toCheck != null) {
			Assert.assertEquals(this.toCheck, invocation.getArguments()[0]);
			pageIndex = 1;
		}

		Pageable request = (Pageable)invocation.getArguments()[pageIndex];
		Assert.assertEquals(this.page, request.getPageNumber());
		Assert.assertEquals(this.pageSize, request.getPageSize());

		return this.getResultList();
	}

	/**
	 * Returns an empty list of ProductDiscontinue objects for testing.
	 *
	 * @return An empty list of ProductDiscontinue objects.
	 */
	private List<ProductDiscontinue> getResultList() {

		List<ProductDiscontinue> list = new ArrayList<>();
		list.add(new ProductDiscontinue());
		return list;
	}
}
