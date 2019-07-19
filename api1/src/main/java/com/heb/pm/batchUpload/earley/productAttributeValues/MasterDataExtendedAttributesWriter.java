package com.heb.pm.batchUpload.earley.productAttributeValues;

import com.heb.pm.entity.MasterDataExtensionAttribute;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;


/**
 * Writes product attribute ties to the DB.
 *
 * @author d116773
 * @since 2.16.0
 */
public class MasterDataExtendedAttributesWriter implements ItemWriter<WrappedMasterDataExtendedAttributes> {

	private static final Logger logger = LoggerFactory.getLogger(MasterDataExtendedAttributesWriter.class);

	@Autowired
	private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	/**
	 * Called by Spring Batch to write out the product attribute ties to the DB.
	 *
	 * @param items The list of product attribute ties to write out.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WrappedMasterDataExtendedAttributes> items) throws Exception {

		List<MasterDataExtensionAttribute> masterDataExtensionAttributes = new LinkedList<>();
		items.forEach((i) -> {
			if (i.hasEntity()) {
				i.getEntity().forEach((j) ->{masterDataExtensionAttributes.add(j);});
			}
		});
		try {
			this.productAttributeManagementServiceClient.updateMasterDataExtendedAttribute(masterDataExtensionAttributes);
		} catch (Exception e) {
			items.forEach((i) -> {if (i.hasEntity()) {i.fail(e.getMessage());}});
			return;
		}
		items.forEach((i) -> {if (i.hasEntity()) {i.succeed();}});
	}
}
