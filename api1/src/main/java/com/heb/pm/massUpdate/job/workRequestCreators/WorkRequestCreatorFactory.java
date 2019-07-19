package com.heb.pm.massUpdate.job.workRequestCreators;

import com.heb.pm.massUpdate.MassUpdateParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Constructs WorkRequestCreators.
 *
 * @author d116773
 * @since 2.12.0
 */
@Service
public class WorkRequestCreatorFactory {

	private static final Logger logger = LoggerFactory.getLogger(WorkRequestCreatorFactory.class);

	private static final String UNKNOWN_ATTRIBUTE_EXCEPTION = "%s is an unknown attribute type";
	private static final String REQUEST_LOG_MESSAGE = "Request for attribute of type %s";

	@Autowired
	@Qualifier("thirdPartySellableWorkRequestCreator")
	private WorkRequestCreator thirdPartySellableWorkRequestCreator;

	@Autowired
	@Qualifier("selectIngredientsWorkRequestCreator")
	private WorkRequestCreator selectIngredientsWorkRequestCreator;

	@Autowired
	@Qualifier("totallyTexasWorkRequestCreator")
	private WorkRequestCreator totallyTexasWorkRequestCreator;

	@Autowired
	@Qualifier("goLocalWorkRequestCreator")
	private WorkRequestCreator goLocalWorkRequestCreator;

	@Autowired
	@Qualifier("effectiveDatedMaintenanceWorkRequestCreator")
	private WorkRequestCreator effectiveDatedMaintenanceWorkRequestCreator;

	@Autowired
	@Qualifier("selfManufacturedWorkRequestCreator")
	private SelfManufacturedWorkRequestCreator selfManufacturedWorkRequestCreator;

	@Autowired
	@Qualifier("taxCategoryWorkRequestCreator")
	private TaxCategoryWorkRequestCreator taxCategoryWorkRequestCreator;

	@Autowired
	@Qualifier("primoPickWorkRequestCreator")
	private PrimoPickWorkRequestCreator primoPickWorkRequestCreator;

	@Autowired
	@Qualifier("unassignProductsWorkRequestCreator")
	private UnassignProductsWorkRequestCreator unassignProductsWorkRequestCreator;

	@Autowired
	@Qualifier("tagTypeWorkRequestCreator")
	private TagTypeWorkRequestCreator tagTypeWorkRequestCreator;

	@Autowired
	@Qualifier("moveProductsWorkRequestCreator")
	private MoveProductsWorkRequestCreator moveProductsWorkRequestCreator;

	@Autowired
	@Qualifier("addOrRemoveProductsWorkRequestCreator")
	private AddOrRemoveProductsWorkRequestCreator addOrRemoveProductsWorkRequestCreator;

	@Autowired
	@Qualifier("massFillWorkRequestCreator")
	private MassFillWorkRequestCreator massFillWorkRequestCreator;

	@Autowired
	@Qualifier("pdpTemplateWorkRequestCreator")
	private PdpTemplateWorkRequestCreator pdpTemplateWorkRequestCreator;

	@Autowired
	@Qualifier("fulfillmentChannelWorkRequestCreator")
	private FulfillmentChannelWorkRequestCreator fulfillmentChannelWorkRequestCreator;

	@Autowired
	@Qualifier("showOnSiteWorkRequestCreator")
	private ShowOnSiteWorkRequestCreator showOnSiteWorkRequestCreator;

	@Autowired
	@Qualifier("distinctiveWorkRequestCreator")
	private DistinctiveWorkRequestCreator distinctiveWorkRequestCreator;

	@Autowired
	@Qualifier("produceOrganicWorkRequestCreator")
	private ProduceOrganicWorkRequestCreator produceOrganicWorkRequestCreator;

	/**
	 * Returns a WorkRequestCreator for a requested type.
	 *
	 * @param type The type of attribute you want a creator to update.
	 * @return A class that create CandidateWorkRequests of a specific type.
	 */
	public WorkRequestCreator getCreator(MassUpdateParameters.Attribute type) {

		WorkRequestCreatorFactory.logger.info(String.format(WorkRequestCreatorFactory.REQUEST_LOG_MESSAGE, type));

		if (type.equals(MassUpdateParameters.Attribute.THIRD_PARTY_SELLABLE)) {
			return this.thirdPartySellableWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.SELECT_INGREDIENTS)) {
			return this.selectIngredientsWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.GO_LOCAL)) {
			return this.goLocalWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.TOTALLY_TEXAS)) {
			return this.totallyTexasWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.FOOD_STAMP) || type.equals(MassUpdateParameters.Attribute.FSA) ||
				type.equals(MassUpdateParameters.Attribute.TAX_FLAG)) {
			return this.effectiveDatedMaintenanceWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.SELF_MANUFACTURED)) {
			return this.selfManufacturedWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.TAX_CATEGORY)) {
			return this.taxCategoryWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.PRIMO_PICK)) {
			return this.primoPickWorkRequestCreator;
		}

		if(type.equals((MassUpdateParameters.Attribute.UNASSIGN_PRODUCTS))) {
			return this.unassignProductsWorkRequestCreator;
		}

		if (type.equals(MassUpdateParameters.Attribute.TAG_TYPE)) {
			return this.tagTypeWorkRequestCreator;
		}

		if(type.equals((MassUpdateParameters.Attribute.MOVE_HIERARCHY_PRODUCT))) {
			return this.moveProductsWorkRequestCreator;
		}

		if(type.equals((MassUpdateParameters.Attribute.ADD_HIERARCHY_PRODUCT)) ||
				type.equals((MassUpdateParameters.Attribute.REMOVE_HIERARCHY_PRODUCT)) ||
				type.equals((MassUpdateParameters.Attribute.ADD_HIERARCHY_PRODUCT_GROUP)) ||
				type.equals((MassUpdateParameters.Attribute.REMOVE_HIERARCHY_PRODUCT_GROUP))) {
			return this.addOrRemoveProductsWorkRequestCreator;
		}

		if(type.equals(MassUpdateParameters.Attribute.PRIMARY_PATH)) {
			return this.massFillWorkRequestCreator;
		}

		if (type.equals((MassUpdateParameters.Attribute.PDP_TEMPLATE))) {
			return this.pdpTemplateWorkRequestCreator;
		}

		if (type.equals((MassUpdateParameters.Attribute.FULFILLMENT_CHANNEL))) {
			return this.fulfillmentChannelWorkRequestCreator;
		}
		// return work request creator for Show on site case.
		if (type.equals(MassUpdateParameters.Attribute.SHOW_ON_SITE)) {
			return this.showOnSiteWorkRequestCreator;
		}
		// return work request creator for distinctive case.
		if (type.equals(MassUpdateParameters.Attribute.DISTINCTIVE)) {
			return this.distinctiveWorkRequestCreator;
		}
		// return work request creator for produce organic.
		if (type.equals(MassUpdateParameters.Attribute.PRODUCE_ORGANIC)) {
			return this.produceOrganicWorkRequestCreator;
		}
		// If we get here, then there isn't a registered creator for the type the user sent.
		String errorMessage = String.format(WorkRequestCreatorFactory.UNKNOWN_ATTRIBUTE_EXCEPTION, type);
		WorkRequestCreatorFactory.logger.error(errorMessage);
		throw new UnknownAttributeException(errorMessage);
	}
}
