package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ItemClass;
import com.heb.pm.index.DocumentWrapper;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Wraps an ItemClass object for storage in an index.
 *
 * @author m314029
 * @since 2.6.0
 */
@Document(indexName="item-classes", type="item-class")
public class ItemClassDocument extends DocumentWrapper<ItemClass, String> {

	/**
	 * Constructs a new ItemClassDocument.
	 */
	public ItemClassDocument() {}

	/**
	 * Constructs a new ItemClassDocument.
	 *
	 * @param s The ItemClass to wrap.
	 */
	public ItemClassDocument(ItemClass s) { super(s); }

	/**
	 * Returns a key for the document.
	 *
	 * @param data The data this object will store.
	 * @return A key for the document based on its data.
	 */
	@Override
	protected String toKey(ItemClass data) {
		return data == null || data.getNormalizedId() == null ? "" : Integer.toString(data.getNormalizedId());
	}
}
