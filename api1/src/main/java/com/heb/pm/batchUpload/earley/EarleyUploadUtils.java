package com.heb.pm.batchUpload.earley;

import com.heb.pm.entity.*;
import com.heb.pm.repository.AttributeCodeRepository;
import com.heb.pm.repository.AttributeRepository;
import com.heb.pm.repository.GenericEntityRepository;
import com.heb.util.controller.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Collets the functions and properties that are common across all of the Earley load jobs.
 *
 * @author d116773
 * @since 2.16.0
 */
@Service
public class EarleyUploadUtils {

	public static final String EARLEY_USER_ID = "EARLEY";

	@Value("${app.earley.sourceSystemId}")
	private int earleySourceSystemId;

	@Value("${app.earley.intentId}")
	private int earleyIntentId;

	@Value("${app.sourceSystemId}")
	private int applicationSourceSystem;

	@Autowired
	private AttributeCodeRepository attributeCodeRepository;

	@Autowired
	private AttributeRepository attributeRepository;

	@Autowired
	private GenericEntityRepository genericEntityRepository;

	/**
	 * Returns the source system ID for Earley.
	 *
	 * @return The source system ID for Earley.
	 */
	public long getEarleySourceSystemId() {
		return this.earleySourceSystemId;
	}

	/**
	 * Returns the external ID for Earley attribute/values. In the file, they are just numbers, and we need
	 * something more to identify them.
	 *
	 * @param earleyValueId The ID of the attribute/value in the Earley file.
	 * @return The formatted ID of the attribute/value for our system.
	 */
	public static String getEarlyValueExternalId(String earleyValueId) {
		return String.format("EMV%s", earleyValueId);
	}

	/**
	 * In some instances, we need a numeric ID for a hierarchy node. This returns a searchable number based
	 * on the ID from the Earley file.
	 *
	 * @param earleyHierarchyId The Earley hierarchy ID from the file.
	 * @return A numeric represenetation of the ID.
	 */
	public static Long getEarlyHierarchyIdAsLong(String earleyHierarchyId) {
		return Long.parseLong("290" + earleyHierarchyId.replaceFirst("EMH", ""));
	}

	/**
	 * Creates a CandidateWorkRequest to save off the status of a record the job is processing.
	 *
	 * @param transactionId The transaction ID to tie the CandidateWorkRequests to.
	 * @param message The message to save in the CandidateStatus.
	 * @param success Whether or not the record was successfully processed.
	 * @param userId The ID of the user who submitted this job.
	 * @return A CandidateWorkRequest based on the parameters passed.
	 */
	public CandidateWorkRequest candidateWorkRequestFrom(Long transactionId, String message, boolean success,
														 String userId) {

		CandidateWorkRequest candidateWorkRequest = new CandidateWorkRequest();
		candidateWorkRequest.setCreateDate(LocalDateTime.now());
		candidateWorkRequest.setUserId(userId);
		candidateWorkRequest.setLastUpdateDate(LocalDateTime.now());
		candidateWorkRequest.setStatus(
				success ? CandidateWorkRequest.REQUEST_STATUS_PASS : CandidateWorkRequest.REQUEST_STATUS_FAIL);
		candidateWorkRequest.setStatusChangeReason(CandidateWorkRequest.STAT_CHG_RSN_ID_WRKG);
		candidateWorkRequest.setIntent(this.earleyIntentId);
		candidateWorkRequest.setLastUpdateUserId(userId);
		candidateWorkRequest.setSourceSystem(this.applicationSourceSystem);
		candidateWorkRequest.setTrackingId(transactionId);

		CandidateStatus candidateStatus = new CandidateStatus();
		candidateStatus.setKey(new CandidateStatusKey());
		candidateStatus.getKey().setLastUpdateDate(LocalDateTime.now());
		candidateStatus.getKey().setStatus(CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD);
		candidateStatus.setUpdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
		candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
		candidateStatus.setCommentText(message.length() <= 100 ? message : message.substring(0, 100));

		candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
		candidateWorkRequest.setCandidateStatuses(new LinkedList<>());
		candidateWorkRequest.getCandidateStatuses().add(candidateStatus);

		return candidateWorkRequest;
	}

	/**
	 * Looks up and attribute code by an external ID. Will return null if not found.
	 *
	 * @param externalId The external ID to look for.
	 * @return An attribute code tied to that external ID.
	 * @throws FileProcessingException
	 */
	public AttributeCode getAttributeCode(String externalId) throws FileProcessingException {
		List<AttributeCode> attributeCodes = this.attributeCodeRepository.findByAttributeValueXtrnlId(externalId);
		if (attributeCodes.isEmpty()) {
			return null;
		}
		if (attributeCodes.size() > 1) {
			throw new FileProcessingException(String.format("More than one attribute value found for the code %s", externalId));
		}
		return attributeCodes.get(0);
	}

	/**
	 * Returns the attribute with a given Earley ID.
	 *
	 * @param externalId The Earley ID.
	 * @return The attribute with that Earley ID.
	 */
	public Attribute getAttribute(String externalId) throws FileProcessingException {

		List<Attribute> attributes =
				this.attributeRepository.findByExternalIdAndSourceSystemId(externalId, this.earleySourceSystemId);
		if (attributes.isEmpty()) {
			return null;
		}
		// To deal with any data issues, just return the first one in the list.
//		if (attributes.size() > 1) {
//			throw new FileProcessingException(String.format("There are more than one attributes with the ID %s", externalId));
//		}
		return attributes.get(0);
	}

	/**
	 * Returns a GenericEntity that has a given external ID.
	 *
	 * @param externalId The external ID to look for.
	 * @return The GenericEntity with that matching ID.
	 * @throws FileProcessingException
	 */
	public GenericEntity getEntityForExternalId(String externalId) throws FileProcessingException {

		if (externalId == null) {
			return null;
		}

		List<GenericEntity> genericEntities = this.genericEntityRepository.findByDisplayTextAndType(
				externalId, GenericEntity.EntyType.CUSTH.getName());
		if (genericEntities.isEmpty()) {
			return null;
		}
		if (genericEntities.size() > 1) {
			throw new FileProcessingException(String.format("More than one entity found for ID %s", externalId));
		}
		return genericEntities.get(0);
	}
}
