package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubCommodity;
import com.heb.pm.index.DocumentWrapper;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Wraps a SubCommodity for storage in an index.
 *
 * @author d116773
 * @since 2.0.2
 */
@Document(indexName="sub-commodity", type="subCommodity")
public class SubCommodityDocument extends DocumentWrapper<SubCommodity, String> {

	/**
	 * Constructs a new SubCommodityDocument.
	 */
	public SubCommodityDocument() {}

	/**
	 * Constructs a new SubCommodityDocument.
	 *
	 * @param sc The SubCommodity for this document to wrap.
	 */
	public SubCommodityDocument(SubCommodity sc) {
		super(sc);
	}

	/**
	 * Returns a suitable key for the index for a given SubCommodity.
	 *
	 * @param data The data this object will store.
	 * @return A key for the SubCommodity.
	 */
	@Override
	protected String toKey(SubCommodity data) {
		return data == null ? "" :  Integer.toString(data.getNormalizedId());
	}
}
