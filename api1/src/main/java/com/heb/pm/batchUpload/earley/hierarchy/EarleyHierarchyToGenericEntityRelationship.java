package com.heb.pm.batchUpload.earley.hierarchy;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.entity.*;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import com.heb.pm.repository.GenericEntityRepository;
import com.heb.pm.repository.HierarchyContextRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Converts records from the Earley hierarchy file to entity relationships.
 *
 * @author d116773
 * @since 2.16.0
 */
public class EarleyHierarchyToGenericEntityRelationship implements ItemProcessor<List<String>, WrappedGenericEntityRelationship> {

	@Value("${app.earley.HierarchyContext}")
	private String hierarchyContext;

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	private HierarchyContextRepository hierarchyContextRepository;

	@Autowired
	private GenericEntityRepository genericEntityRepository;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	private EarleyHierarchyParser earleyHierarchyParser = new EarleyHierarchyParser();

	/**
	 * Called by the Spring Batch framework to convert records from the Earley hierarchy file to entity relationships.
	 *
	 * @param csvRecord A record in the hierarchy file.
	 * @return A GenericEntityRelationship with the entity defined and a tie back to its parent.
	 * @throws Exception
	 */
	@Override
	public WrappedGenericEntityRelationship process(List<String> csvRecord) throws Exception {

		try {
			String earleyHierarchyId = this.earleyHierarchyParser.parseEarleyHierarchyId(csvRecord);
			String shortName = this.earleyHierarchyParser.parseShortName(csvRecord);
//			String longName = this.earleyHierarchyParser.parseLongName(csvRecord);
			String earleyParentIdAsString = this.earleyHierarchyParser.parseParentEarleyHierarchyId(csvRecord);

			GenericEntity childEntity = this.earleyUploadUtils.getEntityForExternalId(earleyHierarchyId);

			GenericEntity parentEntity;

			// If the parent hierarchy is blank, then it's tied to the root node (an L1). Otherwise, they've given
			// the ID of the parent.
			if (earleyParentIdAsString.isEmpty()) {
				Long parentEntityId = this.hierarchyContextRepository.findOne(this.hierarchyContext).getParentEntityId();
				parentEntity = this.genericEntityRepository.findOne(parentEntityId);
			} else {
				parentEntity = this.earleyUploadUtils.getEntityForExternalId(earleyParentIdAsString);
			}

			GenericEntityRelationship genericEntityRelationship;

			if (childEntity == null) {
				// Nothing exists, so create it all.
				genericEntityRelationship =
						this.makeFullStructure(earleyHierarchyId, parentEntity, earleyParentIdAsString);
			} else {
				this.entityManager.detach(childEntity);
				// Find the existing relationship.
				genericEntityRelationship = this.getExistingRelationship(childEntity, parentEntity);
				if (genericEntityRelationship == null) {
					// The child entity exists, but the relationship doesn't.
					genericEntityRelationship =
							this.makeNewRelationship(childEntity, parentEntity, earleyParentIdAsString);
				} else {
					this.entityManager.detach(genericEntityRelationship);
					genericEntityRelationship.setAction(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
				}
			}

			// Update all the fields that are update-able.
			genericEntityRelationship.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
			genericEntityRelationship.setDisplay(Boolean.TRUE);
			genericEntityRelationship.setActive(this.earleyHierarchyParser.parseActive(csvRecord));
			// If there's no description (really an error condition), create it.
			if (genericEntityRelationship.getGenericChildEntity().getDescriptions().isEmpty()) {
				GenericEntityDescriptionKey genericEntityDescriptionKey = new GenericEntityDescriptionKey();
				genericEntityDescriptionKey.setHierarchyContext(this.hierarchyContext);
				GenericEntityDescription genericEntityDescription = new GenericEntityDescription();
				genericEntityDescription.setKey(genericEntityDescriptionKey);
				genericEntityDescription.setAction(GenericEntityDescription.INSERT);
				genericEntityRelationship.getGenericChildEntity().getDescriptions().add(genericEntityDescription);
			} else {
				genericEntityRelationship.getGenericChildEntity().getDescriptions().get(0).setAction(GenericEntityDescription.UPDATE);
			}
			// Since this job created it, there's only one description.
			GenericEntityDescription description =
					genericEntityRelationship.getGenericChildEntity().getDescriptions().get(0);
			description.setShortDescription(shortName.length() <= 50 ? shortName : shortName.substring(0, 50));
			description.setLongDescription(shortName.length() <= 255 ? shortName : shortName.substring(0, 255));

			childEntity = genericEntityRelationship.getGenericChildEntity();
			childEntity.setAbbreviation(shortName.length() <= 6 ? shortName : shortName.substring(0, 6));

			return new WrappedGenericEntityRelationship(genericEntityRelationship);
		} catch (Exception e) {
			return new WrappedGenericEntityRelationship(e.getMessage());
		}
	}

	/**
	 * Called to construct the full data structure. This will make a relationship, the entity, and the entity
	 * description.
	 *
	 * @param earleyHierarchyId The Earley ID of this location in the hierarchy.
	 * @param parentEntity The entity that represents the parent of the one to create. This may be null.
	 * @param earleyParentIdAsString The Earley ID of the partent location. This may be null if this should be
	 *                               tied to the root hierarchy.
	 * @return The entity relationship that contains the constructed objects.
	 */
	private GenericEntityRelationship makeFullStructure(String earleyHierarchyId, GenericEntity parentEntity,
													   String earleyParentIdAsString) {

		GenericEntityDescriptionKey genericEntityDescriptionKey = new GenericEntityDescriptionKey();
		genericEntityDescriptionKey.setHierarchyContext(this.hierarchyContext);
		GenericEntityDescription genericEntityDescription = new GenericEntityDescription();
		genericEntityDescription.setKey(genericEntityDescriptionKey);
		genericEntityDescription.setAction(GenericEntityDescription.INSERT);

		GenericEntity genericEntity = new GenericEntity();
		genericEntity.setDisplayNumber(EarleyUploadUtils.getEarlyHierarchyIdAsLong(earleyHierarchyId));
		genericEntity.setDisplayText(earleyHierarchyId);
		genericEntity.setType(GenericEntity.EntyType.CUSTH.getName());
		genericEntity.getDescriptions().add(genericEntityDescription);
		genericEntity.setAction(GenericEntity.INSERT);

		return this.makeNewRelationship(genericEntity, parentEntity, earleyParentIdAsString);
	}

	/**
	 * Takes an existing entity and builds the entity relationship it should be a child in.
	 *
	 * @param child The child entity to add to the relationship.
	 * @param parentEntity The entity that represents the parent of the one to create. This may be null.
	 * @param earleyParentIdAsString The Earley ID of the partent location. This may be null if this should be
	 *                               tied to the root hierarchy.
	 * @return The entity relationship with the supplied child and parent tied together.
	 */
	private GenericEntityRelationship makeNewRelationship(GenericEntity child, GenericEntity parentEntity,
														 String earleyParentIdAsString) {

		GenericEntityRelationshipKey key = new GenericEntityRelationshipKey();
		key.setChildEntityId(child.getId());
		if (parentEntity != null) {
			key.setParentEntityId(parentEntity.getId());
		}
		key.setHierarchyContext(this.hierarchyContext);
		GenericEntityRelationship genericEntityRelationship = new GenericEntityRelationship();
		genericEntityRelationship.setKey(key);
		genericEntityRelationship.setGenericChildEntity(child);
		genericEntityRelationship.setGenericParentEntity(parentEntity);
		genericEntityRelationship.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);

		// This is needed in the writer if the parent doesn't already exist but gets created while processing
		// this batch.
		genericEntityRelationship.setParentDisplayNumber(earleyParentIdAsString);

		// Determine if this is the default parent or not.
		if (child.getId() == null) {
			// The child entity does not already exist in the DB, so this must be the default relationship.
			genericEntityRelationship.setDefaultParent(Boolean.TRUE);
			genericEntityRelationship.setSequence(1L);
		} else {
			List<GenericEntityRelationship> existingRelationships =
					this.genericEntityRelationshipRepository.findByKeyChildEntityIdAndHierarchyContext(child.getId(), this.hierarchyContext);
			if (existingRelationships.isEmpty()) {
				// The child exists, but it's not in any relationships already.
				genericEntityRelationship.setDefaultParent(Boolean.TRUE);
				genericEntityRelationship.setSequence(1L);
			} else {
				// THe child is already in relationships, so this cannot be the default parent.
				genericEntityRelationship.setDefaultParent(Boolean.FALSE);
				genericEntityRelationship.setSequence((long)existingRelationships.size() + 1);
			}
		}


		genericEntityRelationship.setDisplay(Boolean.TRUE);
		genericEntityRelationship.setEffectiveDate(LocalDate.now());
		genericEntityRelationship.setExpirationDate(LocalDate.of(9999, 12, 31));

		genericEntityRelationship.setAction(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());

		return genericEntityRelationship;
	}

	/**
	 * Returns an existing (in the DB) relationship between a child and parent node.
	 *
	 * @param child The child node of the relationship.
	 * @param parent The parent node of the relationship.
	 * @return The existing relationship between the two. Will be null if it does not exist.
	 */
	private GenericEntityRelationship getExistingRelationship(GenericEntity child, GenericEntity parent) {

		if (child == null || parent == null) {
			return null;
		}

		GenericEntityRelationshipKey key = new GenericEntityRelationshipKey();
		key.setChildEntityId(child.getId());
		key.setParentEntityId(parent.getId());
		key.setHierarchyContext(this.hierarchyContext);

		return this.genericEntityRelationshipRepository.findOne(key);
	}
}
