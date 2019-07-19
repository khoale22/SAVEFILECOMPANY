package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.productHierarchy.SubCommodityDocument;
import com.heb.pm.repository.SubCommodityIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Wtier for the SubCommodity index batch job.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityWriter implements ItemWriter<SubCommodityDocument>{

	private static final Logger logger = LoggerFactory.getLogger(SubCommodityWriter.class);

	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called write with null or empty list.";
	private static final String LOG_MESSAGE = "Writing %s sub-commodities starting with %s.";

	@Autowired
	private SubCommodityIndexRepository indexRepository;

	/**
	 * Called by the Spring batch framework to write the sub-commodities to the inedex.
	 *
	 * @param subCommodityDocuments The list of sub-commodities to write.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends SubCommodityDocument> subCommodityDocuments) throws Exception {

		if (subCommodityDocuments == null || subCommodityDocuments.isEmpty()) {
			SubCommodityWriter.logger.info(SubCommodityWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		SubCommodityWriter.logger.debug(String.format(SubCommodityWriter.LOG_MESSAGE, subCommodityDocuments.size(),
				subCommodityDocuments.get(0).getKey()));

		this.indexRepository.save(subCommodityDocuments);
	}

	/**
	 * Sets the SubCommodityIndexRepository for this object to use. This is mainly for testing.
	 *
	 * @param indexRepository The SubCommodityIndexRepository for this object to use.
	 */
	public void setIndexRepository(SubCommodityIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
