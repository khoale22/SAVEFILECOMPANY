/*
 * BdmServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.Bdm;
import com.heb.pm.repository.BdmIndexRepository;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests BdmService.
 *
 * @author m314029
 * @since 2.0.6
 */
public class BdmServiceTest {

	private static final String SEARCH_STRING = "SEARCH-STRING";
	private static final String REGEX_STRING = "*" + SEARCH_STRING + "*";

	private static final String BDM_CODE = "A1   ";
	private static final String FIRST_NAME = "KAREN               ";
	private static final String LAST_NAME = "CASSADY             ";
	private static final String FULL_NAME = "KAREN CASSADY                 ";

	/*
	 * Tests findByRegularExpression
	 */

	/**
	 * Tests findByRegularExpression.
	 */
	@Test
	public void findByRegularExpression() {
		BdmService service = new BdmService();
		service.setIndexRepository(this.getRepository(10, 100, BDM_CODE));
		PageableResult<Bdm> list = service.findByRegularExpression(SEARCH_STRING,
				10, 100);
		Assert.assertEquals(this.getTestBdm(), list.getData().iterator().next());
	}

	/*
	 * Tests findBdm
	 */

	/**
	 * Tests findBdm.
	 */
	@Test
	public void findBdm() {
		BdmService service = new BdmService();
		service.setIndexRepository(this.getRepository(10, 100, BDM_CODE));
		Bdm bdm = service.findBdm(BDM_CODE);
		Assert.assertEquals(this.getTestBdm(), bdm);
	}

	/*
	 * Support functions
	 */

	/**
	 * Returns a Bdm to test with.
	 *
	 * @return A Bdm to test with.
	 */
	private Bdm getTestBdm() {

		Bdm bdm = new Bdm();
		bdm.setBdmCode(BDM_CODE);
		bdm.setFirstName(FIRST_NAME);
		bdm.setLastName(LAST_NAME);
		bdm.setFullName(FULL_NAME);
		return bdm;
	}

	/**
	 * Mocks up the call to findByRegularExpression on the repository. It checks to make sure the right
	 * parameters are passed in.
	 *
	 * @param page The page to expect.
	 * @param pageSize The pageSize to expect.
	 * @return The mocked up call to findByRegularExpression
	 */
	private Answer<Page<BdmDocument>> getRegularExpressionAnswer(int page, int pageSize) {
		return invocation -> {
			Assert.assertEquals(REGEX_STRING, invocation.getArguments()[0]);
			PageRequest request = (PageRequest)invocation.getArguments()[1];
			Assert.assertEquals(page, request.getPageNumber());
			Assert.assertEquals(pageSize, request.getPageSize());
			return new PageImpl<>(this.getDocumentList());
		};
	}

	/**
	 * Returns a list of BdmDocuments to test with.
	 *
	 * @return A list of BdmDocuments to test with.
	 */
	private List<BdmDocument> getDocumentList() {
		List<BdmDocument> documents = new ArrayList<>(1);
		documents.add(new BdmDocument(this.getTestBdm()));
		return documents;
	}

	/**
	 * Mocks up a BdmIndexRepository to test with.
	 *
	 * @param page The page to expect.
	 * @param pageSize The pageSize to expect.
	 * @return A BdmIndexRepository to test with.
	 */
	public BdmIndexRepository getRepository(int page, int pageSize, String id) {

		BdmIndexRepository repository = Mockito.mock(BdmIndexRepository.class);
		Mockito.doAnswer(this.getRegularExpressionAnswer(page, pageSize)).when(repository)
				.findByRegularExpression(Mockito.anyString(), Mockito.anyObject());
		Mockito.doAnswer(this.getFindOneAnswer(id)).when(repository).findOne(Mockito.anyString());

		return repository;
	}

	/**
	 * Mocks up a call to findOne in the repository. It checks to make sure the right parameters are passed in.
	 *
	 * @param id The bdm ID to expect.
	 * @return The mocked up call to findOne.
	 */
	private Answer<BdmDocument> getFindOneAnswer(String id) {
		return invocation -> {
			Assert.assertEquals(id, invocation.getArguments()[0]);
			return new BdmDocument(this.getTestBdm());
		};
	}
}
