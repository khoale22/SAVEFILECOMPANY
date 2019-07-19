/*
 * DictionaryService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB
 */

package com.heb.pm.dictionary;

import com.heb.util.jpa.PageableResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service endpoint for functions related to viewing and modifying dictionary.
 *
 * @author vn47792
 * @since 2.7.0
 */
@Service
public class DictionaryService {

	@Autowired
	VocabularyRepository vocabularyRepository;

	@Autowired
	VocabularyRepositoryWithCount vocabularyRepositoryWithCount;

	@Autowired
	SuggestionRepository suggestionRepository;

	@Autowired
	CaseCodeRepository caseCodeRepository;

	@Autowired
	WordCodeRepository wordCodeRepository;

	//log messages
	private static final String MESSAGE_BY_THROWS_EXIST_WORDS = "Words already exist in the dictionary.";

	/**
	 * Sets the VocabularyRepository for this object to use to fetch data. This is mainly for
	 * testing.
	 *
	 * @param repository The VocabularyRepository for this object to use.
	 */
	public void setRepository(VocabularyRepository repository) {
		this.vocabularyRepository = repository;
	}

	/**
	 * Fetches a window of all the vocabulary records.
	 *
	 * @param page
	 * @param pageSize
	 * @return An object containing the vocabulary records.
	 */
	public PageableResult<Vocabulary> findAllVocabulary(int page, int pageSize, boolean includeCount) {
		Pageable dictionaryRequest = new PageRequest(page, pageSize, Vocabulary.getDefaultSort());
		PageableResult<Vocabulary> results;

		Page<Vocabulary> data = this.vocabularyRepositoryWithCount.findAll(dictionaryRequest);
		results = new PageableResult<>(page, data.getTotalPages(), data.getTotalElements(), data.getContent());

		return results;
	}

	/**
	 * Searches vocabulary by  word text, word case and classification.
	 *
	 * @param wordText     The word text to search.
	 * @param caseCode     The case code to search.
	 * @param wordCode     The word code to search.
	 * @param includeCount Tells the repository whether it needs to get the count or not.
	 * @param page         The page of data to pull
	 * @param pageSize     The number of records being asked for.
	 * @return An object that contains  vocabulary records matched search criteria.
	 */
	public PageableResult<Vocabulary> findVocabularyByParameter(String wordText, String caseCode, String wordCode,
																boolean includeCount, int page, int pageSize) {
		Pageable dictionaryRequest = new PageRequest(page, pageSize, Vocabulary.getDefaultSort());
		PageableResult<Vocabulary> results;

		List<String> caseCodeList = null;
		List<String> wordCodeList = null;

		if (caseCode != null && !caseCode.trim().isEmpty()) {
			caseCodeList = new ArrayList<String>(Arrays.asList(caseCode.trim()));
		}

		if (wordCode != null && !wordCode.trim().isEmpty()) {
			wordCodeList = new ArrayList<String>(Arrays.asList(wordCode.trim()));
		}

		if (includeCount) {
			results = searchVocabularyByParameterWithCount(wordText, caseCodeList, wordCodeList, dictionaryRequest);
		} else {
			results = searchVocabularyByParameterWithoutCount(wordText, caseCodeList, wordCodeList, dictionaryRequest);
		}
		return results;
	}

	/**
	 * Returns a list of suggestion data matching with word text.
	 *
	 * @param wordTextList The list of word text to look for data about.
	 * @return An iterable list of vocabulary records.
	 */
	public List<Suggestion> findSuggestionByWordText(List<String> wordTextList) {
		return this.suggestionRepository.findSuggestionByWordText(wordTextList, Suggestion.getDefaultSort());
	}

