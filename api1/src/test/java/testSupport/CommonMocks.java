/*
 * CommonMocks
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package testSupport;

import com.heb.util.controller.UserInfo;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * A collection of Mocks that are used across multiple test classes.
 *
 * @author d116773
 * @since 2.0.2
 */
public final class CommonMocks {

	/**
	 * Private so the class cannot be instantiated.
	 */
	private CommonMocks() {}

	/**
	 * Returns an HttpServletRequest to pass to controller functions.
	 *
	 * @return An HttpServletRequest to pass to controller functions.
	 */
	public static HttpServletRequest getServletRequest() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		Mockito.when(request.getLocale()).thenReturn(Locale.US);
		return request;
	}

	/**
	 * Returns a UserInfo that controllers can use for logging.
	 *
	 * @return A UserInfo that controllers can use for logging.
	 */
	public static UserInfo getUserInfo() {
		UserInfo userInfo = Mockito.mock(UserInfo.class);
		Mockito.when(userInfo.getUserId()).thenReturn("test user ID");
		return userInfo;
	}

	/**
	 * Returns a MessageSource controllers can use to retrieve messages.
	 *
	 * @return A MessageSource controllers can use to retrieve messages.
	 */
	public static MessageSource getMessageSource() {
		MessageSource messageSource = Mockito.mock(MessageSource.class);
		Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.anyObject(),
				Mockito.anyString(), Mockito.anyObject())).thenReturn("Test message");
		return messageSource;
	}

	/**
	 * Returns a mock HttpServletResponse controllers can use to write to the response.
	 *
	 * @return A mock HttpServletResponse controllers can use to write to the response.
	 */
	public static HttpServletResponse getServletResponse() {
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		return response;
	}
}
