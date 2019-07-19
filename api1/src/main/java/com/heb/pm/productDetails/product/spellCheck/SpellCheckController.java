package com.heb.pm.productDetails.product.spellCheck;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.dictionary.DictionaryTransactional;
import com.heb.pm.dictionary.Suggestion;
import com.heb.pm.dictionary.Vocabulary;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller for spell check.
 *
 * @author vn73545
 * @since 2.7.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + SpellCheckController.SPELL_CHECK_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_ECOMMERCE_VIEW)
public class SpellCheckController {

	private static final Logger logger = LoggerFactory.getLogger(SpellCheckController.class);

	protected static final String SPELL_CHECK_URL = "/spellCheck";
	/**
	 * Holds the path to request the list of vocabularies.
	 */
	private static final String FIND_VOCABULARIES = "/findVocabularies";
	private static final String FIND_ALL_SUGGESTIONS = "/findAllSuggestions";
	private static final String VALIDATE_TEXT = "/validateText";
	private static final String VALIDATE_SPELL_CHECK_TEXT = "/validateSpellCheckText";
	private static final String VALIDATE_ROMANCE_COPY_SPELL_CHECK = "/validateRomanceCopySpellCheck";
	private static final String VALIDATE_BRAND_SPELL_CHECK = "/validateBrandSpellCheck";
	// log messages
	private static final String LOG_VALIDATE_TEXT_MESSAGE =
			"User %s from IP %s has requested validation of text: %s.";
	private static final String LOG_FIND_VOCABULARIES =
			"User %s from IP %s has requested to find vocabularies";
	private static final String LOG_FIND_ALL_SUGGESTIONS =
			"User %s from IP %s has requested to find all suggestions";
	private static final String LOG_VALIDATE_TEXT =
			"User %s from IP %s has requested to validate text";
	private static final String LOG_VALIDATE_SPELL_CHECK_TEXT =
			"User %s from IP %s has requested to validate spell check of text";
	private static final String LOG_VALIDATE_ROMANCE_COPY_SPELL_CHECK =
			"User %s from IP %s has requested to validate romance copy spell check of text";
	private static final String LOG_VALIDATE_BRAND_SPELL_CHECK =
			"User %s from IP %s has requested to validate brand spell check of text";
	private static final String LOG_ADD_NEW_VOCABULARY =
			"User %s from IP %s has requested to add new a vocabulary";
	// error messages
	private static final String ERROR_CONNECTION_TO_DICTIONARY_DATA =
			"Error connecting to Dictionary database. Please try again later.";
	private static final String ADD_VOCABULARIES_SUCCESS_MESSAGE = "A new word is added successfully.";
	private static final String ADD_NEW_VOCABULARY = "/addNewVocabulary";

