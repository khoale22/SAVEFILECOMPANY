package com.heb.pm.productDetails.product.spellCheck;

import com.heb.pm.dictionary.*;
import com.heb.util.controller.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Service class for managing vocabulary in Dictionary data.
 *
 * @author vn73545
 * @since 2.7.0
 */
@Service
public class SpellCheckService {

	private static final Logger logger = LoggerFactory.getLogger(SpellCheckService.class);

	private static final String ANY_SPACE_REGEX = "\\s+";
	private static final String NO_VOWEL_REGEX = "^[^aeiouy]+$";
	private static final String SINGLE_SPACE = " ";
	private static final int ONE_CHARACTER = 1;
	private static final String VALID = "valid";
	private static final String MIXED = "mixed";
	private static final String UPPER_CASE = "upper";
	private static final String CAPITALIZE_FIRST_LETTER = "cap1";
	private static final String LOWER_CASE_CODE = "lower";
	private static final String PRIMARY_KEY_CONFLICT_MESSAGE = "Unable to perform the requested action. Please contact the system administrator";
	// log messages
	private static final String NO_SUGGESTIONS_FOUND_FOR_FIX_WORD_CODE_OR_MIX_CASE_CODE = "Dictionary.Vocabulary table" +
			"has %s as %s case code, but Dictionary.Suggestion table has no suggestions.";
	private static final String USER_ENTERED_BANNED_WORD = "User: %s entered banned word %s.";
	private static final String REMOVING_BANNED_WORD = "Text: %s is banned according to the dictionary rules and is" +
			" being removed from the original text.";
	private static final String MODIFIED_FIXED_WORD = "Original text: %s needs to be fixed according to the dictionary " +
			"rules and is being replaced with: %s.";
	private static final String APPLYING_SPELL_CHECK_RULES = "Applying spell check rules on: %s.";

	@Autowired
	private VocabularyRepository vocabularyRepository;

	@Autowired
	private SuggestionRepository suggestionRepository;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @return A String with the corrected text (if any).
	 */
	public String validateText(String textToValidate) {
		List<String> spellCheckAppliedWords = this.applySpellCheckRules(textToValidate);
		List<String> caseCorrectedText = new ArrayList<>();
		for (String text : spellCheckAppliedWords) {
			caseCorrectedText.add(
					this.getCaseCodeConversionValue(text, spellCheckAppliedWords.indexOf(text) == 0));
		}
		return this.convertToFullTextValue(caseCorrectedText);
	}

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @return A String with the corrected text (if any).
	 */
	public String validateSpellCheckText(String textToValidate) {
		List<String> spellCheckAppliedWords = this.applySpellCheckRules(textToValidate);
		List<String> caseCorrectedText = new ArrayList<>();
		for (String text : spellCheckAppliedWords) {
			caseCorrectedText.add(
					this.getSpellCheckCaseCodeConversionValue(text, spellCheckAppliedWords.indexOf(text) == 0));
		}
		return this.convertToFullTextValue(caseCorrectedText);
	}

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @return A String with the corrected text (if any).
	 */
	public String validateRomanceCopySpellCheck(String textToValidate) {
		List<String> spellCheckAppliedWords = this.applySpellCheckRules(textToValidate);
		List<String> caseCorrectedText = new ArrayList<>();
		for (String text : spellCheckAppliedWords) {
			caseCorrectedText.add(
					this.getRomanceCopySpellCheckCaseCodeConversionValue(text, spellCheckAppliedWords.indexOf(text) == 0));
		}
		return this.convertToFullTextValue(caseCorrectedText);
	}

