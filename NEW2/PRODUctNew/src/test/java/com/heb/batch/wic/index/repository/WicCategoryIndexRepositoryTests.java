/*
 * WicCategoryIndexRepositoryTests
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.batch.wic.index.repository;

import com.heb.batch.wic.index.WicCategoryDocument;
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
 * Unit tests for WicCategoryIndexRepository.
 *
 * @author vn03512
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:h2-jpa-config-test.xml","classpath:elesticsearch-config-test.xml", "classpath:component-scan-config-test.xml" })
public class WicCategoryIndexRepositoryTests {
	private static final String ID1 = "10000000000047531204125000001315";
	private static final String ID2 = "20000000000047531204125000001315";

	@Autowired
	private WicCategoryIndexRepository repository;
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
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
		WicCategoryDocument pscDocument2 = createSampleDocument(ID2);

		// When
		repository.saveAll(Arrays.asList(pscDocument1, pscDocument2));

		// Then
		Optional<WicCategoryDocument> document1FromElasticSearch = repository.findById(ID1);
		assertThat(document1FromElasticSearch.isPresent(), is(true));
		Optional<WicCategoryDocument> document2FromElasticSearch = repository.findById(ID2);
		assertThat(document2FromElasticSearch.isPresent(), is(true));
	}

	@Test
	public void shouldSaveDocument() {

		// Given
		WicCategoryDocument wcDocument1 = createSampleDocument(ID1);

		// When
		repository.save(wcDocument1);

		// Then
		Optional<WicCategoryDocument> documentFromElasticSearch = repository.findById(ID1);
		assertThat(documentFromElasticSearch.isPresent(), is(true));
		WicCategoryDocument wcDocument2 = documentFromElasticSearch.get();
		assertThat(wcDocument2.getWicCatId(), equalTo(wcDocument1.getWicCatId()));
		assertThat(wcDocument2.getDescription(), equalTo(wcDocument1.getDescription()));
	}

	@Test
	public void shouldReturnCountOfDocuments() {

		// Given
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);

		// When
		Long count = repository.count();

		// Then
		assertThat(count, is(greaterThanOrEqualTo(1L)));
	}

	@Test
	public void shouldFindAllDocuments() {

		// When
		Iterable<WicCategoryDocument> results = repository.findAll();

		// Then
		assertThat(results, is(notNullValue()));
	}

	@Test
	public void shouldDeleteDocument() {

		// Given
		String documentId = ID1;
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);

		// When
		repository.deleteById(documentId);

		// Then
		Optional<WicCategoryDocument> entityFromElasticSearch = repository.findById(documentId);
		assertThat(entityFromElasticSearch.isPresent(), is(false));
	}

	@Test
	public void shouldFindAllByIdQuery() {

		// Given
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);
		WicCategoryDocument pscDocument2 = createSampleDocument(ID2);
		repository.save(pscDocument2);

		// When
		Iterable<WicCategoryDocument> sampleEntities = repository.findAllById(Arrays.asList(ID1, ID2));

		// Then
		assertNotNull("sample entities cant be null..", sampleEntities);
		List<WicCategoryDocument> entities = CollectionUtils.iterableAsArrayList(sampleEntities);
		assertThat(entities.size(), is(2));
	}

	@Test
	public void shouldSaveIterableEntities() {

		// Given
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
		WicCategoryDocument pscDocument2 = createSampleDocument(ID2);
		Iterable<WicCategoryDocument> sampleEntities = Arrays.asList(pscDocument1, pscDocument2);

		// When
		repository.saveAll(sampleEntities);

		// Then
		Page<WicCategoryDocument> entities = repository.findAll(PageRequest.of(0, 50));
		assertNotNull(entities);
	}

	@Test
	public void shouldReturnTrueGivenDocumentWithIdExists() {

		// Given
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
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
		Page<WicCategoryDocument> sampleEntities = repository.findAll(PageRequest.of(0, 50));
		assertThat(sampleEntities.getTotalElements(), equalTo(0L));
	}

	@Test
	public void shouldDeleteEntity() {

		// Given
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);

		// When
		repository.delete(pscDocument1);

		// Then
		Optional<WicCategoryDocument> document1FromElasticSearch = repository.findById(ID1);
		assertThat(document1FromElasticSearch.isPresent(), is(false));
	}

	@Test
	public void shouldDeleteIterableEntities() {

		// Given
		WicCategoryDocument pscDocument1 = createSampleDocument(ID1);
		repository.save(pscDocument1);
		WicCategoryDocument pscDocument2 = createSampleDocument(ID2);
		repository.save(pscDocument2);
		Iterable<WicCategoryDocument> documentsShouldBeDeleted = Arrays.asList(pscDocument1, pscDocument2);

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
	 * @return WicCategoryDocument
	 */
	private static WicCategoryDocument createSampleDocument(String id) {
		WicCategoryDocument wcDocument1 = new WicCategoryDocument();
		wcDocument1.setWicCatId(id);
		wcDocument1.setDescription("AB CD EFG");
		return wcDocument1;
	}
}