	@Autowired
	private SpellCheckService spellCheckService;
	@Autowired
	private UserInfo userInfo;

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @param request The HTTP request that initiated this call.
	 * @return A String with the corrected text (if any).
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SpellCheckController.VALIDATE_TEXT)
	public OriginalTextAndValidatedText validateText(
			@RequestParam("textToValidate") String textToValidate, HttpServletRequest request) {
		SpellCheckController.logger.info(
				String.format(
						SpellCheckController.LOG_VALIDATE_TEXT, this.userInfo.getUserId(), request.getRemoteAddr())
		);
		try{
			return new OriginalTextAndValidatedText(textToValidate, this.spellCheckService.validateText(textToValidate));
		} catch (RuntimeException exception){
			logger.info(exception.getLocalizedMessage());
			throw new RuntimeException(SpellCheckController.ERROR_CONNECTION_TO_DICTIONARY_DATA);
		}
	}

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @param request The HTTP request that initiated this call.
	 * @return A String with the corrected text (if any).
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SpellCheckController.VALIDATE_SPELL_CHECK_TEXT)
	public OriginalTextAndValidatedText validateSpellCheckText(
			@RequestParam("textToValidate") String textToValidate, HttpServletRequest request) {
		SpellCheckController.logger.info(
				String.format(
						SpellCheckController.LOG_VALIDATE_SPELL_CHECK_TEXT, this.userInfo.getUserId(), request.getRemoteAddr())
		);
		try{
			return new OriginalTextAndValidatedText(textToValidate, this.spellCheckService.validateSpellCheckText(textToValidate));
		} catch (RuntimeException exception){
			logger.info(exception.getLocalizedMessage());
			throw new RuntimeException(SpellCheckController.ERROR_CONNECTION_TO_DICTIONARY_DATA);
		}
	}

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @param request The HTTP request that initiated this call.
	 * @return A String with the corrected text (if any).
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SpellCheckController.VALIDATE_ROMANCE_COPY_SPELL_CHECK)
	public OriginalTextAndValidatedText validateRomanceCopySpellCheck(
			@RequestParam("textToValidate") String textToValidate, HttpServletRequest request) {
		SpellCheckController.logger.info(
				String.format(
						SpellCheckController.LOG_VALIDATE_ROMANCE_COPY_SPELL_CHECK, this.userInfo.getUserId(), request.getRemoteAddr())
				);
		try{
			return new OriginalTextAndValidatedText(textToValidate, this.spellCheckService.validateRomanceCopySpellCheck(textToValidate));
		} catch (RuntimeException exception){
			logger.info(exception.getLocalizedMessage());
			throw new RuntimeException(SpellCheckController.ERROR_CONNECTION_TO_DICTIONARY_DATA);
		}
	}

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @param request The HTTP request that initiated this call.
	 * @return A String with the corrected text (if any).
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SpellCheckController.VALIDATE_BRAND_SPELL_CHECK)
	public OriginalTextAndValidatedText validateBrandSpellCheck(
			@RequestParam("textToValidate") String textToValidate, HttpServletRequest request) {
		SpellCheckController.logger.info(
				String.format(
						SpellCheckController.LOG_VALIDATE_BRAND_SPELL_CHECK, this.userInfo.getUserId(), request.getRemoteAddr())
				);
		try{
			return new OriginalTextAndValidatedText(textToValidate, this.spellCheckService.validateBrandSpellCheck(textToValidate));
		} catch (RuntimeException exception){
			logger.info(exception.getLocalizedMessage());
			throw new RuntimeException(SpellCheckController.ERROR_CONNECTION_TO_DICTIONARY_DATA);
		}
	}

	/**
	 * Returns the list of vocabularies and the list suggestions based on textToValidate.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @param request The HTTP request that initiated this call.
	 * @return the VocabularyAndSuggestion object.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SpellCheckController.FIND_VOCABULARIES)
	public VocabularyAndSuggestion findVocabularies(@RequestParam("textToValidate") String textToValidate, HttpServletRequest request) {
		SpellCheckController.logger.info(
				String.format(
						SpellCheckController.LOG_FIND_VOCABULARIES, this.userInfo.getUserId(), request.getRemoteAddr())
		);
		try{
			List<Vocabulary> vocabularies = this.spellCheckService.findVocabularies(textToValidate);
			List<Suggestion> suggestions = this.spellCheckService.findSuggestions(textToValidate);
			List<Vocabulary> vocabulariesBySuggestion = this.spellCheckService.findVocabulariesBySuggestion(suggestions);
			boolean isExisting = false;
			for (Vocabulary vocabularyBySuggestion: vocabulariesBySuggestion) {
				isExisting = false;
				for (Vocabulary vocabulary: vocabularies) {
					if(vocabularyBySuggestion.getKey().equals(vocabulary.getKey())){
						isExisting = true;
						break;
					}
				}
				if(!isExisting){
					vocabularies.add(vocabularyBySuggestion);
				}
			}
			VocabularyAndSuggestion vocabularyAndSuggestion = new VocabularyAndSuggestion(vocabularies, suggestions);
			return vocabularyAndSuggestion;
		} catch (RuntimeException exception){
			logger.info(exception.getLocalizedMessage());
			throw new RuntimeException(SpellCheckController.ERROR_CONNECTION_TO_DICTIONARY_DATA);
		}
	}

	/**
	 * Find all suggestions.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return the list of suggestion.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SpellCheckController.FIND_ALL_SUGGESTIONS)
	public List<Suggestion> findAllSuggestions(HttpServletRequest request) {
		SpellCheckController.logger.info(
				String.format(
						SpellCheckController.LOG_FIND_ALL_SUGGESTIONS, this.userInfo.getUserId(), request.getRemoteAddr())
				);
		return this.spellCheckService.findAllSuggestions();
	}

	/**
     * Add new a vocabulary.
     * @param wordText           The word text to add new.
     * @param caseCode           The case code to add new.
     * @param wordCode           The word code to add new.
     * @param status           	 The word is suggestion or not.
     * @param request            The HTTP request that initiated this call.
     * @return Status process data to insert database.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.GET, value = SpellCheckController.ADD_NEW_VOCABULARY)
    public ModifiedEntity<Vocabulary> addNewVocabulary(@RequestParam(value = "wordText", required = true) String wordText,
													   @RequestParam(value = "wordCode", required = true) String wordCode,
													   @RequestParam(value = "caseCode", required = true) String caseCode,
													   @RequestParam(value = "status", required = true) boolean status,
													   HttpServletRequest request) {
    	SpellCheckController.logger.info(
    			String.format(
    					SpellCheckController.LOG_ADD_NEW_VOCABULARY, this.userInfo.getUserId(), request.getRemoteAddr())
    			);
    	if(status){//Suggestion word
    		this.spellCheckService.changeVocabulary(wordText, wordCode, caseCode, userInfo.getUserId());
    	}else{
    		this.spellCheckService.addNewVocabulary(wordText, wordCode, caseCode, userInfo.getUserId());
    	}
    	return new ModifiedEntity<>(null, ADD_VOCABULARIES_SUCCESS_MESSAGE);
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
	/**
	 * Custom return object when a user requests to find all the Vocabularies. Contains the list of vocabularies
	 * and the list of suggestions.
	 */
	private class VocabularyAndSuggestion{
		VocabularyAndSuggestion(List<Vocabulary> vocabularies, List<Suggestion> suggestions){
			this.suggestions = suggestions;
			this.vocabularies = vocabularies;
		}

		private List<Suggestion> suggestions;

		private List<Vocabulary> vocabularies;

		public List<Suggestion> getSuggestions() {
			return this.suggestions;
		}

		public List<Vocabulary> getVocabularies() {
			return this.vocabularies;
		}
	}
}