	/**
	 * Validate a string based on Vocabulary rules.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @return A String with the corrected text (if any).
	 */
	public String validateBrandSpellCheck(String textToValidate) {
		List<String> toReturn = new ArrayList<>();
		textToValidate = textToValidate.replaceAll(String.valueOf((char) 160), SpellCheckService.SINGLE_SPACE);
		List<String> originalTextSplit = Arrays.asList(textToValidate.split(SpellCheckService.ANY_SPACE_REGEX));
		List<String> bannedAndFixedWordCodes = new ArrayList<>();
		bannedAndFixedWordCodes.add(WordCode.CodeValues.BANNED.getName());
		List<Vocabulary> bannedOrFixedVocabularyRules;
		Vocabulary bannedRule;
		String currentValue;
		for (String text : originalTextSplit) {
			// set current value as the current string text in the originalTextSplit
			currentValue = text;
			// find all entries in vocabulary matching current text and is banned or fix
			bannedOrFixedVocabularyRules = this.vocabularyRepository.
					findByKeyWordTextIgnoreCaseAndWordCodeAttributeIn(text, bannedAndFixedWordCodes);
			// if there is either a ban or fix vocabulary record
			if (!bannedOrFixedVocabularyRules.isEmpty()) {
				bannedRule = this.getVocabularyRuleForSpecificWordCode(
						bannedOrFixedVocabularyRules, WordCode.CodeValues.BANNED.getName());
				// if there is a ban record, do not add this word to return list
				if (bannedRule != null) {
					continue;
				}
			}
			// add current value to return list
			toReturn.add(currentValue);
		}
		return this.convertToFullTextValue(toReturn);
	}

	/**
	 * Find the vocabulary based on textToValidate.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @return a list of vocabularies.
	 */
	public List<Vocabulary> findVocabularies(String textToValidate) {
	    textToValidate = textToValidate.replaceAll(String.valueOf((char) 160), SpellCheckService.SINGLE_SPACE);
		List<String> originalTextSplit = Arrays.asList(textToValidate.split(SpellCheckService.ANY_SPACE_REGEX));
		List<Vocabulary> returnList = new ArrayList<>();
		List<String> wordList = new ArrayList<String>();
		for (String text : originalTextSplit) {
			String word = text.toLowerCase().trim();
			if (!wordList.contains(word)) {
				wordList.add(word);
			}
		}
		List<Vocabulary> vocabularies = this.vocabularyRepository.findByKeyWordTextIgnoreCaseIn(wordList);
		if(CollectionUtils.isNotEmpty(vocabularies)){
			returnList.addAll(vocabularies);
		}
		return returnList;
	}

	/**
	 * Find the vocabulary based on the list of suggestions.
	 *
	 * @param suggestions the list of suggestions to search for.
	 * @return a list of vocabularies.
	 */
	public List<Vocabulary> findVocabulariesBySuggestion(List<Suggestion> suggestions) {
		List<Vocabulary> returnList = new ArrayList<>();
		List<String> wordList = new ArrayList<String>();
		for (Suggestion suggestion : suggestions) {
			String word = suggestion.getKey().getCorrectedText().trim();
			if (!wordList.contains(word)) {
				wordList.add(word);
			}
		}
		List<Vocabulary> vocabularies = this.vocabularyRepository.findByKeyWordTextIgnoreCaseIn(wordList);
		if(CollectionUtils.isNotEmpty(vocabularies)){
			returnList.addAll(vocabularies);
		}
		return returnList;
	}

	/**
	 * Find suggestions based on textToValidate.
	 *
	 * @param textToValidate The text to validate against the vocabulary rules.
	 * @return the list of vocabularies.
	 */
	public List<Suggestion> findSuggestions(String textToValidate) {
	    textToValidate = textToValidate.replaceAll(String.valueOf((char) 160), SpellCheckService.SINGLE_SPACE);
		List<String> originalTextSplit = Arrays.asList(textToValidate.split(SpellCheckService.ANY_SPACE_REGEX));
		List<Suggestion> returnList = new ArrayList<>();
		List<String> wordList = new ArrayList<String>();
		for (String text : originalTextSplit) {
			String word = text.toLowerCase().trim();
			if (!wordList.contains(word)) {
				wordList.add(word);
			}
		}
		List<Suggestion> suggestions = this.suggestionRepository.findByKeyWordTextIgnoreCaseIn(wordList);
		if(CollectionUtils.isNotEmpty(suggestions)){
			returnList.addAll(suggestions);
		}
		return returnList;
	}

	/**
	 * Find all suggestions.
	 *
	 * @return the list of suggestion.
	 */
	public List<Suggestion> findAllSuggestions() {
		return this.suggestionRepository.findAll();
	}

