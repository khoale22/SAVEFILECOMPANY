/*
 *
 * NutrientsServiceTest.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.Nutrient;
import com.heb.pm.entity.NutrientRoundingRule;
import com.heb.pm.entity.NutrientRoundingRuleKey;
import com.heb.pm.entity.NutrientStatementDetail;
import com.heb.pm.entity.NutrientStatementDetailsKey;
import com.heb.pm.entity.NutrientStatementHeader;
import com.heb.pm.entity.NutrientUom;
import com.heb.pm.repository.NutrientRepository;
import com.heb.pm.repository.NutrientRepositoryWithCount;
import com.heb.pm.repository.NutrientRoundingRuleRepository;
import com.heb.pm.repository.NutrientStatementHeaderRepository;
import com.heb.pm.repository.NutrientStatementHeaderRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;

/**
 •	This class is used to represent a upc swap. *
 •	@author s753601
 •	@since 2.4.0

 */
public class NutrientsServiceTest {

    private static final int testPage = 0;
    private static final int testPageSize = 2;
    private static final int MAX_ROUNDING_RULE_SIZE = 9999;
    private static final int MIN_ROUNDING_RULE_SIZE = 0;
    private static final Long MAX_NUTRIENT_STATEMENT = 9999999L;
    private static final Long MAX_NUTRIENT_CODE = 999L;

	@InjectMocks
	private NutrientsService nutrientsService;

    @Mock
    private NutrientRepository repository;

//    @Mock
//    private NutrientStatementDetailRepository nutrientStatementDetailRepository;

    @Mock
    private NutrientRoundingRuleRepository roundingRuleRepository;

    @Mock
    private NutrientRepositoryWithCount repositoryWithCount;

    @Mock
    private NutrientStatementHeaderRepository nutrientStatementHeaderRepository;

    @Mock
	private NutrientStatementHeaderRepositoryWithCount nutrientStatementHeaderRepositoryWithCount;

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

    /**
     * This method is tested by the next two methods.
     */
	@Test
	public void testFindByNutrientCode() {

    }

    /**
     * Tests the searchNutrientCodeWithoutCounts method in nutrient service
     */
    @Test
	public void testSearchNutrientCodeWithoutCounts(){
//		List<Nutrient> testData = this.repository.findByNutrientCodeIn(getDefaultNutrientCodeList(), getDefaultNutrientRequest());
//		PageableResult<Nutrient> defaultCase = new PageableResult<Nutrient>(testPage, testData);
//		PageableResult<Nutrient> testResult = this.nutrientsService.findByNutrientCode(getDefaultNutrientCodeList(), false, testPage, testPageSize);
//		Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
//		Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
//		Assert.assertEquals(defaultCase.getData(), testResult.getData());
	}

    /**
     * Tests the searchNutrientCodeWithCounts method in nutrient service
     */
	@Test
	public void testSearchNutrientCodeWithCounts(){
//		Page<Nutrient> page = new PageImpl<>(this.getDefaultNutrientList(), null, 0);
//		PageableResult<Nutrient> defaultCase = new PageableResult<Nutrient>(getDefaultNutrientRequest().getPageNumber(), page.getTotalPages(), page.getTotalElements(), page.getContent());
//        Mockito.when(this.repositoryWithCount.findByNutrientCodeIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(page);
//		PageableResult testResult = this.nutrientsService.findByNutrientCode(getDefaultNutrientCodeList(), true, testPage, testPageSize);
//		Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
//		Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
//		Assert.assertEquals(defaultCase.getData(), testResult.getData());
	}

    /**
     * This method is used and tested in the next two tests.
     */
	@Test
	public void testFindByNutrientDescription() {

	}

    /**
     * Tests the searchDescriptionWithoutCounts method in nutrient service
     */
	@Test
	public void testSearchDescriptionWithoutCounts(){
	    List<Nutrient> testData = this.repository.findByNutrientDescriptionContains(getDefaultNutrientDescriptionList(), getDefaultNutrientRequest());
        PageableResult<Nutrient> defaultCase = new PageableResult<Nutrient>(getDefaultNutrientRequest().getPageNumber(), testData);
        PageableResult<Nutrient> testResult = this.nutrientsService.findByNutrientDescription(getDefaultNutrientDescriptionList(), false, testPage, testPageSize);
        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
        Assert.assertEquals(defaultCase.getData(), testResult.getData());
	}

    /**
     * Tests the searchDescriptionWithCounts method in nutrient service
     */
	@Test
	public void testSearchDescriptionWithCounts(){
        Page<Nutrient> page = new PageImpl<>(this.getDefaultNutrientList(), null, 0);
        PageableResult<Nutrient> defaultCase = new PageableResult<Nutrient>(getDefaultNutrientRequest().getPageNumber(), page.getTotalPages(), page.getTotalElements(), page.getContent());
        Mockito.when(this.repositoryWithCount.findByNutrientDescriptionContains(Mockito.anyList(), Mockito.any(Pageable.class))).thenReturn(page);
        PageableResult testResult = this.nutrientsService.findByNutrientDescription(getDefaultNutrientDescriptionList(), true, testPage, testPageSize);
        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
        Assert.assertEquals(defaultCase.getData(), testResult.getData());
	}

