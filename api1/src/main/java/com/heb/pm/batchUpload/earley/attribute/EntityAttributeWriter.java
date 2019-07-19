package com.heb.pm.batchUpload.earley.attribute;

import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.EcommerUserGroupAttribute;
import com.heb.pm.entity.EntityAttribute;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Writes attributes to the DB.
 *
 * @author d116773
 * @since 2.15.0
 */
public class EntityAttributeWriter implements ItemWriter<WrappedEntityAttribute> {

	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;

	/**
	 * Called by Spring Batch to save entity attribtues.
	 *
	 * @param entityAttributes The list of entity attributes to save.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WrappedEntityAttribute> entityAttributes) throws Exception {

		List<Attribute> attributesToUpdate = new LinkedList<>();

		try {
			// First need to loop through and pull out the attributes that need to be updated.
			for (WrappedEntityAttribute entityAttribute : entityAttributes) {
				if (entityAttribute.hasEntity()) {
					attributesToUpdate.add(entityAttribute.getEntity().getAttribute());
				}
			}

			// Write out the attributes.
			if (!attributesToUpdate.isEmpty()) {
				this.codeTableManagementServiceClient.writeAttribute(attributesToUpdate);
				for (Attribute attribute : attributesToUpdate) {
					// Update and save the ecom_usr_grp_attr recrods
					for (EcommerUserGroupAttribute ecommerUserGroupAttribute : attribute.getEcommerUserGroupAttributes()) {
						ecommerUserGroupAttribute.getKey().setAttributeId(attribute.getAttributeId());
					}
					this.codeTableManagementServiceClient.writeEcommerUserGroupAttributes(
							attribute.getEcommerUserGroupAttributes());
				}
			}

			// Now, need to write out the entity attributes
			List<EntityAttribute> entityAttributesToUpdate = new LinkedList<>();
			for (WrappedEntityAttribute entityAttribute : entityAttributes) {
				if (entityAttribute.hasEntity()) {
					// The step above may have created attributes, so save the ID to the key.
					entityAttribute.getEntity().getKey().setAttributeId(
							entityAttribute.getEntity().getAttribute().getAttributeId());
					entityAttributesToUpdate.add(entityAttribute.getEntity());
				}
			}
			if (!entityAttributesToUpdate.isEmpty()) {
				this.codeTableManagementServiceClient.writeEntityAttributes(entityAttributesToUpdate);

			}

			// Mark all the processed entity/attributes as successfully done.
			for (WrappedEntityAttribute entityAttribute : entityAttributes) {
				if (entityAttribute.getEntity() != null) {
					entityAttribute.succeed();
				}
			}
		} catch (Exception e) {
			// If there was any kind of error, mark the batch as not processed.
			for (WrappedEntityAttribute entityAttribute : entityAttributes) {
				entityAttribute.fail(e.getLocalizedMessage());
			}
		}
	}
}