	/**
	 * Add a new vocabulary to vocabulary table in edit mode.
	 *
	 * @param wordText the word text to add.
	 * @param wordCode the word code to add.
	 * @param caseCode the case code to add.
	 */
	public void addNewVocabulary(String wordText, String wordCode, String caseCode, String userId) {
		if (caseCode.equalsIgnoreCase(this.LOWER_CASE_CODE) || caseCode.equalsIgnoreCase(this.UPPER_CASE) || caseCode.equalsIgnoreCase(this.CAPITALIZE_FIRST_LETTER)) {
			List<Vocabulary> vocabularies = this.vocabularyRepository.findByKeyWordTextIgnoreCaseAndWordCodeAttributeAndKeyCaseCodeAttribute(wordText, wordCode, caseCode);
			if (vocabularies != null && vocabularies.size() > 0) {
				throw new IllegalArgumentException(PRIMARY_KEY_CONFLICT_MESSAGE);
			} else {
				this.vocabularyRepository.save(this.createVocabularyByInputValue(wordText, caseCode, wordCode, userId));
			}
		} else {
			List<Vocabulary> vocabularies = this.vocabularyRepository.findByKeyWordTextIgnoreCaseAndWordCodeAttributeAndKeyCaseCodeAttribute(wordText, VALID, MIXED);
			if (vocabularies == null || vocabularies.size() == 0) {
				this.vocabularyRepository.save(this.createVocabularyByInputValue(wordText, caseCode, wordCode, userId));
			}
			this.suggestionRepository.save(this.createSuggestionByInputValue(wordText.toLowerCase(), StringUtils.trim(wordText), userId));
		}
	}

	/**
	 * Change status of suggestion vocabulary to vocabulary table in edit mode.
	 *
	 * @param wordText the word text to add.
	 * @param wordCode the word code to add.
	 * @param caseCode the case code to add.
	 */
	public void changeVocabulary(String wordText, String wordCode, String caseCode, String userId) {
		List<Vocabulary> vocabularies = 
				this.vocabularyRepository.findByKeyWordTextAndWordCodeAttribute(wordText, WordCode.CodeValues.SUGGEST.getName());
		if(CollectionUtils.isNotEmpty(vocabularies)){
			this.vocabularyRepository.delete(vocabularies);
		}
		this.vocabularyRepository.save(this.createVocabularyByInputValue(wordText, caseCode, wordCode, userId));
		List<Suggestion> suggestions = this.suggestionRepository.findByKeyWordTextIgnoreCase(wordText);
		if(CollectionUtils.isNotEmpty(suggestions)){
			this.suggestionRepository.delete(suggestions);
		}
	}

	/**
	 * Create a vocabulary object by word text, case code, word code entered.
	 * @param wordText     The word text to add new.
	 * @param caseCode     The case code to add new.
	 * @param wordCode     The word code to add new.
	 * @param userId       The user id handle in this request.
	 * @return The vocabulary object
	 */
	private Vocabulary createVocabularyByInputValue(String wordText, String caseCode, String wordCode, String userId){
		Vocabulary vocabulary = new Vocabulary();
		VocabularyKey key = new VocabularyKey();
		key.setWordText(StringUtils.trim(wordText));
		key.setCaseCodeAttribute(StringUtils.trim(caseCode));
		vocabulary.setKey(key);
		vocabulary.setWordCodeAttribute(StringUtils.trim(wordCode));
		vocabulary.setActive(true);
		vocabulary.setLstUpdtUid(userId);
		vocabulary.setLstUpdtTs(LocalDate.now());
		return vocabulary;
	}

	/**
	 * Create a suggestion object by word text, suggestion text entered.
	 *
	 * @param wordText       The word text to add new.
	 * @param suggestionText The suggestion text to add new.
	 * @param userId         The user id handle in this request.
	 * @return The suggestion object
	 */
	private Suggestion createSuggestionByInputValue(String wordText, String suggestionText, String userId) {
		Suggestion suggestion = new Suggestion();
		SuggestionKey suggestionKey = new SuggestionKey();
		suggestionKey.setWordText(StringUtils.trim(wordText));
		suggestionKey.setCorrectedText(StringUtils.trim(suggestionText));
		suggestion.setKey(suggestionKey);
		suggestion.setActive(true);
		suggestion.setLstUpdtTs(LocalDate.now());
		suggestion.setLstUpdtUid(userId);
		return suggestion;
	}

