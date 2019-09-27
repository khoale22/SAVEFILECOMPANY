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

import com.heb.batch.wic.index.WicSubCategoryDocument;
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
 * Unit tests for WicSubCategoryIndexRepository.
 *
 * @author vn03512
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:h2-jpa-config-test.xml","classpath:elesticsearch-config-test.xml", "classpath:component-scan-config-test.xml" })
public class WicSubCategoryIndexRepositoryTests {
	private static final String ID1 = "10000000000047531204125000001315";
	private static final String ID2 = "20000000000047531204125000001315";

	@Autowired
	private WicSubCategoryIndexRepository repository;
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
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		WicSubCategoryDocument wscDocument2 = createSampleDocument(ID2);

		// When
		repository.saveAll(Arrays.asList(wscDocument1, wscDocument2));

		// Then
		Optional<WicSubCategoryDocument> document1FromElasticSearch = repository.findById(ID1);
		assertThat(document1FromElasticSearch.isPresent(), is(true));
		Optional<WicSubCategoryDocument> document2FromElasticSearch = repository.findById(ID2);
		assertThat(document2FromElasticSearch.isPresent(), is(true));
	}

	@Test
	public void shouldSaveDocument() {

		// Given
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);

		// When
		repository.save(wscDocument1);

		// Then
		Optional<WicSubCategoryDocument> documentFromElasticSearch = repository.findById(ID1);
		assertThat(documentFromElasticSearch.isPresent(), is(true));
		WicSubCategoryDocument wscDocument2 = documentFromElasticSearch.get();
		assertThat(wscDocument2.getId(), equalTo(wscDocument1.getId()));
		assertThat(wscDocument2.getWicSubCategoryId(), equalTo(wscDocument1.getWicSubCategoryId()));
		assertThat(wscDocument2.getWicCategoryId(), equalTo(wscDocument1.getWicCategoryId()));
		assertThat(wscDocument2.getDescription(), equalTo(wscDocument1.getDescription()));
		assertThat(wscDocument2.getLebSwitch(), equalTo(wscDocument1.getLebSwitch()));
	}

	@Test
	public void shouldReturnCountOfDocuments() {

		// Given
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		repository.save(wscDocument1);

		// When
		Long count = repository.count();

		// Then
		assertThat(count, is(greaterThanOrEqualTo(1L)));
	}

	@Test
	public void shouldFindAllDocuments() {

		// When
		Iterable<WicSubCategoryDocument> results = repository.findAll();

		// Then
		assertThat(results, is(notNullValue()));
	}

	@Test
	public void shouldDeleteDocument() {

		// Given
		String documentId = ID1;
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		repository.save(wscDocument1);

		// When
		repository.deleteById(documentId);

		// Then
		Optional<WicSubCategoryDocument> entityFromElasticSearch = repository.findById(documentId);
		assertThat(entityFromElasticSearch.isPresent(), is(false));
	}

	@Test
	public void shouldFindAllByIdQuery() {

		// Given
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		repository.save(wscDocument1);
		WicSubCategoryDocument wscDocument2 = createSampleDocument(ID2);
		repository.save(wscDocument2);

		// When
		Iterable<WicSubCategoryDocument> sampleEntities = repository.findAllById(Arrays.asList(ID1, ID2));

		// Then
		assertNotNull("sample entities cant be null..", sampleEntities);
		List<WicSubCategoryDocument> entities = CollectionUtils.iterableAsArrayList(sampleEntities);
		assertThat(entities.size(), is(2));
	}

	@Test
	public void shouldSaveIterableEntities() {

		// Given
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		WicSubCategoryDocument wscDocument2 = createSampleDocument(ID2);
		Iterable<WicSubCategoryDocument> sampleEntities = Arrays.asList(wscDocument1, wscDocument2);

		// When
		repository.saveAll(sampleEntities);

		// Then
		Page<WicSubCategoryDocument> entities = repository.findAll(PageRequest.of(0, 50));
		assertNotNull(entities);
	}

	@Test
	public void shouldReturnTrueGivenDocumentWithIdExists() {

		// Given
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		repository.save(wscDocument1);

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
		Page<WicSubCategoryDocument> sampleEntities = repository.findAll(PageRequest.of(0, 50));
		assertThat(sampleEntities.getTotalElements(), equalTo(0L));
	}

	@Test
	public void shouldDeleteEntity() {

		// Given
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		repository.save(wscDocument1);

		// When
		repository.delete(wscDocument1);

		// Then
		Optional<WicSubCategoryDocument> document1FromElasticSearch = repository.findById(ID1);
		assertThat(document1FromElasticSearch.isPresent(), is(false));
	}

	@Test
	public void shouldDeleteIterableEntities() {

		// Given
		WicSubCategoryDocument wscDocument1 = createSampleDocument(ID1);
		repository.save(wscDocument1);
		WicSubCategoryDocument wscDocument2 = createSampleDocument(ID2);
		repository.save(wscDocument2);
		Iterable<WicSubCategoryDocument> documentsShouldBeDeleted = Arrays.asList(wscDocument1, wscDocument2);

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
	 * @return WicSubCategoryDocument
	 */
	private static WicSubCategoryDocument createSampleDocument(String id) {
		WicSubCategoryDocument wscDocument1 = new WicSubCategoryDocument();
		wscDocument1.setId(id);
		wscDocument1.setWicCategoryId(1234567L);
		wscDocument1.setWicSubCategoryId(987654L);
		wscDocument1.setDescription("AB CD EFG");
		wscDocument1.setLebSwitch("N");
		return wscDocument1;
	}
}