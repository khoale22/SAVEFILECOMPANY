package com.heb.pm.batchUpload.earley.attribute;

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
 * Keeps track in the candidate tables of attribute records as they are added.
 *
 * @author d116773
 * @since 2.16.0
 */
public class CandidateWriter implements ItemWriter<WrappedEntityAttribute> {

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
	 * Called by Spring Batch to save the candidate table records to the DB.
	 *
	 * @param items The list of WrappedEntityAttributes to write.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends WrappedEntityAttribute> items) throws Exception {

		List<CandidateWorkRequest> candidateWorkRequests = new LinkedList<>();

		for (WrappedEntityAttribute wrappedEntityAttribute : items) {
			this.recordCount++;
			String message;
			if (wrappedEntityAttribute.getEntity() != null) {
				message = String.format("record=[%d],message=[%s],Attribute=[%s]",
						this.recordCount,
						wrappedEntityAttribute.getMessage(),
						wrappedEntityAttribute.getEntity().getKey().getAttributeId());
			} else {
				message = String.format("record=[%d],message=[%s]",
						this.recordCount,
						wrappedEntityAttribute.getMessage());
			}

			candidateWorkRequests.add(this.earleyUploadUtils.candidateWorkRequestFrom(this.transactionId,
					message, wrappedEntityAttribute.getSuccess(), this.userId));

			this.candidateWorkRequestRepository.save(candidateWorkRequests);
		}
	}
}
