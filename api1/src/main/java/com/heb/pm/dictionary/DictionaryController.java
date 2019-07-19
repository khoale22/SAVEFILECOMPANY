/*
 * DictionaryController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.dictionary;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for functions related to viewing and modifying dictionary.
 *
 * @author vn47792
 * @since 2.7.0
 */

@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + DictionaryController.Dictionary_URL)
@AuthorizedResource(ResourceConstants.UTILITIES_DICTIONARY)
public class DictionaryController {

    protected static final String Dictionary_URL = "/dictionary";
    private static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    //log messages
    private static final String MESSAGE_BY_METHOD = "User %s from IP %s requested %s";

    private static final String FIND_ALL_MESSAGE = "all vocabulary.";
    private static final String FIND_VOCABULARY_BY_PARAMETER_MESSAGE = "vocabulary data for the following wordText = %s, caseCode = %s, wordCode = %s.";
    private static final String FIND_SUGGESTION_BY_WORD_TEXT_MESSAGE = "suggestion data for the following wordText = [%s].";
    private static final String FIND_ALL_CASE_CODE_MESSAGE = "all case code order by %s.";
    private static final String FIND_ALL_WORD_CODE_MESSAGE = "all word code order by %s.";

    private static final String ADD_NEW_VOCABULARY_MESSAGE = "to add new a vocabulary with wordText = %s, caseCode = %s, wordCode = %s and correctText  = %s.";

    private static final String DELETE_VOCABULARIES_MESSAGE = "to delete the list of vocabulary [%s]";
    private static final String DELETE_SUGGESTIONS_MESSAGE= "to delete the list of suggestion [%s]";

    private static final String UPDATE_VOCABULARIES_MESSAGE = "to update the list of vocabulary [%s].";
    private static final String UPDATE_SUGGESTIONS_MESSAGE= "to update the list of suggestion [%s]";

    //return message
    private static final String ADD_VOCABULARIES_SUCCESS_MESSAGE = "A new word is added successfully.";
    private static final String DELETE_VOCABULARIES_SUCCESS_MESSAGE = "Deleted successfully.";
    private static final String UPDATE_VOCABULARIES_SUCCESS_MESSAGE = "Updated successfully.";

    // Defaults related to paging.
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private UserInfo userInfo;
    @Autowired
    private DictionaryService dictionaryService;

    /**
     * Logs a users request for all ingredient statements.
     *
     * @param ipAddress The IP address the user is logged in from.
     * @messageFromMethod messageFromMethod The detail message sent from method.
     */
    private void showLogFromMethodRequest(String ipAddress, String messageFromMethod) {
        DictionaryController.logger.info(String.format(DictionaryController.MESSAGE_BY_METHOD, this.userInfo
                .getUserId(), ipAddress, messageFromMethod));
    }

