package com.heb.pm.eCommerce;

import com.heb.pm.ecommerce.EcommerceController;
import com.heb.pm.ecommerce.EcommerceService;
import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.LogicalPhysicalRelationship;
import com.heb.pm.entity.LogicalPhysicalRelationshipKey;
import com.heb.pm.entity.RelationshipGroup;
import com.heb.pm.entity.SourceSystem;
import com.heb.pm.entity.TargetSystemAttributePriority;
import com.heb.pm.entity.TargetSystemAttributePriorityKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import testSupport.CommonMocks;
import java.util.ArrayList;
import java.util.List;

/**
 * @author s753601
 * @version 2.7.0
 */
public class EcommerceControllerTest {
	private String DEFAULT_NAME_DESCRIPTION_STRING = "Test";
	private String GET_RELATIONSHIP_GROUP_CODE = "Grp ";
	private String SKIP_RELATIONSHIP_GROUP_CODE = "";


	@InjectMocks
	private EcommerceController ecommerceController;

	@Mock
	private EcommerceService ecommerceService;

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getSourcePriorityTable() throws Exception {
		this.ecommerceController.setUserInfo(CommonMocks.getUserInfo());
		Mockito.when(this.ecommerceService.getSourcePriorityTable()).thenReturn(getTargetSystemAttributePriorityList());
		List<TargetSystemAttributePriority> testResult = this.ecommerceController.getSourcePriorityTable(CommonMocks.getServletRequest());
		Assert.assertEquals(getTargetSystemAttributePriorityList(), testResult);
	}

	/**
	 * Generates a generic List<TargetSystemAttributePriority>
	 * @return a list used for testing
	 */
	protected List<TargetSystemAttributePriority> getTargetSystemAttributePriorityList(){
		ArrayList<TargetSystemAttributePriority> targetSystemAttributePriorities = new ArrayList<>();
		TargetSystemAttributePriority first = getDefaultTargetSystemAttributePriority(3, false);
		TargetSystemAttributePriority second = getDefaultTargetSystemAttributePriority(4, true);
		targetSystemAttributePriorities.add(first);
		targetSystemAttributePriorities.add(second);
		return targetSystemAttributePriorities;
	}

	/**
	 * Generates a generic TargetSystemAttributePriority
	 * The reason has everything set so that it can be resolved.
	 * @param number for the priority
	 * @return the generic object
	 */
	protected TargetSystemAttributePriority getDefaultTargetSystemAttributePriority(int number, boolean flag){
		TargetSystemAttributePriority defaultTargetSystemAttributePriority = new TargetSystemAttributePriority();
		TargetSystemAttributePriorityKey key = new TargetSystemAttributePriorityKey();
		Attribute attribute = new Attribute();
		attribute.setAttributeName(DEFAULT_NAME_DESCRIPTION_STRING);
		SourceSystem sourceSystem = new SourceSystem();
		sourceSystem.setDescription(DEFAULT_NAME_DESCRIPTION_STRING);
		LogicalPhysicalRelationship logicalPhysicalRelationship = new LogicalPhysicalRelationship();
		logicalPhysicalRelationship.setKey(new LogicalPhysicalRelationshipKey());
		if(flag){
			logicalPhysicalRelationship.getKey().setRelationshipGroupTypeCode(GET_RELATIONSHIP_GROUP_CODE);
			RelationshipGroup relationshipGroup = new RelationshipGroup();
			relationshipGroup.setRelationshipGroupDescription(DEFAULT_NAME_DESCRIPTION_STRING);
			logicalPhysicalRelationship.setRelationshipGroup(relationshipGroup);
		} else {
			logicalPhysicalRelationship.getKey().setRelationshipGroupTypeCode(SKIP_RELATIONSHIP_GROUP_CODE);
		}
		logicalPhysicalRelationship.setAttribute(attribute);
		defaultTargetSystemAttributePriority.setKey(key);
		defaultTargetSystemAttributePriority.setAttribute(attribute);
		defaultTargetSystemAttributePriority.setSourceSystem(sourceSystem);
		defaultTargetSystemAttributePriority.setLogicalPhysicalRelationship(logicalPhysicalRelationship);
		defaultTargetSystemAttributePriority.setAttributePriorityNumber(number);
		return defaultTargetSystemAttributePriority;
	}

}