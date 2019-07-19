package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.ItemClass;
import com.heb.pm.productHierarchy.ItemClassDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Processor for the ItemClass index batch job.
 *
 * @author m314029
 * @since 2.6.0
 */
public class ItemClassProcessor implements ItemProcessor<ItemClass, ItemClassDocument> {

	/**
	 * Converts a ItemClass to a ItemClassDocument.
	 *
	 * @param item The ItemClass to process.
	 * @return item converted to a ItemClassDocument.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public ItemClassDocument process(ItemClass item) throws Exception {
		return item == null ? null : new ItemClassDocument(item);
	}
}
