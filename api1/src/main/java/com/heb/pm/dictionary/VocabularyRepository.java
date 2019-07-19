/*
 * VocabularyRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for Vocabulary entity.
 *
 * @author m314029
 * @since 2.7.0
 */
public interface VocabularyRepository extends JpaRepository<Vocabulary, VocabularyKey> {

	/**
	 * Searches for a vocabulary by word text ignoring case, and matching one of the given case codes.
	 *
	 * @param wordText Text to search by.
	 * @param caseCodes The case codes to search by.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByKeyWordTextIgnoreCaseAndKeyCaseCodeAttributeIn(String wordText, List<String> caseCodes);

	/**
	 * Searches for a vocabulary by word text ignoring case, and matching one of the given word codes.
	 *
	 * @param wordText Text to search by.
	 * @param wordCodes The word codes to search by.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByKeyWordTextIgnoreCaseAndWordCodeAttributeIn(String wordText, List<String> wordCodes);

	/**
	 * Searches for a vocabulary by word text, and matching with word code.
	 *
	 * @param wordText Text to search by.
	 * @param wordCodeAttribute The word code to search by.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByKeyWordTextAndWordCodeAttribute(String wordText, String wordCodeAttribute);

	/**
	 * Searches for a vocabulary by word text ignoring case containing.
	 *
	 * @param wordText    Text to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of vocabularies matching the search.
	 */
	@Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
			"ORDER BY CASE WHEN LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
	List<Vocabulary> findByKeyWordTextIgnoreCaseContaining(@Param(value = "wordText")String wordText, Pageable pageRequest);

	/**
	 * Searches for a vocabulary by word text ignoring case containing, and matching one of the given case codes.
	 *
	 * @param wordText    Text to search by.
	 * @param caseCodes   The case codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of vocabularies matching the search.
	 */
	@Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
			"AND vb.key.caseCodeAttribute in :caseCodes ORDER BY CASE WHEN LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
	List<Vocabulary> findByKeyWordTextIgnoreCaseContainingAndKeyCaseCodeAttributeIn(@Param(value = "wordText")String wordText,
																					@Param(value = "caseCodes")List<String>caseCodes,
																					Pageable pageRequest);

	/**
	 * Searches for a vocabulary by word text ignoring case containing, and matching one of the given word codes.
	 *
	 * @param wordText    Text to search by.
	 * @param wordCodes   The word codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of vocabularies matching the search.
	 */
	@Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
			"AND vb.wordCodeAttribute in :wordCodes ORDER BY CASE WHEN LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
	List<Vocabulary> findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeIn(@Param(value = "wordText")String wordText,
																				 @Param(value = "wordCodes")List<String> wordCodes,
																				 Pageable pageRequest);

	/**
	 * Searches for a vocabulary by word text ignoring case containing, and matching one of the given word codes and
	 * matching one of the given CaseCode.
	 *
	 * @param wordText    Text to search by.
	 * @param wordCodes   The word codes to search by.
	 * @param caseCodes   The case codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of vocabularies matching the search.
	 */
	@Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
			"AND vb.wordCodeAttribute in :wordCodes AND vb.key.caseCodeAttribute in :caseCodes ORDER BY CASE WHEN " +
			"LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
	List<Vocabulary> findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeInAndKeyCaseCodeAttributeIn(@Param(value = "wordText")String wordText,
																										  @Param(value = "wordCodes")List<String> wordCodes,
																										  @Param(value = "caseCodes")List<String> caseCodes,
																										  Pageable pageRequest);

	/**
	 * Searches for matching one of the given word codes.
	 *
	 * @param wordCodes   The word codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByWordCodeAttributeIn(List<String> wordCodes, Pageable pageRequest);

	/**
	 * Searches for , and matching one of the given case codes.
	 *
	 * @param caseCodes   The word codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByKeyCaseCodeAttributeIn(List<String> caseCodes, Pageable pageRequest);

	/**
	 * Searches for  matching one of the given word codes and
	 * matching one of the given CaseCode.
	 *
	 * @param wordCodes   The word codes to search by.
	 * @param caseCodes   The case codes to search by.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByWordCodeAttributeInAndKeyCaseCodeAttributeIn(
			List<String> wordCodes, List<String> caseCodes, Pageable pageRequest);

	/**
	 * Searches for a vocabulary by word text ignoring case.
	 *
	 * @param wordText    Text to search by.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByKeyWordTextIgnoreCase(String wordText);

	/**
	 * Searches for a vocabulary by word text ignoring case.
	 *
	 * @param wordTexts    Text to search by.
	 * @return A list of vocabularies matching the search.
	 */
	List<Vocabulary> findByKeyWordTextIgnoreCaseIn(List<String> wordTexts);

	/**
	 * Search for a vocabulary by word text ignoring case and and word code and case code.
	 *
	 * @param wordText the word text to search for.
	 * @param wordCode the word code to search for.
	 * @param caseCode the case code to search for.
	 * @return the list of vocabularies.
	 */
	List<Vocabulary> findByKeyWordTextIgnoreCaseAndWordCodeAttributeAndKeyCaseCodeAttribute(String wordText, String wordCode, String caseCode);
}