    /**
     * Fetches a window of all the vocabulary records.
     *
     * @param page         The page of data to pull.
     * @param pageSize     The number of records being asked for.
     * @param includeCount Tells the repository whether it needs to get the count or not.
     * @param request      The HTTP request that initiated this call.
     * @return An iterable list of vocabulary records.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET)
    public PageableResult<Vocabulary> findAllVocabulary(@RequestParam(value = "page", required = false) Integer page,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        @RequestParam(value = "includeCounts", required = false) Boolean includeCount,
                                                        HttpServletRequest request) {
        this.showLogFromMethodRequest(request.getRemoteAddr(), DictionaryController.FIND_ALL_MESSAGE);

        int p = page == null ? DictionaryController.DEFAULT_PAGE : page;
        int s = pageSize == null ? DictionaryController.DEFAULT_PAGE_SIZE : pageSize;

        return this.dictionaryService.findAllVocabulary(p, s, includeCount);
    }

    /**
     * Returns a list of vocabulary data matching with word text, case code or word code.
     *
     * @param wordText     The word text to search.
     * @param caseCode     The case code to search.
     * @param wordCode     The word code to search.
     * @param includeCount Tells the repository whether it needs to get the count or not.
     * @param page         The page of data to pull
     * @param pageSize     The number of records being asked for.
     * @param request      The HTTP request that initiated this call.
     * @return An object that contains  vocabulary records matched search criteria.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "findVocabularyByParameter")
    public PageableResult<Vocabulary> findVocabularyByParameter(@RequestParam(value = "wordText", required = false) String wordText,
                                                                @RequestParam(value = "caseCode", required = false) String caseCode,
                                                                @RequestParam(value = "wordCode", required = false) String wordCode,
                                                                @RequestParam(value = "includeCounts", required = false) Boolean includeCount,
                                                                @RequestParam(value = "page", required = false) Integer page,
                                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                HttpServletRequest request) {
        this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(FIND_VOCABULARY_BY_PARAMETER_MESSAGE, wordText, caseCode, wordCode));

        boolean ic = includeCount == null ? Boolean.FALSE : includeCount;
        int p = page == null ? DictionaryController.DEFAULT_PAGE : page;
        int s = pageSize == null ? DictionaryController.DEFAULT_PAGE_SIZE : pageSize;

        return this.dictionaryService.findVocabularyByParameter(wordText, caseCode, wordCode, includeCount, page, pageSize);
    }

    /**
     * Returns a list of suggestion data matching with word text.
     *
     * @param wordTextList The list of word text to look for data about.
     * @param request      The HTTP request that initiated this call.
     * @return An list of Suggestion records.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "findSuggestionByWordText")
    public List<Suggestion> findSuggestionByWordText(@RequestParam(value = "wordTextList", required = false)
                                                     List<String> wordTextList, HttpServletRequest request) {
        this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(FIND_SUGGESTION_BY_WORD_TEXT_MESSAGE,
                ListFormatter.formatAsString(wordTextList)));

        return this.dictionaryService.findSuggestionByWordText(wordTextList);
    }

    /**
     * Returns a list of case code data.
     *
     * @param sortBy       The field name set to sort.
     * @param request      The HTTP request that initiated this call.
     * @return An list of Case Code records.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "findAllCaseCode")
    public List<CaseCode> findAllCaseCode(@RequestParam(value = "sortBy", required = false) String sortBy,
                                          HttpServletRequest request) {
        this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(FIND_ALL_CASE_CODE_MESSAGE, sortBy));

        Sort sort = sortBy == null ? CaseCode.getDefaultSort() : new Sort( new Sort.Order(Sort.Direction.ASC, sortBy));
        return this.dictionaryService.findAllCaseCode(sort);
    }

    /**
     * Returns a list of word code data.
     *
     * @param sortBy       The field name set to sort.
     * @param request      The HTTP request that initiated this call.
     * @return An list of word code records.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "findAllWordCode")
    public List<WordCode> findAllWordCode(@RequestParam("sortBy") String sortBy, HttpServletRequest request) {
        this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(FIND_ALL_WORD_CODE_MESSAGE, sortBy));

        Sort sort = sortBy == null ? WordCode.getDefaultSort() : new Sort( new Sort.Order(Sort.Direction.ASC, sortBy));
        return this.dictionaryService.findAllWordCode(sort);
    }

    /**
     * Add new a vocabulary.
     * @param wordText           The word text to add new.
     * @param caseCode           The case code to add new.
     * @param wordCode           The word code to add new.
     * @param suggestionDesc     The suggestion text to add new.
     * @param request            The HTTP request that initiated this call.
     * @return Status process data to insert database.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.GET, value = "addNewVocabulary")
    public ModifiedEntity<Vocabulary> addNewVocabulary(@RequestParam(value = "wordText", required = true) String wordText,
                                                       @RequestParam(value = "caseCode", required = true) String caseCode,
                                                       @RequestParam(value = "wordCode", required = true) String wordCode,
                                                       @RequestParam(value = "suggestionDesc", required = true) String suggestionDesc,
                                                        HttpServletRequest request) {
        this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(ADD_NEW_VOCABULARY_MESSAGE, wordText, caseCode,
                wordCode, suggestionDesc));
        Vocabulary vocabulary = this.dictionaryService.addNewVocabulary(wordText, caseCode, wordCode ,suggestionDesc,
                userInfo.getUserId());
        return new ModifiedEntity<>(vocabulary, ADD_VOCABULARIES_SUCCESS_MESSAGE);
    }

    /**
     * Delete a list of vocabulary in dictionary.
     * @param dictionaryVO  The list of vocabulary, suggestion requested to delete.
     * @param request       The HTTP request that initiated this call.
     * @return  The list of vocabulary deleted and a message for the front end.
     */

    @RequestMapping(method = RequestMethod.POST, value = "deleteVocabularies")
    public ModifiedEntity<DictionaryVO> deleteVocabularies(@RequestBody DictionaryVO dictionaryVO,
                                                           HttpServletRequest request) {

        if(dictionaryVO.getVocabularyListDelete() != null && !dictionaryVO.getVocabularyListDelete().isEmpty()){
            this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(DELETE_VOCABULARIES_MESSAGE, ListFormatter
                    .formatAsString(dictionaryVO.getVocabularyListDelete())));

            this.dictionaryService.deleteVocabularies(dictionaryVO.getVocabularyListDelete());
        }

        if(dictionaryVO.getSuggestionListDelete() != null && !dictionaryVO.getSuggestionListDelete().isEmpty()){
            this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(DELETE_SUGGESTIONS_MESSAGE, ListFormatter
                    .formatAsString(dictionaryVO.getSuggestionListDelete())));

            this.dictionaryService.deleteSuggestions(dictionaryVO.getSuggestionListDelete());
        }
        return new ModifiedEntity<>(dictionaryVO, DELETE_VOCABULARIES_SUCCESS_MESSAGE);
    }

    /**
     * Update a list of vocabulary in dictionary.
     * @param dictionaryVO  The list of vocabulary, suggestion requested to delete/update/insert.
     * @param request       The HTTP request that initiated this call.
     * @return  The list of vocabulary updated and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "updateVocabularies")
    public ModifiedEntity<DictionaryVO> updateVocabularies(@RequestBody DictionaryVO dictionaryVO,
                                                               HttpServletRequest request) {

        this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(UPDATE_VOCABULARIES_MESSAGE, ListFormatter
                .formatAsString(dictionaryVO.getVocabularyListUpdate())));

        List<Vocabulary> vocabulariesRet = this.dictionaryService.updateVocabularies(dictionaryVO
                .getVocabularyListDelete(), dictionaryVO.getVocabularyListUpdate(), this.userInfo.getUserId());

        this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(UPDATE_SUGGESTIONS_MESSAGE, ListFormatter
                .formatAsString(dictionaryVO.getSuggestionListUpdate())));

        List<Suggestion> suggestionsRet = this.dictionaryService.updateSuggestions(dictionaryVO
                .getSuggestionListDelete(), dictionaryVO.getSuggestionListUpdate(), this.userInfo.getUserId());
        return new ModifiedEntity<>(dictionaryVO, UPDATE_VOCABULARIES_SUCCESS_MESSAGE);
    }
}