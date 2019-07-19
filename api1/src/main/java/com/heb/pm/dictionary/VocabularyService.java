package com.heb.pm.dictionary;

import com.heb.util.controller.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for managing vocabulary in Dictionary data.
 *
 * @author m314029
 * @since 2.7.0
 */
@Service
public class VocabularyService {

	private static final Logger logger = LoggerFactory.getLogger(VocabularyService.class);

	private static final String ANY_SPACE_REGEX = "\\s+";
	private static final String NO_VOWEL_REGEX = "^[^aeiouy]+$";
	private static final String SINGLE_SPACE = " ";
	private static final int ONE_CHARACTER = 1;

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
		for(String text : spellCheckAppliedWords){
			caseCorrectedText.add(this.getCaseCodeConversionValue(text));
		}
		return this.convertToFullTextValue(caseCorrectedText);
	}

	/**
	 * This method gets the corrected word from the suggestions table when a vocabulary case code is 'mixed' or
	 * case code is 'fix'. If the rule says it needs to be fixed or mixed, but no suggestions are available, log a
	 * message and return the original text. Otherwise return the first suggestion found.
	 *
	 * @param text The text that was searched for.
	 * @param caseOrWordCode The case or word code attached to the vocabulary record.
	 * @return The given text if there is no change, null if the word is banned, and the fixed text if it needs
	 * to change.
	 */
	private String getCorrectedWordTextForFixWordCodeOrMixedCaseCode(String text, String caseOrWordCode) {
		List<Suggestion> suggestions = this.suggestionRepository.findByKeyWordTextIgnoreCase(text);
		if(suggestions == null || suggestions.isEmpty()){
			// If the vocabulary rule says it needs to be fixed or mixed, but there are no suggestions, log a message
			// and return the original text
			logger.debug(String.format(
					VocabularyService.NO_SUGGESTIONS_FOUND_FOR_FIX_WORD_CODE_OR_MIX_CASE_CODE, text, caseOrWordCode));
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
		for(int indexOfString = 0; indexOfString < validatedText.size(); indexOfString++){
			if(indexOfString != 0){
				returnString = returnString.concat(VocabularyService.SINGLE_SPACE);
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
		List<String> originalTextSplit = Arrays.asList(textToValidate.split(VocabularyService.ANY_SPACE_REGEX));
		List<String> bannedAndFixedWordCodes = new ArrayList<>();
		bannedAndFixedWordCodes.add(WordCode.CodeValues.BANNED.getName());
		bannedAndFixedWordCodes.add(WordCode.CodeValues.FIX.getName());
		List<Vocabulary> bannedOrFixedVocabularyRules;
		Vocabulary bannedRule;
		Vocabulary fixedRule;
		String currentValue;
		for(String text : originalTextSplit) {
			logger.info(String.format(VocabularyService.APPLYING_SPELL_CHECK_RULES, text));

			// set current value as the current string text in the originalTextSplit
			currentValue = text;

			// find all entries in vocabulary matching current text and is banned or fix
			bannedOrFixedVocabularyRules = this.vocabularyRepository.
					findByKeyWordTextIgnoreCaseAndWordCodeAttributeIn(text, bannedAndFixedWordCodes);

			// if there is either a ban or fix vocabulary record
			if(!bannedOrFixedVocabularyRules.isEmpty()){
				bannedRule = this.getVocabularyRuleForSpecificWordCode(
						bannedOrFixedVocabularyRules, WordCode.CodeValues.BANNED.getName());

				// if there is a ban record, do not add this word to return list
				if (bannedRule != null) {
					logger.debug(String.format(
							VocabularyService.USER_ENTERED_BANNED_WORD, this.userInfo.getUserId(), text));
					logger.info(String.format(VocabularyService.REMOVING_BANNED_WORD, text));
					continue;
				}
				fixedRule = this.getVocabularyRuleForSpecificWordCode(
						bannedOrFixedVocabularyRules, WordCode.CodeValues.FIX.getName());

				// if there is a fix record, set current value = corrected text found in suggestion table
				if (fixedRule != null) {
					currentValue = this.getCorrectedWordTextForFixWordCodeOrMixedCaseCode(
							text, WordCode.CodeValues.FIX.getName());

					// if the text was replaced, log a message containing both values (otherwise replacement not found)
					if(!currentValue.trim().equalsIgnoreCase(text)) {
						logger.info(String.format(VocabularyService.MODIFIED_FIXED_WORD, text, currentValue));
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
	 * 4. Else return word text converted to title case.
	 *
	 * @param textToValidate Text to convert based on highest precedence case code value.
	 * @return Converted text value of given textToValidate.
	 */
	private String getCaseCodeConversionValue(String textToValidate) {

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
		if(currentVocabularyCaseRules.isEmpty()){
			return this.getNoCaseRuleForText(textToValidate);
		}

		// get the vocabulary record by case code precedence
		// 1. MIXED_CASE_CODE: get the corrected text from getCorrectedWordTextForFixWordCodeOrMixedCaseCode()
		if(this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.MIXED.getName()) != null){
			return this.getCorrectedWordTextForFixWordCodeOrMixedCaseCode(
					textToValidate, CaseCode.CodeValues.MIXED.getName());
		}

		// 2. UPPER_CASE_CODE: return text.toUpperCase()
		else if(this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.UPPER_CASE.getName()) != null){
			return textToValidate.toUpperCase();
		}

		// 3. CAPITALIZE_FIRST_LETTER_CODE: return convertToTitleCase()
		else if(this.getVocabularyRuleForSpecificCaseCode(
				currentVocabularyCaseRules, CaseCode.CodeValues.CAPITALIZE_FIRST_LETTER.getName()) != null) {
			return this.convertToTitleCase(textToValidate);
		}
		// 4. else LOWER_CASE_CODE OR PREPOSITION_CASE_CODE AND first word in phrase: return convertToTitleCase()
		else {
			return this.convertToTitleCase(textToValidate);
		}
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
		if(textWithNoCaseRule.length() > VocabularyService.ONE_CHARACTER &&
				this.doesStringNotContainVowel(textWithNoCaseRule.toLowerCase())){
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
		return stringToLookForNoVowel.matches(VocabularyService.NO_VOWEL_REGEX);
	}

	/**
	 * This method returns all vocabulary rules matching a given word text, and a list of case codes.
	 *
	 * @param wordText Text to search for.
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
	 * @param caseCode The case code to find.
	 * @return The vocabulary with the given case code if found, null otherwise.
	 */
	private Vocabulary getVocabularyRuleForSpecificCaseCode(List<Vocabulary> currentVocabularyRules, String caseCode) {
		for(Vocabulary vocabulary : currentVocabularyRules){
			if(vocabulary.getKey().getCaseCodeAttribute().trim().equals(caseCode) && vocabulary.getActive()){
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
	 * @param wordCode The word code to find.
	 * @return The vocabulary with the given word code if found, null otherwise.
	 */
	private Vocabulary getVocabularyRuleForSpecificWordCode(List<Vocabulary> currentVocabularyRules, String wordCode) {
		for(Vocabulary vocabulary : currentVocabularyRules){
			if(vocabulary.getWordCodeAttribute().trim().equals(wordCode) && vocabulary.getActive()){
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
