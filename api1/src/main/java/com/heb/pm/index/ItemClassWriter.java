package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.productHierarchy.ItemClassDocument;
import com.heb.pm.repository.ItemClassIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for the ItemClass index batch job.
 *
 * @author m314029
 * @since 2.6.0
 */
public class ItemClassWriter implements ItemWriter<ItemClassDocument> {

	public static final Logger logger = LoggerFactory.getLogger(ItemClassWriter.class);

	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called write with null or empty list.";
	private static final String LOG_MESSAGE = "Writing %s item-classes starting with %s.";

	@Autowired
	private ItemClassIndexRepository indexRepository;

	/**
	 * Writes ItemClassDocuments to the index.
	 *
	 * @param items The items to write.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends ItemClassDocument> items) throws Exception {
		if (items == null || items.isEmpty()) {

			ItemClassWriter.logger.debug(ItemClassWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		if (ItemClassWriter.logger.isDebugEnabled()) {
			ItemClassWriter.logger.debug(String.format(ItemClassWriter.LOG_MESSAGE,
					items.size(), items.get(0)));
		}

		this.indexRepository.save(items);
	}

	/**
	 * Sets the ItemClassIndexRepository for this object. This is mainly for testing.
	 *
	 * @param indexRepository The ItemClassIndexRepository for this object.
	 */
	public void setIndexRepository(ItemClassIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
