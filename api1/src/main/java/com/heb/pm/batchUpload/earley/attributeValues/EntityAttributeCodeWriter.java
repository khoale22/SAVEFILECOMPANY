package com.heb.pm.batchUpload.earley.attributeValues;

import com.heb.pm.entity.AttributeCode;
import com.heb.pm.entity.EntityAttributeCode;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Writes EntityAttributeCodes from the Earley file.
 *
 * @author d116773
 * @since 2.16.0
 */
public class EntityAttributeCodeWriter implements ItemWriter<WrappedEntityAttributeCode> {

	private static final Logger logger = LoggerFactory.getLogger(EntityAttributeCodeWriter.class);

	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;

	/**
	 * Called by Spring Batch to save the EntityAttributeCodes.
	 *
	 * @param items The list of EntityAttributeCodes to save.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WrappedEntityAttributeCode> items) throws Exception {

		items.forEach((i) -> {
			if (i.hasEntity()) {
				logger.info(i.getEntity().toString());
			}
		});
		List<AttributeCode> attributeCodesToAdd = new LinkedList<>();

		// Write out any attribute values that need to be written out.
		for (WrappedEntityAttributeCode wrappedEntityAttributeCode : items) {
			if (wrappedEntityAttributeCode.hasEntity()) {
				attributeCodesToAdd.add(wrappedEntityAttributeCode.getEntity().getAttributeCode());
			}
		}

		try {
			this.codeTableManagementServiceClient.writeAttributeCodes(attributeCodesToAdd);
		} catch (Exception e) {
			// If there are any errors, update the wrapped values so it can be put into the tracking table.
			for (WrappedEntityAttributeCode wrappedEntityAttributeCode : items) {
				wrappedEntityAttributeCode.fail(e.getMessage());
			}
			return;
		}

		// Write out the entity attribute codes.
		List<EntityAttributeCode> entityAttributeCodesToWrite = new LinkedList<>();

		for (WrappedEntityAttributeCode wrappedEntityAttributeCode : items) {
			if (wrappedEntityAttributeCode.hasEntity()) {
				// Update the attribute code id. These may have been created in the step above, in which case
				// they will not be set in the EntityAttributeCodes.
				wrappedEntityAttributeCode.getEntity().getKey().setAttributeCodeId(
						wrappedEntityAttributeCode.getEntity().getAttributeCode().getAttributeCodeId());
				entityAttributeCodesToWrite.add(wrappedEntityAttributeCode.getEntity());
			}
		}

		try {
			this.codeTableManagementServiceClient.writeEntityAttributeCodes(entityAttributeCodesToWrite);
		} catch (Exception e) {
			// If there are any errors, update the wrapped values so it can be put into the tracking table.
			for (WrappedEntityAttributeCode wrappedEntityAttributeCode : items) {
				wrappedEntityAttributeCode.fail(e.getMessage());
			}
			return;
		}

		// Update the status of each wrapped entity.
		for (WrappedEntityAttributeCode wrappedEntityAttributeCode : items) {
			if (wrappedEntityAttributeCode.hasEntity()) {
				wrappedEntityAttributeCode.succeed();
			}
		}
	}
}
