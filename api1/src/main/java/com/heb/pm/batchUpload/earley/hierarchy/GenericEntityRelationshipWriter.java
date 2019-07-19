package com.heb.pm.batchUpload.earley.hierarchy;

import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.batchUpload.earley.EntityNotFoundException;
import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.pm.entity.GenericEntity;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;

/**
 * Writes the output of the Early load jobs.
 *
 * @author d116773
 * @since 2.15.0
 */
public class GenericEntityRelationshipWriter implements ItemWriter<WrappedGenericEntityRelationship> {

	private static final Logger logger = LoggerFactory.getLogger(GenericEntityRelationshipWriter.class);

	@Value("${app.earley.HierarchyContext}")
	private String hierarchyContext;

	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	/**
	 * Called by Spring Batch to write Early files.
	 *
	 * @param items The list of relationships to write.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WrappedGenericEntityRelationship> items) throws Exception {

		// Don't need to do anything if the list is empty.
		if (items.isEmpty()) {
			return;
		}

		List<GenericEntityRelationship> entityRelationshipsToAdd = new LinkedList<>();

		// This first part works on the children and parent entities and preps the relationship to be added. Only
		// ones that are ready to be written are added to entityRelationshipsToAdd.
		for (WrappedGenericEntityRelationship wrappedEntityRelationship : items) {

			if (wrappedEntityRelationship.hasEntity()) {
				for (GenericEntityRelationship entityRelationship : wrappedEntityRelationship.getEntity()) {

					GenericEntityRelationshipWriter.logger.debug(entityRelationship.toString());

					if (entityRelationship.getGenericChildEntity() != null) {
						// Each entity has to be written separately. If they were all updates, we wouldn't have to, but
						// if it's being added then that's the only way to get the key.
						try {
							long newEntityID =
									this.codeTableManagementServiceClient.writeEntity(entityRelationship.getGenericChildEntity());
							// If this entity is added, then populate the values in the rest of the relationship.
							entityRelationship.getKey().setChildEntityId(newEntityID);
							entityRelationship.getGenericChildEntity().setId(newEntityID);
						} catch (Exception e) {
							wrappedEntityRelationship.fail(e.getMessage());
							continue;
						}

						// If the parent entity ID is null, then the parent entity might not have existed when the processor
						// was running. It has to exist now (it'll be an error if it doesn't). Look it up set it.
						if (entityRelationship.getKey().getParentEntityId() == null) {
							try {
								entityRelationship.getKey().setParentEntityId(
										this.getParentEntityId(entityRelationship.getParentDisplayNumber()));
							} catch (Exception e) {
								wrappedEntityRelationship.fail(e.getMessage());
								continue;
							}
						}
					}
					entityRelationshipsToAdd.add(entityRelationship);
				}
			}
		}

		// Write out the relationships. These will be in entityRelationshipsToAdd.
		try {
			this.codeTableManagementServiceClient.writeEntityRelationships(entityRelationshipsToAdd);
			for (WrappedGenericEntityRelationship wrappedEntityRelationship : items) {
				if (wrappedEntityRelationship.getSuccess() == null) {
					wrappedEntityRelationship.succeed();
				}
			}
		} catch (Exception e) {
			// All were written in the same transaction, so any error will apply to all of them.
			for (WrappedGenericEntityRelationship wrappedEntityRelationship : items) {
				wrappedEntityRelationship.fail(e.getMessage());
			}
		}
	}

	/**
	 * Looks up an entity based on its display number. It is meant for looking up parents for a given entity,
	 * and, as such, will return the root of the heb.com hierarchy when passed a null.
	 *
	 * @param displayNumber The entity display number to look up.
	 * @return The entity wht the matching display number.
	 */
	private long getParentEntityId(String displayNumber) throws EntityNotFoundException, FileProcessingException {

		GenericEntity genericEntity = this.earleyUploadUtils.getEntityForExternalId(displayNumber);

		if (genericEntity == null) {
			throw new EntityNotFoundException("Parent entity with display number " + displayNumber + " not found");
		}

		return genericEntity.getId();
	}
}
