package com.heb.pm.batchUpload.earley.productAttributeValues;

import com.heb.pm.CoreTransactional;
import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.MasterDataExtensionAttribute;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;

/**
 * Keeps track in the candidate tables of attribute value records as they are added.
 *
 * @author d116773
 * @since 2.16.0
 */
public class CandidateWriter implements ItemWriter<WrappedMasterDataExtendedAttributes> {

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Value("#{jobParameters['userId']}")
	private String userId;

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	private long recordCount = 0;

	/**
	 * Called by Spring Batch to write out the candidate records.
	 *
	 * @param items The MasterDataExtendedAttributes to track in the candidate table.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends WrappedMasterDataExtendedAttributes> items) throws Exception {

		List<CandidateWorkRequest> candidateWorkRequests = new LinkedList<>();

		for (WrappedMasterDataExtendedAttributes wrappedMasterDataExtendedAttribute : items) {

			this.recordCount++;
			if (wrappedMasterDataExtendedAttribute.getEntity() == null || wrappedMasterDataExtendedAttribute.getEntity().isEmpty()) {
				String message = String.format("record=[%d],message=[%s]",
						recordCount, wrappedMasterDataExtendedAttribute.getMessage());
				candidateWorkRequests.add(this.earleyUploadUtils.candidateWorkRequestFrom(this.transactionId,
						message, wrappedMasterDataExtendedAttribute.getSuccess(), this.userId));
			}
			else {
				for (MasterDataExtensionAttribute masterDataExtensionAttribute : wrappedMasterDataExtendedAttribute.getEntity()) {
					String message = String.format("record=[%d],message=[%s],Product=[%d],Attribute=[%d],Attribute Value=[%s]",
							this.recordCount,
							wrappedMasterDataExtendedAttribute.getMessage(),
							masterDataExtensionAttribute.getKey().getId(),
							masterDataExtensionAttribute.getKey().getAttributeId(),
							masterDataExtensionAttribute.getAttributeCodeId()
					);
					candidateWorkRequests.add(this.earleyUploadUtils.candidateWorkRequestFrom(this.transactionId,
							message, wrappedMasterDataExtendedAttribute.getSuccess(), this.userId));
				}
			}
			this.candidateWorkRequestRepository.save(candidateWorkRequests);
		}
	}
}
