package com.heb.pm.productSearch;

import com.heb.pm.entity.*;
import com.heb.util.controller.IntegerListFromStringFormatter;
import com.heb.util.controller.LongListFromStringFormatter;
import com.heb.util.controller.StringListFromStringFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class allows the front end to pass parameters in a fairly raw format from text boxes, etc. This will handle
 * parsing those to convert the data to more formal objects (lists of Integers, etc).
 *
 * @author d116773
 * @since 2.13.0
 */
public class RawSearchCriteria {

	private String productIds;
	private String upcs;
	private String itemCodes;
	private String caseUpcs;
	private String productGroupIds;

	private String description;
	private boolean searchByProductDescription;
	private boolean searchByCustomerFriendlyDescription;
	private boolean searchByDisplayName;
	private boolean searchAllExtendedAttributes;
	private String entityType;

	private SubDepartment subDepartment;

	private String classCodes;
	private ItemClass itemClass;

	private String commodityCodes;
	private ClassCommodity classCommodity;

	private String subCommodityCodes;
	private SubCommodity subCommodity;

	private String bdms;
	private Bdm bdm;

	private String vendorNumbers;
	private Vendor vendor;

	private Long trackingId;

	private GenericEntityRelationshipKey lowestCustomerHierarchyNode;

	private List<CustomSearchEntry> customSearchEntries;

	private List<Long> excludedProducts;

	private boolean useSession = true;
	private boolean firstSearch;
	private int page;
	private int pageSize;

	private LongListFromStringFormatter longListFromStringFormatter = new LongListFromStringFormatter();
	private IntegerListFromStringFormatter integerListFromStringFormatter = new IntegerListFromStringFormatter();
	private StringListFromStringFormatter stringListFromStringFormatter = new StringListFromStringFormatter();

	/**
	 * Returns the list of custom search entries for BYOS.
	 *
	 * @return The list of custom search entries for BYOS.
	 */
	public List<CustomSearchEntry> getCustomSearchEntries() {
		return customSearchEntries;
	}

	/**
	 * Sets the list of custom search entries for BYOS.
	 *
	 * @param customSearchEntries The list of custom search entries for BYOS.
	 */
	public void setCustomSearchEntries(List<CustomSearchEntry> customSearchEntries) {
		this.customSearchEntries = customSearchEntries;
	}

	/**
	 * Returns the list of product IDs the user wants to search by.
	 *
	 * @return The list of product IDs the user wants to search by.
	 */
	public String getProductIds() {
		return productIds;
	}

	/**
	 * Sets the list of product IDs the user wants to search by.
	 *
	 * @param productIds The list of product IDs the user wants to search by.
	 */
	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	/**
	 * Returns the list of UPCs the user wants to search by.
	 *
	 * @return The list of UPCs the user wants to search by.
	 */
	public String getUpcs() {
		return upcs;
	}

	/**
	 * Sets the list of UPCs the user wants to search by.
	 *
	 * @param upcs The list of UPCs the user wants to search by.
	 */
	public void setUpcs(String upcs) {
		this.upcs = upcs;
	}

	/**
	 * Returns the list of item codes the user wants to search by.
	 *
	 * @return The list of item codes the user wants to search by.
	 */
	public String getItemCodes() {
		return itemCodes;
	}

	/**
	 * Sets the list of item codes the user wants to search by.
	 *
	 * @param itemCodes The list of item codes the user wants to search by.
	 */
	public void setItemCodes(String itemCodes) {
		this.itemCodes = itemCodes;
	}

	/**
	 * Returns the list of case UPCs the user wants to search by.
	 *
	 * @return The list of case UPCs the user wants to search by.
	 */
	public String getCaseUpcs() {
		return caseUpcs;
	}

	/**
	 * Sets the list of case UPCs the user wants to search by.
	 *
	 * @param caseUpcs The list of case UPCs the user wants to search by.
	 */
	public void setCaseUpcs(String caseUpcs) {
		this.caseUpcs = caseUpcs;
	}

	/**
	 * Sets whether or not this is the first search done with this criteria.
	 *
	 * @param firstSearch True if this is the first time to run this search and false otherwise.
	 */
	public void setFirstSearch(boolean firstSearch) {
		this.firstSearch = firstSearch;
	}


	/**
	 * setter for useSession
	 * @param useSession
	 */
	public void setUseSession(boolean useSession) {
		this.useSession = useSession;
	}

