/*
 * VocabularyRepositoryWithCount
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.dictionary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for Vocabulary entity.
 *
 * @author vn47792
 * @since 2.7.0
 */
public interface VocabularyRepositoryWithCount extends JpaRepository<Vocabulary, VocabularyKey> {

    /**
     * Searches for a vocabulary by word text ignoring case.
     *
     * @param wordText  Text to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A Page of vocabularies matching the search.
     */
    @Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
            "ORDER BY CASE WHEN LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
    Page<Vocabulary> findByKeyWordTextIgnoreCaseContaining(@Param(value = "wordText")String wordText, Pageable pageRequest);

    /**
     * Searches for a vocabulary by word text ignoring case containing, and matching one of the given case codes.
     *
     * @param wordText  Text to search by.
     * @param caseCodes The case codes to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A Page of vocabularies matching the search.
     */
    @Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
            "AND vb.key.caseCodeAttribute in :caseCodes ORDER BY CASE WHEN LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
    Page<Vocabulary> findByKeyWordTextIgnoreCaseContainingAndKeyCaseCodeAttributeIn(@Param(value = "wordText")String wordText,
                                                                                    @Param(value = "caseCodes")List<String> caseCodes,
                                                                                    Pageable pageRequest);

    /**
     * Searches for a vocabulary by word text ignoring case containing, and matching one of the given word codes.
     *
     * @param wordText  Text to search by.
     * @param wordCodes The word codes to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A Page of vocabularies matching the search.
     */
    @Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
            "AND vb.wordCodeAttribute in :wordCodes ORDER BY CASE WHEN LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
    Page<Vocabulary> findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeIn(@Param(value = "wordText")String wordText,
                                                                                 @Param(value = "wordCodes")List<String> wordCodes,
                                                                                 Pageable pageRequest);

    /**
     * Searches for a vocabulary by word text ignoring case containing, and matching one of the given word codes and
     * matching one of the given CaseCode.
     *
     * @param wordText  Text to search by.
     * @param wordCodes The word codes to search by.
     * @param caseCodes The case codes to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A Page of vocabularies matching the search.
     */
    @Query("SELECT vb FROM Vocabulary vb WHERE LOWER(vb.key.wordText) LIKE LOWER(CONCAT('%',CONCAT(:wordText, '%'))) " +
            "AND vb.wordCodeAttribute in :wordCodes AND vb.key.caseCodeAttribute in :caseCodes ORDER BY CASE WHEN " +
            "LOWER(vb.key.wordText) = LOWER(:wordText) THEN 1 ELSE 2 END, vb.key.wordText")
    Page<Vocabulary> findByKeyWordTextIgnoreCaseContainingAndWordCodeAttributeInAndKeyCaseCodeAttributeIn(@Param(value = "wordText")String wordText,
                                                                                                          @Param(value = "wordCodes")List<String> wordCodes,
                                                                                                          @Param(value = "caseCodes")List<String> caseCodes,
                                                                                                          Pageable pageRequest);

    /**
     * Searches for matching one of the given word codes.
     *
     * @param wordCodes The word codes to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A page of vocabularies matching the search.
     */
    Page<Vocabulary> findByWordCodeAttributeIn(List<String> wordCodes, Pageable pageRequest);

    /**
     * Searches for , and matching one of the given case codes.
     *
     * @param caseCodes The word codes to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A page of vocabularies matching the search.
     */
    Page<Vocabulary> findByKeyCaseCodeAttributeIn(List<String> caseCodes, Pageable pageRequest);

    /**
     * Searches for  matching one of the given word codes and
     * matching one of the given CaseCode.
     *
     * @param wordCodes The word codes to search by.
     * @param caseCodes The case codes to search by.
     * @param pageRequest Page information to include page, page size, and sort order.
     * @return A page of vocabularies matching the search.
     */
    Page<Vocabulary> findByWordCodeAttributeInAndKeyCaseCodeAttributeIn(
            List<String> wordCodes, List<String> caseCodes, Pageable pageRequest);
}
