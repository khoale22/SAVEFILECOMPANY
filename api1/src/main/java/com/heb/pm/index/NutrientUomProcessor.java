package com.heb.pm.index;

import com.heb.pm.entity.NutrientUom;
import com.heb.pm.scaleManagement.NutrientUomDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * The type Nutrient uom Processor.
 *
 * @author m594201
 * @since 2.1.0
 */
public class NutrientUomProcessor implements ItemProcessor<NutrientUom, NutrientUomDocument> {

	/**
	 * Called by the Spring Batch framework. It will wrap a NutrientUom in a NutrientUomDocument and return it.
	 *
	 * @param nutrientUom The nutrientUom to wrap.
	 * @return The wrapped NutrientUom.
	 * @throws Exception
	 */
	@Override
	public NutrientUomDocument process(NutrientUom nutrientUom) throws Exception {
		return new NutrientUomDocument(nutrientUom);
	}
}
