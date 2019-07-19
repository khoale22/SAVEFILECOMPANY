package com.heb.pm.scaleManagement;

import com.heb.pm.entity.NutrientUom;
import com.heb.pm.index.DocumentWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * The type Nutrient uom document.
 *
 * @author m594201
 * @since 2.1.0
 */
@Document(indexName = "nutrient-uoms", type = "nutrient-uom")
public class NutrientUomDocument extends DocumentWrapper<NutrientUom, String>{

	/**
	 * Instantiates a new Nutrient uom document.
	 */
	public NutrientUomDocument(){}

	/**
	 * Instantiates a new Nutrient uom document.
	 *
	 * @param n the n
	 */
	public NutrientUomDocument(NutrientUom n) {super(n);}

	/**
	 * Returns a key for the object in the index.
	 *
	 * @param data The data this object will store.
	 * @return A key for the object in the index.
	 */
	@Override
	protected String toKey(NutrientUom data) { return data == null ? "" : data.getNormalizedUomId();}

	/**
	 * Returns the default sort for this object. In this case, it is ascending by the key.
	 *
	 * @return The default sort for this object.
	 */
	public static Sort getDefaultSort() {
		return new Sort(new Sort.Order(Sort.Direction.ASC, "key"));
	}
}
