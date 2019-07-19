package com.heb.pm.dictionary;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for Dictionary vocabulary.
 *
 * @author m314029
 * @since 2.7.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + VocabularyController.VOCABULARY_URL)
@AuthorizedResource(ResourceConstants.SHELF_ATTRIBUTES_CUSTOMER_FRIENDLY_DESCRIPTION)
public class VocabularyController {

	private static final Logger logger = LoggerFactory.getLogger(VocabularyController.class);

	protected static final String VOCABULARY_URL = "/dictionary/vocabulary";

	// log messages
	private static final String LOG_VALIDATE_TEXT_MESSAGE =
			"User %s from IP %s has requested validation of text: %s.";

	// error messages
	private static final String ERROR_CONNECTION_TO_DICTIONARY_DATA =
			"Error connecting to Dictionary database. Please try again later.";

	@Autowired
	private VocabularyService vocabularyService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @param request The HTTP request that initiated this call.
	 * @return A String with the corrected text (if any).
	 */
	@EditPermission
	@DictionaryTransactional
	@RequestMapping(method = RequestMethod.GET, value = "validateText")
	public OriginalTextAndValidatedText validateText(
			@RequestParam("textToValidate") String textToValidate, HttpServletRequest request) {

		this.logValidateText(request.getRemoteAddr(), textToValidate);
		try{
			return new OriginalTextAndValidatedText(textToValidate, this.vocabularyService.validateText(textToValidate));
		} catch (RuntimeException exception){
			logger.info(exception.getLocalizedMessage());
			throw new RuntimeException(VocabularyController.ERROR_CONNECTION_TO_DICTIONARY_DATA);
		}
	}

	/**
	 * Logs a user's request of validating text against the vocabulary rules.
	 *
	 * @param ip The IP address the request is coming from.
	 * @param textToValidate The text to validate against the vocabulary rules.
	 */
	private void logValidateText(String ip, String textToValidate) {
		VocabularyController.logger.info(
				String.format(
						VocabularyController.LOG_VALIDATE_TEXT_MESSAGE, this.userInfo.getUserId(), ip, textToValidate)
		);
	}

	/**
	 * Custom return object when a user requests to validate text against the Vocabulary. Contains the original text
	 * and the text after it was validated.
	 */
	private class OriginalTextAndValidatedText{
		OriginalTextAndValidatedText(String previousText, String validatedText){
			this.originalText = previousText;
			this.validatedText = validatedText;
		}

		private String originalText;

		private String validatedText;

		public String getOriginalText() {
			return this.originalText;
		}

		public String getValidatedText() {
			return this.validatedText;
		}
	}
}