	/**
	 * Whether to use the session or not to save info
	 * @return
	 */
	public boolean isUseSession() {
		return useSession;
	}

	/**
	 * Returns whether or not this is the first search done with this criteria. This will make the code return
	 * counts as well as clear out any data related to the user's session in the temp table.
	 *
	 * @return True if this is the first time to run this search and false otherwise.
	 */
	public boolean isFirstSearch() {
		return firstSearch;
	}

	/**
	 * Returns the page of data the user is requesting. The first page is 0.
	 *
	 * @return The page of data the user is requesting.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Sets the page of data the user is requesting.
	 *
	 * @param page The page of data the user is requesting.
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Returns the number of records the user is requesting.
	 *
	 * @return The number of records the user is requesting.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the number of records the user is requesting.
	 *
	 * @param pageSize The number of records the user is requesting.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Returns the SubDepartment the user wants to search by.
	 *
	 * @return The SubDepartment the user wants to search by.
	 */
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	/**
	 * Sets the SubDepartment the user wants to search by.
	 *
	 * @param subDepartment The SubDepartment the user wants to search by.
	 */
	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * Returns the product description the user wants to search by.
	 *
	 * @return The product description the user wants to search by.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the product description the user wants to search by.
	 *
	 * @param description The product description the user wants to search by.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns whether or not the user wants to include product description (product_master) in their search by
	 * description.
	 *
	 * @return True if they want to include product description and false otherwise.
	 */
	public boolean getSearchByProductDescription() {
		return searchByProductDescription;
	}

	/**
	 * Sets whether or not the user wants to include product description (product_master) in their search by
	 * description.
	 *
	 * @param searchByProductDescription True if they want to include product description and false otherwise.
	 */
	public void setSearchByProductDescription(boolean searchByProductDescription) {
		this.searchByProductDescription = searchByProductDescription;
	}

	/**
	 * Returns whether the search should include searching by customer friendly
	 * description (prod_desc_txt for TAG1 and TAG2).
	 *
	 * @return True if the search should include searching by customer friendly  description and false otherwise.
	 */
	public boolean getSearchByCustomerFriendlyDescription() {
		return searchByCustomerFriendlyDescription;
	}

	/**
	 * Sets whether the search should include searching by customer friendly description.
	 *
	 * @param searchByCustomerFriendlyDescription True if the search should include searching by customer friendly
	 *                                            description and false otherwise.
	 */
	public void setSearchByCustomerFriendlyDescription(boolean searchByCustomerFriendlyDescription) {
		this.searchByCustomerFriendlyDescription = searchByCustomerFriendlyDescription;
	}

	/**
	 * Returns whether the search should include searching by display name
	 * (mst_dta_extn_attr but display name only).
	 *
	 * @return True if the search should include searching by display name  and false otherwise.
	 */
	public boolean getSearchByDisplayName() {
		return searchByDisplayName;
	}

	/**
	 * Sets whether the search should include searching by display name.
	 *
	 * @param searchByDisplayName True if the search should include searching by display name and false otherwise.
	 */
	public void setSearchByDisplayName(boolean searchByDisplayName) {
		this.searchByDisplayName = searchByDisplayName;
	}

	/**
	 * Returns whether the search should include searching by all extended attributes
	 * (mst_dta_extn_attr text attributes only).
	 *
	 * @return True if the search should include searching by dall extended attributes and false otherwise.
	 */
	public boolean getSearchAllExtendedAttributes() {
		return searchAllExtendedAttributes;
	}

	/**
	 * Sets whether the search should include searching by all extended attributes.
	 *
	 * @param searchAllExtendedAttributes True if the search should include searching by all extended attributes
	 *                                       and false otherwise.
	 */
	public void setSearchAllExtendedAttributes(boolean searchAllExtendedAttributes) {
		this.searchAllExtendedAttributes = searchAllExtendedAttributes;
	}

	// Many of the remaining have a list of codes and a matching object. This is to support various ways of searching
	// on the front-end. For example, they may chose an item class from a drop-down (in which case the front end has
	// an object), or they may paste a list of item classes (in which case the front end has a list of integers). To
	// make the logic a bit simpler on the front-end, this class contains both.

	/**
	 * Returns the list of item class codes the user wants to search by.
	 *
	 * @return The list of item class codes the user wants to search by.
	 */
	public String getClassCodes() {
		return classCodes;
	}