	/**
	 * This method gets the corrected word from the suggestions table when a vocabulary case code is 'mixed' or
	 * case code is 'fix'. If the rule says it needs to be fixed or mixed, but no suggestions are available, log a
	 * message and return the original text. Otherwise return the first suggestion found.
	 *
	 * @param text           The text that was searched for.
	 * @param caseOrWordCode The case or word code attached to the vocabulary record.
	 * @return The given text if there is no change, null if the word is banned, and the fixed text if it needs
	 * to change.
	 */
	private String getCorrectedWordTextForFixWordCodeOrMixedCaseCode(String text, String caseOrWordCode) {
		List<Suggestion> suggestions = this.suggestionRepository.findByKeyWordTextIgnoreCase(text);
		if (suggestions == null || suggestions.isEmpty()) {
			// If the vocabulary rule says it needs to be fixed or mixed, but there are no suggestions, log a message
			// and return the original text
			logger.debug(String.format(
					SpellCheckService.NO_SUGGESTIONS_FOUND_FOR_FIX_WORD_CODE_OR_MIX_CASE_CODE, text, caseOrWordCode));
			return text;
		} else {
			// There may be more than one record in the suggestion table for this word text. The reason is that the
			// suggestion table is also used for suggesting a word, and there may be more than one suggestion. In
			// the case of a fix or mix correction, we only want the first occurrence.
			return suggestions.get(0).getKey().getCorrectedText().trim();
		}
	}

	/**
	 * Converts the list of validated strings back into one space separated string.
	 *
	 * @param validatedText List of validated text values.
	 * @return The validated text list as one string separated by a space.
	 */
	private String convertToFullTextValue(List<String> validatedText) {
		String returnString = StringUtils.EMPTY;
		for (int indexOfString = 0; indexOfString < validatedText.size(); indexOfString++) {
			if (indexOfString != 0) {
				returnString = returnString.concat(SpellCheckService.SINGLE_SPACE);
			}
			returnString = returnString.concat(validatedText.get(indexOfString));
		}
		return returnString;
	}

	/**
	 * This method takes in one string, then splits it on 'spaces'. Each of these strings is then looked up in the
	 * vocabulary data for records with 'baned' or 'fix' word codes.
	 * 1. If banned, do not add to return list.
	 * 2. If fix, get suggested word and add suggested word to return list.
	 * 3. Else, add word to return list.
	 * After applying these word codes, return the list of strings as it is now spell checked.
	 *
	 * @param textToValidate Text to run vocabulary rules on.
	 * @return A list of strings after removing banned words and replacing fixed words from original textToValidate.
	 */
	private List<String> applySpellCheckRules(String textToValidate) {
		List<String> toReturn = new ArrayList<>();
		textToValidate = textToValidate.replaceAll(String.valueOf((char) 160), SpellCheckService.SINGLE_SPACE);
		List<String> originalTextSplit = Arrays.asList(textToValidate.split(SpellCheckService.ANY_SPACE_REGEX));
		List<String> bannedAndFixedWordCodes = new ArrayList<>();
		bannedAndFixedWordCodes.add(WordCode.CodeValues.BANNED.getName());
		bannedAndFixedWordCodes.add(WordCode.CodeValues.FIX.getName());
		List<Vocabulary> bannedOrFixedVocabularyRules;
		Vocabulary bannedRule;
		Vocabulary fixedRule;
		String currentValue;
		for (String text : originalTextSplit) {
			logger.info(String.format(SpellCheckService.APPLYING_SPELL_CHECK_RULES, text));

			// set current value as the current string text in the originalTextSplit
			currentValue = text;

			// find all entries in vocabulary matching current text and is banned or fix
			bannedOrFixedVocabularyRules = this.vocabularyRepository.
					findByKeyWordTextIgnoreCaseAndWordCodeAttributeIn(text, bannedAndFixedWordCodes);

			// if there is either a ban or fix vocabulary record
			if (!bannedOrFixedVocabularyRules.isEmpty()) {
				bannedRule = this.getVocabularyRuleForSpecificWordCode(
						bannedOrFixedVocabularyRules, WordCode.CodeValues.BANNED.getName());

				// if there is a ban record, do not add this word to return list
				if (bannedRule != null) {
					logger.debug(String.format(
							SpellCheckService.USER_ENTERED_BANNED_WORD, this.userInfo.getUserId(), text));
					logger.info(String.format(SpellCheckService.REMOVING_BANNED_WORD, text));
					continue;
				}
				fixedRule = this.getVocabularyRuleForSpecificWordCode(
						bannedOrFixedVocabularyRules, WordCode.CodeValues.FIX.getName());

				// if there is a fix record, set current value = corrected text found in suggestion table
				if (fixedRule != null) {
					currentValue = this.getCorrectedWordTextForFixWordCodeOrMixedCaseCode(
							text, WordCode.CodeValues.FIX.getName());

					// if the text was replaced, log a message containing both values (otherwise replacement not found)
					if (!currentValue.trim().equalsIgnoreCase(text)) {
						logger.info(String.format(SpellCheckService.MODIFIED_FIXED_WORD, text, currentValue));
					}
				}
			}

			// add current value to return list
			toReturn.add(currentValue);
		}
		return toReturn;
	}

