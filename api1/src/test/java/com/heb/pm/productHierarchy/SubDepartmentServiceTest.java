/*
 * SubDepartmentServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import com.heb.pm.repository.SubDepartmentIndexRepository;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tests SubDepartmentService.
 *
 * @author d116773
 * @since 2.0.2
 */
//@RunWith(LoggingSupportTestRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SubDepartmentServiceTest {

	/*
	 * findByRegularExpression
	 */

	/**
	 * Tests findByRegularExpression.
	 */
	@Test
	public void findByRegularExpression() {

		SubDepartmentService subDepartmentService = new SubDepartmentService();
		subDepartmentService.setIndexRepository(this.getSubDepartmentIndexRepository("SrchStrng"));

		PageableResult<SubDepartment> result = subDepartmentService.findByRegularExpression("SrchStrng", 0, 100);
		Assert.assertTrue(result.isComplete());
		Assert.assertEquals(1L, (long)result.getRecordCount());
		Assert.assertEquals(1L, (long)result.getPageCount());
		Assert.assertEquals(this.getSubDepartment(), result.getData().iterator().next());
	}

	/*
	 * findAll
	 */

	/**
	 * Tests findAll.
	 */
	@Test
	public void findAll() {
		SubDepartmentService subDepartmentService = new SubDepartmentService();
		subDepartmentService.setIndexRepository(this.getSubDepartmentIndexRepository("SrchStrng"));

		Iterable<SubDepartment> subDepartments = subDepartmentService.findAll();
		Iterator<SubDepartment> itr = subDepartments.iterator();
		Assert.assertEquals(this.getSubDepartment(), itr.next());
		Assert.assertFalse(itr.hasNext());
	}

	/*
	 * findSubDepartment
	 */

	/**
	 * Tests findSubDepartment
	 */
	@Test
	public void findSubDepartment() {
		SubDepartmentService service= new SubDepartmentService();
		service.setIndexRepository(this.getSubDepartmentIndexRepository("07A"));

		SubDepartment s = service.findSubDepartment("07A");
		Assert.assertEquals(this.getSubDepartmentDocument().getData(), s);
	}
	/*
	 * Support functions
	 */

	/**
	 * Returns a SubDepartment to test with.
	 *
	 * @return A SubDepartmetn to test with.
	 */
	private SubDepartment getSubDepartment() {

		SubDepartmentKey key = new SubDepartmentKey();
		key.setSubDepartment("A   ");
		key.setDepartment("07     ");

		SubDepartment subDepartment = new SubDepartment();
		subDepartment.setKey(key);
		subDepartment.setName("my sub-department");
		return subDepartment;
	}

	/**
	 * Returns a SubDepartmentDocument to test with.
	 *
	 * @return A SubDepartmentDocument to test with.
	 */
	private SubDepartmentDocument getSubDepartmentDocument() {
		return new SubDepartmentDocument(this.getSubDepartment());
	}

	private List<SubDepartmentDocument> getSubDepartmentDocumentList() {

		List<SubDepartmentDocument> subDepartmentDocuments = new ArrayList<>();
		subDepartmentDocuments.add(this.getSubDepartmentDocument());
		return subDepartmentDocuments;
	}

	/**
	 * Returns a Page with a list with the SubDepartmentDocument from getSubDepartmentDocument inside of it.
	 *
	 * @return A Page with a list with the SubDepartmentDocument from getSubDepartmentDocument inside of it.
	 */
	private Page<SubDepartmentDocument> getSubDepartmentListPage() {

		return new PageImpl<>(this.getSubDepartmentDocumentList(), null, 1);
	}

	/**
	 * An answer for getRegularExpression that will make sure the string passed to findByRegularExpression
	 * gets converted to the right regular expression string and is converted to all upper case.
	 */
	private class RegularExpressionCheckingCallChecker implements Answer<Page<SubDepartmentDocument>> {

		private String searchString;
		private Page<SubDepartmentDocument> page;

		/**
		 * Constructs a new RegularExpressionCheckingCallChecker.
		 *
		 * @param searchString The string which will be passed to findByRegularExpression on the index repository.
		 * @param page The Page to return from the mocked function.
		 */
		public RegularExpressionCheckingCallChecker(String searchString, Page<SubDepartmentDocument> page) {
			this.searchString = String.format("*%s*", searchString.toUpperCase());
			this.page = page;
		}

		/**
		 * The mocked call to findByRegularExpression.
		 *
		 * @param invocation The method call.
		 * @return The Page passed as a constructor parameter to this class.
		 * @throws Throwable
		 */
		@Override
		public Page<SubDepartmentDocument> answer(InvocationOnMock invocation) throws Throwable {
			String regularExpression = (String)invocation.getArguments()[0];
			Assert.assertEquals(this.searchString, regularExpression);
			return this.page;
		}
	}

	/**
	 * An answer for findOne that will make sure the right search string gets passed to the method.
	 */
	private class StringCheckingCallChecker implements Answer<SubDepartmentDocument> {

		private String searchString;
		private SubDepartmentDocument toReturn;

		/**
		 * Constructs a new StringCheckingCallChecker.
		 *
		 * @param searchString The string to check gets passed to the method.
		 * @param toReturn The SubDepartmentDocument for the method to return.
		 */
		public StringCheckingCallChecker(String searchString, SubDepartmentDocument toReturn) {
			this.searchString = searchString;
			this.toReturn = toReturn;
		}

		/**
		 * Moclks the call to findOne.
		 *
		 * @param invocation The method call.
		 * @return The SubDepartmentDocument passed as a constructor parameter to this class.
		 * @throws Throwable
		 */
		@Override
		public SubDepartmentDocument answer(InvocationOnMock invocation) throws Throwable {
			Assert.assertEquals(this.searchString, invocation.getArguments()[0]);
			return this.toReturn;
		}
	}

	/**
	 * Returns a SubDepartmentIndexRepository to use in tests.
	 *
	 * @param searchString The search string that will be passed to findByRegularExpression.
	 * @return A SubDepartmentIndexRepository to use in tests.
	 */
	private SubDepartmentIndexRepository getSubDepartmentIndexRepository(String searchString) {

		SubDepartmentIndexRepository indexRepository = Mockito.mock(SubDepartmentIndexRepository.class);

		Page<SubDepartmentDocument> page = this.getSubDepartmentListPage();

		// Need the answer for calls to findByRegularExpression so that it can check that the
		// regular expression is created correctly.
		Mockito.doAnswer(new RegularExpressionCheckingCallChecker(searchString, page)).when(indexRepository)
				.findByRegularExpression(Mockito.anyString(), Mockito.anyObject());

		// Mock the call to findOne
		Mockito.doAnswer(new StringCheckingCallChecker(searchString, this.getSubDepartmentDocument()))
				.when(indexRepository).findOne(Mockito.anyString());

		// For findAll, just getting the right list back is enough.
		Mockito.when(indexRepository.findAll()).thenReturn(this.getSubDepartmentDocumentList());

		return indexRepository;
	}

	// The code below was used to test the service. Saving to put into a functional-test directory.
