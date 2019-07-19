package com.heb.pm.dictionary;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Suggestion entity.
 *
 * @author m314029
 * @since 2.7.0
 */
public interface SuggestionRepository extends JpaRepository<Suggestion, SuggestionKey> {

    String SEARCH_SUGGESTION_BY_WORD_TEXT_LIST = "select sug from Suggestion sug where sug.key.wordText in :wordTextList";

    /**
     * Searches for suggestion records matching given word text ignoring case.
     *
     * @param wordText Word text to search for records on.
     * @return List of all suggestions with given word text.
     */
    List<Suggestion> findByKeyWordTextIgnoreCase(String wordText);

    /**
     * Searches for suggestion records matching given word text ignoring case.
     *
     * @param wordTextList The list of Word text to search for records on.
     * @return List of all suggestions with given word text.
     */
    List<Suggestion> findByKeyWordTextIgnoreCaseIn(List<String> wordTextList);

    /**
     * Searches for suggestion records matching given word text ignoring case.
     *
     * @param wordTextList The list of Word text to search for records on.
     * @return List of all suggestions with given word text.
     */
    @Query(SuggestionRepository.SEARCH_SUGGESTION_BY_WORD_TEXT_LIST)
    List<Suggestion> findSuggestionByWordText(@Param("wordTextList") List<String> wordTextList, Sort sort);
}