	/**
	 * This method converts a word text into a case versioned word text by precedence:
	 * 1. If MIXED_CASE_CODE rule exists, return corrected word from suggested table
	 * 2. If UPPER_CASE_CODE rule exists, return word text in all capital letters.
	 * 3. If CAPITALIZE_FIRST_LETTER_CODE rule exists, return word text with first letter capitalized.
	 * 4. If index of word is not 0 (first in phrase), and LOWER_CASE_CODE or PREPOSITION_CASE_CODE rule exists,
	 * return word text in all lower case letters.
	 * 5. Else return word text converted to title case.
	 *
	 * @param textToValidate    Text to convert based on highest precedence case code value.
	 * @param firstWordInPhrase Whether the textToValidate is the first word in the original text phrase.
	 * @return Converted text value of given textToValidate.
	 */
	private String getCaseCodeConversionValue(String textToValidate, boolean firstWordInPhrase) {

		// create the list of case codes used for customer friendly description
		List<String> caseCodes = new ArrayList<>();
		caseCodes.add(CaseCode.CodeValues.MIXED.getName());
		caseCodes.add(CaseCode.CodeValues.UPPER_CASE.getName());
		caseCodes.add(CaseCode.CodeValues.CAPITALIZE_FIRST_LETTER.getName());
		caseCodes.add(CaseCode.CodeValues.LOWER_CASE_CODE.getName());
		caseCodes.add(CaseCode.CodeValues.PREPOSITION.getName());

		// get all rules based on the textToValidate and the case codes
		List<Vocabulary> currentVocabularyCaseRules = this.getVocabularyRulesForCaseCodeList(textToValidate, caseCodes);

		// if there are no rules, call getNoCaseRulForText to get the correct value
		if (currentVocabularyCaseRules.isEmpty()) {
			return this.convertToTitleCase(textToValidate);
		}else{
			for (Vocabulary vocabulary : currentVocabularyCaseRules) {
				if (vocabulary.getWordCodeAttribute().trim().equals(WordCode.CodeValues.SUGGEST.getName())) {
					return this.convertToTitleCase(textToValidate);
				}
			}
		}

		// get the vocabulary record by case code precedence
		// 1. MIXED_CASE_CODE: get the corrected text from getCorrectedWordTextForFixWordCodeOrMixedCaseCode()
		if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.MIXED.getName()) != null) {
			return this.getCorrectedWordTextForFixWordCodeOrMixedCaseCode(
					textToValidate, CaseCode.CodeValues.MIXED.getName());
		}

		// 2. UPPER_CASE_CODE: return text.toUpperCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.UPPER_CASE.getName()) != null) {
			return textToValidate.toUpperCase();
		}

		// 3. CAPITALIZE_FIRST_LETTER_CODE: return convertToTitleCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.CAPITALIZE_FIRST_LETTER.getName()) != null) {
			return this.convertToTitleCase(textToValidate);
		}

		// PREPOSITION_CASE_CODE AND first word in phrase: return convertToTitleCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.PREPOSITION.getName()) != null) {
			if (firstWordInPhrase) {
				return this.convertToTitleCase(textToValidate);
			}
		}

		// LOWER_CASE_CODE : return convertToTitleCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.LOWER_CASE_CODE.getName()) != null) {
			return this.convertToTitleCase(textToValidate);
		}

		return textToValidate;
	}

	/**
	 * This method converts a word text into a case versioned word text by precedence:
	 *
	 * @param textToValidate    Text to convert based on highest precedence case code value.
	 * @param firstWordInPhrase Whether the textToValidate is the first word in the original text phrase.
	 * @return Converted text value of given textToValidate.
	 */
	private String getSpellCheckCaseCodeConversionValue(String textToValidate, boolean firstWordInPhrase) {
		// create the list of case codes used for customer friendly description
		List<String> caseCodes = new ArrayList<>();
		caseCodes.add(CaseCode.CodeValues.MIXED.getName());
		caseCodes.add(CaseCode.CodeValues.UPPER_CASE.getName());
		caseCodes.add(CaseCode.CodeValues.CAPITALIZE_FIRST_LETTER.getName());
		caseCodes.add(CaseCode.CodeValues.LOWER_CASE_CODE.getName());
		caseCodes.add(CaseCode.CodeValues.PREPOSITION.getName());

		// get all rules based on the textToValidate and the case codes
		List<Vocabulary> currentVocabularyCaseRules = this.getVocabularyRulesForCaseCodeList(textToValidate, caseCodes);

		// if there are no rules, call getNoCaseRulForText to get the correct value
		if (currentVocabularyCaseRules.isEmpty()) {
			return textToValidate;
		}else{
			for (Vocabulary vocabulary : currentVocabularyCaseRules) {
				if (vocabulary.getWordCodeAttribute().trim().equals(WordCode.CodeValues.SUGGEST.getName())) {
					return textToValidate;
				}
			}
		}

		// get the vocabulary record by case code precedence
		// 1. MIXED_CASE_CODE: get the corrected text from getCorrectedWordTextForFixWordCodeOrMixedCaseCode()
		if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.MIXED.getName()) != null) {
			return this.getCorrectedWordTextForFixWordCodeOrMixedCaseCode(
					textToValidate, CaseCode.CodeValues.MIXED.getName());
		}

		// 2. UPPER_CASE_CODE: return text.toUpperCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.UPPER_CASE.getName()) != null) {
			return textToValidate.toUpperCase();
		}

		// 3. CAPITALIZE_FIRST_LETTER_CODE: return convertToTitleCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.CAPITALIZE_FIRST_LETTER.getName()) != null) {
			return this.convertToTitleCase(textToValidate);
		}

		// PREPOSITION_CASE_CODE AND first word in phrase: return convertToTitleCase()
		if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.PREPOSITION.getName()) != null) {
			if (firstWordInPhrase) {
				return this.convertToTitleCase(textToValidate);
			}
		}
		return textToValidate;
	}

	/**
	 * This method converts a word text into a case versioned word text by precedence:
	 *
	 * @param textToValidate    Text to convert based on highest precedence case code value.
	 * @param firstWordInPhrase Whether the textToValidate is the first word in the original text phrase.
	 * @return Converted text value of given textToValidate.
	 */
	private String getRomanceCopySpellCheckCaseCodeConversionValue(String textToValidate, boolean firstWordInPhrase) {
		// create the list of case codes used for customer friendly description
		List<String> caseCodes = new ArrayList<>();
		caseCodes.add(CaseCode.CodeValues.MIXED.getName());
		caseCodes.add(CaseCode.CodeValues.UPPER_CASE.getName());
		caseCodes.add(CaseCode.CodeValues.CAPITALIZE_FIRST_LETTER.getName());
		caseCodes.add(CaseCode.CodeValues.LOWER_CASE_CODE.getName());
		caseCodes.add(CaseCode.CodeValues.PREPOSITION.getName());
		
		// get all rules based on the textToValidate and the case codes
		List<Vocabulary> currentVocabularyCaseRules = this.getVocabularyRulesForCaseCodeList(textToValidate, caseCodes);
		
		// if there are no rules, call getNoCaseRulForText to get the correct value
		if (currentVocabularyCaseRules.isEmpty()) {
			return textToValidate;
		}else{
			for (Vocabulary vocabulary : currentVocabularyCaseRules) {
				if (vocabulary.getWordCodeAttribute().trim().equals(WordCode.CodeValues.SUGGEST.getName())) {
					return textToValidate;
				}
			}
		}
		// New rule: if word rule is valid and lower --> no correction required
		if ((this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.LOWER_CASE_CODE.getName()) != null)
				&& (textToValidate.equals(textToValidate.toLowerCase()))) {
			return textToValidate;
		}
		
		// get the vocabulary record by case code precedence
		// 1. MIXED_CASE_CODE: get the corrected text from getCorrectedWordTextForFixWordCodeOrMixedCaseCode()
		if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.MIXED.getName()) != null) {
			return this.getCorrectedWordTextForFixWordCodeOrMixedCaseCode(
					textToValidate, CaseCode.CodeValues.MIXED.getName());
		}

		// 2. UPPER_CASE_CODE: return text.toUpperCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.UPPER_CASE.getName()) != null) {
			return textToValidate.toUpperCase();
		}

		// 3. CAPITALIZE_FIRST_LETTER_CODE: return convertToTitleCase()
		else if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.CAPITALIZE_FIRST_LETTER.getName()) != null) {
			return this.convertToTitleCase(textToValidate);
		}

		// PREPOSITION_CASE_CODE AND first word in phrase: return convertToTitleCase()
		if (this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.PREPOSITION.getName()) != null) {
			if (firstWordInPhrase) {
				return this.convertToTitleCase(textToValidate);
			}
		}
		return textToValidate;
	}

	/**
	 * When no case rules are found for a text, return:
	 * 1. if(length of text is greater than one character, and does not contain a vowel or 'y') it is probably an
	 * acronym, so return text.toUpperCase()
	 * 2. else return convertToTitleCase(text)
	 *
	 * @param textWithNoCaseRule The text that did not have any case rules found.
	 * @return textWithNoCaseRule.toUpperCase() if string length > 1 and contains no vowels or 'y'; otherwise return
	 * convertToTitleCase(textWithNoCaseRule)
	 */
	private String getNoCaseRuleForText(String textWithNoCaseRule) {

		// if text length is > 1 AND does not contain a vowel or 'y', it is probably an acronym: return text.toUpperCase
		if (textWithNoCaseRule.length() > SpellCheckService.ONE_CHARACTER &&
				this.doesStringNotContainVowel(textWithNoCaseRule.toLowerCase())) {
			return textWithNoCaseRule.toUpperCase();
		}

		// else return convertToTitleCase(text)
		else {
			return this.convertToTitleCase(textWithNoCaseRule);
		}
	}

	/**
	 * This method uses the String method matches to compare a given string to a regex containing any character besides
	 * a vowel. If no vowel is found in the string, return true, otherwise return false.
	 *
	 * @param stringToLookForNoVowel String to match against the no vowel regex.
	 * @return True if no vowel is found, false otherwise.
	 */
	private boolean doesStringNotContainVowel(String stringToLookForNoVowel) {
		return stringToLookForNoVowel.matches(SpellCheckService.NO_VOWEL_REGEX);
	}

	/**
	 * This method returns all vocabulary rules matching a given word text, and a list of case codes.
	 *
	 * @param wordText  Text to search for.
	 * @param caseCodes List of case codes to search for.
	 * @return List of all vocabulary records matching the search criteria.
	 */
	private List<Vocabulary> getVocabularyRulesForCaseCodeList(String wordText, List<String> caseCodes) {
		return this.vocabularyRepository.findByKeyWordTextIgnoreCaseAndKeyCaseCodeAttributeIn(wordText, caseCodes);
	}

	/**
	 * This method returns the vocabulary value matching the case code specified if it exists; null otherwise.
	 *
	 * @param currentVocabularyRules List of all vocabulary rules matching a given text.
	 * @param caseCode               The case code to find.
	 * @return The vocabulary with the given case code if found, null otherwise.
	 */
	private Vocabulary getVocabularyRuleForSpecificCaseCode(List<Vocabulary> currentVocabularyRules, String caseCode) {
		for (Vocabulary vocabulary : currentVocabularyRules) {
			if (vocabulary.getKey().getCaseCodeAttribute().trim().equals(caseCode) && vocabulary.getActive()) {
				return vocabulary;
			}
		}

		// if a rule for the given caseCode was not found, return null
		return null;
	}

	/**
	 * This method returns the vocabulary value matching the word code specified if it exists; null otherwise.
	 *
	 * @param currentVocabularyRules List of all vocabulary rules matching a given text.
	 * @param wordCode               The word code to find.
	 * @return The vocabulary with the given word code if found, null otherwise.
	 */
	private Vocabulary getVocabularyRuleForSpecificWordCode(List<Vocabulary> currentVocabularyRules, String wordCode) {
		for (Vocabulary vocabulary : currentVocabularyRules) {
			if (vocabulary.getWordCodeAttribute().trim().equals(wordCode) && vocabulary.getActive()) {
				return vocabulary;
			}
		}

		// if a rule for the given wordCode was not found, return null
		return null;
	}

	/**
	 * Takes a string and converts to title case value (Capitalize first letter, lower case rest).
	 *
	 * @param text Text to convert to title case.
	 * @return Title case converted text value.
	 */
	private String convertToTitleCase(String text) {
		return StringUtils.capitalize(text.toLowerCase());
	}
}