	/**
	 * Sets the list of item class codes the user wants to search by.
	 *
	 * @param classCodes The list of item class codes the user wants to search by.
	 */
	public void setClassCodes(String classCodes) {
		this.classCodes = classCodes;
	}

	/**
	 * Returns the ItemClass the user wants to search by.
	 *
	 * @return The ItemClass the user wants to search by.
	 */
	public ItemClass getItemClass() {
		return itemClass;
	}

	/**
	 * Sets the ItemClass the user wants to search by.
	 *
	 * @param itemClass The ItemClass the user wants to search by.
	 */
	public void setItemClass(ItemClass itemClass) {
		this.itemClass = itemClass;
	}

	/**
	 * Returns the list of commodities the user wants to search by.
	 *
	 * @return The list of commodities the user wants to search by.
	 */
	public String getCommodityCodes() {
		return commodityCodes;
	}

	/**
	 * Sets the list of commodities the user wants to search by.
	 *
	 * @param commodityCodes The list of commodities the user wants to search by.
	 */
	public void setCommodityCodes(String commodityCodes) {
		this.commodityCodes = commodityCodes;
	}

	/**
	 * Returns the ClassCommodity (commodity) the user wants to search by.
	 *
	 * @return The ClassCommodity (commodity) the user wants to search by.
	 */
	public ClassCommodity getClassCommodity() {
		return classCommodity;
	}

	/**
	 * Sets the ClassCommodity (commodity) the user wants to search by.
	 *
	 * @param classCommodity The ClassCommodity (commodity) the user wants to search by.
	 */
	public void setClassCommodity(ClassCommodity classCommodity) {
		this.classCommodity = classCommodity;
	}

	/**
	 * Returns the list of sub-commodities the user wants to search by.
	 *
	 * @return The list of sub-commodities the user wants to search by.
	 */
	public String getSubCommodityCodes() {
		return subCommodityCodes;
	}

	/**
	 * Sets the list of sub-commodities the user wants to search by.
	 *
	 * @param subCommodityCodes The list of sub-commodities the user wants to search by.
	 */
	public void setSubCommodityCodes(String subCommodityCodes) {
		this.subCommodityCodes = subCommodityCodes;
	}

	/**
	 * Returns the SubCommodity the user wants to search by.
	 *
	 * @return The SubCommodity the user wants to search by.
	 */
	public SubCommodity getSubCommodity() {
		return subCommodity;
	}

	/**
	 * Sets the SubCommodity the user wants to search by.
	 *
	 * @param subCommodity The SubCommodity the user wants to search by.
	 */
	public void setSubCommodity(SubCommodity subCommodity) {
		this.subCommodity = subCommodity;
	}

	/**
	 * Returns the list of BDMs the user wants to search by.
	 *
	 * @return The list of BDMs the user wants to search by.
	 */
	public String getBdms() {
		return bdms;
	}

	/**
	 * Sets the list of BDMs the user wants to search by.
	 *
	 * @param bdms The list of BDMs the user wants to search by.
	 */
	public void setBdms(String bdms) {
		this.bdms = bdms;
	}

	/**
	 * Returns the Bdm the user wants to search by.
	 *
	 * @return The Bdm the user wants to search by.
	 */
	public Bdm getBdm() {
		return bdm;
	}

	/**
	 * Sets the Bdm the user wants to search by.
	 *
	 * @param bdm The Bdm the user wants to search by.
	 */
	public void setBdm(Bdm bdm) {
		this.bdm = bdm;
	}

	/**
	 * Returns the list of vendors the user wants to search by.
	 *
	 * @return The list of vendors the user wants to search by.
	 */
	public String getVendorNumbers() {
		return vendorNumbers;
	}

	/**
	 * Sets the list of vendors the user wants to search by.
	 *
	 * @param vendorNumbers The list of vendors the user wants to search by.
	 */
	public void setVendorNumbers(String vendorNumbers) {
		this.vendorNumbers = vendorNumbers;
	}

	/**
	 * Returns the Vendor the user wants to search by.
	 *
	 * @return The Vendor the user wants to search by.
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * Sets the Vendor the user wants to search by.
	 *
	 * @param vendor The Vendor the user wants to search by.
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * Returns tracking id of a batch or mass update request.
	 * @return tracking id.
	 */
	public Long getTrackingId() {
		return trackingId;
	}

