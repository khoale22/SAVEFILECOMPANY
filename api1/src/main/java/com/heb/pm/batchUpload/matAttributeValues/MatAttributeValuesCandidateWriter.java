package com.heb.pm.batchUpload.matAttributeValues;

import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.MasterDataExtensionAttribute;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;
/**
 * Writes WrappedMatAttributeValueToMasterDataExtendedAttribute to the dynamic attribute table.
 *
 * @author s573181
 * @since 2.29.0
 */
public class MatAttributeValuesCandidateWriter implements ItemWriter<WrappedMatAttributeValueToMasterDataExtendedAttribute> {


	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Value("#{jobParameters['userId']}")
	private String userId;

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	private long recordCount = 1;

	/**
	 * Called by Spring Batch to write out the candidate records.
	 *
	 * @param items The MasterDataExtendedAttributes to track in the candidate table.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WrappedMatAttributeValueToMasterDataExtendedAttribute> items) throws Exception {

		List<CandidateWorkRequest> candidateWorkRequests = new LinkedList<>();

		for (WrappedMatAttributeValueToMasterDataExtendedAttribute wrappedDynamicAttribute : items) {

			this.recordCount++;
			if (wrappedDynamicAttribute.getEntity() == null) {
				String message = String.format("row=[%d],message=[%s]",
						recordCount, wrappedDynamicAttribute.getMessage());
				candidateWorkRequests.add(this.earleyUploadUtils.candidateWorkRequestFrom(this.transactionId,
						message, false, this.userId));
			} else {
				String message = String.format("row=[%d], message=[Success], Type=[%s], ID=[%d], Attribute=[%d], Attribute Value=[%s]",
						this.recordCount,
						wrappedDynamicAttribute.getEntity().getKey().getItemProdIdCode(),
						wrappedDynamicAttribute.getEntity().getKey().getId(),
						wrappedDynamicAttribute.getEntity().getKey().getAttributeId(),
						this.getAttributeValue(wrappedDynamicAttribute.getEntity())
				);
				candidateWorkRequests.add(this.earleyUploadUtils.candidateWorkRequestFrom(this.transactionId,
						message, true, this.userId));
			}
			this.candidateWorkRequestRepository.save(candidateWorkRequests);
		}
	}

	/**
	 * Returns the set attribute value.
	 *
	 * @param masterDataExtensionAttribute the masterDataExtensionAttribute to get the value from.
	 * @return the set attribute value.
	 */
	private String getAttributeValue(MasterDataExtensionAttribute masterDataExtensionAttribute) {

		if(masterDataExtensionAttribute.getAttributeValueText() != null) {
			return masterDataExtensionAttribute.getAttributeValueText();
		} else if(masterDataExtensionAttribute.getAttributeValueNumber() != null) {
			return masterDataExtensionAttribute.getAttributeValueNumber().toString();
		} else if(masterDataExtensionAttribute.getAttributeValueTime() != null) {
			return masterDataExtensionAttribute.getAttributeValueTime().toString();
		} else if(masterDataExtensionAttribute.getAttributeValueDate() != null) {
			return masterDataExtensionAttribute.getAttributeValueDate().toString();
		} else if(masterDataExtensionAttribute.getAttributeCodeId() != null) {
			return masterDataExtensionAttribute.getAttributeCodeId().toString();
		} else {
			return StringUtils.EMPTY;
		}
	}
}
