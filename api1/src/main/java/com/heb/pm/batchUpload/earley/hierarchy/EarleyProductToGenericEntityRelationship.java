package com.heb.pm.batchUpload.earley.hierarchy;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.batchUpload.earley.EntityNotFoundException;
import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.pm.entity.*;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import com.heb.pm.repository.GenericEntityRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Converts a record in the Earley product file to an entity relationship.
 *
 * @author d116773
 * @since 2.15.0
 */
public class EarleyProductToGenericEntityRelationship implements ItemProcessor<List<String>, WrappedGenericEntityRelationship> {

	private static final Logger logger = LoggerFactory.getLogger(EarleyProductToGenericEntityRelationship.class);

	@Value("${app.earley.HierarchyContext}")
	private String hierarchyContext;

	@Autowired
	private GenericEntityRepository genericEntityRepository;

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	EarleyProductParser earleyProductParser = new EarleyProductParser();

	/**
	 * Called by the Spring Batch framework to convert an Earley product file to an entity relationship.
	 *
	 * @param csvRecord The record from the Earley product file.
	 * @return An entity relationship.
	 * @throws Exception
	 */
	@Override
	public WrappedGenericEntityRelationship process(List<String> csvRecord) throws Exception {

		// Get the product ID.
		long productId;
		try {
			productId = this.earleyProductParser.parseProductId(csvRecord);
		} catch (FileProcessingException e) {
			return new WrappedGenericEntityRelationship(e.getMessage());
		}

		// Should this product be shown on heb.com?
		boolean showOnHebCom = this.earleyProductParser.parseShowOnHebCom(csvRecord);

		try {
			// Get the parent node.
			String earleyHierarchyId = this.earleyProductParser.parseParentHierarchyId(csvRecord);
			GenericEntity parentEntity = this.earleyUploadUtils.getEntityForExternalId(earleyHierarchyId);
			if (parentEntity == null) {
				return new WrappedGenericEntityRelationship(String.format("Unable to find entity for ID %s", earleyHierarchyId));
			}

			// Make sure the parent doesn't already have children that are not products.
			Long nonProductChildrenOfParent = this.genericEntityRelationshipRepository.countNonProductChildrenEntities(parentEntity.getId(), this.hierarchyContext);
			if (nonProductChildrenOfParent > 0) {
				return new WrappedGenericEntityRelationship(String.format("Hierarchy level %s contains entries that are not products", earleyHierarchyId));
			}

			GenericEntity productEntity = this.getProductEntity(productId);

			// Find the existing relationships the product is in.
			List<GenericEntityRelationship> existingRelationships =
					this.genericEntityRelationshipRepository.findByKeyChildEntityIdAndHierarchyContext(productEntity.getId(),
							this.hierarchyContext);
			existingRelationships.forEach(this.entityManager::detach);

			GenericEntityRelationship productToHierarchyTie = this.getProductToHierarchyTie(productEntity, parentEntity,
					existingRelationships, showOnHebCom);

			// See if the product is already tied to something else in the hierarchy. If so, it'll be updated to
			// not have that as the primary parent.
			List<GenericEntityRelationship> dePrimaryExisting = this.dePrimaryExisting(existingRelationships, parentEntity);

			if (dePrimaryExisting.isEmpty()) {
				return new WrappedGenericEntityRelationship(productToHierarchyTie);
			} else {
				List<GenericEntityRelationship> relationshipsToUpdate = new LinkedList<>();
				relationshipsToUpdate.add(productToHierarchyTie);
				relationshipsToUpdate.addAll(dePrimaryExisting);
				return new WrappedGenericEntityRelationship(relationshipsToUpdate);
			}
		} catch (Exception e)  {
			return new WrappedGenericEntityRelationship(e.getMessage());
		}
	}

