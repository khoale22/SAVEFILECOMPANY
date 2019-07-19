package com.heb.pm.batchUpload.earley.productAttributeValues;

import com.heb.pm.batchUpload.earley.WrappedEarlyEntity;
import com.heb.pm.entity.MasterDataExtensionAttribute;

import java.util.List;

/**
 * Wraps a MasterDataExtensionAttribute.
 *
 * @author d116773
 * @since 2.16.0
 */
public class WrappedMasterDataExtendedAttributes extends WrappedEarlyEntity<List<MasterDataExtensionAttribute>> {

	/**
	 * Constructs a new WrappedMasterDataExtendedAttributes.
	 *
	 * @param entity The entity to wrap.
	 */
	public WrappedMasterDataExtendedAttributes(List<MasterDataExtensionAttribute> entity) {
		super(entity);
	}

	/**
	 * Constructs a new WrappedMasterDataExtendedAttributes.
	 *
	 * @param message The message about the Record.
	 */
	public WrappedMasterDataExtendedAttributes(String message) {
		super(message);
	}
}
