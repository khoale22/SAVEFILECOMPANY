package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubCommodity;
import com.heb.pm.entity.SubCommodityKey;
import com.heb.pm.repository.SubCommodityIndexRepository;
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
 * Tests SubCommodityService.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityServiceTest {

	private static final String SEARCH_STRING = "SEARCH-STRING";
	private static final String REGEX_STRING = "*" + SEARCH_STRING + "*";

	private static final int CLASS_CODE = 39;
	private static final int COMMODITY_CODE = 8222;
	private static final int SUB_COMMODITY_CODE = 9551;
	private static final String SUB_COMMODITY_NAME = "LEAN MULT SRV STUFD SNDWCH    ";

	/**
	 * Tests findByRegularExpression.
	 */
	@Test
	public void findByRegularExpression() {
		SubCommodityService service = new SubCommodityService();
		service.setIndexRepository(this.getRepository(10, 100, SubCommodityServiceTest.SUB_COMMODITY_CODE));
		PageableResult<SubCommodity> list = service.findByRegularExpression(SubCommodityServiceTest.SEARCH_STRING,
				10, 100);
		Assert.assertEquals(this.getTestSubCommodity(), list.getData().iterator().next());
	}

	/**
	 * Tests findSubCommodity;
	 */
	@Test
	public void findSubCommodity() {
		SubCommodityService service = new SubCommodityService();
		service.setIndexRepository(this.getRepository(10, 100, SubCommodityServiceTest.SUB_COMMODITY_CODE));
		SubCommodity sc = service.findSubCommodity(SubCommodityServiceTest.SUB_COMMODITY_CODE);
		Assert.assertEquals(this.getTestSubCommodity(), sc);
	}

	/**
	 * Mocks up the call to findByRegularExpression on the repository. It checks to make sure the right
	 * parameters are passed in.
	 *
	 * @param page The page to expect.
	 * @param pageSize The pageSize to expect.
	 * @return The mocked up call to findByRegularExpression
	 */
	private Answer<Page<SubCommodityDocument>> getRegularExpressionAnswer(int page, int pageSize) {
		return invocation -> {
			Assert.assertEquals(SubCommodityServiceTest.REGEX_STRING, invocation.getArguments()[0]);
			PageRequest request = (PageRequest)invocation.getArguments()[1];
			Assert.assertEquals(page, request.getPageNumber());
			Assert.assertEquals(pageSize, request.getPageSize());
			return new PageImpl<>(this.getDocumentList());
		};
	}

	/**
	 * Mocks up a call to findOne in the repository. It checks to make sure the right parameters are passed in.
	 *
	 * @param id The sub-commodity ID to expect.
	 * @return The mocked up call to findOne.
	 */
	private Answer<SubCommodityDocument> getFindOneAnswer(int id) {
		return invocation -> {
			Assert.assertEquals(Integer.toString(id), invocation.getArguments()[0]);
			return new SubCommodityDocument(this.getTestSubCommodity());
		};
	}

	/**
	 * Returns a key to test with.
	 *
	 * @return A key to test with.
	 */
	private SubCommodityKey getTestKey() {

		SubCommodityKey key = new SubCommodityKey();
		key.setClassCode(SubCommodityServiceTest.CLASS_CODE);
		key.setCommodityCode(SubCommodityServiceTest.COMMODITY_CODE);
		key.setSubCommodityCode(SubCommodityServiceTest.SUB_COMMODITY_CODE);
		return key;
	}

	/**
	 * Returns a SubCommodity to test with.
	 *
	 * @return A SubCommodity to test with.
	 */
	private SubCommodity getTestSubCommodity() {

		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setKey(this.getTestKey());
		subCommodity.setName(SubCommodityServiceTest.SUB_COMMODITY_NAME);
		return subCommodity;
	}

	/**
	 * Returns a list of SubCommodityDocuments to test with.
	 *
	 * @return A list of SubCommodityDocuments to test with.
	 */
	private List<SubCommodityDocument> getDocumentList() {
		List<SubCommodityDocument> documents = new ArrayList<>(1);
		documents.add(new SubCommodityDocument(this.getTestSubCommodity()));
		return documents;
	}

	/**
	 * Mocks up a SubCommodityIndexRepository to test with.
	 *
	 * @param page The page to expect.
	 * @param pageSize The pageSize to expect.
	 * @return A SubCommodityIndexRepository to test with.
	 */
	public SubCommodityIndexRepository getRepository(int page, int pageSize, int id) {

		SubCommodityIndexRepository repository = Mockito.mock(SubCommodityIndexRepository.class);
		Mockito.doAnswer(this.getRegularExpressionAnswer(page, pageSize)).when(repository)
				.findByRegularExpression(Mockito.anyString(), Mockito.anyObject());
		Mockito.doAnswer(this.getFindOneAnswer(id)).when(repository).findOne(Mockito.anyString());

		return repository;
	}
}