	/**
	 * Handles search with criteria when user does want   total number of
	 * records that matched the search criteria
	 * matched criteria
	 *
	 * @param wordText          text to search.
	 * @param caseCodeList      The list of case code to search.
	 * @param wordCodeList      The list of word code to search.
	 * @param dictionaryRequest Page information to include page, page size, and sort order.
	 * @return An object containing the vocabulary records based on search criteria
	 */
	private PageableResult<Vocabulary> searchVocabularyByParameterWithCount(String wordText, List<String>
			caseCodeList, List<String> wordCodeList, Pageable dictionaryRequest) {

		Page<Vocabulary> data;

		if (StringUtils.isNotEmpty(wordText)) {
			if (CollectionUtils.isEmpty(caseCodeList)) {
				if (CollectionUtils.isEmpty(wordCodeList)) {
					data = this.vocabularyRepositoryWithCount.findByKeyWordTextIgnoreCaseContaining(wordText,
							dictionaryRequest);
				} else {
					data = this.vocabularyRepositoryWithCount
							.findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeIn(wordText, wordCodeList,
									dictionaryRequest);
				}
			} else {
				if (CollectionUtils.isEmpty(wordCodeList)) {
					data = this.vocabularyRepositoryWithCount
							.findByKeyWordTextIgnoreCaseContainingAndKeyCaseCodeAttributeIn(wordText, caseCodeList,
									dictionaryRequest);
				} else {
					data = this.vocabularyRepositoryWithCount
							.findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeInAndKeyCaseCodeAttributeIn
									(wordText, wordCodeList, caseCodeList, dictionaryRequest);
				}
			}

		} else {
			if (CollectionUtils.isEmpty(caseCodeList)) {
				data = this.vocabularyRepositoryWithCount.findByWordCodeAttributeIn(wordCodeList,
						dictionaryRequest);
			} else {
				if (CollectionUtils.isEmpty(wordCodeList)) {
					data = this.vocabularyRepositoryWithCount
							.findByKeyCaseCodeAttributeIn(caseCodeList, dictionaryRequest);
				} else {
					data = this.vocabularyRepositoryWithCount.findByWordCodeAttributeInAndKeyCaseCodeAttributeIn
							(wordCodeList, caseCodeList, dictionaryRequest);
				}

			}
		}
		return new PageableResult<>(dictionaryRequest.getPageNumber(), data.getTotalPages(), data
				.getTotalElements(),
				data.getContent());
	}

	/**
	 * Handles search with criteria when user does  not want  total number of
	 * records that matched the search criteria
	 *
	 * @param wordText          text to search.
	 * @param caseCodeList      The list of case code to search.
	 * @param wordCodeList      The list of word code to search.
	 * @param dictionaryRequest Page information to include page, page size, and sort order.
	 * @return An object containing the vocabulary records based on search criteria
	 */
	private PageableResult<Vocabulary> searchVocabularyByParameterWithoutCount(String wordText, List<String>
			caseCodeList, List<String> wordCodeList, Pageable dictionaryRequest) {

		List<Vocabulary> data = null;
		PageableResult<Vocabulary> results;

		if (StringUtils.isNotEmpty(wordText)) {
			if (CollectionUtils.isEmpty(caseCodeList)) {
				if (CollectionUtils.isEmpty(wordCodeList)) {
					data = this.vocabularyRepository.findByKeyWordTextIgnoreCaseContaining(wordText,
							dictionaryRequest);
				} else {
					data = this.vocabularyRepository
							.findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeIn(wordText, wordCodeList,
									dictionaryRequest);
				}
			} else {
				if (CollectionUtils.isEmpty(wordCodeList)) {
					data = this.vocabularyRepository
							.findByKeyWordTextIgnoreCaseContainingAndKeyCaseCodeAttributeIn(wordText, caseCodeList,
									dictionaryRequest);
				} else {
					data = this.vocabularyRepository
							.findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeInAndKeyCaseCodeAttributeIn
									(wordText, wordCodeList, caseCodeList, dictionaryRequest);
				}
			}
		} else {
			if (CollectionUtils.isEmpty(wordCodeList)) {
				data = this.vocabularyRepository.findByKeyCaseCodeAttributeIn(caseCodeList, dictionaryRequest);
			} else {
				data = this.vocabularyRepository.findByWordCodeAttributeInAndKeyCaseCodeAttributeIn
						(wordCodeList, caseCodeList, dictionaryRequest);
			}
		}
		return new PageableResult<>(dictionaryRequest.getPageNumber(), data);
	}

	/**
	 * Returns a list of case code data.
	 *
	 * @param sortBy The Sort Type for list data return
	 * @return An iterable list of case code records.
	 */
	public List<CaseCode> findAllCaseCode(Sort sortBy) {
		return this.caseCodeRepository.findAll(sortBy);
	}

	/**
	 * Returns a list of word code data.
	 *
	 * @param sortBy The Sort Type for list data return
	 * @return An iterable list of word code records.
	 */
	public List<WordCode> findAllWordCode(Sort sortBy) {
		return this.wordCodeRepository.findAll(sortBy);
	}

