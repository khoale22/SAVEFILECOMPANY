package com.heb.pm.batchUpload.matAttributeValues;

import com.heb.pm.entity.MasterDataExtensionAttribute;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Writes MasterDataExtendedAttributes.
 *
 * @author s573181
 * @since 2.29.0
 */
public class MatAttributeValuesToMasterDataExtendedAttributesWriter implements ItemWriter<WrappedMatAttributeValueToMasterDataExtendedAttribute> {

	private static final Logger logger = LoggerFactory.getLogger(MatAttributeValuesToMasterDataExtendedAttributesWriter.class);


	@Autowired
	private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	@Value("#{jobParameters['userId']}")
	private String userId;
	/**
	 * Called by Spring Batch to write out the product attribute ties to the DB.
	 *
	 * @param items The list of product attribute ties to write out.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WrappedMatAttributeValueToMasterDataExtendedAttribute> items) throws Exception {
		List<MasterDataExtensionAttribute> masterDataExtensionAttributes = new LinkedList<>();
		items.forEach((i) -> {
			if (i.hasEntity()) {
				i.getEntity().setCreateTime(LocalDateTime.now());
				i.getEntity().setCreateUserId(userId);
				i.getEntity().setLstUpdtTs(LocalDate.now());
				i.getEntity().setLastUdateUserId(userId);
				masterDataExtensionAttributes.add(i.getEntity());
			}
		});

		try {
			this.productAttributeManagementServiceClient.updateMasterDataExtendedAttribute(masterDataExtensionAttributes);
		} catch (CheckedSoapException e) {
			logger.debug(e.getMessage());
			items.forEach((i) -> {
				i.fail(e.getMessage());
			});
		}

	}
}
