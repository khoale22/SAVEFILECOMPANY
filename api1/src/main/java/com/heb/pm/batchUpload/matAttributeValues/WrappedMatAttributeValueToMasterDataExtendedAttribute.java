package com.heb.pm.batchUpload.matAttributeValues;

import com.heb.pm.batchUpload.earley.WrappedEarlyEntity;
import com.heb.pm.entity.MasterDataExtensionAttribute;
/**
 * Wraps a MasterDataExtensionAttribute.
 *
 * @author s573181
 * @since 2.29.0
 */
public class WrappedMatAttributeValueToMasterDataExtendedAttribute extends WrappedEarlyEntity<MasterDataExtensionAttribute> {
	/**
	 * Constructs a new WrappedMatAttributeValueToMasterDataExtendedAttribute.
	 *
	 * @param entity The entity to wrap.
	 */
	public WrappedMatAttributeValueToMasterDataExtendedAttribute(MasterDataExtensionAttribute entity) {
		super(entity);
	}

	/**
	 * Constructs a new WrappedMatAttributeValueToMasterDataExtendedAttribute.
	 *
	 * @param message The message about the Record.
	 */
	public WrappedMatAttributeValueToMasterDataExtendedAttribute(String message) {
		super(message);
	}
}
