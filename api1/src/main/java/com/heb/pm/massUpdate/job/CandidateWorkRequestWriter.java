package com.heb.pm.massUpdate.job;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

/**
 * The writer for the mass update batch job.
 *
 * @author d116773
 * @since 2.12.0
 */
public class CandidateWorkRequestWriter implements ItemWriter<CandidateWorkRequest> {

	private static final Logger logger = LoggerFactory.getLogger(CandidateWorkRequestWriter.class);

	private  static final String EMPTY_LIST_MESSAGE = "List of candidate work requests empty";

	private static final String WRITE_MESSAGE = "Writing %d candidate work requests";
	private static final String PRODUCT_LOG_MESSAGE = "Writing product IDs [%s]";

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	/**
	 * Called by the Spring framework to write out the CandidateWorkRequests that go into the mass update.
	 *
	 * @param items The list of CandidateWorkRequests to write.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends CandidateWorkRequest> items) throws Exception {

		// Just log an empty list.
		if (items.isEmpty()) {
			CandidateWorkRequestWriter.logger.info(CandidateWorkRequestWriter.EMPTY_LIST_MESSAGE);
			return;
		}

		StringBuilder sb = new StringBuilder();
		Iterator<? extends CandidateWorkRequest> itr = items.iterator();
		do {
			sb.append(itr.next().getProductId());
			if (itr.hasNext()) {
				sb.append(",");
			}
		} while (itr.hasNext());

		CandidateWorkRequestWriter.logger.info(
					String.format(CandidateWorkRequestWriter.PRODUCT_LOG_MESSAGE, sb.toString()));

		this.candidateWorkRequestRepository.save(items);
	}
}
