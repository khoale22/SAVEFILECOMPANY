package com.heb.pm.batchUpload.earley.hierarchy;

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
 * Keeps track in the candidate tables of hierarchy nodes as they are added.
 *
 * @author d116773
 * @since 2.16.0
 */
public class CandidateWriter implements ItemWriter<WrappedGenericEntityRelationship> {

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
	 * Called by Spring Batch to write out candidate records for the hierarchy nodes.
	 *
	 * @param items The hierarchy nodes being written.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends WrappedGenericEntityRelationship> items) throws Exception {

		List<CandidateWorkRequest> candidateWorkRequests = new LinkedList<>();

		for (WrappedGenericEntityRelationship wrappedGenericEntityRelationship : items) {
			this.recordCount++;
			String message = String.format("record=[%d],message=[%s]",
						this.recordCount, wrappedGenericEntityRelationship.getMessage());

			candidateWorkRequests.add(this.earleyUploadUtils.candidateWorkRequestFrom(this.transactionId,
					message, wrappedGenericEntityRelationship.getSuccess(), this.userId));

			this.candidateWorkRequestRepository.save(candidateWorkRequests);
		}
	}
}
