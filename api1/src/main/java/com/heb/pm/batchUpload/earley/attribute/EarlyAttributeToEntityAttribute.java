package com.heb.pm.batchUpload.earley.attribute;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.pm.entity.*;
import com.heb.pm.repository.AttributeRepository;
import com.heb.pm.repository.EntityAttributeRepository;
import com.heb.pm.repository.GenericEntityRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates EntityAttributes and Attributes for Early attributes.
 *
 * @author d116773
 * @since 2.15.0
 */
public class EarlyAttributeToEntityAttribute implements ItemProcessor<List<String>, WrappedEntityAttribute> {

	@Value("${app.earley.sourceSystemId}")
	private long earleySourceSystemId;

	@Autowired
	private EntityAttributeRepository entityAttributeRepository;

	@Autowired
	private GenericEntityRepository genericEntityRepository;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	private EarleyAttributeParser attributeParser = new EarleyAttributeParser();

	/**
	 * Called by SpringBatch to create the EntityAttribtues.
	 *
	 * @param item The record from the upload file that contains the attribute data.
	 * @return An EntityAttribute based on the record.
	 * @throws Exception
	 */
	@Override
	public WrappedEntityAttribute process(List<String> item) throws Exception {

		// Parse the file
		String hierarhcyId = this.attributeParser.parseParentHierarchyId(item);
		String attribtueId = this.attributeParser.parseAttributeId(item);
		String name = this.attributeParser.parseAttributeName(item);
		String description = this.attributeParser.parseDescription(item);
		boolean required = this.attributeParser.parseRequired(item);
		boolean multiValue = this.attributeParser.parseMultiValue(item);
		String helpText = this.attributeParser.parseHelpText(item);

		GenericEntity parentHierarchyNode;

		try {
			// Get the hierarchy node this attribute is tied to.
			parentHierarchyNode = this.getParentEntity(hierarhcyId);

			EntityAttribute entityAttribute;

			// See if there is an attribute for this record already
			Attribute attribute = this.earleyUploadUtils.getAttribute(attribtueId);
			if (attribute == null) {
				// If not, build it.
				attribute = new Attribute();
				attribute.setAction(Attribute.INSERT);
				attribute.setSourceSystemId(this.earleySourceSystemId);
				attribute.setAttributeName(name);
				attribute.setAttributeDescription(name);
				attribute.setAttributeInstantDescription(description.length() <= 255 ? description : description.substring(0, 255));
				attribute.setMultipleValueSwitch(multiValue);
				attribute.setAttributeDomainCode(Attribute.LIST_DOMAIN_CODE);
				attribute.setAttributeValueList(true);
				attribute.setExternalId(attribtueId);
				attribute.setRequired(required ? "Y" : "N");
				attribute.setPrecision(0L);
				attribute.setMaximumLength(1L);
				attribute.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);
				attribute.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
				attribute.setOptionalOrRequired(Attribute.OPTIONAL);
				attribute.setLogicalOrPhysical(Attribute.PHYSICAL_ATTRIBUTE);
				attribute.setDynamicAttributeSwitch(true);

				// These are always specs
				EcommerUserGroupAttributeKey ecommerUserGroupAttributeKey = new EcommerUserGroupAttributeKey();
				ecommerUserGroupAttributeKey.setUsrInrfcGrpCd(EcommerUserGroupAttribute.SPEC);
				EcommerUserGroupAttribute ecommerUserGroupAttribute = new EcommerUserGroupAttribute();
				ecommerUserGroupAttribute.setKey(ecommerUserGroupAttributeKey);
				ecommerUserGroupAttribute.setSequence(1L);
				attribute.getEcommerUserGroupAttributes().add(ecommerUserGroupAttribute);

				// Since it's a new attribute, then the entity attribute cannot exist either, so make a new one.
				entityAttribute = this.getNewEntityAttribute(parentHierarchyNode, attribute);

			} else {
				// Detach the attribute so it can be updated without trying to save it.
				this.entityManager.detach(attribute);

				if (attribute.getEcommerUserGroupAttributes().isEmpty()) {
					EcommerUserGroupAttributeKey ecommerUserGroupAttributeKey = new EcommerUserGroupAttributeKey();
					ecommerUserGroupAttributeKey.setUsrInrfcGrpCd(EcommerUserGroupAttribute.SPEC);
					EcommerUserGroupAttribute ecommerUserGroupAttribute = new EcommerUserGroupAttribute();
					ecommerUserGroupAttribute.setKey(ecommerUserGroupAttributeKey);
					ecommerUserGroupAttribute.setSequence(1L);
					attribute.getEcommerUserGroupAttributes().add(ecommerUserGroupAttribute);
				} else {
					for (EcommerUserGroupAttribute ecommerUserGroupAttribute : attribute.getEcommerUserGroupAttributes()) {
						this.entityManager.detach(ecommerUserGroupAttribute);
						ecommerUserGroupAttribute.setAction(EcommerUserGroupAttribute.NOOP);
					}
				}
				// If the attribute already exists, see if the entity attribute does.
				entityAttribute = this.getEntityAttribute(parentHierarchyNode, attribute);
				if (entityAttribute == null) {

					// If not, we need a new one.
					entityAttribute = this.getNewEntityAttribute(parentHierarchyNode, attribute);

					// Update the modifiable attributes of the attribute.
					attribute.setAction(Attribute.UPDATE);;
					attribute.setAttributeName(name);
					attribute.setAttributeDescription(name);
					attribute.setAttributeInstantDescription(description.length() <= 255 ? description : description.substring(0, 255));

					attribute.setRequired(required ? "Y" : "N");
					attribute.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
					attribute.setDynamicAttributeSwitch(true);

				} else {

					// Detach the entity-attribute so it can be updated without trying to save it.
					this.entityManager.detach(entityAttribute);

					// Update what might have changed at the entity attribute level.
					entityAttribute.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
					entityAttribute.setRequired(attribute.getRequired().equals("Y"));
					entityAttribute.setEntityAttributeFieldName(attribute.getAttributeName());
					entityAttribute.setAction(EntityAttribute.UPDATE);

					// Update the modifiable attributes of the attribute itself.
					entityAttribute.getAttribute().setAction(Attribute.UPDATE);
					entityAttribute.getAttribute().setAttributeDescription(description.length() <= 255 ? description : description.substring(0, 255));
					entityAttribute.getAttribute().setAttributeInstantDescription(helpText.length() <= 255 ? helpText : helpText.substring(0, 255));
					entityAttribute.getAttribute().setRequired(required ? "Y" : "N");
					entityAttribute.getAttribute().setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
					entityAttribute.getAttribute().setDynamicAttributeSwitch(true);
					entityAttribute.getAttribute().setAttributeName(name);
					entityAttribute.getAttribute().setAttributeDescription(name);
					entityAttribute.getAttribute().setAttributeInstantDescription(description.length() <= 255 ? description : description.substring(0, 255));

					if (entityAttribute.getAttribute().getEcommerUserGroupAttributes().isEmpty()) {
						EcommerUserGroupAttributeKey ecommerUserGroupAttributeKey = new EcommerUserGroupAttributeKey();
						ecommerUserGroupAttributeKey.setUsrInrfcGrpCd(EcommerUserGroupAttribute.SPEC);
						EcommerUserGroupAttribute ecommerUserGroupAttribute = new EcommerUserGroupAttribute();
						ecommerUserGroupAttribute.setKey(ecommerUserGroupAttributeKey);
						ecommerUserGroupAttribute.setSequence(1L);
						entityAttribute.getAttribute().getEcommerUserGroupAttributes().add(ecommerUserGroupAttribute);
					}
				}
			}

			return new WrappedEntityAttribute(entityAttribute);

		} catch (Exception e) {
			// Any exception, just set the error message to be saved to the candidate tables so the user can see them.
			return new WrappedEntityAttribute(e.getLocalizedMessage());
		}
	}

	/**
	 * Returns the Entity for the place in the hierarchy this attribute should be tied to.
	 *
	 * @param hierarchyId The Earley ID of the place in the hierarchy.
	 * @return The Entity tied to the Earley ID.
	 * @throws FileProcessingException
	 */
	private GenericEntity getParentEntity(String hierarchyId) throws FileProcessingException {
		List<GenericEntity> genericEntities =
				this.genericEntityRepository.findByDisplayTextAndType(hierarchyId, GenericEntity.EntyType.CUSTH.getName());
		if (genericEntities.isEmpty()) {
			throw new FileProcessingException(String.format("Entity for hierarchy ID %s not found", hierarchyId));
		}
		if (genericEntities.size() > 1) {
			throw new FileProcessingException(String.format("More than one entity found for hierarchy ID %s", hierarchyId));
		}
		return  genericEntities.get(0);
	}

	/**
	 * Constructs a new EntityAttribute for a given place in the hierarchy.
	 *
	 * @param parentHierarchyNode The place in the hierarchy to tie the attribute to.
	 * @param attribute The attribute.
	 * @return A new EntityAttribute for a given place in the hierarchy.
	 */
	private EntityAttribute getNewEntityAttribute(GenericEntity parentHierarchyNode, Attribute attribute) {
		EntityAttribute entityAttribute = new EntityAttribute();
		entityAttribute.setKey(new EntityAttributeKey());
		entityAttribute.getKey().setEntityId(parentHierarchyNode.getId());
		entityAttribute.getKey().setAttributeId(attribute.getAttributeId());
		entityAttribute.setAttribute(attribute);
		entityAttribute.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);
		entityAttribute.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
		entityAttribute.setBaseAttribute(true);
		entityAttribute.setEntityAttributeFieldName(attribute.getAttributeName());
		entityAttribute.setRequired(attribute.getRequired().equals("Y"));
		entityAttribute.setSequenceNumber(1L);
		entityAttribute.setAction(EntityAttribute.INSERT);
		return entityAttribute;
	}

	/**
	 * Finds an entity attribute for a location in the hierarchy and attribute.
	 *
	 * @param parentHierarchyNode The entity for the place in the hierarchy.
	 * @param attribute The attribute.
	 * @return The entity attribute for a location in the hierarchy and attribute.
	 */
	private EntityAttribute getEntityAttribute(GenericEntity parentHierarchyNode, Attribute attribute) {
		return this.entityAttributeRepository.findByKeyEntityIdAndKeyAttributeId(parentHierarchyNode.getId(),
				attribute.getAttributeId());
	}
}
