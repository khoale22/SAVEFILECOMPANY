package com.heb.pm.product;

import com.heb.pm.entity.*;
import com.heb.pm.repository.DynamicAttributeRepository;
import com.heb.pm.repository.ProductInfoRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds all business logic related to dynamic attributes.
 *
 * @author m314029
 * @since 2.18.4
 */
@Service
public class DynamicAttributeService {

	@Autowired
	private DynamicAttributeRepository repository;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	/**
	 * Returns dynamic attributes matching the given key id and key type.
	 *
	 * @param keyId Key id to look for.
	 * @param keyType Key type to look for.
	 * @return List of dynamic attributes matching the search.
	 */
	public List<DynamicAttribute> getByKeyIdAndKeyType(Long keyId, String keyType) {
		return this.repository.findByKeyKeyAndKeyKeyType(keyId, keyType);
	}

	/**
	 * Returns a list of dynamic attributes matching the casepacks associated with the given prod id.
	 *
	 * @param productMaster The product master to search on.
	 * @param itemType Item type to look for.
	 * @return List of dynamic attributes matching the search.
	 */
	public List<DynamicAttribute> findCasePackDynamicAttributesByProductIdAndItemType(ProductMaster productMaster, String itemType) {

		List<ProdItem> prodItems = null;
		if(productMaster == null || CollectionUtils.isEmpty(productMaster.getProdItems())) {
			return new ArrayList<>();
		} else {
			prodItems = productMaster.getProdItems();
		}
		List<Long> itemCodeIds = new ArrayList<>();

		// add items that match the given item type
		for(ProdItem prodItem : prodItems) {
			if(itemType.equalsIgnoreCase(prodItem.getKey().getItemType().trim())){
				itemCodeIds.add(prodItem.getKey().getItemCode());
			}
		}
		Set<DynamicAttribute> dynamicAttributes = new HashSet<>(
				this.repository.findAllByKeyKeyInAndKeyKeyType(itemCodeIds, itemType));
		return new ArrayList<>(dynamicAttributes);
	}

	/**
	 * Returns dynamic atributes matching the given key ids and key type.
	 *
	 * @param keyIds Key ids to look for.
	 * @param keyType Key type to look for.
	 * @return List of dynamic attributes matching the search.
	 */
	public List<DynamicAttribute> getByKeyIdsAndKeyType(List<Long> keyIds, String keyType) {
		return this.repository.findByKeyKeyInAndKeyKeyType(keyIds, keyType);
	}

	/**
	 * Returns the max sequence number
	 *
	 * @param attributeId Attribute Id to look for.
	 * @param keyId Key ids to look for.
	 * @param keyType Key type to look for.
	 * @return The max sequence number.
	 */
	public Integer findMaxSequenceNumberForByAttributeIdAndKeyIdAndKeyType(Long attributeId, Long keyId, String keyType, Long sourceSystem) {
		return this.repository.findMaxSequenceNumberForAttributeAndKey(attributeId.intValue(), keyId, keyType, sourceSystem.intValue());
	}
}
