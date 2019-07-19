package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.ProductDiscontinueKey;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * Working on removing dependency on Spring environment for unit tests. This class will be removed. I am leaving
 * it here to keep the functions for when I create a functional test group.
 */
/**
 * Test class for ProductDiscontinueRepository.
 *
 * @author d11677
 * @since 2.0.0
 */
//@RunWith(LoggingSupportTestRunner.class)
//@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ProductDiscontinueRepositoryTest {

//	private static final Logger logger = LoggerFactory.getLogger(ProductDiscontinueRepositoryTest.class);
//
//	private static final int FIRST_PAGE = 0;
//	private static final int PAGE_SIZE = 100;
//
//	@Autowired
//	private ProductDiscontinueRepository productDiscontinueRepository;
//
//
//	 * findDiscontinuedByItemCodes
//	 */
//
//	/**
//	 * Does the actual verification on the findDiscontinuedByItemCodes functions.
//	 *
//	 * @param list the list of products returned by one of the findDiscontinuedByItemCodes functions.
//	 */
//	private void validateDiscontinueByItemCode(List<ProductDiscontinue> list) {
//
//		// These items are discontinued.
//		Assert.assertTrue(this.listIncludesItem(66L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(157997L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(9020508597L, ItemMasterKey.DSD, list));
//
//		// These items are active.
//		Assert.assertTrue(this.listDoesNotIncludeItem(622L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(223L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(9020507600L, ItemMasterKey.DSD, list));
//
//		// This matching number is discontinued in the warehouse but active in DSD.
//		Assert.assertTrue(this.listIncludesItem(6289L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(6289L, ItemMasterKey.DSD, list));
//
//		// This matching number is active in the warehouse and DSD.
//		Assert.assertTrue(this.listDoesNotIncludeItem(12282L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(12282L, ItemMasterKey.WAREHOUSE, list));
//
//		// This matching number is active in the warehouse but discontinued in DSD.
//		Assert.assertTrue(this.listIncludesItem(28429L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(28429L, ItemMasterKey.WAREHOUSE, list));
//
//		// This matching number is discontinued in warehouse and DSD.
//		Assert.assertTrue(this.listIncludesItem(30154L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(30154L, ItemMasterKey.WAREHOUSE, list));
//	}
//
//	/**
//	 * Test findDiscontinuedByItemCodesWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findDiscontinuedByItemCodesWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page = this.productDiscontinueRepository.findDiscontinuedByItemCodesWithCount(
//				this.getItemCodeList(), pageable);
//
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//		this.validateDiscontinueByItemCode(page.getContent());
//	}
//
//	/**
//	 * Test findDiscontinuedByItemCodes.
//	 */
//	@Test
//	@Transactional
//	public void findDiscontinuedByItemCodes() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list = this.productDiscontinueRepository.findDiscontinuedByItemCodes(
//				this.getItemCodeList(), pageable);
//
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//		this.validateDiscontinueByItemCode(list);
//	}
//
//	/*
//	 * findActiveByItemCodes
//	 */
//
//	/**
//	 * Does the actual verification on the findActiveByItemCodes functions.
//	 *
//	 * @param list the list of products returned by one of the findActiveByItemCodes functions.
//	 */
//	private void validateFindActiveByItemCodes(List<ProductDiscontinue> list) {
//
//		// These items are discontinued.
//		Assert.assertTrue(this.listDoesNotIncludeItem(66L, ItemMasterKey.WAREHOUSE,list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(157997L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(9020508597L, ItemMasterKey.DSD, list));
//
//		// These items are active.
//		Assert.assertTrue(this.listIncludesItem(622L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(223L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(9020507600L, ItemMasterKey.DSD, list));
//
//		// This matching number is discontinued in the warehouse but active in DSD.
//		Assert.assertTrue(this.listDoesNotIncludeItem(6289L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(6289L, ItemMasterKey.DSD, list));
//
//		// This matching number is active in the warehouse and DSD.
//		Assert.assertTrue(this.listIncludesItem(12282L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(12282L, ItemMasterKey.WAREHOUSE, list));
//
//		// This matching number is active in the warehouse but discontinued in DSD.
//		Assert.assertTrue(this.listDoesNotIncludeItem(28429L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(28429L, ItemMasterKey.WAREHOUSE, list));
//
//		// This matching number is discontinued in warehouse and DSD.
//		Assert.assertTrue(this.listDoesNotIncludeItem(30154L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(30154L, ItemMasterKey.WAREHOUSE, list));
//	}
//
//	/**
//	 * Test findActiveByItemCodesWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findActiveByItemCodesWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page = this.productDiscontinueRepository.findActiveByItemCodesWithCount(
//				this.getItemCodeList(), pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindActiveByItemCodes(page.getContent());
//	}
//
//	/**
//	 * Test findActiveByItemCodes.
//	 */
//	@Test
//	@Transactional
//	public void findActiveByItemCodes() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list = this.productDiscontinueRepository.findActiveByItemCodes(
//				this.getItemCodeList(), pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindActiveByItemCodes(list);
//	}
//
//	/*
//	 * findByItemCodes
//	 */
//
//	/**
//	 * Does the actual verification on the findByItemCodes functions.
//	 *
//	 * @param list the list of products returned by one of the findByItemCodes functions.
//	 */
//	private void validateFindByItemCodes(List<ProductDiscontinue> list) {
//
//		// These items are either DSD or warehouse.
//		Assert.assertTrue(this.listIncludesItem(66L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(157997L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(9020508597L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(622L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(223L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(9020507600L, ItemMasterKey.DSD, list));
//
//		// These items match something in DSD and warehouse.
//		Assert.assertTrue(this.listIncludesItem(6289L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(6289L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(12282L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(12282L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(28429L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(28429L, ItemMasterKey.WAREHOUSE, list));
//		Assert.assertTrue(this.listIncludesItem(30154L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listIncludesItem(30154L, ItemMasterKey.WAREHOUSE, list));
//
//		// These were not included in the search.
//		Assert.assertTrue(this.listDoesNotIncludeItem(9034112004L, ItemMasterKey.DSD, list));
//		Assert.assertTrue(this.listDoesNotIncludeItem(11496L, ItemMasterKey.WAREHOUSE, list));
//	}
//
//	/**
//	 * Tests findByItemCodesWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findByItemCodesWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page =
//				this.productDiscontinueRepository.findByItemCodesWithCount(this.getItemCodeList(), pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindByItemCodes(page.getContent());
//
//	}
//
//	/**
//	 * Tests findByItemCodes.
//	 */
//	@Test
//	@Transactional
//	public void findByItemCodes() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list =
//				this.productDiscontinueRepository.findByItemCodes(this.getItemCodeList(), pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindByItemCodes(list);
//
//	}
//
//	/*
//	 * findByUpcs
//	 */
//
//	/**
//	 * Does the actual verification on the findByUpcs functions.
//	 *
//	 * @param list the list of products returned by one of the findByUpcs functions.
//	 */
//	private void validateFindByUpcs(List<ProductDiscontinue> list) {
//
//		// Warehouse UPC.
//		Assert.assertTrue(this.listIncludesUpc(9015932361L, list));
//
//		// DSD UPC.
//		Assert.assertTrue(this.listIncludesUpc(9020508597L, list));
//
//		// For the matching items, this includes both, so make sure both are there.
//		Assert.assertTrue(this.listIncludesUpc(6289L, list));
//		Assert.assertTrue(this.listIncludesUpc(9027100110L, list));
//
//		// For the matching items, this includes the DSD, but not the warehouse
//		Assert.assertTrue(this.listIncludesUpc(12282L, list));
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9034154321L, list));
//
//		// For the matching items, this list includes the warehouse, but not the DSD
//		Assert.assertTrue(this.listIncludesUpc(9034123115L, list));
//		Assert.assertTrue(this.listDoesNotIncludeUpc(28429L, list));
//
//		// Neither of these was in the search.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9020215100L, list));
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9017439469L, list));
//	}
//
//	/**
//	 * Tests findByUpcsWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findByUpcsWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page =
//				this.productDiscontinueRepository.findByUpcsWithCount(this.getUpcList(false), pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindByUpcs(page.getContent());
//	}
//
//	/**
//	 * Tests findByUpcs.
//	 */
//	@Test
//	@Transactional
//	public void findByUpcs() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list =
//				this.productDiscontinueRepository.findByUpcs(this.getUpcList(false), pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindByUpcs(list);
//	}
//
//	/*
//	 * findDiscontinuedByUpcs
//	 */
//
//	/**
//	 * Does the actual verification on the findDiscontinuedByUpcs functions.
//	 *
//	 * @param list the list of products returned by one of the findDiscontinuedByUpcs functions.
//	 */
//	private void  validateFindDiscontinuedByUpcs(List<ProductDiscontinue> list) {
//
//		// Active warehouse UPC.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(4775412479L, list));
//
//		// Disco warehouse UPC.
//		Assert.assertTrue(this.listIncludesUpc(9015932361L, list));
//
//		// Active DSD UPC.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9020507600L, list));
//
//		// Disco DSD UPC.
//		Assert.assertTrue(this.listIncludesUpc(9020508597L, list));
//
//		// This matching number is discontinued in the warehouse but active in DSD.
//		Assert.assertTrue(this.listIncludesUpc(9027100110L, list));
//		Assert.assertTrue(this.listDoesNotIncludeUpc(6289L, list));
//
//		// This matching number is active in the warehouse and DSD.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(12282L, list));
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9034154321L, list));
//
//		// This matching number is active in the warehouse but discontinued in DSD.
//		Assert.assertTrue(this.listIncludesUpc(28429L, list));
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9034123115L, list));
//
//		// This matching number is discontinued in warehouse and DSD.
//		Assert.assertTrue(this.listIncludesUpc(9034100109L, list));
//		Assert.assertTrue(this.listIncludesUpc(30154L, list));
//	}
//
//	/**
//	 * Tests findDiscontinuedByUpcsWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findDiscontinuedByUpcsWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page =
//				this.productDiscontinueRepository.findDiscontinuedByUpcsWithCount(this.getUpcList(true), pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindDiscontinuedByUpcs(page.getContent());
//	}
//
//	/**
//	 * Tests findDiscontinuedByUpcs.
//	 */
//	@Test
//	@Transactional
//	public void findDiscontinuedByUpcs() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list =
//				this.productDiscontinueRepository.findDiscontinuedByUpcs(this.getUpcList(true), pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindDiscontinuedByUpcs(list);
//	}
//
//	/*
//	 * findByActiveUpcs
//	 */
//
//	/**
//	 * Does the actual verification on the findActiveByUpcs functions.
//	 *
//	 * @param list the list of products returned by one of the findActiveByUpcs functions.
//	 */
//	private void  validateFindActiveByUpcs(List<ProductDiscontinue> list) {
//
//		// Active warehouse UPC.
//		Assert.assertTrue(this.listIncludesUpc(4775412479L, list));
//
//		// Disco warehouse UPC.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9015932361L, list));
//
//		// Active DSD UPC.
//		Assert.assertTrue(this.listIncludesUpc(9020507600L, list));
//
//		// Disco DSD UPC.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9020508597L, list));
//
//		// This matching number is discontinued in the warehouse but active in DSD.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9027100110L, list));
//		Assert.assertTrue(this.listIncludesUpc(6289L, list));
//
//		// This matching number is active in the warehouse and DSD.
//		Assert.assertTrue(this.listIncludesUpc(12282L, list));
//		Assert.assertTrue(this.listIncludesUpc(9034154321L, list));
//
//		// This matching number is active in the warehouse but discontinued in DSD.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(28429L, list));
//		Assert.assertTrue(this.listIncludesUpc(9034123115L, list));
//
//		// This matching number is discontinued in warehouse and DSD.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9034100109L, list));
//		Assert.assertTrue(this.listDoesNotIncludeUpc(30154L, list));
//	}
//
//	/**
//	 * Tests findActiveByUpcsWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findActiveByUpcWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page =
//				this.productDiscontinueRepository.findActiveByUpcsWithCount(this.getUpcList(true), pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindActiveByUpcs(page.getContent());
//
//	}
//
//	/**
//	 * Tests findActiveByUpcs.
//	 */
//	@Test
//	@Transactional
//	public void findActiveByUpc() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list =
//				this.productDiscontinueRepository.findActiveByUpcs(this.getUpcList(true), pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindActiveByUpcs(list);
//	}
//
//	/*
//	 * findByProductIds.
//	 */
//
//	/**
//	 * Does the actual validation on the results of the findByProductIds functions.
//	 *
//	 * @param list The results of the findByProductIds functions.
//	 */
//	public void validateFindByProductIds(List<ProductDiscontinue> list) {
//
//		// Product ID 1783147 has 2 UPCs tied to it, make sure they both show up.
//		Assert.assertTrue(this.listIncludesUpc(9015932361L, list));
//		Assert.assertTrue(this.listIncludesUpc(9015932362L, list));
//
//		// Check product tied to a DSD UPC.
//		Assert.assertTrue(this.listIncludesUpc(9035500102L, list));
//
//		// This product was not in the search.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9036105268L, list));
//	}
//
//	/**
//	 * Tests findByProductIdsWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findByProductIdsWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page =
//				this.productDiscontinueRepository.findByProductIdsWithCount(this.getProductIdList(), pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindByProductIds(page.getContent());
//	}
//
//	/**
//	 * Tests findByProductIds.
//	 */
//	@Test
//	@Transactional
//	public void findByProductIds() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list =
//				this.productDiscontinueRepository.findByProductIds(this.getProductIdList(), pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindByProductIds(list);
//	}
//
//	/*
//	 * findDiscontinuedByProductIds
//	 */
//
//	/**
//	 * Does the actual validation for the results of findDiscontinuedByProductIds functions.
//	 *
//	 * @param list The results of findDiscontinuedByProductIds functions.
//	 */
//	private void validateFindDiscontinuedByProductIds(List<ProductDiscontinue> list) {
//
//		// Discontinued warehouse.
//		// Product ID 1783147 has 2 UPCs tied to it, make sure they both show up.
//		Assert.assertTrue(this.listIncludesUpc(9015932361L, list));
//		Assert.assertTrue(this.listIncludesUpc(9015932362L, list));
//
//		// Active warehouse.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9027100010L, list));
//
//		//Discontinued DSD
//		Assert.assertTrue(this.listIncludesUpc(9020508597L, list));
//
//		// Active DSD
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9035500102L, list));
//
//		// This product was not in the search.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9036105268L, list));
//	}
//
//	/**
//	 * Tests findDiscontinuedByProductIdsWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findDiscontinuedByProductIdWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page =
//				this.productDiscontinueRepository.findDiscontinuedByProductIdsWithCount(this.getProductIdList(),
//						pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindDiscontinuedByProductIds(page.getContent());
//
//	}
//
//	/**
//	 * Tests findDiscontinuedByProductIds.
//	 */
//	@Test
//	@Transactional
//	public void findDiscontinuedByProductIds() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list =
//				this.productDiscontinueRepository.findDiscontinuedByProductIds(this.getProductIdList(),
//						pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindDiscontinuedByProductIds(list);
//
//	}
//
//	/*
//	 * findActiveByProductIds
//	 */
//
//	/**
//	 * Does the actual validation on the results of the findActiveByProductIds functions.
//	 *
//	 * @param list The results of the findActiveByProductIds functions.
//	 */
//	private void validateFindActiveByProductIds(List<ProductDiscontinue> list) {
//
//		// Discontinued warehouse.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9015932361L, list));
//
//		// Active warehouse.
//		Assert.assertTrue(this.listIncludesUpc(9027100010L, list));
//
//		//Discontinued DSD
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9020508597L, list));
//
//		// Active DSD
//		Assert.assertTrue(this.listIncludesUpc(9035500102L, list));
//
//		// This product was not in the search.
//		Assert.assertTrue(this.listDoesNotIncludeUpc(9036105268L, list));
//	}
//
//	/**
//	 * Tests findActiveByProductIdsWithCount.
//	 */
//	@Test
//	@Transactional
//	public void findActiveByProductIdWithCount() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		Page<ProductDiscontinue> page =
//				this.productDiscontinueRepository.findActiveByProductIdsWithCount(this.getProductIdList(),
//						pageable);
//		page.getContent().forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindActiveByProductIds(page.getContent());
//	}
//
//	/**
//	 * Tests findActiveByProductIds.
//	 */
//	@Test
//	@Transactional
//	public void findActiveByProductId() {
//
//		Pageable pageable = new PageRequest(ProductDiscontinueRepositoryTest.FIRST_PAGE,
//				ProductDiscontinueRepositoryTest.PAGE_SIZE, ProductDiscontinueKey.getDefaultSort());
//
//		List<ProductDiscontinue> list =
//				this.productDiscontinueRepository.findActiveByProductIds(this.getProductIdList(),
//						pageable);
//		list.forEach((p) -> ProductDiscontinueRepositoryTest.logger.debug(p.toString()));
//
//		this.validateFindActiveByProductIds(list);
//	}
//
//	/*
//	 * Support functions.
//	 */
//
//	/**
//	 * Returns a list of product IDs to look for.
//	 *
//	 * @return A list of prodcut IDs to look for.
//	 */
//	private List<Long> getProductIdList() {
//
//		List<Long> productIds = new ArrayList<>();
//
//		// Warehouse
//		productIds.add(1783147L); // Discontinued
//		productIds.add(1753389L); // Active
//
//		// DSD
//		productIds.add(1943705L); // Active
//		productIds.add(1798109L); // Discontinued
//
//		return productIds;
//	}
//
//	/**
//	 * Returns a list of UPCs to look for.
//	 *
//	 * @param includeAll This will cause the function to add all the UPCs that tie up to getItemCodeList. If false,
//	 *                   some will be excluded because I didn't want the generic one to include them so I could
//	 *                   test it differently, but it's needed for the active/discontinued tests.
//	 * @return A list of UPCs to look for.
//	 */
//	private List<Long> getUpcList(boolean includeAll) {
//
//		List<Long> upcs = new ArrayList<>();
//
//		// UPCs tied to warehouse items.
//		upcs.add(9015932361L);
//		upcs.add(4775412479L);
//		upcs.add(9015932322L);
//		upcs.add(9015935024L);
//
//		// UPCs tied to DSD products.
//		upcs.add(9020508597L);
//		upcs.add(9020507600L);
//
//		// UPCs that match up to the DSD and warehouse products from getItemCodeList.
//		upcs.add(6289L);
//		upcs.add(9027100110L);
//		upcs.add(12282L);
//		if (includeAll) {
//			upcs.add(9034154321L);
//		}
//		if (includeAll) {
//			upcs.add(28429L);
//		}
//		upcs.add(9034123115L);
//
//		upcs.add(30154L);
//		upcs.add(9034100109L);
//
//		return upcs;
//	}
//
//	/**
//	 * Returns a list of item codes (includes DSD UPCs) to test with.
//	 *
//	 * @return A list of item codes to test with.
//	 */
//	private List<Long> getItemCodeList() {
//
//		List<Long> itemCodes = new ArrayList<>();
//
//		// Warehouse item codes.
//		itemCodes.add(66L);
//		itemCodes.add(622L);
//		itemCodes.add(223L);
//		itemCodes.add(157997L);
//
//		// DSD item codes.
//		itemCodes.add(9020508597L);
//		itemCodes.add(9020507600L);
//
//		// This is both in the sense there's an item code and UPC with the same value.
//		itemCodes.add(6289L);
//		itemCodes.add(12282L);
//		itemCodes.add(28429L);
//		itemCodes.add(30154L);
//
//		return itemCodes;
//	}
//
//	/**
//	 * Check to see if an item code is in a list.
//	 *
//	 * @param itemCode The item code to look for.
//	 * @param itemType The type of item to look for.
//	 * @param list The list to look through.
//	 * @return True if the item code is in the list and false otherwise.
//	 */
//	private boolean listIncludesItem(long itemCode, String itemType, Collection<ProductDiscontinue> list) {
//
//		for (ProductDiscontinue p : list) {
//			if (p.getKey().getItemCode() == itemCode && p.getKey().getItemType().equals(itemType)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * Check to see if an item code is missing from a list.
//	 *
//	 * @param itemCode The item code to look for.
//	 * @param itemType The type of item to look for.
//	 * @param list The list to look through.
//	 * @return True if the item code is not in the list and false otherwise.
//	 */
//	private boolean listDoesNotIncludeItem(long itemCode, String itemType, Collection<ProductDiscontinue> list) {
//
//		for (ProductDiscontinue p : list) {
//			if (p.getKey().getItemCode() == itemCode && p.getKey().getItemType().equals(itemType)) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	/**
//	 * Check to see if a UPC is in alist.
//	 *
//	 * @param upc The upc to look for.
//	 * @param list The list to look through.
//	 * @return True if the item code is in the list and false otherwise.
//	 */
//	private boolean listIncludesUpc(long upc, Collection<ProductDiscontinue> list) {
//
//		for (ProductDiscontinue p : list) {
//			if (p.getKey().getUpc() == upc) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * Check to see if a UPC is missing from a list.
//	 *
//	 * @param upc The item code to look for.
//	 * @param list The list to look through.
//	 * @return True if the item code is not in the list and false otherwise.
//	 */
//	private boolean listDoesNotIncludeUpc(long upc, Collection<ProductDiscontinue> list) {
//
//		for (ProductDiscontinue p : list) {
//			if (p.getKey().getUpc() == upc) {
//				return false;
//			}
//		}
//		return true;
//	}
}
