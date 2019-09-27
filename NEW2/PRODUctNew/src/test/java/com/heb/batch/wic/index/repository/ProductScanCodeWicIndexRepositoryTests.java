/*
 * ProductScanCodeWicIndexRepositoryTests
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.batch.wic.index.repository;

import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.repository.ProductScanCodeWicRepository;
import org.aspectj.lang.annotation.Before;
import org.elasticsearch.common.util.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Unit tests for ProductScanCodeWicIndexRepository.
 *
 * @author vn03512
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:h2-jpa-config-test.xml","classpath:elesticsearch-config-test.xml", "classpath:component-scan-config-test.xml" })
public class ProductScanCodeWicIndexRepositoryTests {
	private static final String ID1 = "10000000000047531204125000001315";
	private static final String ID2 = "20000000000047531204125000001315";

	@Autowired
	private ProductScanCodeWicIndexRepository repository;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Before(value = "")
	public void before() {
		elasticsearchTemplate.deleteIndex(ProductScanCodeWicRepository.class);
		elasticsearchTemplate.createIndex(ProductScanCodeWicRepository.class);
		elasticsearchTemplate.putMapping(ProductScanCodeWicRepository.class);
		elasticsearchTemplate.refresh(ProductScanCodeWicRepository.class);
	}

	@Test
	public void shouldDoBulkIndexDocument() {

		// Given
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		ProductScanCodeWicDocument pscDocument2 = createSampleDocument(ID2);

		// When
		repository.saveAll(Arrays.asList(pscDocument1, pscDocument2));

		// Then
		Optional<ProductScanCodeWicDocument> document1FromElasticSearch = repository.findById(ID1);
		assertThat(document1FromElasticSearch.isPresent(), is(true));
		Optional<ProductScanCodeWicDocument> document2FromElasticSearch = repository.findById(ID2);
		assertThat(document2FromElasticSearch.isPresent(), is(true));
	}

	@Test
	public void shouldSaveDocument() {

		// Given
		ProductScanCodeWicDocument pscwDocument1 = createSampleDocument(ID1);

		// When
		repository.save(pscwDocument1);

		// Then
		Optional<ProductScanCodeWicDocument> documentFromElasticSearch = repository.findById(ID1);
		assertThat(documentFromElasticSearch.isPresent(), is(true));
		ProductScanCodeWicDocument pscwDocument2 = documentFromElasticSearch.get();
		assertThat(pscwDocument2.getId(), equalTo(pscwDocument1.getId()));
		assertThat(pscwDocument2.getWicAplId(), equalTo(pscwDocument1.getWicAplId()));
		assertThat(pscwDocument2.getUpc(), equalTo(pscwDocument1.getUpc()));
		assertThat(pscwDocument2.getWicCategoryId(), equalTo(pscwDocument1.getWicCategoryId()));
		assertThat(pscwDocument2.getWicSubCategoryId(), equalTo(pscwDocument1.getWicSubCategoryId()));
		assertThat(pscwDocument2.getLebSwitch(), equalTo(pscwDocument1.getLebSwitch()));
		assertThat(pscwDocument2.getWicDescription(), equalTo(pscwDocument1.getWicDescription()));
		assertThat(pscwDocument2.getWicPackageSize(), equalTo(pscwDocument1.getWicPackageSize()));
	}

	@Test
	public void shouldReturnCountOfDocuments() {

		// Given
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);

		// When
		Long count = repository.count();

		// Then
		assertThat(count, is(greaterThanOrEqualTo(1L)));
	}

	@Test
	public void shouldFindAllDocuments() {

		// When
		Iterable<ProductScanCodeWicDocument> results = repository.findAll();

		// Then
		assertThat(results, is(notNullValue()));
	}

	@Test
	public void shouldDeleteDocument() {

		// Given
		String documentId = ID1;
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);

		// When
		repository.deleteById(documentId);

		// Then
		Optional<ProductScanCodeWicDocument> entityFromElasticSearch = repository.findById(documentId);
		assertThat(entityFromElasticSearch.isPresent(), is(false));
	}

	@Test
	public void shouldFindAllByIdQuery() {

		// Given
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);
		ProductScanCodeWicDocument pscDocument2 = createSampleDocument(ID2);
		repository.save(pscDocument2);

		// When
		Iterable<ProductScanCodeWicDocument> sampleEntities = repository.findAllById(Arrays.asList(ID1, ID2));

		// Then
		assertNotNull("sample entities cant be null..", sampleEntities);
		List<ProductScanCodeWicDocument> entities = CollectionUtils.iterableAsArrayList(sampleEntities);
		assertThat(entities.size(), is(2));
	}

	@Test
	public void shouldSaveIterableEntities() {

		// Given
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		ProductScanCodeWicDocument pscDocument2 = createSampleDocument(ID2);
		Iterable<ProductScanCodeWicDocument> sampleEntities = Arrays.asList(pscDocument1, pscDocument2);

		// When
		repository.saveAll(sampleEntities);

		// Then
		Page<ProductScanCodeWicDocument> entities = repository.findAll(PageRequest.of(0, 50));
		assertNotNull(entities);
	}

	@Test
	public void shouldReturnTrueGivenDocumentWithIdExists() {

		// Given
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);

		// When
		boolean exist = repository.existsById(ID1);

		// Then
		assertEquals(true, exist);
	}

	@Test
	public void shouldDeleteAll() {

		// When
		repository.deleteAll();

		// Then
		Page<ProductScanCodeWicDocument> sampleEntities = repository.findAll(PageRequest.of(0, 50));
		assertThat(sampleEntities.getTotalElements(), equalTo(0L));
	}

	@Test
	public void shouldDeleteEntity() {

		// Given
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);

		// When
		repository.delete(pscDocument1);

		// Then
		Optional<ProductScanCodeWicDocument> document1FromElasticSearch = repository.findById(ID1);
		assertThat(document1FromElasticSearch.isPresent(), is(false));
	}

	@Test
	public void shouldDeleteIterableEntities() {

		// Given
		ProductScanCodeWicDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);
		ProductScanCodeWicDocument pscDocument2 = createSampleDocument(ID2);
		repository.save(pscDocument2);
		Iterable<ProductScanCodeWicDocument> documentsShouldBeDeleted = Arrays.asList(pscDocument1, pscDocument2);

		// When
		repository.deleteAll(documentsShouldBeDeleted);

		// Then
		assertThat(repository.findById(ID1).isPresent(), is(false));
		assertThat(repository.findById(ID2).isPresent(), is(false));
	}

	/**
	 * Create a document sample with id parameter.
	 *
	 * @param id The document's id.
	 * 
	 * @return ProductScanCodeWicDocument
	 */
	private static ProductScanCodeWicDocument createSampleDocument(String id) {
		ProductScanCodeWicDocument pscwDocument1 = new ProductScanCodeWicDocument();
		pscwDocument1.setId(id);
		pscwDocument1.setWicAplId(10000000000047531L);
		pscwDocument1.setWicCategoryId(13L);
		pscwDocument1.setWicSubCategoryId(15L);
		pscwDocument1.setLebSwitch("N");
		pscwDocument1.setWicDescription("FRUIT/VEGETABLES");
		pscwDocument1.setWicPackageSize(100.0);
		return pscwDocument1;
	}
}