	/**
	 * Add new a vocabulary.
	 *
	 * @param wordText       The word text to add new.
	 * @param caseCode       The case code to add new.
	 * @param wordCode       The word code to add new.
	 * @param suggestionText The suggestion text to add new.
	 * @param userId         The user id handle in this request.
	 * @return The vocabulary insert successfully.
	 */
	@DictionaryTransactional
	public Vocabulary addNewVocabulary(String wordText, String caseCode, String wordCode, String suggestionText,
									   String userId) {
		Vocabulary vocabulary = this.createVocabularyByInputValue(wordText, caseCode, wordCode, userId);
		Vocabulary vocabularyExist = this.vocabularyRepository.findOne(vocabulary.getKey());
		if (vocabularyExist != null) {
			throw new IllegalArgumentException(MESSAGE_BY_THROWS_EXIST_WORDS);
		} else {
			vocabulary = this.vocabularyRepository.save(vocabulary);
			//Add new Suggestion Data
			if (StringUtils.isNotEmpty(StringUtils.trim(suggestionText))) {
				this.suggestionRepository.save(this.createSuggestionByInputValue(wordText, suggestionText, userId));
			}
		}
		return vocabulary;
	}

	/**
	 * Create a vocabulary object by word text, case code, word code entered.
	 *
	 * @param wordText The word text to add new.
	 * @param caseCode The case code to add new.
	 * @param wordCode The word code to add new.
	 * @param userId   The user id handle in this request.
	 * @return The vocabulary object
	 */
	private Vocabulary createVocabularyByInputValue(String wordText, String caseCode, String wordCode, String userId) {
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
	 * Delete a list of vocabulary in dictionary.
	 *
	 * @param vocabularies The list of vocabulary requested delete.
	 */
	@DictionaryTransactional
	public void deleteVocabularies(List<Vocabulary> vocabularies) {
		this.vocabularyRepository.delete(vocabularies);
	}

	/**
	 * Update a list of vocabulary in dictionary.
	 *
	 * @param vocabulariesDelete The list of vocabulary requested delete.
	 * @param vocabulariesSave   The list of vocabulary requested update.
	 * @param userId             The user id handle in this request.
	 */
	@DictionaryTransactional
	public List<Vocabulary> updateVocabularies(List<Vocabulary> vocabulariesDelete, List<Vocabulary> vocabulariesSave,
											   String userId) {
		if (vocabulariesDelete != null && !vocabulariesDelete.isEmpty()) {
			this.vocabularyRepository.delete(vocabulariesDelete);
		}
		if (vocabulariesSave != null && !vocabulariesSave.isEmpty()) {
			this.resolveVocabulary(vocabulariesSave, userId);
			this.vocabularyRepository.save(vocabulariesSave);
		}
		return vocabulariesSave;
	}

	/**
	 * Delete a list of suggestion in dictionary.
	 *
	 * @param suggestions The list of suggestion requested delete.
	 */
	@DictionaryTransactional
	public void deleteSuggestions(List<Suggestion> suggestions) {
		this.suggestionRepository.delete(suggestions);
	}

	/**
	 * Update a list of suggestion in dictionary.
	 *
	 * @param suggestionsDelete The list of suggestion requested delete.
	 * @param suggestionsSave   The list of suggestion requested save.
	 * @param userId            The user id handle in this request.
	 */
	@DictionaryTransactional
	public List<Suggestion> updateSuggestions(List<Suggestion> suggestionsDelete, List<Suggestion> suggestionsSave,
											  String userId) {
		if (suggestionsDelete != null && !suggestionsDelete.isEmpty()) {
			this.suggestionRepository.delete(suggestionsDelete);
		}
		if (suggestionsSave != null && !suggestionsSave.isEmpty()) {
			this.resolveSuggestion(suggestionsSave, userId);
			return this.suggestionRepository.save(suggestionsSave);
		}
		return suggestionsSave;
	}

	/**
	 * Set default value for last update time and user id for vocabulary when handle update.
	 *
	 * @param vocabularies The list of vocabulary requested update.
	 * @param userId       The user id requested update.
	 */
	private void resolveVocabulary(List<Vocabulary> vocabularies, String userId) {
		for (Vocabulary vocabulary : vocabularies) {
			vocabulary.setActive(true);
			vocabulary.setLstUpdtUid(userId);
			vocabulary.setLstUpdtTs(LocalDate.now());
		}
	}

	/**
	 * Set default value for last update time and user id for suggestion when handle update.
	 *
	 * @param suggestions The list of suggestion requested update.
	 * @param userId      The user id requested update.
	 */
	private void resolveSuggestion(List<Suggestion> suggestions, String userId) {
		for (Suggestion suggestion : suggestions) {
			suggestion.setActive(true);
			suggestion.setLstUpdtUid(userId);
			suggestion.setLstUpdtTs(LocalDate.now());
		}
	}
}