package com.heb.pm.batchUpload.earley.attributeValues;

import com.heb.pm.CoreTransactional;
import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.entity.CandidateWorkRequest;
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
public class CandidateWriter implements ItemWriter<WrappedEntityAttributeCode> {

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
	 * Called by Spring Batch to write out candidate records for the attribute values.
	 *
	 * @param items The attribute values being written.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends WrappedEntityAttributeCode> items) throws Exception {

		List<CandidateWorkRequest> candidateWorkRequests = new LinkedList<>();

		for (WrappedEntityAttributeCode wrappedEntityAttributeCode : items) {
			this.recordCount++;
			String message;
			if (wrappedEntityAttributeCode.hasEntity()) {
				message = String.format("record=[%d],message=[%s],Attribute=[%s],Attribute Value=[%s]",
						this.recordCount,
						wrappedEntityAttributeCode.getMessage(),
						wrappedEntityAttributeCode.getEntity().getKey().getAttributeId(),
						wrappedEntityAttributeCode.getEntity().getAttributeCode().getAttributeValueText()
				);
			} else {
				message = String.format("record=[%d],message=[%s]", this.recordCount, wrappedEntityAttributeCode.getMessage());
			}

			candidateWorkRequests.add(this.earleyUploadUtils.candidateWorkRequestFrom(this.transactionId,
					message, wrappedEntityAttributeCode.getSuccess(), this.userId));

			this.candidateWorkRequestRepository.save(candidateWorkRequests);
		}
	}
}