	/**
	 * Sets tracking id.
	 * @param trackingId
	 */
	public void setTrackingId(Long trackingId) {
		this.trackingId = trackingId;
	}

	/**
	 * Returns a list of products to exclude from the search results. Please note that the generic search framework
	 * ignores this list and it is the responsibility of the calling client to remove the products from the result.
	 * It's really here to support mass update.
	 *
	 * @return A list of products to exclude from the search results.
	 */
	public List<Long> getExcludedProducts() {
		return excludedProducts;
	}

	/**
	 * Sets a list of products to exclude from the search results.
	 *
	 * @param excludedProducts A list of products to exclude from the search results.
	 */
	public void setExcludedProducts(List<Long> excludedProducts) {
		this.excludedProducts = excludedProducts;
	}

	/**
	 * Returns the LowestCustomerHierarchyNode the user wants to search by.
	 *
	 * @return LowestCustomerHierarchyNode the user wants to search by.
	 */
	public GenericEntityRelationshipKey getLowestCustomerHierarchyNode() {
		return lowestCustomerHierarchyNode;
	}

	/**
	 * Sets the LowestCustomerHierarchyNode the user wants to search by.
	 *
	 * @param lowestCustomerHierarchyNode The LowestCustomerHierarchyNode the user wants to search by.
	 */
	public void setLowestCustomerHierarchyNode(GenericEntityRelationshipKey lowestCustomerHierarchyNode) {
		this.lowestCustomerHierarchyNode = lowestCustomerHierarchyNode;
	}

	/**
	 * Returns ProductGroupIds.
	 *
	 * @return The ProductGroupIds.
	 **/
	public String getProductGroupIds() {
		return productGroupIds;
	}

	/**
	 * Sets the ProductGroupIds.
	 *
	 * @param productGroupIds The ProductGroupIds.
	 **/
	public void setProductGroupIds(String productGroupIds) {
		this.productGroupIds = productGroupIds;
	}

	/**
	 * Returns EntityType.
	 *
	 * @return The EntityType.
	 **/
	public String getEntityType() {
		return entityType;
	}

