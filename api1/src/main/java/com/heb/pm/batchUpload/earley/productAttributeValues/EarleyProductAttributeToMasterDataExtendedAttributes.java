package com.heb.pm.batchUpload.earley.productAttributeValues;

import com.heb.pm.batchUpload.earley.EarleyUploadUtils;
import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.pm.entity.*;
import com.heb.pm.repository.GenericEntityRepository;
import com.heb.pm.repository.MasterDataExtensionAttributeRepository;
import com.heb.pm.repository.ProductMasterRepository;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.xmlns.ei.masterdataextnattribute.MasterDataExtnAttribute;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Converts Earley product/attribte/value recrods to MasterDataExtendedAttributes.
 *
 * @author d116773
 * @since 2.16.0
 */
public class EarleyProductAttributeToMasterDataExtendedAttributes implements ItemProcessor<List<String>, WrappedMasterDataExtendedAttributes> {

	private static final String VALUES_DELEMITER = "\\|";

	private EarleyProdudctAttributeValuesParser earleyProdudctAttributeValuesParser =
			new EarleyProdudctAttributeValuesParser();
	@Autowired
	private MasterDataExtensionAttributeRepository masterDataExtensionAttributeRepository;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	private EarleyUploadUtils earleyUploadUtils;

	/**
	 * Called by Spring Batch to convert Earley product/attribte/value records to MasterDataExtendedAttributes.
	 *
	 * @param item The product/attribute/value record.
	 * @return The MasterDataExtendedAttribute to write.
	 * @throws Exception
	 */
	@Override
	public WrappedMasterDataExtendedAttributes process(List<String> item) throws Exception {

		// First, get the entity that is tied to the product
		try {
			long productId = Long.parseLong(this.earleyProdudctAttributeValuesParser.parseProductId(item));

			ProductMaster productMaster = this.productMasterRepository.findOne(productId);
			if (productMaster == null) {
				return new WrappedMasterDataExtendedAttributes(String.format("product %d not found", productId));
			}

			String externalAttributeId =
					this.earleyProdudctAttributeValuesParser.parseAttributeId(item);
			Attribute attribute = this.earleyUploadUtils.getAttribute(externalAttributeId);
			if (attribute == null) {
				return new WrappedMasterDataExtendedAttributes(String.format("Unknown attribute: %s", externalAttributeId));
			}

			List<MasterDataExtensionAttribute> masterDataExtensionAttributes = new LinkedList<>();

			String externalValueIds = this.earleyProdudctAttributeValuesParser.parseAttributeValueId(item);
			String[] parsedAttributeValues = externalValueIds.split(VALUES_DELEMITER);

			for (int i = 0; i < parsedAttributeValues.length; i++) {

				String externalAttributeValueId = parsedAttributeValues[i].trim();
				AttributeCode attributeCode =
						this.earleyUploadUtils.getAttributeCode(EarleyUploadUtils.getEarlyValueExternalId(externalAttributeValueId));
				if (attributeCode == null) {
					continue;
				}
				MasterDataExtensionAttribute masterDataExtensionAttribute = new MasterDataExtensionAttribute();
				masterDataExtensionAttribute.setKey(new MasterDataExtensionAttributeKey());

				masterDataExtensionAttribute.getKey().setAttributeId(attribute.getAttributeId());
				masterDataExtensionAttribute.getKey().setId(productMaster.getProdId());
				masterDataExtensionAttribute.getKey().setItemProdIdCode("PROD");
				masterDataExtensionAttribute.getKey().setDataSourceSystem(this.earleyUploadUtils.getEarleySourceSystemId());
				masterDataExtensionAttribute.getKey().setSequenceNumber(0L);

				// See if this code value already exists.
				List<MasterDataExtensionAttribute> existing =
				this.masterDataExtensionAttributeRepository.findExistingAttributeValue(attribute.getAttributeId(),
						productMaster.getProdId(), "PROD", this.earleyUploadUtils.getEarleySourceSystemId(),
						attributeCode.getAttributeCodeId());
				if (existing != null && !existing.isEmpty()) {
					continue;
				}
				masterDataExtensionAttribute.setAttributeCodeId(attributeCode.getAttributeCodeId());
				masterDataExtensionAttribute.setLstUpdtTs(LocalDate.now());
				masterDataExtensionAttribute.setLastUdateUserId(EarleyUploadUtils.EARLEY_USER_ID);
				masterDataExtensionAttribute.setCreateTime(LocalDateTime.now());
				masterDataExtensionAttribute.setCreateUserId(EarleyUploadUtils.EARLEY_USER_ID);
				masterDataExtensionAttribute.setAction(MasterDataExtensionAttribute.INSERT);
				masterDataExtensionAttributes.add(masterDataExtensionAttribute);
			}
			if (masterDataExtensionAttributes.isEmpty()) {
				return new WrappedMasterDataExtendedAttributes(
							String.format("Attribute values for attribute %s already set for product %d",
									externalAttributeId, productId));
			}
			return new WrappedMasterDataExtendedAttributes(masterDataExtensionAttributes);

		} catch (FileProcessingException | NumberFormatException e) {
			return new WrappedMasterDataExtendedAttributes(e.getLocalizedMessage());
		}
	}
}
