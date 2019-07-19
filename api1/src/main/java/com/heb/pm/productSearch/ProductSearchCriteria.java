package com.heb.pm.productSearch;

import com.heb.pm.entity.*;

import java.util.List;

/**
 * Stores the parameters a user has asked to search for products by.
 *
 * @author d116773
 * @since 2.13.0
 */
public class ProductSearchCriteria {

	private String sessionId;
	private List<Long> productIds;
	private List<Long> upcs;
	private List<Long> itemCodes;
	private List<Long> caseUpcs;
	private List<Long> productGroupIds;

	private String description;
	private boolean searchByProductDescription;
	private boolean searchByCustomerFriendlyDescription;
	private boolean searchByDisplayName;
	private boolean searchAllExtendedAttributes;

	private SubDepartment subDepartment;

	private List<Integer> classCodes;
	private ItemClass itemClass;

	private List<Integer> commodityCodes;
	private ClassCommodity classCommodity;

	private List<Integer> subCommodityCodes;
	private SubCommodity subCommodity;

	private List<Long> excludedProducts;

	private List<String> bdmCodes;
	private Bdm bdm;

	private List<Integer> vendorNumbers;
	private Vendor vendor;

	private Long trackingId;

	private GenericEntityRelationshipKey lowestCustomerHierarchyNode;

	private List<CustomSearchEntry> customSearchEntries;

	public List<CustomSearchEntry> getCustomSearchEntries() {
		return customSearchEntries;
	}

	private String entityType;

	public void setCustomSearchEntries(List<CustomSearchEntry> customSearchEntries) {
		this.customSearchEntries = customSearchEntries;
	}

	/**
	 * Return's the user's session ID. This should equal the JSESSION ID and is used to keep track of the user's
	 * search criteria in the temp table.
	 *
	 * @return The user's session ID.
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the user's session ID.
	 *
	 * @param sessionId The user's session ID.
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Returns the list of product IDs the user wants to search by.
	 *
	 * @return The list of product IDs the user wants to search by.
	 */
	public List<Long> getProductIds() {
		return productIds;
	}

