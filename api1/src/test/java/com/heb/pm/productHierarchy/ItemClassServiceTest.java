package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ItemClass;
import com.heb.pm.repository.ItemClassIndexRepository;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l730832 on 5/29/2017.
 */
public class ItemClassServiceTest {

	private static final String SEARCH_STRING = "SRCH";
	private static final String REGEX_STRING = "*SRCH*";

	private static final int CLASS_ID = 2334;
	private static final int COMMODITY_ID = 23423;
	private static final String DESCRIPTION = "test class";
	private static final String ITEM_CLASS_INDEX = "234";

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Tests findItemClassesByRegularExpression.
	 */
	@Test
	public void findItemClassesByRegularExpression() {
		ItemClassService itemClassService = this.getService(19, 23);
		PageableResult<ItemClass> list = itemClassService.findItemClassesByRegularExpression(
				ItemClassServiceTest.SEARCH_STRING, 19, 23);
		Assert.assertEquals(this.getTestItemClass(), list.getData().iterator().next());
	}

	/**
	 * Returns a ClassCommodityService to test with. It will have index repositories configured for testing.
	 *
	 * @param page The page number expected in the call.
	 * @param pageSize The page size expected in the call.
	 * @return A ClassCommodityService to test with.
	 */
	private ItemClassService getService(int page, int pageSize) {
		ItemClassService service = new ItemClassService();

		ItemClassIndexRepository itemClassIndexRepository = Mockito.mock(ItemClassIndexRepository.class);
		Mockito.doAnswer(this.getClassSearchAnswer(page, pageSize)).when(itemClassIndexRepository).findByRegularExpression(
				Mockito.anyString(), Mockito.anyObject());
		Mockito.when(itemClassIndexRepository.findOne(Mockito.anyString()))
				.thenReturn(new ItemClassDocument(this.getTestItemClass()));
		service.setItemClassIndexRepository(itemClassIndexRepository);

		return service;
	}

	/**
	 * Returns an answer for when findByRegularExpression is called on the ClassIndexRepository. It checks to make
	 * sure the right parameters are passed to the function.
	 *
	 * @param page The page number expected in the call.
	 * @param pageSize The page size expected in the call.
	 * @return An answer for findByRegularExpression.
	 */
	private Answer<Page<ItemClassDocument>> getClassSearchAnswer(int page, int pageSize) {
		return invocation -> {
			Assert.assertEquals(ItemClassServiceTest.REGEX_STRING, invocation.getArguments()[0]);
			PageRequest request = (PageRequest)invocation.getArguments()[1];
			Assert.assertEquals(page, request.getPageNumber());
			Assert.assertEquals(pageSize, request.getPageSize());
			return new PageImpl<>(this.getClassList());
		};
	}

	/**
	 * Returns a list of ClassDocuments to test with.
	 *
	 * @return A list of ClassDocuments to test with.
	 */
	private List<ItemClassDocument> getClassList() {
		List<ItemClassDocument> classCommodities = new ArrayList<>(1);
		classCommodities.add(new ItemClassDocument(this.getTestItemClass()));
		return classCommodities;
	}

	/**
	 * Creates a ItemClass to test with.
	 *
	 * @return A ItemClass to test with.
	 */
	private ItemClass getTestItemClass() {
		ItemClass itemClass = new ItemClass();
		itemClass.setItemClassCode(CLASS_ID);
		itemClass.setItemClassDescription(DESCRIPTION);
		return itemClass;
	}

	/**
	 * Tests findClass.
	 */
	@Test
	public void findClass() {
		ItemClassService itemClassService = this.getService(19, 23);
		ItemClass cc = itemClassService.findOne(ITEM_CLASS_INDEX);
		Assert.assertEquals(this.getTestItemClass(), cc);
	}

}