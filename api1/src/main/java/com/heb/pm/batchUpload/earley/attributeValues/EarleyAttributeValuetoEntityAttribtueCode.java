package com.heb.pm.batchUpload.earley.attributeValues;

import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Converts records from the Earley attributes/value files to EntityAttributCodes.
 *
 * @author d116773
 * @since 2.16.0
 */
public class EarleyAttributeValuetoEntityAttribtueCode implements ItemProcessor<List<String>, WrappedEntityAttributeCode> {

	@Value("${app.earley.sourceSystemId}")
	private long earleySourceSystemId;

	@Autowired
	private EntityAttributeCodeRepository entityAttributeCodeRepository;

	@Autowired
	private EntityAttributeRepository entityAttributeRepository;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	private EarleyAttributeValuesParser earleyAttributeValuesParser = new EarleyAttributeValuesParser();

	/**
	 * Called by Spring Batch to convert records in the CSV file to EntityAttributes.
	 *
	 * @param item The record in a CSV file.
	 * @return The WrappedEntityAttribute to save.
	 * @throws Exception
	 */
	@Override
	public WrappedEntityAttributeCode process(List<String> item) throws Exception {

		EntityAttributeCode entityAttributeCode;

		String externalId =
				EarleyUploadUtils.getEarlyValueExternalId(this.earleyAttributeValuesParser.parseAttributeValueId(item));
		String attributeValue = this.earleyAttributeValuesParser.parseAttributeValue(item);
		String attributeId = this.earleyAttributeValuesParser.parseAttributeId(item);
		String hierarchyId = this.earleyAttributeValuesParser.parseParentHierarchyId(item);

		// See if an attribute value for this already exists
		AttributeCode attributeCode = this.earleyUploadUtils.getAttributeCode(externalId);
		if (attributeCode == null) {
			// If not, make a new one
			attributeCode = new AttributeCode();
			attributeCode.setAttributeValueCode(attributeValue.length() <= 255 ? attributeValue : attributeValue.substring(0, 255));
			attributeCode.setAttributeValueText(attributeValue.length() <= 4000 ? attributeValue : attributeValue.substring(0, 4000));
			attributeCode.setAttributeValueXtrnlId(externalId);
			attributeCode.setXtrnlCodeSwitch(true);
			attributeCode.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);
			attributeCode.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
			attributeCode.setAction(AttributeCode.INSERT);
		} else {
			attributeCode.setAction(AttributeCode.UPDATE);
		}
		// Find the attribute to attach this to.
		Attribute attribute = this.earleyUploadUtils.getAttribute(attributeId);
		if (attribute == null) {
			// If there is no existing attribute, this is an error. Send it on to be logged so the user know.
			return new WrappedEntityAttributeCode(String.format("Unable to find attribute with the ID %s", attributeId));
		}

		// Find the place in the hierarchy to say this value is appropriate for.
		// TODO: this will be used after the next update to the Earley file.
//		GenericEntity entity = this.getParentEntity(hierarchyId);
//		if (entity == null) {
//			return null;
////				throw new FileProcessingException(String.format("Unable to find hierarchy node with the ID %s", attributeId));
//		}

		// Find any enty_attrs this code applies to.
		// TODO: remove this after the earley update.
		List<EntityAttribute> entityAttributes =
				this.entityAttributeRepository.findByKeyAttributeId(attribute.getAttributeId());
		if (entityAttributes.isEmpty()) {
			return new WrappedEntityAttributeCode(String.format("Unable to find entity attributes tied to attribute ID %s", attribute.getAttributeId()));
		}
		// END TODO

		// Make the entity attribute code to attach this to.
		EntityAttributeCodeKey entityAttributeCodeKey = new EntityAttributeCodeKey();
		entityAttributeCodeKey.setAttributeId(attribute.getAttributeId());
		// TODO: use the entity that is commented out.
		entityAttributeCodeKey.setEntityId(entityAttributes.get(0).getKey().getEntityId());
		entityAttributeCodeKey.setAttributeCodeId(attributeCode.getAttributeCodeId());

		entityAttributeCode = this.entityAttributeCodeRepository.findOne(entityAttributeCodeKey);
		if (entityAttributeCode == null) {
			entityAttributeCode = new EntityAttributeCode();
			entityAttributeCode.setKey(entityAttributeCodeKey);
			entityAttributeCode.setAction(EntityAttributeCode.INSERT);
			entityAttributeCode.setAttributeCode(attributeCode);
			entityAttributeCode.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);
			entityAttributeCode.setCreateTime(LocalDateTime.now());
			entityAttributeCode.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
			entityAttributeCode.setLastUpdateTime(LocalDateTime.now());

			return new WrappedEntityAttributeCode(entityAttributeCode);
		} else {
			// If this already exists, then there's no real reason to do anything as there are no values to update.
			// TODO: use the entity that is commented out.
			return new WrappedEntityAttributeCode(String.format("Attribute values already exist for attribute %s at location %s", attribute.getAttributeId(), entityAttributes.get(0).getKey().getEntityId() ));
		}
	}
}