	/**
	 * Sets the list of product IDs the user wants to search by.
	 *
	 * @param productIds The list of product IDs the user wants to search by.
	 */
	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}

	/**
	 * Returns the list of UPCs the user wants to search by.
	 *
	 * @return The list of UPCs the user wants to search by.
	 */
	public List<Long> getUpcs() {
		return upcs;
	}

	/**
	 * Sets the list of UPCs the user wants to search by.
	 *
	 * @param upcs The list of UPCs the user wants to search by.
	 */
	public void setUpcs(List<Long> upcs) {
		this.upcs = upcs;
	}

	/**
	 * Returns the list of item codes the user wants to search by.
	 *
	 * @return The list of item codes the user wants to search by.
	 */
	public List<Long> getItemCodes() {
		return itemCodes;
	}

	/**
	 * Sets the list of item codes the user wants to search by.
	 *
	 * @param itemCodes The list of item codes the user wants to search by.
	 */
	public void setItemCodes(List<Long> itemCodes) {
		this.itemCodes = itemCodes;
	}

	/**
	 * Returns the list of case UPCs the user wants to search by.
	 *
	 * @return The list of case UPCs the user wants to search by.
	 */
	public List<Long> getCaseUpcs() {
		return caseUpcs;
	}

	/**
	 * Sets the list of case UPCs the user wants to search by.
	 *
	 * @param caseUpcs The list of case UPCs the user wants to search by.
	 */
	public void setCaseUpcs(List<Long> caseUpcs) {
		this.caseUpcs = caseUpcs;
	}

	/**
	 * Returns the description the user asked to search by.
	 *
	 * @return The description the user asked to search by.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description the user asked to search by.
	 *
	 * @param description The description the user asked to search by.
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * Returns whether the search should include searching by product description (product_master.prod_eng_des).
	 *
	 * @return True if the search should include searching by product description and false otherwise.
	 */
	public boolean getSearchByProductDescription() {
		return searchByProductDescription;
	}

	/**
	 * Sets whether the search should include searching by product description.
	 *
	 * @param searchByProductDescription True if the search should include searching by product description
	 *                                      and false otherwise.
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
	public List<Integer> getClassCodes() {
		return classCodes;
	}

	/**
	 * Sets the list of item class codes the user wants to search by.
	 *
	 * @param classCodes The list of item class codes the user wants to search by.
	 */
	public void setClassCodes(List<Integer> classCodes) {
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
	public List<Integer> getCommodityCodes() {
		return commodityCodes;
	}

	/**
	 * Sets the list of commodities the user wants to search by.
	 *
	 * @param commodityCodes The list of commodities the user wants to search by.
	 */
	public void setCommodityCodes(List<Integer> commodityCodes) {
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
	public List<Integer> getSubCommodityCodes() {
		return subCommodityCodes;
	}

	/**
	 * Sets the list of sub-commodities the user wants to search by.
	 *
	 * @param subCommodityCodes The list of sub-commodities the user wants to search by.
	 */
	public void setSubCommodityCodes(List<Integer> subCommodityCodes) {
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
	public List<String> getBdmCodes() {
		return bdmCodes;
	}

	/**
	 * Sets the list of BDMs the user wants to search by.
	 *
	 * @param bdmCodes The list of BDMs the user wants to search by.
	 */
	public void setBdmCodes(List<String> bdmCodes) {
		this.bdmCodes = bdmCodes;
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
	public List<Integer> getVendorNumbers() {
		return vendorNumbers;
	}

	/**
	 * Sets the list of vendors the user wants to search by.
	 *
	 * @param vendorNumbers The list of vendors the user wants to search by.
	 */
	public void setVendorNumbers(List<Integer> vendorNumbers) {
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
	 * Returns the LowestCustomerHierarchyNode
	 *
	 * @return LowestCustomerHierarchyNode
	 */
	public GenericEntityRelationshipKey getLowestCustomerHierarchyNode() {
		return lowestCustomerHierarchyNode;
	}

	/**
	 * Sets the LowestCustomerHierarchyNode
	 *
	 * @param lowestCustomerHierarchyNode The LowestCustomerHierarchyNode
	 */
	public void setLowestCustomerHierarchyNode(GenericEntityRelationshipKey lowestCustomerHierarchyNode) {
		this.lowestCustomerHierarchyNode = lowestCustomerHierarchyNode;
	}

	/**
	 * Returns the EntityType
	 *
	 * @return EntityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * Sets the EntityType
	 *
	 * @param entityType The EntityType
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * Returns ProductGroupIds.
	 *
	 * @return The ProductGroupIds.
	 **/
	public List<Long> getProductGroupIds() {
		return productGroupIds;
	}

	/**
	 * Sets the ProductGroupIds.
	 *
	 * @param productGroupIds The ProductGroupIds.
	 **/
	public void setProductGroupIds(List<Long> productGroupIds) {
		this.productGroupIds = productGroupIds;
	}

	/**
	 * Returns whether or not the user has set any sort of search criteria.
	 *
	 * return True if there is some sort of criteria and false otherwise.
	 */
	public boolean hasCriteria() {
		return (this.hasProductSearch()) ||
				(this.hasUpcSearch()) ||
				(this.hasItemCodeSearch()) ||
				(this.hasCaseUpcSearch()) ||
				this.subDepartment != null ||
				(this.hasClassSearch()) ||
				this.itemClass != null ||
				(this.hasCommoditySearch()) ||
				this.classCommodity != null ||
				(this.hasSubCommoditySearch()) ||
				this.subCommodity != null ||
				(this.bdmCodes != null && !this.bdmCodes.isEmpty()) ||
				this.bdm != null ||
				(this.vendorNumbers != null && !this.vendorNumbers.isEmpty()) ||
				this.vendor != null ||
				this.lowestCustomerHierarchyNode != null ||
				this.description != null ||
				(this.customSearchEntries != null && !this.customSearchEntries.isEmpty());
	}

	/**
	 * Returns whether this search includes product IDs.
	 *
	 * @return true if this search includes product IDs and false otherwise.
	 */
	public boolean hasProductSearch() {
		return this.productIds != null && !this.productIds.isEmpty();
	}

	/**
	 * Returns whether this search includes UPCs
	 *
	 * @return true if this search includes UPCs and false otherwise.
	 */
	public boolean hasUpcSearch() {
		return this.upcs != null && !this.upcs.isEmpty();
	}

	/**
	 * Returns whether this search includes item codes.
	 *
	 * @return true if this search includes item codes and false otherwise.
	 */
	public boolean hasItemCodeSearch() {
		return this.itemCodes != null && !this.itemCodes.isEmpty();
	}

	/**
	 * Returns whether this search includes case UPCs.
	 *
	 * @return true if this search includes case UPCs and false otherwise.
	 */
	public boolean hasCaseUpcSearch() {
		return this.caseUpcs != null && !this.caseUpcs.isEmpty();
	}

	/**
	 * Returns whether this search includes item classes (not the object, a list only).
	 *
	 * @return true if this search includes item classes and false otherwise.
	 */
	public boolean hasClassSearch() {
		return this.classCodes != null && !this.classCodes.isEmpty();
	}

	/**
	 * Returns whether this search includes commodities (not the object, a list only).
	 *
	 * @return true if this search includes commodities and false otherwise.
	 */
	public boolean hasCommoditySearch(){
		return this.commodityCodes != null && !this.commodityCodes.isEmpty();
	}

	/**
	 * Returns whether this search includes sub-commodities (not the object, a list only).
	 *
	 * @return true if this search includes sub-commodities and false otherwise.
	 */
	public boolean hasSubCommoditySearch() {
		return this.subCommodityCodes != null && !this.subCommodityCodes.isEmpty();
	}

	/**
	 * Returns whether or not this is a simple search. If it is, it can be used to return hits.
	 *
	 * @return True if this is a simple search and false otherwise.
	 */
	public boolean isSimpleSearch() {

		// See if there's anything there that's not the most basic search
		if (this.subDepartment != null || this.itemClass != null || this.classCommodity != null ||
				this.subCommodity != null || this.bdm != null || this.vendor != null || this.description != null ||
				(this.customSearchEntries != null && !this.customSearchEntries.isEmpty())) {
			return false;
		}

		// They can only have one of the basic search entries
		return this.hasProductSearch() ^ this.hasUpcSearch() ^ this.hasItemCodeSearch() ^ this.hasCaseUpcSearch() ^
				this.hasClassSearch() ^ this.hasCommoditySearch() ^ this.hasSubCommoditySearch();
	}

	/**
	 * Returns whether or not this search is for a product in the MAT hierarchy
	 *
	 * @return True if it is a search for a product in the MAT hierarchy
	 */
	public boolean isSearchByMat() {

		if (customSearchEntries == null) {
			return false;
		}

		for (CustomSearchEntry customSearchEntry : customSearchEntries) {
			if (customSearchEntry.getType() == CustomSearchEntry.MAT) {
				return true;
			}
		}

		return false;
	}
}