    /**
     * This method is used and tested by the next two tests.
     */
	@Test
	public void testFindAll() {

	}

    /**
     * Tests the findAllWithoutCount method in nutrient service
     */
//    @Test
//    public void testFindAllWithoutCount() {
//        List<Nutrient> testData = this.repository.findAllByPage(getDefaultNutrientRequest());
//        PageableResult<Nutrient> defaultCase = new PageableResult<Nutrient>(getDefaultNutrientRequest().getPageNumber(), testData);
//        PageableResult<Nutrient> testResult = this.nutrientsService.findAll(false, testPage);
//        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
//        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
//        Assert.assertEquals(defaultCase.getData(), testResult.getData());
//    }

    /**
     * Tests the findAllWithCount method in nutrient service
     */
    @Test
    public void testFindAllWithCount(){
        Page<Nutrient> page = new PageImpl<>(this.getDefaultNutrientList(), null, 0);
        PageableResult<Nutrient> defaultCase = new PageableResult<Nutrient>(getDefaultNutrientRequest().getPageNumber(), page.getTotalPages(), page.getTotalElements(), page.getContent());
        Mockito.when(repositoryWithCount.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        PageableResult testResult = this.nutrientsService.findAll(true, testPage, testPageSize);
        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
        Assert.assertEquals(defaultCase.getData(), testResult.getData());
    }

    /**
     * This method is used and tested by the next two tests.
     */
	@Test
	public void testFindAllStatementId() {

	}

    /**
     * Tests the findAllStatementIdWithoutCount method in nutrient service
     */
//	@Test
//	public void testFindAllStatementIdWithoutCount(){
//        List<NutrientStatementHeader> testData = this.nutrientStatementHeaderRepository.findAllByPage(getDefaultNutrientRequest());
//        PageableResult<NutrientStatementHeader> defaultCase = new PageableResult<NutrientStatementHeader>(getDefaultNutrientRequest().getPageNumber(), testData);
//        PageableResult<NutrientStatementHeader> testResult = this.nutrientsService.findAllStatementId(false, testPage);
//        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
//        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
//        Assert.assertEquals(defaultCase.getData(), testResult.getData());
//	}

    /**
     * Tests the findAllStatementIdWithCount method in nutrient service
     */
//	@Test
//	public void testFindAllStatementIdWithCount(){
//		Page<NutrientStatementHeader> page = new PageImpl<>(this.nutrientStatementHeaderRepository.findAllByPage(getDefaultNutrientRequest()));
//        PageableResult<NutrientStatementHeader> defaultCase = new PageableResult<>(getDefaultNutrientRequest().getPageNumber(), page.getTotalPages(), page.getTotalElements(), page.getContent());
//        Mockito.when(this.nutrientStatementHeaderRepositoryWithCount.findAll(Mockito.any(Pageable.class))).thenReturn(page);
//        PageableResult testResult = this.nutrientsService.findAllStatementId(true, testPage);
//        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
//        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
//        Assert.assertEquals(defaultCase.getData(), testResult.getData());
//	}

    /**
     * Tests the updateNutritionData method in nutrient service
     */
	@Test
	public void testUpdateNutritionData() {
	    Nutrient nutrient = buildcompleteNutrient();
		LocalDate testTime = LocalDate.now();
		nutrient.setLstModifiedDate(testTime);
		Mockito.when(this.repository.save(Mockito.any(Nutrient.class))).then(returnsFirstArg());
		Nutrient testNutrient = this.nutrientsService.updateNutrient(getDefaultNutrient());
		nutrient.setUomCode(nutrient.getNutrientUom().getNutrientUomCode());
		Assert.assertEquals(nutrient.getLstModifiedDate(), testNutrient.getLstModifiedDate());
		Assert.assertEquals(nutrient.getUomCode(), testNutrient.getUomCode());
	}
	
    /**
     * Tests the addNutrientStatementData method in nutrient service
     */
	@Test
	public void testAddNutrientStatementData() {
        NutrientStatementHeader nutrientStatementHeader = buildcompleteNutrient().getNutrientStatementDetails().get(0).getNutrientStatementHeader();

        NutrientStatementHeader testHeader = this.nutrientsService.addNutrientStatementData(nutrientStatementHeader);
        Assert.assertEquals("A", testHeader.getStatementMaintenanceSwitch());
        Assert.assertEquals(1, testHeader.getNutrientStatementDetailList().get(0).getKey().getNutrientStatementNumber());
        Assert.assertEquals(1, testHeader.getNutrientStatementDetailList().get(0).getKey().getNutrientLabelCode());
	}

    /**
     * Single Database Call not tested
     */
	@Test
	public void testFindNutrientRoundingRulesByNutrientCode() {

	}

    /**
     * Single Database Call not tested
     */
	@Test
	public void testFindNutrientByNutrientStatementNumber() {

	}

    /**
     * The method is used in tested in the next two tests.
     */
	@Test
	public void testFindNutrientStatementByNutrientCode() {

	}


    /**
     * Tests the findNutrientStatementByNutrientCodeWithoutCount method in nutrient service
     */
//    @Test
//    public void testFindNutrientStatementByNutrientCodeWithoutCount(){
//        List<NutrientStatementHeader> testSearchResults = getDefaultNutrientStatementHeaderList();
//        Mockito.when(this.nutrientStatementHeaderRepository.findDistinctByNutrientStatementDetailListKeyNutrientLabelCodeIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(testSearchResults);
//	    PageableResult<NutrientStatementHeader> testNutrientStatement = nutrientsService.findNutrientStatementByNutrientCode(getDefaultNutrientCodeList(), false, testPageSize, testPage);
//        Assert.assertEquals(testSearchResults, testNutrientStatement.getData());
//    }


    /**
     * Tests the findNutrientStatementByNutrientCodeWithCount method in nutrient service
     */
//    @Test
//    public void testFindNutrientStatementByNutrientCodeWithCount(){
//        Page<NutrientStatementHeader> page = new PageImpl<>(this.nutrientStatementHeaderRepository.findAllByPage(getDefaultNutrientRequest()));
//        PageableResult<NutrientStatementHeader> defaultCase = new PageableResult<>(getDefaultNutrientRequest().getPageNumber(), page.getTotalPages(), page.getTotalElements(), page.getContent());
//        Mockito.when(this.nutrientStatementHeaderRepositoryWithCount.findDistinctByNutrientStatementDetailListKeyNutrientLabelCodeIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(page);
//        PageableResult<NutrientStatementHeader> testResult = nutrientsService.findNutrientStatementByNutrientCode(getDefaultNutrientCodeList(), true, testPageSize, testPage);
//        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
//        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
//        Assert.assertEquals(defaultCase.getData(), testResult.getData());
//    }

    /**
     * This method calls orderNutrientHeaderBySequence and is tested by testOrderNutientHeaderBySequence
     */
    @Test
    public void testOrderPageBySequence(){

    }


    /**
     * This method tests the orderNutrientHeaderBySequence method in nutrient service.
     */
//    @Test
//    public void testOrderNutrientHeaderBySequence(){
//        List<NutrientStatementHeader> defaultSearchResults = getDefaultNutrientStatementHeaderList();
//        List<NutrientStatementHeader> testSearchResults = getDefaultNutrientStatementHeaderList();
//        addNutrientsOutOfOrder(defaultSearchResults.get(0));
//        addNutrientsOutOfOrder(testSearchResults.get(0));
//        Mockito.when(this.nutrientStatementHeaderRepository.findDistinctByNutrientStatementDetailListKeyNutrientLabelCodeIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(testSearchResults);
//        nutrientsService.findNutrientStatementByNutrientCode(getDefaultNutrientCodeList(), false, testPageSize, testPage);
//        Assert.assertEquals(0.5, testSearchResults.get(0).getNutrientStatementDetailList().get(0).getNutrient().getFedLblSequence(), 0.0);
//        Assert.assertEquals(1.0, testSearchResults.get(0).getNutrientStatementDetailList().get(1).getNutrient().getFedLblSequence(), 0.0);
//        Assert.assertEquals(1.5, testSearchResults.get(0).getNutrientStatementDetailList().get(2).getNutrient().getFedLblSequence(), 0.0);
//    }

    /**
     * This method is used and tested by the next two tests.
     */
	@Test
	public void testFindNutrientStatementByStatementId() {
	}

    /**
     * This method tests the findNutrientStatementByStatementIdWithCount method in nutrient service.
     */
//	@Test
//	public void testFindNutrientStatementByStatementIdWithCount(){
//        Page<NutrientStatementHeader> page = new PageImpl<>(this.nutrientStatementHeaderRepository.findAllByPage(getDefaultNutrientRequest()));
//        PageableResult<NutrientStatementHeader> defaultCase = new PageableResult<>(getDefaultNutrientRequest().getPageNumber(), page.getTotalPages(), page.getTotalElements(), page.getContent());
//        Mockito.when(this.nutrientStatementHeaderRepositoryWithCount.findByNutrientStatementNumberIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(page);
//        PageableResult<NutrientStatementHeader> testResult = nutrientsService.findNutrientStatementByStatementId(getDefaultNutrientCodeList(), true, testPageSize, testPage);
//        Assert.assertEquals(defaultCase.isComplete(), testResult.isComplete());
//        Assert.assertEquals(defaultCase.getPage(), testResult.getPage());
//        Assert.assertEquals(defaultCase.getData(), testResult.getData());
//	}

    /**
     * This method test the findNutrientStatementByStatementIdWithoutCount method in nutrient
     */
//	@Test
//	public void testFindNutrientStatementByStatementIdWithoutCount(){
//        List<NutrientStatementHeader> testSearchResults = getDefaultNutrientStatementHeaderList();
//        Mockito.when(this.nutrientStatementHeaderRepository.findByNutrientStatementNumberIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(testSearchResults);
//        PageableResult<NutrientStatementHeader> testNutrientStatement = nutrientsService.findNutrientStatementByStatementId(getDefaultNutrientCodeList(), false, testPageSize, testPage);
//        Assert.assertEquals(testSearchResults, testNutrientStatement.getData());
//	}
//

    /**
     * This method is used and tested by the next nine tests
     */
	@Test
	public void testUpdate() {

	}

    /**
     * This method tests that the NutrientRoundingRules are valid in nutrient service.
     */
	@Test
	public void testIsValidRoundingRules(){
        Assert.assertEquals(null, this.nutrientsService.update(getNurtrientRoundingRuleList(), null));
        List<NutrientRoundingRule> testList= getNurtrientRoundingRuleList();
	    NutrientRoundingRule testRule = getDefaultNutrientRoundingRule();
	    testRule.setUpperBound(99);
	    NutrientRoundingRuleKey testKey = testRule.getKey();
	    testKey.setLowerBound(100);
	    testList.add(testRule);
	    Assert.assertEquals("Lower boundary cannot be greater than upper boundary. ", this.nutrientsService.update(testList, null));

    }

    /**
     * This method test if a NutrientRoundingRuleKey is updated after getting a lower boundary conflict.
     */
    @Test
    public void testLowerBoundaryConflicts(){
        List<NutrientRoundingRule> testList = getNurtrientRoundingRuleList();
        NutrientRoundingRule testRule = getDefaultNutrientRoundingRule();
        testRule.setUpperBound(50);
        NutrientRoundingRuleKey testKey = testRule.getKey();
        testKey.setLowerBound(1);
        testList.add(testRule);
        testRule = getDefaultNutrientRoundingRule();
        testRule.setUpperBound(55);
        testKey = testRule.getKey();
        testKey.setLowerBound(1);
        testList.add(testRule);
        this.nutrientsService.update(testList, null);
        Assert.assertEquals("Boundary conflict with line: 2. Boundary conflict with line: 3. ", testList.get(0).getRoundingRulesError());
        Assert.assertEquals("Boundary conflict with line: 1. Rule has same lower boundary as line: 3. Boundary conflict with line: 3. ", testList.get(1).getRoundingRulesError());
        Assert.assertEquals("Boundary conflict with line: 1. Rule has same lower boundary as line: 2. Boundary conflict with line: 2. ", testList.get(2).getRoundingRulesError());
    }

    /**
     * This method test if a NutrientRoundingRuleKey is updated after getting a higher boundary conflict.
     */
    @Test
    public void testHigherBoundaryConflicts(){
        List<NutrientRoundingRule> testList = getNurtrientRoundingRuleList();
        NutrientRoundingRule testRule = getDefaultNutrientRoundingRule();
        testRule.setUpperBound(55);
        NutrientRoundingRuleKey testKey = testRule.getKey();
        testKey.setLowerBound(1);
        testList.add(testRule);
        testRule = getDefaultNutrientRoundingRule();
        testRule.setUpperBound(55);
        testKey = testRule.getKey();
        testKey.setLowerBound(10);
        testList.add(testRule);
        this.nutrientsService.update(testList, null);
        Assert.assertEquals("Boundary conflict with line: 2. Boundary conflict with line: 3. ", testList.get(0).getRoundingRulesError());
        Assert.assertEquals("Boundary conflict with line: 1. Rule has same upper lower boundary as line: 3. ", testList.get(1).getRoundingRulesError());
        Assert.assertEquals("Boundary conflict with line: 1. Rule has same upper boundary as line: 2. ", testList.get(2).getRoundingRulesError());
    }

    /**
     * This method test if a NutrientRoundingRuleKey is updated after getting an inner boundary conflict.
     */
    @Test
    public void testInnerBoundaryConflicts(){
        List<NutrientRoundingRule> testList = getNurtrientRoundingRuleList();
        NutrientRoundingRule testRule = getDefaultNutrientRoundingRule();
        testRule.setUpperBound(55);
        NutrientRoundingRuleKey testKey = testRule.getKey();
        testKey.setLowerBound(1);
        testList.add(testRule);
        testRule = getDefaultNutrientRoundingRule();
        testRule.setUpperBound(50);
        testKey = testRule.getKey();
        testKey.setLowerBound(10);
        testList.add(testRule);
        this.nutrientsService.update(testList, null);
        Assert.assertEquals("Boundary conflict with line: 2. Boundary conflict with line: 3. ", testList.get(0).getRoundingRulesError());
        Assert.assertEquals("Boundary conflict with line: 1. Boundary conflict with line: 3. ", testList.get(1).getRoundingRulesError());
        Assert.assertEquals("Boundary conflict with line: 1. Boundary conflict with line: 2. ", testList.get(2).getRoundingRulesError());
    }

    /**
     * This Methods tests if there a gap between rounding rules in nutrient service.
     */
	@Test
	public void testIsGapBetweenRoundingRules(){
	    List<NutrientRoundingRule> invalidList = getNurtrientRoundingRuleList();
	    NutrientRoundingRule newRule = getDefaultNutrientRoundingRule();
	    newRule.setUpperBound(45);
	    NutrientRoundingRuleKey badKey = invalidList.get(0).getKey();
	    badKey.setLowerBound(46);
        invalidList.add(newRule);
        Assert.assertEquals("Gap between: 45 and: 46. ", this.nutrientsService.update(invalidList, null));
	}

    /**
     * This method tests the add method in nutrient service.
     */
	@Test
	public void testAdd() {
	    long yesterday = 1L;
	    long notRequiredFederalLable = 0L;
	    Nutrient testNutrient = getDefaultNutrient();
	    Assert.assertEquals(LocalDate.now().minusDays(yesterday), testNutrient.getLstModifiedDate());
        when(this.repository.findOne(Mockito.anyLong())).thenReturn(null);
        List<Nutrient> testList = new ArrayList<>();
        when(this.repository.findByFedLblSequence(Mockito.anyLong())).thenReturn(testList);
        when(this.repository.save(Mockito.any(Nutrient.class))).then(returnsFirstArg());
        Assert.assertEquals(LocalDate.now(), this.nutrientsService.addNutrient(testNutrient).getLstModifiedDate());
        testList = getDefaultNutrientList();
        when(this.repository.findByFedLblSequence(Mockito.anyLong())).thenReturn(testList);
        when(this.repository.save(Mockito.any(Nutrient.class))).then(returnsFirstArg());
        Assert.assertEquals(LocalDate.now(), this.nutrientsService.addNutrient(getDefaultNutrient()).getLstModifiedDate());
        testNutrient = getDefaultNutrient();
        Assert.assertEquals(LocalDate.now().minusDays(yesterday), testNutrient.getLstModifiedDate());
        testNutrient.setFedLblSequence(notRequiredFederalLable);
        Assert.assertEquals(LocalDate.now(), this.nutrientsService.addNutrient(testNutrient).getLstModifiedDate());
	}

    /**
     * This method confirms an exception is thrown when a nutrient is attempted to be added twice
     */
	@Test(expected=IllegalArgumentException.class)
    public void testThrowAlreadyExistsAdd(){
        when(this.repository.findOne(Mockito.anyLong())).thenReturn(getDefaultNutrient());
        this.nutrientsService.addNutrient(getDefaultNutrient());
    }

    /**
     * This method confirms an exception is thrown when a federal lable already exists.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testThrowFederalLableAlreadyExistsAdd(){
        long illegalNutrientCode = 2L;
        List<Nutrient> testList = getDefaultNutrientList();
        testList.get(0).setNutrientCode(illegalNutrientCode);
        when(this.repository.findOne(Mockito.anyLong())).thenReturn(null);
        when(this.repository.findByFedLblSequence(Mockito.anyLong())).thenReturn(testList);
        this.nutrientsService.addNutrient(getDefaultNutrient());
    }

    /**
     * This method is a direct DB call, and is only tested if it can run without error
     */
	@Test
	public void testDelete() {
        this.nutrientsService.deleteNutrient(0L);
	}

    /**
     * This method tests the findHitsByNutrientCodeList method in nutrient service.
     */
	@Test
	public void testFindHitsByNutrientCodeList() {
	    long matches = 0L;
	    long misMatches = 1L;
	    long firstMisMatched = 1L;
	    int misMatchListSize = 0;
        List<Long> testList = getDefaultNutrientCodeList();
        when(this.repositoryWithCount.findAll(Mockito.anyListOf(Long.class))).thenReturn(new ArrayList<Nutrient>());
        Hits noHitsTest = this.nutrientsService.findHitsByNutrientCodeList(testList);
        Assert.assertEquals(matches, noHitsTest.getMatchCount());
        Assert.assertEquals(misMatches, noHitsTest.getNoMatchCount());
        Assert.assertEquals(firstMisMatched, noHitsTest.getNoMatchList().get(0).longValue());
        matches = 1L;
        misMatches = 0L;
        when(this.repositoryWithCount.findAll(Mockito.anyListOf(Long.class))).thenReturn(getDefaultNutrientList());
        Hits hitsTest = this.nutrientsService.findHitsByNutrientCodeList(testList);
        Assert.assertEquals(matches, hitsTest.getMatchCount());
        Assert.assertEquals(misMatches, hitsTest.getNoMatchCount());
        Assert.assertEquals(misMatchListSize, hitsTest.getNoMatchList().size());
	}

    /**
     * This method tests the findNutrientStatementHitsByNutrientStatementCodes in nutrient services
     */
	@Test
	public void testFindNutrientStatementHitsByNutrientStatementCodes(){
        long matches = 0L;
        long misMatches = 1L;
        long firstMisMatched = 1L;
        int misMatchListSize = 0;
        List<NutrientStatementHeader> badList = getDefaultNutrientStatementHeaderList();
        badList.get(0).setNutrientStatementNumber(0L);
        when(this.nutrientStatementHeaderRepository.findByNutrientStatementNumberIn(Mockito.anyListOf(Long.class))).thenReturn(badList);
        Hits noHitsTest = this.nutrientsService.findNutrientStatementHitsByNutrientStatementCodes(getDefaultNutrientCodeList());
        Assert.assertEquals(matches, noHitsTest.getMatchCount());
        Assert.assertEquals(misMatches, noHitsTest.getNoMatchCount());
        Assert.assertEquals(firstMisMatched, noHitsTest.getNoMatchList().get(0).longValue());
        matches = 1L;
        misMatches = 0L;
        when(this.nutrientStatementHeaderRepository.findByNutrientStatementNumberIn(Mockito.anyListOf(Long.class))).thenReturn(getDefaultNutrientStatementHeaderList());
        Hits hitsTest = this.nutrientsService.findNutrientStatementHitsByNutrientStatementCodes(getDefaultNutrientCodeList());
        Assert.assertEquals(matches, hitsTest.getMatchCount());
        Assert.assertEquals(misMatches, hitsTest.getNoMatchCount());
        Assert.assertEquals(misMatchListSize, hitsTest.getNoMatchList().size());
	}

    /**
     * This method confirmst an exception is thrown when nutrient statement id is too large.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testFindNutrientStatementHitsByNutrientStatementCodesThrow(){
        List<Long> illegalList = new ArrayList<>();
        Long tooLargeNutrientStatement = MAX_NUTRIENT_STATEMENT + 1L;
        illegalList.add(tooLargeNutrientStatement);
        this.nutrientsService.findNutrientStatementHitsByNutrientStatementCodes(illegalList);
    }

    /**
     * This method tests the findNutrientStatementHitsByNutrientCodes method in nutrient services
     */
	@Test
	public void testFindNutrientStatementHitsByNutrientCodes(){
        long matches = 0L;
        long misMatches = 1L;
        long firstMisMatched = 1L;
        int misMatchListSize = 0;
        when(this.nutrientStatementHeaderRepository.findByNutrientStatementDetailListKeyNutrientLabelCodeIn(Mockito.anyListOf(Long.class))).thenReturn(getDefaultNutrientStatementHeaderList());
        Hits noHitsTest = this.nutrientsService.findNutrientStatementHitsByNutrientCodes(getDefaultNutrientCodeList());
        Assert.assertEquals(matches, noHitsTest.getMatchCount());
        Assert.assertEquals(misMatches, noHitsTest.getNoMatchCount());
        Assert.assertEquals(firstMisMatched, noHitsTest.getNoMatchList().get(0).longValue());
        matches = 1L;
        misMatches = 0L;
        when(this.nutrientStatementHeaderRepository.findByNutrientStatementNumberIn(Mockito.anyListOf(Long.class))).thenReturn(getDefaultNutrientStatementHeaderList());
        List<Long> hitList = new ArrayList<>();
        hitList.add(2L);
        Hits hitsTest = this.nutrientsService.findNutrientStatementHitsByNutrientCodes(hitList);
        Assert.assertEquals(matches, hitsTest.getMatchCount());
        Assert.assertEquals(misMatches, hitsTest.getNoMatchCount());
        Assert.assertEquals(misMatchListSize, hitsTest.getNoMatchList().size());
	}

    /**
     * This method confirmst an exception is thrown when nutrient code is too large.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testFindNutrientStatementHitsByNutrientCodesThrow(){
        long tooLargeNutrientCode = MAX_NUTRIENT_CODE + 1L;
        List<Long> illegalList = new ArrayList<>();
        illegalList.add(tooLargeNutrientCode);
        this.nutrientsService.findNutrientStatementHitsByNutrientCodes(illegalList);

    }

    /**
     * This method tests the findByRegularExpression method in nutrient service.
     */
	@Test
	public void testFindByRegularExpression() {
        String badString = "Not a number";
        List<Nutrient> resultingList = getDefaultNutrientList();
        when(this.repository.findByNutrientDescriptionIgnoreCaseAndNutrientCodeNotIn(Mockito.anyString(), Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(getDefaultNutrientList());
        when(this.repository.findByNutrientDescriptionContainingIgnoreCaseAndNutrientCodeNotIn(Mockito.anyString(), Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(resultingList);
        PageableResult<Nutrient> result = this.nutrientsService.findByRegularExpression(badString,getDefaultNutrientCodeList());
        Assert.assertEquals(0, result.getPage());
        Assert.assertEquals(false, result.isComplete());
        Assert.assertEquals(resultingList.get(0), result.getData().iterator().next());
        String goodString = "2";
        when(this.repository.findByNutrientDescriptionIgnoreCaseAndNutrientCodeNotIn(Mockito.anyString(), Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(getDefaultNutrientList());
        when(this.repository.findByNutrientCodeContainingAndNutrientCodeNotInOrNutrientDescriptionContainingIgnoreCaseAndNutrientCodeNotIn(Mockito.anyString(), Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(resultingList);
        result = this.nutrientsService.findByRegularExpression(goodString,getDefaultNutrientCodeList());
        Assert.assertEquals(0, result.getPage());
        Assert.assertEquals(false, result.isComplete());
        Assert.assertEquals(resultingList.get(0), result.getData().iterator().next());
    }

    /**
     * This method tests the findMandatedNutrientByStatementId method in nutrient service
     */
//    @Test
//	public void testFindMandatedNutrientsByStatementId(){
//        when(this.repository.findByFedLblSequenceNot(Mockito.anyDouble())).thenReturn(getDefaultNutrientList());
//        List<NutrientStatementDetail> results = this.nutrientsService.findMandatedNutrientsByStatementId(null);
//        Assert.assertEquals(1, results.size());
//        List<NutrientStatementDetail> matchList = getDefaultNutrientStatementDetailList();
//        matchList.get(1).getKey().setNutrientLabelCode(1L);
//        when(this.nutrientStatementDetailRepository.findByKeyNutrientStatementNumber(Mockito.anyLong())).thenReturn(matchList);
//        results =this.nutrientsService.findMandatedNutrientsByStatementId(0L);
//        Assert.assertEquals(0, results.size());
//	}

    /**
     * This method is a direct DB call, all that is tested is that it does not crash
     */
//	@Test
//	public void testDeleteNutrientStatement(){
//        this.nutrientsService.deleteNutrientStatement(0L);
//	}

    /**
     * Generate Default common nutrient unit of measure
     * @return a nutrient unit of measure with a code of 0 and a description of "Test"
     */
    private NutrientUom getDefaultCommonNutrientUom(){
        NutrientUom nutrientUom = new NutrientUom();
        nutrientUom.setNutrientUomCode(1L);
        nutrientUom.setNutrientUomDescription("Common Test");
        return nutrientUom;
    }

    /**
     * Generate Default metric nutrient unit of measure
     * @return a nutrient unit of measure with a code of 0 and a description of "Test"
     */
    private NutrientUom getDefaultMetricNutrientUom(){
        NutrientUom nutrientUom = new NutrientUom();
        nutrientUom.setNutrientUomCode(2L);
        nutrientUom.setNutrientUomDescription("Metric Test");
        return nutrientUom;
    }

    /**
     * Generate a default nutrientStatementDetailsKey for testing purposes
     * @return a simple NutrientStatementDetailsKey
     */
    private NutrientStatementDetailsKey getDefaultNutrientStatementDetailsKey(){
        NutrientStatementDetailsKey statementDetailsKey = new NutrientStatementDetailsKey();
        statementDetailsKey.setNutrientLabelCode(2L);
        statementDetailsKey.setNutrientStatementNumber(2L);
        return statementDetailsKey;
    }

    /**
     * Generate Default Nutrient Statement Header, to be used for testing
     * @return the default Nutrient statement header
     */
    private NutrientStatementHeader getDefaultNutrientStatementHeader(){
        NutrientStatementHeader nutrientStatementHeader = new NutrientStatementHeader();
        nutrientStatementHeader.setNutrientStatementNumber(1L);
        nutrientStatementHeader.setMeasureQuantity(1.0);
        nutrientStatementHeader.setMeasureQuantity(1.0);
        nutrientStatementHeader.setUomMetricCode(getDefaultMetricNutrientUom().getNutrientUomCode());
        nutrientStatementHeader.setUomCommonCode(getDefaultCommonNutrientUom().getNutrientUomCode());
        nutrientStatementHeader.setServingsPerContainer(1L);
        nutrientStatementHeader.setEffectiveDate(LocalDate.now().minusDays(1L));
//        nutrientStatementHeader.setStatementMaintenanceSwitch("Test Switch");
        nutrientStatementHeader.setNutrientMetricUom(getDefaultMetricNutrientUom());
        nutrientStatementHeader.setNutrientCommonUom(getDefaultCommonNutrientUom());
        return nutrientStatementHeader;
    }

    /**
     * Constructs a generic Nutrient Statement Header list of one element
     * @return default Nutrient Statement Header List
     */
    private List<NutrientStatementHeader> getDefaultNutrientStatementHeaderList(){
        List<NutrientStatementHeader> defaultList = new ArrayList<>();
        NutrientStatementHeader defaultHeader = getDefaultNutrientStatementHeader();
        defaultHeader.setNutrientStatementDetailList(getDefaultNutrientStatementDetailList());
        defaultList.add(defaultHeader);
        return defaultList;
    }

    /**
     * Generate Default nutrient statement detail, to be used as a reference for testing
     * @return the default nutrient statement detail.
     */
    private NutrientStatementDetail getDefaultNutrientStatementDetail(Nutrient nutrient){
        NutrientStatementDetail nutrientStatementDetail = new NutrientStatementDetail();
        nutrientStatementDetail.setKey(getDefaultNutrientStatementDetailsKey());
        nutrientStatementDetail.setNutrientStatementQuantity(1.0);
        nutrientStatementDetail.setNutrientDailyValue(1L);
        nutrientStatementDetail.setNutrientPDDQuantity(1.0);
        nutrientStatementDetail.setNutrient(nutrient);
        return nutrientStatementDetail;
    }

    /**
     * Generate Default nutrient statement detail list, to be used as a reference for testing
     * @return the default nutrient statement detail list.
     */
    private List<NutrientStatementDetail> getDefaultNutrientStatementDetailList(){
        Nutrient defaultNutrient = getDefaultNutrient();
        Nutrient otherDefaultNutrient = getDefaultNutrient();
        otherDefaultNutrient.setFedLblSequence(1.5);
        List<NutrientStatementDetail> nutrientStatementDetails = new ArrayList<>();
        nutrientStatementDetails.add(getDefaultNutrientStatementDetail(defaultNutrient));
        nutrientStatementDetails.add(getDefaultNutrientStatementDetail(otherDefaultNutrient));
        return nutrientStatementDetails;
    }

    /**
     * Generates default Nutrient
     * @return a with all parameters either 0 or null with the exception of Nutrient Code with value of 1
     */
    private Nutrient getDefaultNutrient(){
        Nutrient defaultNutrient = new Nutrient();
        defaultNutrient.setNutrientCode(1L);
        defaultNutrient.setNutrientDescription("Test Nutrient");
        defaultNutrient.setNutrientUom(getDefaultCommonNutrientUom());
        defaultNutrient.setUomCode(getDefaultCommonNutrientUom().getNutrientUomCode());
        defaultNutrient.setFedLblSequence(1L);
        defaultNutrient.setRecommendedDailyAmount(1L);
        defaultNutrient.setUsePercentDailyValue(true);
        defaultNutrient.setLstModifiedDate(LocalDate.now().minusDays(1));
//        defaultNutrient.setMaintenanceSwitch("Default Nutrient Switch");
        defaultNutrient.setFederalRequired(true);
        defaultNutrient.setDefaultBehaviorOverrideSequence(1.0);
        defaultNutrient.setFederalRequiredPdv(true);
        return defaultNutrient;
    }

    /**
     * Generates default list of nutrients
     * @return A single element list with the default Nutrient inside
     */
    private List<Nutrient> getDefaultNutrientList(){
        List<Nutrient> list = new ArrayList<>();
        list.add(getDefaultNutrient());
        return list;
    }

	/**
     * Generates a Default List of NutrientCodes made up of a single long of value 0l
     * @return Default Nutrient Code List
     */
    private List<Long> getDefaultNutrientCodeList() {
        List<Long> nutrientCodeList = new ArrayList<>();
        nutrientCodeList.add(1L);
        return nutrientCodeList;
    }

    /**
     * Generates Default Nutrient Request.
     * @return A pagable with starting page of 0 and a page size of 2(min allowed) and no sorting.
     */
    private Pageable getDefaultNutrientRequest(){
        return new PageRequest(testPage, testPageSize);
	}

    /**
     * Generate Default Nutrient description list, to be used as a reference for testing
     * @return the default Nutrient description list.
     */
    private List<String> getDefaultNutrientDescriptionList() {
        List<String> nutrientCodeList = new ArrayList<>();
        nutrientCodeList.add("This is a test description");
        return nutrientCodeList;
    }

    /**
     * Builds a complete nutrient with default value
     * @return a nutrient where no reference pointers point to null.
     */
    private Nutrient buildcompleteNutrient(){
        Nutrient complete = getDefaultNutrient();
        NutrientStatementHeader completeHeader = getDefaultNutrientStatementHeader();
        NutrientStatementDetail completeDetail = getDefaultNutrientStatementDetail(complete);
        completeDetail.setNutrientStatementHeader(completeHeader);
        ArrayList<NutrientStatementDetail> completeList= new ArrayList<>();
        completeList.add(completeDetail);
        completeHeader.setNutrientStatementDetailList(completeList);
        complete.setNutrientStatementDetails(completeList);
        return complete;
    }

    /**
     * Constructs an Nutrient header that is out of order
     * @param header where nutrients federal sequences are out of order.
     */
    private void addNutrientsOutOfOrder(NutrientStatementHeader header){
        Nutrient otherNutrient = getDefaultNutrient();
        otherNutrient.setFedLblSequence(0.5);
        NutrientStatementDetail otherDetail = getDefaultNutrientStatementDetail(otherNutrient);
        List<NutrientStatementDetail> headersDetails = header.getNutrientStatementDetailList();
        headersDetails.add(otherDetail);
    }

    /**
     * Generates a simple rule key for testing
     * @return the default NutrientRoundingRuleKey
     */
    private NutrientRoundingRuleKey getDefaultNutrientRoundingRuleKey(){
        NutrientRoundingRuleKey key = new NutrientRoundingRuleKey();
        key.setLowerBound(MIN_ROUNDING_RULE_SIZE);
        key.setNutrientCode((int)getDefaultNutrient().getNutrientCode());
        return key;
    }
    /**
     * A generic rule that is valid
     * @return a valid rounding rule
     */
    private NutrientRoundingRule getDefaultNutrientRoundingRule(){
        NutrientRoundingRule rule = new NutrientRoundingRule();
        rule.setIncrementQuantity(1);
        rule.setKey(getDefaultNutrientRoundingRuleKey());
        rule.setUpperBound(MAX_ROUNDING_RULE_SIZE);
        return rule;
    }


    /**
     * This will construct a simple valid NutrientRoundingRule that will throw no errors
     * @return default valid NutrientRoundingRuleList
     */
    private List<NutrientRoundingRule> getNurtrientRoundingRuleList(){
        List<NutrientRoundingRule> list = new ArrayList<>();
        list.add(getDefaultNutrientRoundingRule());
        return list;
    }

}