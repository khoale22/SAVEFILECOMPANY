package com.heb.pm.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Clears out the requesting index.
 *
 * @author m314029
 * @since 2.12.0
 */
public class IndexEmptyTasklet implements Tasklet {

	public static final Logger logger = LoggerFactory.getLogger(IndexEmptyTasklet.class);

	private static final String INDEX_CLEAR_BEGIN_LOG_MESSAGE = "The index is being deleted.";
	private static final String INDEX_CLEAR_END_LOG_MESSAGE = "The index deletion is complete.";

	private ElasticsearchRepository indexRepository;

	/**
	 * Called by SpringBatch. This will call the repository used by a given index to delete existing records.
	 *
	 * @param contribution Ignored.
	 * @param chunkContext Ignored.
	 * @return The status of the task.
	 * @throws Exception Exception that will be thrown if exception is encountered.
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info(INDEX_CLEAR_BEGIN_LOG_MESSAGE);
		this.indexRepository.deleteAll();
		logger.info(INDEX_CLEAR_END_LOG_MESSAGE);
		return RepeatStatus.FINISHED;
	}

	public void setIndexRepository(ElasticsearchRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