//	@Autowired
//	private SubDepartmentService subDepartmentService;
//
//	/**
//	 * Since the configuration of Elasticsearch is a little to complex for the XML file, it's put here. It will
//	 * create all the beans needed to run Elasticsearch for this test.
//	 */
//	@Configuration
//	@EnableElasticsearchRepositories(basePackages = "com.heb.pm.subDepartment")
//	@ImportResource("classpath*:testConfig.xml")
//	static class ContextConfiguration {
//
//		private static final String elasticsearchRoot = "elasticsearchRoot";
//
//		@Bean
//		public Client getSearchTemplate() {
//
//			File rootDir = new File(ContextConfiguration.elasticsearchRoot);
//			if (!rootDir.isDirectory()) {
//				boolean success = rootDir.mkdir();
//				if (!success) {
//					throw new RuntimeException("could not create directory " + ContextConfiguration.elasticsearchRoot);
//				}
//			}
//
//			ImmutableSettings.Builder settings =
//					ImmutableSettings.settingsBuilder().put("http.enabled", "false")
//							.put("path.data", ContextConfiguration.elasticsearchRoot);
//
//			return new NodeBuilder().local(true).settings(settings.build()).node().client();
//		}
//
//		@Bean
//		public ElasticsearchOperations elasticsearchTemplate() {
//			return new ElasticsearchTemplate(getSearchTemplate());
//		}
//	}
//
//	//@Test
//	public void findAll() {
//		this.subDepartmentService.findAll().forEach((s) -> System.out.println(s));
//	}
//
//	@Test
//	public void findAllByRegularExpression() {
//		PageableResult<SubDepartment> data = this.subDepartmentService.findByRegularExpression("07", 0, 100);
//		data.getData().forEach((s) -> System.out.println(s));
//	}
}