	/**
	 * Sets the EntityType.
	 *
	 * @param entityType The EntityType.
	 **/
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * Converts the data from this object into the more formal ProductSearchCriteria object.
	 *
	 * @return A ProductSearchCriteria object based on this object's data.
	 */
	public ProductSearchCriteria toProductSearchCriteria() {

		ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();

		if (this.productIds != null) {
			productSearchCriteria.setProductIds(this.longListFromStringFormatter.parse(this.productIds, Locale.US));
		}

		if (this.upcs != null) {
			productSearchCriteria.setUpcs(this.longListFromStringFormatter.parse(this.upcs, Locale.US));
		}

		if (this.itemCodes != null) {
			productSearchCriteria.setItemCodes(this.longListFromStringFormatter.parse(this.itemCodes, Locale.US));
		}

		if (this.caseUpcs != null) {
			productSearchCriteria.setCaseUpcs(this.longListFromStringFormatter.parse(this.caseUpcs, Locale.US));
		}

		if (this.subDepartment != null) {
			productSearchCriteria.setSubDepartment(this.subDepartment);
		}

		if (this.classCodes != null) {
			productSearchCriteria.setClassCodes(this.integerListFromStringFormatter.parse(this.classCodes, Locale.US));
		}

		if (this.itemClass != null) {
			productSearchCriteria.setItemClass(this.itemClass);
		}

		if (this.commodityCodes != null) {
			productSearchCriteria.setCommodityCodes(
					this.integerListFromStringFormatter.parse(this.commodityCodes, Locale.US));
		}

		if (this.classCommodity != null) {
			productSearchCriteria.setClassCommodity(this.classCommodity);
		}

		if (this.subCommodityCodes != null) {
			productSearchCriteria.setSubCommodityCodes(
					this.integerListFromStringFormatter.parse(this.subCommodityCodes, Locale.US));
		}

		if (this.subCommodity != null) {
			productSearchCriteria.setSubCommodity(this.subCommodity);
		}

		if (this.bdms != null) {
			productSearchCriteria.setBdmCodes(this.stringListFromStringFormatter.parse(this.bdms, Locale.US));
		}

		if (this.bdm != null) {
			productSearchCriteria.setBdm(this.bdm);
		}

		if (this.vendorNumbers != null) {
			productSearchCriteria.setVendorNumbers(
					this.integerListFromStringFormatter.parse(this.vendorNumbers, Locale.US));
		}

		if (this.vendor != null) {
			productSearchCriteria.setVendor(this.vendor);
		}

		if (this.description != null) {
			productSearchCriteria.setDescription(this.description);
		}

		productSearchCriteria.setSearchByProductDescription(this.searchByProductDescription);
		productSearchCriteria.setSearchByCustomerFriendlyDescription(this.searchByCustomerFriendlyDescription);
		productSearchCriteria.setSearchByDisplayName(this.searchByDisplayName);
		productSearchCriteria.setSearchAllExtendedAttributes(this.searchAllExtendedAttributes);

		if (this.customSearchEntries != null && !this.customSearchEntries.isEmpty()) {
			productSearchCriteria.setCustomSearchEntries(this.customSearchEntries);
			// For image search on BY-OS, we need to convert upcs string, product ids string and item codes string to the array list.
			for(CustomSearchEntry customSearchEntry : productSearchCriteria.getCustomSearchEntries()){
				if(customSearchEntry.getType() == CustomSearchEntry.IMAGE_CHANGE_ON_DATE){
					if(customSearchEntry.getUpcs() != null) {
                        productSearchCriteria.setUpcs(this.longListFromStringFormatter.parse(customSearchEntry.getUpcs(), Locale.US));
                        if(productSearchCriteria.getUpcs() == null){
							productSearchCriteria.setUpcs(new ArrayList<>());
						}
                        productSearchCriteria.getUpcs().addAll(this.longListFromStringFormatter.parse(customSearchEntry.getUpcs(), Locale.US));
					}
					if(customSearchEntry.getProductIds() != null) {
						if(productSearchCriteria.getProductIds() == null){
							productSearchCriteria.setProductIds(new ArrayList<>());
						}
                        productSearchCriteria.getProductIds().addAll(this.longListFromStringFormatter.parse(customSearchEntry.getProductIds(), Locale.US));
					}
					if(customSearchEntry.getItemCodes() != null) {
						if(productSearchCriteria.getItemCodes() == null){
							productSearchCriteria.setItemCodes(new ArrayList<>());
						}
                        productSearchCriteria.getItemCodes().addAll(this.longListFromStringFormatter.parse(customSearchEntry.getItemCodes(), Locale.US));
					}
				}
			}
		}

		if(this.lowestCustomerHierarchyNode != null) {
			productSearchCriteria.setLowestCustomerHierarchyNode(this.lowestCustomerHierarchyNode);
		}

		if(this.productGroupIds != null) {
			productSearchCriteria.setProductGroupIds(this.longListFromStringFormatter.parse(this.productGroupIds, Locale.US));
		}

		if (this.entityType != null && !this.entityType.isEmpty()) {
			productSearchCriteria.setEntityType(this.entityType);
		}
		productSearchCriteria.setTrackingId(this.trackingId);
		productSearchCriteria.setExcludedProducts(this.getExcludedProducts());

		return productSearchCriteria;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "RawSearchCriteria{" +
				"productIds='" + productIds + '\'' +
				", upcs='" + upcs + '\'' +
				", itemCodes='" + itemCodes + '\'' +
				", caseUpcs='" + caseUpcs + '\'' +
				", description='" + description + '\'' +
				", searchByProductDescription=" + searchByProductDescription +
				", searchByCustomerFriendlyDescription=" + searchByCustomerFriendlyDescription +
				", searchByDisplayName=" + searchByDisplayName +
				", searchAllExtendedAttributes=" + searchAllExtendedAttributes +
				", subDepartment=" + subDepartment +
				", classCodes='" + classCodes + '\'' +
				", itemClass=" + itemClass +
				", commodityCodes='" + commodityCodes + '\'' +
				", classCommodity=" + classCommodity +
				", subCommodityCodes='" + subCommodityCodes + '\'' +
				", subCommodity=" + subCommodity +
				", bdms='" + bdms + '\'' +
				", bdm=" + bdm +
				", vendorNumbers='" + vendorNumbers + '\'' +
				", vendor=" + vendor +
				", trackingId=" + trackingId +
				", lowestCustomerHierarchyNode=" + lowestCustomerHierarchyNode +
				", customSearchEntries=" + customSearchEntries +
				", excludedProducts=" + excludedProducts +
				", useSession=" + useSession +
				", firstSearch=" + firstSearch +
				", page=" + page +
				", pageSize=" + pageSize +
				'}';
	}
}