	/**
	 * Go through the list of existing relationships a child is in and return any not added by this process
	 * that has a primary parent set to true. There should be only one, but I'm finding data where that is not
	 * true, this returns the list of all the ones that have primary parent set to true.
	 *
	 * @param existingRelationships The relationships the product is already in.
	 * @param parentEntity The parent entity this class is currently processing (the Earley one).
	 * @return A list of relationships that have the default parent set to true that need to be set to false. This
	 * list may be empty but will not be null.
	 */
	private List<GenericEntityRelationship> dePrimaryExisting(List<GenericEntityRelationship> existingRelationships,
														GenericEntity parentEntity) {

		List<GenericEntityRelationship> genericEntityRelationships = new LinkedList<>();

		for (GenericEntityRelationship er : existingRelationships) {
			if (er.getDefaultParent()) {
				// This is the one added by this process, so just go on to the others.
				if (er.getKey().getParentEntityId().equals(parentEntity.getId())) {
					continue;
				}
				er.setAction(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
				er.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
				// Any changes will be done on the other child.
				er.getGenericChildEntity().setAction(GenericEntity.NOOP);
				for (GenericEntityDescription genericEntityDescription : er.getGenericChildEntity().getDescriptions()) {
					genericEntityDescription.setAction(GenericEntityDescription.NOOP);
				}
				er.setDefaultParent(Boolean.FALSE);
				genericEntityRelationships.add(er);
			}
		}
		return genericEntityRelationships;
	}

	/**
	 * Determines if a particular entity relationship already exits.
	 *
	 * @param parentEntity The parent entity.
	 * @param entityRelationships The list of relationships for a product entity.
	 * @return The relationship that ties the two together and null if it doesn't exist.
	 */
	private GenericEntityRelationship getExistingRelationship(GenericEntity parentEntity,
										 List<GenericEntityRelationship> entityRelationships) {

		for (GenericEntityRelationship er : entityRelationships) {
			if (er.getKey().getParentEntityId().equals(parentEntity.getId())) {
				return er;
			}
		}
		return null;
	}

	/**
	 * Returns the entity relationship that ties a product to its parent.
	 *
	 * @param productEntity The entity for the product.
	 * @param parentEntity The entity for the parent.
	 * @param showOnHebCom Should this be shown on HEB.com?
	 * @return The entity relationship that ties a product to its parent.
	 */
	private GenericEntityRelationship getProductToHierarchyTie(GenericEntity productEntity,
															   GenericEntity parentEntity,
															   List<GenericEntityRelationship> existingRelationships,
															   boolean showOnHebCom) {

		// If the product entity ID is null, then the entity doesn't exist yet, and that's easy.
		if (productEntity.getId() == null) {
			// Since the entity is new, this has to be its default parent and it's sequence 1.
			return this.genericEntityRelationshipFrom(productEntity, parentEntity, showOnHebCom, 1L);
		}

		GenericEntityRelationship existingRelationship = this.getExistingRelationship(parentEntity, existingRelationships);
		if (existingRelationship != null) {
			existingRelationship.setAction(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
			existingRelationship.setExpirationDate(LocalDate.of(9999, 12, 31));
			existingRelationship.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
			existingRelationship.setActive(showOnHebCom);
			existingRelationship.setDisplay(showOnHebCom);
			existingRelationship.setDefaultParent(Boolean.TRUE);
			return existingRelationship;
		}

		// The relationship does not exist, so make a new one.
		return this.genericEntityRelationshipFrom(productEntity, parentEntity,
				showOnHebCom, existingRelationships.size() + 1);
	}

	/**
	 * Constructs a new GenericEntityRelationship tying a product to its parent node.
	 *
	 * @param productEntity The entity for the product.
	 * @param parentEntity The entity for the parent node.
	 * @param showOnHebCom Should this be shown on HEB.com?
	 * @param sequence What is the sequence number of this product in the parent's list of products?
	 * @return A new GenericEntityRelationship tying a product to its parent node.
	 */
	private GenericEntityRelationship genericEntityRelationshipFrom(GenericEntity productEntity,
																  GenericEntity parentEntity,
																  boolean showOnHebCom, long sequence) {

		GenericEntityRelationship productToHierarchyTie = new GenericEntityRelationship();
		productToHierarchyTie.setKey(new GenericEntityRelationshipKey());

		productToHierarchyTie.setAction(CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
		productToHierarchyTie.getKey().setHierarchyContext(this.hierarchyContext);
		productToHierarchyTie.getKey().setParentEntityId(parentEntity.getId());
		productToHierarchyTie.getKey().setChildEntityId(productEntity.getId());
		productToHierarchyTie.setDisplay(showOnHebCom);
		productToHierarchyTie.setSequence(sequence);
		productToHierarchyTie.setEffectiveDate(LocalDate.now());
		productToHierarchyTie.setExpirationDate(LocalDate.of(9999, 12, 31));
		productToHierarchyTie.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);
		productToHierarchyTie.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
		productToHierarchyTie.setActive(showOnHebCom);
		productToHierarchyTie.setDefaultParent(Boolean.TRUE);
		productToHierarchyTie.setGenericChildEntity(productEntity);

		return productToHierarchyTie;
	}

	/**
	 * Grabs the entity for a product. If it does not exist, will create one. The ID of a new one will be null.
	 *
	 * @param productId The product's ID.
	 * @return The product's entity.
	 */
	private GenericEntity getProductEntity(long productId) throws FileProcessingException {
		List<GenericEntity> genericEntities =
				this.genericEntityRepository.findByDisplayNumberAndType(productId, GenericEntity.EntyType.PROD.getName());

		// There's more than one entity tied to this product, so throw an eror.
		if (genericEntities.size() > 1) {
			throw new FileProcessingException(String.format("Product %d has more than one entity", productId));
		}

		// The product entity does not exist, so create a new one
		if (genericEntities.isEmpty()) {
			GenericEntity genericEntity = new GenericEntity();
			genericEntity.setAction(GenericEntity.INSERT);
			genericEntity.setType(GenericEntity.EntyType.PROD.getName());
			genericEntity.setAbbreviation(GenericEntity.EntyType.PROD.getName());
			genericEntity.setDisplayNumber(productId);
			genericEntity.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);
			genericEntity.setLastUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);

			// Don't need to add descriptions as the DAO will handle it.
			return genericEntity;
		}

		// The entity already exists, so use that one.
		GenericEntity productEntity = genericEntities.get(0);

		productEntity.setAction(GenericEntity.NOOP);
		// If the entity doesn't have a description (probably an error, but recoverable), add it.
		if (productEntity.getDescriptions().isEmpty()) {
			productEntity.getDescriptions().add(this.entityDescriptionFrom());
		} else {
			for (GenericEntityDescription description : productEntity.getDescriptions()) {
				description.setAction(GenericEntityDescription.NOOP);
			}
		}

		this.entityManager.detach(productEntity);

		return productEntity;
	}

	/**
	 * Creates a new entity description for a product entity.
	 *
	 * @return A new entity description for a product entity.
	 */
	private GenericEntityDescription entityDescriptionFrom() {
		GenericEntityDescription genericEntityDescription = new GenericEntityDescription();
		genericEntityDescription.setKey(new GenericEntityDescriptionKey());
		genericEntityDescription.getKey().setHierarchyContext(this.hierarchyContext);
		genericEntityDescription.setAction(GenericEntityDescription.INSERT);
		genericEntityDescription.setShortDescription("PROD");
		return genericEntityDescription;
	}

}
