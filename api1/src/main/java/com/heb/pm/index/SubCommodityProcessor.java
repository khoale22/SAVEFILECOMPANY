package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.SubCommodity;
import com.heb.pm.productHierarchy.SubCommodityDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Processor for the SubCommodity index batch job.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubCommodityProcessor implements ItemProcessor<SubCommodity, SubCommodityDocument> {

	/**
	 * Called by the Spring Batch framework. It will wrap a SubCommodity in a SubCommodityDocument and return it.
	 *
	 * @param sc The SubCommodity to wrap.
	 * @return The wrapped SubCommodity.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public SubCommodityDocument process(SubCommodity sc) throws Exception {
		return new SubCommodityDocument(sc);
	}
}
