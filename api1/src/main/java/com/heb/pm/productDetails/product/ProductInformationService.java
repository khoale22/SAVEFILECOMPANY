package com.heb.pm.productDetails.product;

import com.heb.pm.CoreTransactional;
import com.heb.pm.ResourceConstants;
import com.heb.pm.audit.AuditService;
import com.heb.pm.batchUpload.jms.BatchUploadMessageTibcoSender;
import com.heb.pm.entity.*;
import com.heb.pm.entity.ProductRelationship.ProductRelationshipCode;
import com.heb.pm.jms.MediaMasterXmlHelper;
import com.heb.pm.mediaMasterMessage.MediaMasterEvent;
import com.heb.pm.mediaMasterMessage.MenuLabel;
import com.heb.pm.repository.*;
import com.heb.pm.ws.*;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.jpa.SwitchToBooleanConverter;
import com.heb.util.ws.SoapException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigInteger;

/**
 * This holds all of the business logic for Product Info Screen.
 *
 * @author m594201
 * @since 2.12.0
 */
@Service
public class ProductInformationService {
	private static final Logger logger = LoggerFactory.getLogger(ProductInformationService.class);
	private static final String USER_EDITABLE_PRODUCT_INFORMATION_PARAMETERS_LOG_MESSAGE =
			"User %s is authorized to update product information {RESOURCE, value}: %s.";
	private static final String PRODUCT_NOT_FOUND_FOR_UPC =
			"Product not found for UPC: %d.";
	private static final String GOODS_PRODUCT_NOT_FOUND_ERROR_MESSAGE =
			"Goods Product not found for product id: %d.";
	private static final String DELETE_ACTION_CODE = "D";
	private static final LocalDate FOREVER = LocalDate.of(9999, 12, 31);
	private static final String MAP_PRICE_UPDATE_ERROR =
			"Could not update map price at this time. Please try again later.";;;
	public static final String PRODUCT_RETAIL_LINKS_EXPORT_HEADER = "Product Id, Item Id, Selling Unit UPCs, Product Description, Size, Quantity, UOM, Sub Commodity";
	private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
	private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";
	private static final String PRODUCT_HAS_WAREHOUSE_ITEMS_ERROR_MESSAGE = "This product has warehouse items. Please update the sub commodity and vendor in OMI.";
	private static final String SUB_COMMODITY_NOT_BELONG_TO_DEPARTMENT_ERROR_MESSAGE = "Stop! Hierarchy changes requires more review.";

	@Autowired
	private AuditService auditService;

	@Autowired
	private VertexServiceClient vertexServiceClient;

	@Autowired
	private PriceServiceClient priceServiceClient;

	@Autowired
	private ProductAssortmentServiceClient productAssortmentServiceClient;

	@Autowired
	private ProductRelationshipRepository productRelationshipRepository;

	@Autowired
	private MapPriceRepository mapPriceRepository;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductInfoRepositoryWithCounts productInfoRepositoryWithCounts;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private EffectiveDatedMaintenanceRepository effectiveDatedMaintenanceRepository;

	@Autowired
	private GoodsProductRepository goodsProductRepository;

	@Autowired
	private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	@Autowired
	private BatchUploadMessageTibcoSender messageTibcoSender;

	@Autowired
	private  MediaMasterXmlHelper mediaMasterXmlHelper;

    @Autowired
    private ItemClassRepository itemClassRepository;

    @Autowired
    private SubCommodityRepository subCommodityRepository;

    private SwitchToBooleanConverter switchToBooleanConverter = null;

	private static final Integer ZONE_PRIORITY_1 = 210;
	private static final Integer ZONE_PRIORITY_2 = 158;
	private static final Integer ZONE_PRIORITY_3 = 646;
	private static final Integer ZONE_PRIORITY_4 = 645;

	public static final String SHOW_CALORIES = "SHOW CALORIES";

	/**
	 * Gets tax category.  Calls webservice client and retrieves the vertex tax data.
	 *
	 * @param taxCode the tax code
	 * @return the tax category
	 */
	public VertexTaxCategory getVertexTaxCategory(String taxCode) {
		try{
			return this.vertexServiceClient.getTaxCategory(taxCode);
		} catch (Exception e){
			if(e.getMessage().equals("No Records Found")) {
				VertexTaxCategory vertexTaxCategory = new VertexTaxCategory();
				vertexTaxCategory.setCategoryName("Unknown ID");
				vertexTaxCategory.setDvrCode(taxCode);
				return vertexTaxCategory;
			}
			throw new SoapException(e.getMessage());
		}
	}

	/**
	 * Gets price information.  Calls the price service to retrieve product price data.
	 *
	 * @param upc the upc
	 * @return the price information
	 */
	public PriceDetail getPriceInformation(long upc) {

		List<PriceDetail> priceDetail;
		List<PriceDetail> matchedPriceDetails = new ArrayList<>();
		PriceDetail returnPriceDetail = null;
		Map<Integer, PriceDetail> map;

		try{
			priceDetail = this.priceServiceClient.getPriceDetailByUpc(String.valueOf(upc));

			for(PriceDetail priceDetail1 : priceDetail) {
				// Check if t he zone exists.  If it does check if the wanted values exist.  If so add to the list.
				if(priceDetail1.getZone() != null && (priceDetail1.getZone().equals(ZONE_PRIORITY_1) || priceDetail1.getZone().equals(ZONE_PRIORITY_2)
						|| priceDetail1.getZone().equals(ZONE_PRIORITY_3) || priceDetail1.getZone().equals(ZONE_PRIORITY_4))){

					matchedPriceDetails.add(priceDetail1);
				}
			}

			map = matchedPriceDetails.stream().collect(
					Collectors.toMap(PriceDetail::getZone, p -> p));

			//return value for following zones 210, 158, 646, 645  in the same order of priority
			if(map.containsKey(ZONE_PRIORITY_1)) {
				returnPriceDetail = map.get(ZONE_PRIORITY_1);
			} else if(map.containsKey(ZONE_PRIORITY_2)) {
				returnPriceDetail = map.get(ZONE_PRIORITY_2);
			} else if(map.containsKey(ZONE_PRIORITY_3)) {
				returnPriceDetail = map.get(ZONE_PRIORITY_3);
			} else if(map.containsKey(ZONE_PRIORITY_4)) {
				returnPriceDetail = map.get(ZONE_PRIORITY_4);
			}
		} catch (Exception e){
			throw new SoapException(e.getMessage());
		}

		return returnPriceDetail;
	}
	 //	Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	/**
	 * This method is used to return PriceInformation
	 * @param upc
	 * @param effectiveDate
	 * @param storeNo
	 * @return PriceDetail
	 */
	public PriceDetail getPriceInformation(long upc,Date effectiveDate,int storeNo) {
		PriceDetail returnPriceDetail = null;
		List<PriceDetail> priceDetailList;
		try{
			priceDetailList = this.priceServiceClient.getPriceDetailByUpc(String.valueOf(upc),effectiveDate,storeNo);
			if(priceDetailList != null){
				for(PriceDetail priceDetail : priceDetailList) {
					returnPriceDetail = priceDetail;
				}
			}
		} catch (Exception e){
			throw new SoapException(e.getMessage());
		}
		return returnPriceDetail;
	}
	
	 //	End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	
	/**
	 * Gets total count.  Calls Product assortment Service to pull the SSA count value.
	 *
	 * @param productId the product id
	 * @return the total count
	 */
	public ProductAssortment getTotalCount(String productId) {
		try{
			return this.productAssortmentServiceClient.getAssortmentByProductId(productId);
		} catch (Exception e){
			throw new SoapException(e.getMessage());
		}
	}

	/**
	 * This method finds a deposit product if it exists by searching the product relationship repository for a record
	 * that has the given product id, and relationship code of 'DEPOS'.
	 *
	 * @param productId The product id to search for.
	 * @return The related product master.
	 */
	public ProductMaster findRelatedProductByDepositProductRelationship(Long productId) {
		List<ProductRelationship> relationships =
				this.productRelationshipRepository.
						findByKeyProductIdAndKeyProductRelationshipCode(
								productId, ProductRelationshipCode.DEPOSIT_PRODUCT.getValue());
		return this.getFirstRelatedProduct(relationships);
	}

	/**
	 * Although the data supports more than one related product, the front end only supports one related product.
	 * If the list of product relationships is not empty, return the first relationship's related product. Else
	 * return empty product master.
	 *
	 * @param relationships List of product relationships searching on product id and 'DEPOS' relationship code.
	 * @return The first entry's related product, or empty product.
	 */
	private ProductMaster getFirstRelatedProduct(List<ProductRelationship> relationships) {
		if(relationships != null && !relationships.isEmpty()){
			relationships.get(0).getRelatedProduct().getProdId();
			return relationships.get(0).getRelatedProduct();
		}
		return new ProductMaster();
	}

	/**
	 * This method queries the repository to get all map prices by upc and ordered by effective time, then calls
	 * helper methods to return the current map price.
	 *
	 * @param upc The upc to search for.
	 * @return Map price that is the current effective map price.
	 */
	public List<MapPrice> getEffectiveMapPrices(Long upc) {
		LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
		return this.findEffectiveMapPrices(this.mapPriceRepository.findByKeyUpcAndExpirationTimeGreaterThanEqualOrderByKeyEffectiveTime(upc, startOfToday));
	}

	/**
	 * This method takes in a list of map prices ordered by effective time that are not expired, then returns a list
	 * of map prices representing today's and tomorrow's map price. If the list is empty, or if there is no map price
	 * effective before two days from now, return an empty list. Else return today's and tomorrow's map price.
	 *
	 * @param mapPricesOrderedByEffectiveTime Map prices ordered by effective time.
	 * @return The first effective map price found, or empty map price if none are found.
	 */
	private List<MapPrice> findEffectiveMapPrices(List<MapPrice> mapPricesOrderedByEffectiveTime) {
		LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
		List<MapPrice> toReturn = new ArrayList<>();
		boolean foundFirst = false;
		for(MapPrice mapPrice : mapPricesOrderedByEffectiveTime){
			// if first hasn't already been found, and effective time is before tonight at midnight and not expired
			if(!foundFirst && mapPrice.getKey().getEffectiveTime().isBefore(startOfToday.plusDays(1)) &&
					mapPrice.getExpirationTime().isAfter(startOfToday)){
				// add current map price as the first element
				toReturn.add(mapPrice);
				foundFirst = true;
			}
			// if effective time is before tomorrow night midnight and doesn't expire until after tonight midnight
			if(mapPrice.getKey().getEffectiveTime().isBefore(startOfToday.plusDays(2)) &&
					mapPrice.getExpirationTime().isAfter(startOfToday.plusDays(1))){
				// if first element hasn't been added, add a new map price
				if(toReturn.isEmpty()){
					toReturn.add(new MapPrice());
				}
				// add current map price as the second element
				toReturn.add(mapPrice);
				break;
			}
			// if the return list has 2
			if(toReturn.size() == 2){
				break;
			}
		}
		if(toReturn.isEmpty()){
			toReturn.add(new MapPrice());
			toReturn.add(new MapPrice());
		} else if(toReturn.size() == 1){
			toReturn.add(new MapPrice());
		}
		return toReturn;
	}

	/**
	 * This method sends the values the current user can update to the web service to update a product
	 * master.
	 *
	 * @param productInfoParameters Set of product information a user can update.
	 */
	public void update(ProductInfoParameters productInfoParameters) {

		ProductMaster originalProductMaster =
				this.productInfoRepository.findOne(productInfoParameters.getProductMaster().getProdId());

        try {
            List<UpdatableField> listUpdateField = this.getUserEditableUpdatableFieldsForProductManagementService(originalProductMaster, productInfoParameters);
            //the validation only executed when user changed sub-commodity
            for (UpdatableField updatableField : listUpdateField) {
                if (updatableField.getField().equals(ResourceConstants.PRODUCT_SUB_COMMODITY)) {
                    validateSubCommodity(originalProductMaster, productInfoParameters);
                    break;
                }
            }
            this.productManagementServiceClient.updateProductInformation(listUpdateField, productInfoParameters.getProductMaster().getProdId());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new SoapException(e.getMessage());
        }
        List<MapPrice> mapPriceUpdates = this.getMapPriceUpdates(productInfoParameters);
        Boolean newSelectIngredients = this.getSelectIngredientsToUpdate(originalProductMaster, productInfoParameters.getProductMaster());
        this.productAttributeManagementServiceClient.updateFromProductInfo(productInfoParameters.getProductMaster().getProdId(),
                newSelectIngredients, mapPriceUpdates, this.userInfo.getUserId());

		//send msg to media master when show calories is checked.
		if(productInfoParameters.getProductMaster().getShowCalories() && !originalProductMaster.getShowCalories()){
			MediaMasterEvent mediaMasterEvent = new MediaMasterEvent();
			mediaMasterEvent.setHeader(this.mediaMasterXmlHelper.generateMediaMasterEventHeader());
			mediaMasterEvent.setBody(this.mediaMasterXmlHelper.generateMediaMasterEventBody(new MenuLabel(BigInteger
					.valueOf(productInfoParameters.getProductMaster().getProdId()), SHOW_CALORIES)));
			this.messageTibcoSender.sendMesageToJMSQueue(this.mediaMasterXmlHelper.marshallFulfillmentEvent(mediaMasterEvent));
		}
	}

    /**
     * Determines if the select ingredients property has changed. If so, it returns true or false (based on the new
     * setting of the property). If it hasn't changed or the user does not have permission, then it returns null.
     *
     * @param originalProductMaster The product master in the DB.
     * @param updatedProductMaster  The product master as the user has updated it.
     * @return The new value for select ingredients and null if no change.
     */
    private Boolean getSelectIngredientsToUpdate(ProductMaster originalProductMaster, ProductMaster updatedProductMaster) {

		// Check the user's permissions.
		if (!this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_INFO_SELECT_INGREDIENTS)) {
			return null;
		}

		// See if select ingredients is already set.
		boolean isSelectIngredients = false;
		for (ProductMarketingClaim productMarketingClaim : originalProductMaster.getProductMarketingClaims()) {
			if (productMarketingClaim.getKey().getMarketingClaimCode().equals(MarketingClaim.Codes.SELECT_INGREDIENTS.getCode())) {
				isSelectIngredients = true;
			}
		}

		// Compare select ingredients from the UI with what's in the DB.
		if (updatedProductMaster.getSelectIngredients() != null && !updatedProductMaster.getSelectIngredients().equals(isSelectIngredients)) {
			return updatedProductMaster.getSelectIngredients();
		}
		return null;
	}

	/**
	 * Determines any map price updates to send to the backend.
	 *
	 * @param productInfoParameters Set of product information a user can update.
	 */
	private List<MapPrice> getMapPriceUpdates(ProductInfoParameters productInfoParameters) {
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_MAP)){
			List<MapPrice> currentEffectiveMapPrices =
					this.getEffectiveMapPrices(
							productInfoParameters.getProductMaster().getProductPrimaryScanCodeId());
			List<MapPrice> updatedEffectiveMapPrices = productInfoParameters.getMapPrices();
			// if there was a problem getting either the current or updated map prices, throw error
			if(currentEffectiveMapPrices.size() < 2 || updatedEffectiveMapPrices.size() < 2){
				throw new IllegalArgumentException(MAP_PRICE_UPDATE_ERROR);
			}
			// the user can only update the effective map price for tomorrow, which should be the second element
			// in the lists
			MapPrice currentEffectiveMapPrice = currentEffectiveMapPrices.get(1);
			MapPrice updatedEffectiveMapPrice = updatedEffectiveMapPrices.get(1);
			List<MapPrice> mapPriceUpdates = new ArrayList<>();
			if(this.isMapPriceUpdated(currentEffectiveMapPrice, updatedEffectiveMapPrice)){
				// change was detected
				if(currentEffectiveMapPrice.getMapAmount() == null){
					// current map price is null
					// new map price needs to be created
					updatedEffectiveMapPrice.setExpirationTime(FOREVER.atStartOfDay());
					updatedEffectiveMapPrice.setKey(new MapPriceKey());
					updatedEffectiveMapPrice.getKey().setEffectiveTime(LocalDate.now().plusDays(1).atStartOfDay());
					updatedEffectiveMapPrice.getKey().setUpc(
							productInfoParameters.getProductMaster().getProductPrimaryScanCodeId());
					updatedEffectiveMapPrice.setActionCode(
							ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
					mapPriceUpdates.add(updatedEffectiveMapPrice);
				} else if(updatedEffectiveMapPrice.getMapAmount() == null){
					// if updated map price is null, current either needs to be deleted or expired
					if(currentEffectiveMapPrice.getKey().getEffectiveTime().isEqual(LocalDate.now().plusDays(1).atStartOfDay())){
						// if current map price was effective tomorrow, it must be deleted
						currentEffectiveMapPrice.setActionCode(
								ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
					} else {
						// else current effective map price needs to be set to expired
						currentEffectiveMapPrice.setExpirationTime(LocalDate.now().plusDays(1).atStartOfDay());
						currentEffectiveMapPrice.setActionCode(
								ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
					}
					mapPriceUpdates.add(currentEffectiveMapPrice);
				} else {
					// difference in map price
					if(currentEffectiveMapPrice.getKey().getEffectiveTime().isEqual(LocalDate.now().plusDays(1).atStartOfDay())){
						// current was effective tomorrow
						// only the map amount is changing
						currentEffectiveMapPrice.setExpirationTime(FOREVER.atStartOfDay());
						currentEffectiveMapPrice.setMapAmount(updatedEffectiveMapPrice.getMapAmount());
						currentEffectiveMapPrice.setActionCode(
								ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
						mapPriceUpdates.add(currentEffectiveMapPrice);
					} else {
						// current was effective before tomorrow
						// current needs to expire tomorrow, and new map price needs to be created effective tomorrow
						currentEffectiveMapPrice.setExpirationTime(LocalDate.now().plusDays(1).atStartOfDay());
						currentEffectiveMapPrice.setActionCode(
								ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
						mapPriceUpdates.add(currentEffectiveMapPrice);
						updatedEffectiveMapPrice.setExpirationTime(FOREVER.atStartOfDay());
						updatedEffectiveMapPrice.setKey(new MapPriceKey());
						updatedEffectiveMapPrice.getKey().setEffectiveTime(LocalDate.now().plusDays(1).atStartOfDay());
						updatedEffectiveMapPrice.getKey().setUpc(
								productInfoParameters.getProductMaster().getProductPrimaryScanCodeId());
						updatedEffectiveMapPrice.setActionCode(
								ProductAttributeManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
						mapPriceUpdates.add(updatedEffectiveMapPrice);
					}
				}

				return mapPriceUpdates;
			}
		}
		return null;
	}

	/**
	 * Given two map prices, this method compares the two map amounts to see if there was a change, then returns whether
	 * a change was detected. A change will be detected if:
	 * 1. one map amount equals null and the other does not, or
	 * 2. the map amounts are both not null and the map amounts are different.
	 * Else no change was detected.
	 *
	 * @param currentEffectiveMapPrice Current map price to compare.
	 * @param updatedEffectiveMapPrice Updated map price to compare.
	 * @return True if change was detected, false otherwise.
	 */
	private boolean isMapPriceUpdated(MapPrice currentEffectiveMapPrice, MapPrice updatedEffectiveMapPrice) {
		// if both map amounts are null return false
		if(currentEffectiveMapPrice.getMapAmount() == null && updatedEffectiveMapPrice.getMapAmount() == null){
			return false;
		}
		// if current map amount is not null, return current map amount not equal to updated map amount
		if(currentEffectiveMapPrice.getMapAmount() != null){
			return !currentEffectiveMapPrice.getMapAmount().equals(updatedEffectiveMapPrice.getMapAmount());
		}
		// if current map amount is null, return updated map amount not equal to null
		return updatedEffectiveMapPrice.getMapAmount() != null;
	}

	/**
	 * This method gets the user editable updatable fields given a product information parameters. This
	 * method calls helper functions to get each nested object's editable fields. After calling each of the helper methods,
	 * the function returns all of the values that have changed that the user has access to edit. If no fields were
	 * found that the user did have access to and weren't null, this method returns an empty list of updatable fields.
	 *
	 * @param productInfoParameters Product information parameters to look for user edit access on.
	 * @return List of updatable fields the user has edit access to and have changed.
	 */
	private List<UpdatableField> getUserEditableUpdatableFieldsForProductManagementService(ProductMaster originalProductMaster,
																						   ProductInfoParameters productInfoParameters) {
		List<UpdatableField> userEditableFields = new ArrayList<>();

		// set product master portion of product info parameters
		userEditableFields.addAll(
				this.getUpdatableFieldsFromProductMaster(originalProductMaster, productInfoParameters.getProductMaster()));

		// set deposit related product part of product info parameters
		userEditableFields.addAll(
				this.getUserEditableDepositRelatedProduct(
						productInfoParameters.getDepositRelatedProduct(),
						productInfoParameters.getProductMaster().getProdId()));

		this.logUserEditableProductInformation(userEditableFields);
		return userEditableFields;
	}

	/**
	 * This method gets updatable fields the user has edit access on from a given deposit related product. If no product
	 * was found matching the UPC entered by the user, throw an illegal argument exception declaring a product was not
	 * found for the UPC given. If user does not have edit access on deposit related product, this method returns an
	 * empty list.
	 *
	 * @param depositRelatedProduct Deposit related product to look for user edit access on.
	 * @return List of user editable updatable fields from a deposit related product, or an empty list if none found.
	 */
	private List<UpdatableField> getUserEditableDepositRelatedProduct(ProductMaster depositRelatedProduct, Long productId) {
		List<UpdatableField> toReturn = new ArrayList<>();
		ProductMaster newDepositRelatedProduct = null;
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_DEPOSIT_UPC)){
			ProductMaster originalDepositRelatedProduct = this.findRelatedProductByDepositProductRelationship(productId);
			if(originalDepositRelatedProduct == null && depositRelatedProduct != null &&
					depositRelatedProduct.getProductPrimaryScanCodeId() != null){
				newDepositRelatedProduct = this.productInfoRepository.findBySellingUnitsUpc(
						depositRelatedProduct.getProductPrimaryScanCodeId());
				if(newDepositRelatedProduct == null){
					throw new IllegalArgumentException(
							String.format(
									ProductInformationService.PRODUCT_NOT_FOUND_FOR_UPC,
									depositRelatedProduct.getProductPrimaryScanCodeId()));
				}
				toReturn.add(new UpdatableField(ResourceConstants.PRODUCT_DEPOSIT_UPC, newDepositRelatedProduct));
			} else if(originalDepositRelatedProduct != null && depositRelatedProduct != null &&
					this.objectsNotEqual(
							originalDepositRelatedProduct.getProductPrimaryScanCodeId(),
							depositRelatedProduct.getProductPrimaryScanCodeId())){
				if(depositRelatedProduct.getProductPrimaryScanCodeId() != null){
					newDepositRelatedProduct = this.productInfoRepository.findBySellingUnitsUpc(
							depositRelatedProduct.getProductPrimaryScanCodeId());
					if(newDepositRelatedProduct == null){
						throw new IllegalArgumentException(
								String.format(
										ProductInformationService.PRODUCT_NOT_FOUND_FOR_UPC,
										depositRelatedProduct.getProductPrimaryScanCodeId()));
					}
				}
				if(newDepositRelatedProduct == null){
					newDepositRelatedProduct = originalDepositRelatedProduct;
					newDepositRelatedProduct.setActionCode(DELETE_ACTION_CODE);
				}
				toReturn.add(new UpdatableField(
						ResourceConstants.PRODUCT_DEPOSIT_UPC, newDepositRelatedProduct));
			} else if(originalDepositRelatedProduct != null && depositRelatedProduct == null){
				originalDepositRelatedProduct.setActionCode(DELETE_ACTION_CODE);
				toReturn.add(new UpdatableField(
						ResourceConstants.PRODUCT_DEPOSIT_UPC, originalDepositRelatedProduct));
			}
		}

		return toReturn;
	}

	/**
	 * This method gets updatable fields the user has edit access on from a given product master. If no fields were
	 * found that the user did have access to, this method returns an empty list.
	 *
	 * @param productMaster Product master to look for user edit access on.
	 * @return List of user editable updatable fields from a product master, or an empty list if none found.
	 */
	private List<UpdatableField> getUpdatableFieldsFromProductMaster(ProductMaster originalProductMaster,
																	 ProductMaster productMaster) {
		List<UpdatableField> toReturn = new ArrayList<>();

		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_DESCRIPTION) &&
				this.stringsNotEqualIgnoreCaseIgnoreTrailingSpace(
						originalProductMaster.getDescription(), productMaster.getDescription())){
			toReturn.add(new UpdatableField(
					ResourceConstants.PRODUCT_DESCRIPTION, productMaster.getDescription()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_QUANTITY_REQUIRED) &&
				this.stringsNotEqualIgnoreCaseIgnoreTrailingSpace(
						originalProductMaster.getQuantityRequiredFlag(), productMaster.getQuantityRequiredFlag())){
			toReturn.add(new UpdatableField(ResourceConstants.PRODUCT_QUANTITY_REQUIRED,
					productMaster.getQuantityRequiredFlag()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_STYLE_DESCRIPTION)){
			String styleDescription =
					this.getModifiedSpecifiedDescriptionTypeFromProductDescriptions(
							productMaster.getProductDescriptions(), originalProductMaster.getProductDescriptions(),
							DescriptionType.Codes.STYLE);
			if(styleDescription != null) {
				toReturn.add(new UpdatableField(ResourceConstants.PRODUCT_STYLE_DESCRIPTION, styleDescription));
			}
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_SHOW_CALORIES) &&
				this.objectsNotEqual(
						originalProductMaster.getShowCalories(), productMaster.getShowCalories())){
			toReturn.add(new UpdatableField(ResourceConstants.PRODUCT_SHOW_CALORIES, productMaster.getShowCalories()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_SUB_COMMODITY) &&
				this.objectsNotEqual(
						originalProductMaster.getSubCommodityCode(), productMaster.getSubCommodityCode())){
			toReturn.add(
					new UpdatableField(ResourceConstants.PRODUCT_SUB_COMMODITY, productMaster.getSubCommodityCode()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_PRICE_REQUIRED) &&
				this.objectsNotEqual(
						originalProductMaster.getPriceRequiredFlag(), productMaster.getPriceRequiredFlag())){
			toReturn.add(
					new UpdatableField(ResourceConstants.PRODUCT_PRICE_REQUIRED, productMaster.getPriceRequiredFlag()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_QUALIFYING_CONDITION) &&
				this.objectsNotEqual(
						originalProductMaster.getTaxQualifyingCode(), productMaster.getTaxQualifyingCode())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_QUALIFYING_CONDITION, productMaster.getTaxQualifyingCode()));
		}

		// set goods product portion of product master, since goods product is within a product master
		if(originalProductMaster.getGoodsProduct() != null && productMaster.getGoodsProduct() != null){
			toReturn.addAll(
					this.getUpdatableFieldsFromGoodsProduct(originalProductMaster.getGoodsProduct(), productMaster.getGoodsProduct()));
		}
		return toReturn;
	}

	/**
	 * This helper method determines if two objects are not equal, including null checks. If one of the objects is
	 * null but the other is not, return true. Else do an object equals comparison. If they are equal return false. Else
	 * return true.
	 *
	 * @param original Original state of the object to compare.
	 * @param current Current state of the object to compare.
	 * @return True if objects are not equal, false otherwise.
	 */
	private boolean objectsNotEqual(Object original, Object current){
		if(original == null){
			return current != null;
		} else if(current == null){
			return true;
		} else {
			return !original.equals(current);
		}
	}

	/**
	 * This helper method determines if two Strings are not equal, including null checks. If one of the objects is
	 * null but the other is not, return true. Else do an string equals ignore case comparison after trimming the
	 * values. If they are equal return false. Else return true.
	 *
	 * @param original Original state of the string to compare.
	 * @param current Current state of the string to compare.
	 * @return True if string are not equal, false otherwise.
	 */
	private boolean stringsNotEqualIgnoreCaseIgnoreTrailingSpace(String original, String current){
		if(original == null){
			return current != null;
		} else if(current == null){
			return true;
		} else {
			return !original.trim().equalsIgnoreCase(current.trim());
		}
	}

	/**
	 * This method gets updatable fields the user has edit access on from a given goods product, and original state of
	 * the goods product. If no fields were found that the user did have access to, this method returns an empty list.
	 *
	 * @param originalGoodsProduct Original state of the goods product.
	 * @param goodsProduct Goods product to look for user edit access on.
	 * @return List of user editable updatable fields from a goods product, or an empty list if none found.
	 */
	private List<UpdatableField> getUpdatableFieldsFromGoodsProduct(GoodsProduct originalGoodsProduct, GoodsProduct goodsProduct) {

		List<UpdatableField> toReturn = new ArrayList<>();

		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_GENERIC_PRODUCT) &&
				this.objectsNotEqual(
						originalGoodsProduct.getGenericProductSwitch(), goodsProduct.getGenericProductSwitch())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_GENERIC_PRODUCT, goodsProduct.getGenericProductSwitch()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_SELF_MANUFACTURED) &&
				this.objectsNotEqual(
						originalGoodsProduct.getSelfManufactureSwitch(), goodsProduct.getSelfManufactureSwitch())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_SELF_MANUFACTURED, goodsProduct.getSelfManufactureSwitch()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_COLOR) &&
				this.objectsNotEqual(
						originalGoodsProduct.getColorCode(), goodsProduct.getColorCode())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_COLOR, goodsProduct.getColorCode()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_MODEL) &&
				this.objectsNotEqual(
						originalGoodsProduct.getProductModelText(), goodsProduct.getProductModelText())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_MODEL, goodsProduct.getProductModelText()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_CRITICAL_PRODUCT) &&
				this.objectsNotEqual(
						originalGoodsProduct.getCriticalProductSwitch(), goodsProduct.getCriticalProductSwitch())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_CRITICAL_PRODUCT, goodsProduct.getCriticalProductSwitch()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_PRE_PRICE) &&
				this.objectsNotEqual(
						originalGoodsProduct.getPrePrice(), goodsProduct.getPrePrice())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_PRE_PRICE, goodsProduct.getPrePrice()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_DRUG_FACT_PANEL) &&
				this.objectsNotEqual(
						originalGoodsProduct.getDrugFactPanelSwitch(), goodsProduct.getDrugFactPanelSwitch())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_DRUG_FACT_PANEL, goodsProduct.getDrugFactPanelSwitch()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_FSA) &&
				this.objectsNotEqual(
						originalGoodsProduct.getFsaCode(), goodsProduct.getFsaCode())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_FSA, goodsProduct.getFsaCode() ? "Y" : "N"));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_FAMILY_CODE_1) &&
				this.objectsNotEqual(
						originalGoodsProduct.getFamilyCode1(), goodsProduct.getFamilyCode1())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_FAMILY_CODE_1, goodsProduct.getFamilyCode1()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_FAMILY_CODE_2) &&
				this.objectsNotEqual(
						originalGoodsProduct.getFamilyCode2(), goodsProduct.getFamilyCode2())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_FAMILY_CODE_2, goodsProduct.getFamilyCode2()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_PACKAGING) &&
				this.objectsNotEqual(
						originalGoodsProduct.getPackagingText(), goodsProduct.getPackagingText())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_PACKAGING, goodsProduct.getPackagingText()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_SCALE_LABEL) &&
				this.objectsNotEqual(
						originalGoodsProduct.getScaleLabelSwitch(), goodsProduct.getScaleLabelSwitch())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_SCALE_LABEL, goodsProduct.getScaleLabelSwitch()));
		}
		if(this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_FOOD_STAMP) &&
				this.objectsNotEqual(
						originalGoodsProduct.getFoodStampSwitch(), goodsProduct.getFoodStampSwitch())){
			toReturn.add(
					new UpdatableField(
							ResourceConstants.PRODUCT_FOOD_STAMP, goodsProduct.getFoodStampSwitch()));
		}
		return toReturn;
	}

	/**
	 * This is a helper method to retrieve a specified description type from a list of product descriptions. If the list
	 * is null, empty, or does not contain the specified description type, return null. Else if the description that
	 * matches the description type was changed, return the updated description. Else return null.
	 *
	 * @param productDescriptions List of product descriptions.
	 * @param originalProductDescriptions List of original product descriptions.
	 * @param descriptionType Type of description to look for.
	 * @return Description of the given description type that was changed, or null.
	 */
	private String getModifiedSpecifiedDescriptionTypeFromProductDescriptions(
			List<ProductDescription> productDescriptions, List<ProductDescription> originalProductDescriptions,
			DescriptionType.Codes descriptionType) {
		String descriptionTypeTrimmed = descriptionType.getId().trim();
		if((productDescriptions == null || productDescriptions.isEmpty()) &&
				(originalProductDescriptions == null || originalProductDescriptions.isEmpty())){
			return null;
		}

		ProductDescription updatedProductDescription = null;
		ProductDescription originalProductDescription = null;

		if(productDescriptions != null && !productDescriptions.isEmpty()){
			for(ProductDescription productDescription : productDescriptions){
				if(productDescription.getKey().getDescriptionType().trim().equalsIgnoreCase(descriptionTypeTrimmed)){
					updatedProductDescription = productDescription;
					break;
				}
			}
		}

		if(originalProductDescriptions != null && !originalProductDescriptions.isEmpty()){
			for(ProductDescription productDescription : originalProductDescriptions){
				if(productDescription.getKey().getDescriptionType().trim().equalsIgnoreCase(descriptionTypeTrimmed)){
					originalProductDescription = productDescription;
					break;
				}
			}
		}

		if(updatedProductDescription == null && originalProductDescription == null){
			return null;
		} else if(updatedProductDescription == null){
			return StringUtils.EMPTY;
		} else if(originalProductDescription == null){
			return updatedProductDescription.getDescription();
		} else if(updatedProductDescription.getDescription().trim().equals(originalProductDescription.getDescription().trim())){
			return null;
		} else {
			return updatedProductDescription.getDescription();
		}
	}

	/**
	 * Logger for the product information the user has permissions to edit.
	 *
	 * @param userEditableProductInfoParameters The product information parameters the user has edit permissions for.
	 */
	private void logUserEditableProductInformation(List<UpdatableField> userEditableProductInfoParameters) {
		ProductInformationService.logger.info(
				String.format(ProductInformationService.USER_EDITABLE_PRODUCT_INFORMATION_PARAMETERS_LOG_MESSAGE,
						this.userInfo.getUserId(), userEditableProductInfoParameters.toString())
		);
	}

	/**
	 * Gets all effective maintenance for product information regarding vertex tax categories. This is done by
	 * querying the EffectiveDatedMaintenanceRepository given a table name for the GoodsProduct entity, column name
	 * for vertex tax category code, and a product id.
	 *
	 * @param productId Product id to search for.
	 * @return List of all effective maintenance matching the given parameters.
	 */
	@CoreTransactional
	public List<EffectiveVertexTaxCategory> getAllVertexTaxCategoryEffectiveMaintenanceForProduct(Long productId) {
		List<EffectiveVertexTaxCategory> toReturn = new ArrayList<>();
		List<EffectiveVertexTaxCategory> effectiveVertexTaxCategoriesMaintenances = this.convertEffectiveDatedMaintenanceToEffectiveVertexTaxCategories(
				this.effectiveDatedMaintenanceRepository.
						findByKeyTableNameAndKeyColumnNameInAndProductIdOrderByEffectiveDate(
								EffectiveDatedMaintenance.TABLE_NAME_GOODS_PRODUCT,
								Arrays.asList(
										EffectiveDatedMaintenance.COLUMN_NAME_SALES_TAX_SWITCH,
										EffectiveDatedMaintenance.COLUMN_NAME_VERTEX_TAX_CATEGORY_CODE
								),productId));
		if(effectiveVertexTaxCategoriesMaintenances == null || effectiveVertexTaxCategoriesMaintenances.isEmpty()){
			EffectiveVertexTaxCategory currentTaxCategory = this.createEffectiveVertexTaxCategoryFromProductId(productId);
			if (currentTaxCategory != null) {
				currentTaxCategory.setEffectiveDate(null);
				toReturn.add(currentTaxCategory);
			}
		}else{
			toReturn.addAll(effectiveVertexTaxCategoriesMaintenances);
		}
		return toReturn;
	}

	/**
	 * The method creates an effective vertex tax category from a goods product with the given product id. If the
	 * goods product does not exist, throw an error saying the goods product was not found. Else create the effective
	 * vertex tax category with the given information from the goods product found.
	 *
	 * @param productId Product id to search for
	 * @return Effective vertex tax category created from the goods product with the given product id.
	 */
	private EffectiveVertexTaxCategory createEffectiveVertexTaxCategoryFromProductId(Long productId) {
		GoodsProduct goodsProduct = this.goodsProductRepository.findOne(productId);
		if(goodsProduct == null){
			throw new IllegalArgumentException(String.format(GOODS_PRODUCT_NOT_FOUND_ERROR_MESSAGE, productId));
		}
		if (goodsProduct.getVertexTaxCategoryCode() == null || goodsProduct.getVertexTaxCategoryCode().isEmpty()) {
			return null;
		}
		EffectiveVertexTaxCategory toReturn = new EffectiveVertexTaxCategory();
		toReturn.setProductId(productId);
		toReturn.setRetailTaxable(goodsProduct.getRetailTaxSwitch());
		toReturn.setVertexTaxCategoryCode(goodsProduct.getVertexTaxCategoryCode());
		toReturn.setEffectiveDate(LocalDate.now());
		return toReturn;
	}

	/**
	 * This method converts a list of effective dated maintenance into effective vertex tax categories by grouping
	 * effective dated maintenance by effective date.
	 *
	 * @param effectiveDatedMaintenance List of effective dated maintenance to convert.
	 * @return List of effective vertex tax categories.
	 */
	private List<EffectiveVertexTaxCategory> convertEffectiveDatedMaintenanceToEffectiveVertexTaxCategories(
			List<EffectiveDatedMaintenance> effectiveDatedMaintenance) {
		List<EffectiveVertexTaxCategory> toReturn = new ArrayList<>();
		for(EffectiveDatedMaintenance effectiveMaintenance : effectiveDatedMaintenance){
			this.addValidEffectiveDatedMaintenanceToEffectiveVertexTaxCategories(effectiveMaintenance, toReturn);
		}
		return toReturn;
	}

	/**
	 * This method populates a list of effective vertex tax categories with valid effective dated maintenance records.
	 *
	 * @param effectiveMaintenance Current effective maintenance record.
	 * @param effectiveVertexTaxCategories List of vertex tax categories.
	 */
	private void addValidEffectiveDatedMaintenanceToEffectiveVertexTaxCategories(
			EffectiveDatedMaintenance effectiveMaintenance, List<EffectiveVertexTaxCategory> effectiveVertexTaxCategories) {

		// if the effective maintenance date occurred before tomorrow, it is invalid
		if(effectiveMaintenance.getEffectiveDate().isBefore(LocalDate.now().plusDays(1))){
			return;
		}

		EffectiveVertexTaxCategory effectiveVertexTaxCategory = null;
		for(EffectiveVertexTaxCategory existingEffectiveVertexTaxCategory : effectiveVertexTaxCategories){
			// if an effective vertex tax category shares the same effective date, set variable to given tax category
			if(effectiveMaintenance.getEffectiveDate().equals(existingEffectiveVertexTaxCategory.getEffectiveDate())){
				effectiveVertexTaxCategory = existingEffectiveVertexTaxCategory;
				break;
			}
		}

		// if a vertex tax category that has the effective maintenance date was not found, initialize the tax category
		// by setting the effective date and add to list
		if(effectiveVertexTaxCategory == null){
			effectiveVertexTaxCategory = new EffectiveVertexTaxCategory();
			effectiveVertexTaxCategory.setEffectiveDate(effectiveMaintenance.getEffectiveDate());
			effectiveVertexTaxCategory.setProductId(effectiveMaintenance.getProductId());
			effectiveVertexTaxCategories.add(effectiveVertexTaxCategory);
		}

		// set information on effective tax category based on column name
		switch (effectiveMaintenance.getKey().getColumnName().trim()){
			case EffectiveDatedMaintenance.COLUMN_NAME_VERTEX_TAX_CATEGORY_CODE :{
				effectiveVertexTaxCategory.setVertexTaxCategoryCode(effectiveMaintenance.getTextValue());
				effectiveVertexTaxCategory.setVertexTaxCategorySequenceNumber(effectiveMaintenance.getKey().getSequenceNumber());
				break;
			}
			case EffectiveDatedMaintenance.COLUMN_NAME_SALES_TAX_SWITCH :{
				if(switchToBooleanConverter == null){
					switchToBooleanConverter = new SwitchToBooleanConverter();
				}
				effectiveVertexTaxCategory.setRetailTaxable(
						switchToBooleanConverter.convertToEntityAttribute(effectiveMaintenance.getTextValue()));
				effectiveVertexTaxCategory.setRetailTaxableSequenceNumber(effectiveMaintenance.getKey().getSequenceNumber());
				break;
			}
		}
	}

	/**
	 * This methods calls the product info repository to find all product masters that have the given retail link code.
	 *
	 * @param retailLinkCode Retail link code to look for.
	 * @return List of product masters that have the given retail link code.
	 */
	public List<ProductRetailLink> getAllProductRetailLinksByRetailLinkCode(Long retailLinkCode) {
		List<ProductMaster> productMasters = this.productInfoRepository.findByRetailLink(retailLinkCode);
		return this.convertProductMastersToProductRetailLinks(productMasters);
	}

	/**
	 * This method gets the original list of effective dated maintenance, calls a helper method to convert the given
	 * effective tax categories to effective dated maintenance, then finds the records that changed. After finding
	 * changes, send the changes to the repository to save.
	 *
	 * @param updatedEffectiveVertexCategories Updated list of effective vertex tax categories.
	 */
	@CoreTransactional
	public void updateVertexTaxCategoryEffectiveMaintenance(List<EffectiveVertexTaxCategory> updatedEffectiveVertexCategories) {
		if(!this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_RETAIL_TAX)){
			return;
		}
		List<EffectiveDatedMaintenance> originalList = this.effectiveDatedMaintenanceRepository.
				findByKeyTableNameAndKeyColumnNameInAndProductIdOrderByEffectiveDate(
						EffectiveDatedMaintenance.TABLE_NAME_GOODS_PRODUCT,
						Arrays.asList(
								EffectiveDatedMaintenance.COLUMN_NAME_SALES_TAX_SWITCH,
								EffectiveDatedMaintenance.COLUMN_NAME_VERTEX_TAX_CATEGORY_CODE
						),updatedEffectiveVertexCategories.get(0).getProductId());
		List<EffectiveDatedMaintenance> updatedList =
				this.convertEffectiveVertexTaxCategoriesToEffectiveDatedMaintenance(updatedEffectiveVertexCategories);
		this.updateEffectiveMaintenanceChanges(updatedList, originalList);
	}

	/**
	 * This method compares the updated list of effective dated maintenance to the current list of effective dated
	 * maintenance to find new records, and records that need to be deleted. After looking for changes, return the
	 * current list with the changes.
	 *
	 * @param updatedList Updated list of effective dated maintenance.
	 * @param originalList Current list of effective dated maintenance.
	 */
	private void updateEffectiveMaintenanceChanges(List<EffectiveDatedMaintenance> updatedList, List<EffectiveDatedMaintenance> originalList) {
		Long nextSequenceNumber = this.effectiveDatedMaintenanceRepository.getMaxKeySequenceNumber() + 1;

		List<EffectiveDatedMaintenance> removeList = new ArrayList<>();

		// remove all future effective dated maintenance from original list
		for(EffectiveDatedMaintenance effectiveDatedMaintenance : originalList) {
			if (effectiveDatedMaintenance.getEffectiveDate().isAfter(LocalDate.now())) {
				removeList.add(effectiveDatedMaintenance);
			}
		}
		originalList.removeAll(removeList);
		this.effectiveDatedMaintenanceRepository.delete(removeList);

		// add all updated list of future effective dated maintenance to original list
		for(EffectiveDatedMaintenance effectiveDatedMaintenance : updatedList){
			// if the effective date is before tomorrow, disregard it because this should be 1 record which showed
			// the current data from goods prod that the user cannot edit
			if(effectiveDatedMaintenance.getEffectiveDate().isBefore(LocalDate.now().plusDays(1))){
				continue;
			}
			effectiveDatedMaintenance.setCreateTimeStamp(LocalDateTime.now());
			effectiveDatedMaintenance.setCreateUserId(this.userInfo.getUserId());
			effectiveDatedMaintenance.setLastUpdateTimeStamp(LocalDateTime.now());
			effectiveDatedMaintenance.setLastUpdateUserId(this.userInfo.getUserId());
			effectiveDatedMaintenance.getKey().setSequenceNumber(nextSequenceNumber++);
			originalList.add(effectiveDatedMaintenance);
		}
		this.effectiveDatedMaintenanceRepository.save(originalList);
	}

	/**
	 * Converts a list of effective vertex tax categories to a list of effective dated maintenance.
	 *
	 * @param effectiveVertexCategories List of effective vertex tax categories to convert.
	 * @return List of effective dated maintenance.
	 */
	private List<EffectiveDatedMaintenance> convertEffectiveVertexTaxCategoriesToEffectiveDatedMaintenance(
			List<EffectiveVertexTaxCategory> effectiveVertexCategories) {
		List<EffectiveDatedMaintenance> toReturn = new ArrayList<>();
		for(EffectiveVertexTaxCategory effectiveVertexTaxCategory : effectiveVertexCategories){
			if(!DELETE_ACTION_CODE.equals(effectiveVertexTaxCategory.getActionCode())){
				toReturn.addAll(this.createEffectiveDatedMaintenanceFromEffectiveVertexTaxCategory(effectiveVertexTaxCategory));
			}
		}
		return toReturn;
	}

	/**
	 * Converts an effective vertex tax category into a list of effective dated maintenance.
	 *
	 * @param effectiveVertexTaxCategory Effective vertex tax category to convert.
	 * @return List of effective dated maintenance created from the effective vertex tax category.
	 */
	private List<EffectiveDatedMaintenance> createEffectiveDatedMaintenanceFromEffectiveVertexTaxCategory(EffectiveVertexTaxCategory effectiveVertexTaxCategory) {
		List<EffectiveDatedMaintenance> toReturn = new ArrayList<>();
		EffectiveDatedMaintenance salesTaxEffectiveMaintenance = new EffectiveDatedMaintenance();
		EffectiveDatedMaintenanceKey key = new EffectiveDatedMaintenanceKey();
		key.setColumnName(EffectiveDatedMaintenance.COLUMN_NAME_SALES_TAX_SWITCH);
		key.setTableName(EffectiveDatedMaintenance.TABLE_NAME_GOODS_PRODUCT);
		key.setSequenceNumber(effectiveVertexTaxCategory.getRetailTaxableSequenceNumber());
		salesTaxEffectiveMaintenance.setKey(key);
		salesTaxEffectiveMaintenance.setProductId(effectiveVertexTaxCategory.getProductId());
		salesTaxEffectiveMaintenance.setEffectiveDate(effectiveVertexTaxCategory.getEffectiveDate());
		if(switchToBooleanConverter == null){
			switchToBooleanConverter = new SwitchToBooleanConverter();
		}
		salesTaxEffectiveMaintenance.setTextValue(switchToBooleanConverter.convertToDatabaseColumn(
				effectiveVertexTaxCategory.getRetailTaxable()));
		toReturn.add(salesTaxEffectiveMaintenance);
		EffectiveDatedMaintenance vertexTaxCategoryEffectiveMaintenance = new EffectiveDatedMaintenance();
		key = new EffectiveDatedMaintenanceKey();
		key.setColumnName(EffectiveDatedMaintenance.COLUMN_NAME_VERTEX_TAX_CATEGORY_CODE);
		key.setTableName(EffectiveDatedMaintenance.TABLE_NAME_GOODS_PRODUCT);
		key.setSequenceNumber(effectiveVertexTaxCategory.getVertexTaxCategorySequenceNumber());
		vertexTaxCategoryEffectiveMaintenance.setKey(key);
		vertexTaxCategoryEffectiveMaintenance.setEffectiveDate(effectiveVertexTaxCategory.getEffectiveDate());
		vertexTaxCategoryEffectiveMaintenance.setProductId(effectiveVertexTaxCategory.getProductId());
		vertexTaxCategoryEffectiveMaintenance.setTextValue(effectiveVertexTaxCategory.getVertexTaxCategory().getDvrCode());
		toReturn.add(vertexTaxCategoryEffectiveMaintenance);
		return toReturn;
	}

	/**
	 * Gets all vertex tax category codes by sub commodity code.
	 *
	 * @param subCommodityCode Sub commodity code to search for.
	 * @return List of vertex tax categories matching the search.
	 */
	@CoreTransactional
	public List<String> getAllVertexTaxCategoryCodesBySubCommodityCode(Integer subCommodityCode) {
		return this.productInfoRepository.findDistinctVertexTaxCategoryBySubCommodityCode(subCommodityCode);
	}

	/**
	 * Gets products by vertex tax category by page and page size. If include counts, get a page of data. Else get a
	 * list of products.
	 *
	 * @param vertexTaxCategoryCode Vertex tax category code to look for.
	 * @param includeCount Whether to include counts in return.
	 * @param page Page to retrieve.
	 * @param pageSize Size of page to retrieve.
	 * @return Pageable result containing products that match the query.
	 */
	public PageableResult<ProductMaster> getProductMastersByVertexTaxCategoryCode(
			String vertexTaxCategoryCode, boolean includeCount, int page, int pageSize) {

		Pageable request = new PageRequest(page, pageSize);

		return includeCount ? this.getProductMastersByVertexTaxCategoryCodeWithCounts(vertexTaxCategoryCode.trim(), request) :
				this.getProductMastersByVertexTaxCategoryCodeWithoutCounts(vertexTaxCategoryCode.trim(), request);
	}

	/**
	 * Gets products by vertex tax category by a given request that includes count of all products that match the query.
	 *
	 * @param vertexTaxCategoryCode Vertex tax category code to look for.
	 * @param request Pageable request.
	 * @return Pageable result containing products that match the query including the total count.
	 */
	private PageableResult<ProductMaster> getProductMastersByVertexTaxCategoryCodeWithCounts(String vertexTaxCategoryCode, Pageable request) {
		Page<ProductMaster> data = this.productInfoRepositoryWithCounts.findByGoodsProductVertexTaxCategoryCodeOrderByProdId(vertexTaxCategoryCode, request);

		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Gets products by vertex tax category by a given request that does not include count of all products that match
	 * the query.
	 *
	 * @param vertexTaxCategoryCode Vertex tax category code to look for.
	 * @param request Pageable request.
	 * @return Pageable result containing products that match the query not including the total count.
	 */
	private PageableResult<ProductMaster> getProductMastersByVertexTaxCategoryCodeWithoutCounts(String vertexTaxCategoryCode, Pageable request) {
		List<ProductMaster> data = this.productInfoRepository.findByGoodsProductVertexTaxCategoryCodeOrderByProdId(vertexTaxCategoryCode, request);
		return new PageableResult<>(request.getPageNumber(), data);
	}
	/**
	 * This returns a list of shelf attributes audits based on the prodId.
	 * @param prodId way to uniquely ID the set of shelf attributes audits requested
	 * @return a list of all the changes made to an product's shelf attributes audits.
	 */
	public List<AuditRecord> getShelfAttributesAuditInformation(Long prodId) {
		return this.auditService.getShelfAttributesAuditInformation(prodId);
	}

	/**
	 * This returns a list of product info audits based on the prodId.
	 * @param prodId way to uniquely ID the set of product info audits requested
	 * @return a list of all the changes made to an product's info
	 */
	List<AuditRecord> getProductInfoAuditInformation(Long prodId) {
		return this.auditService.getProductInfoAuditInformation(prodId);
	}

	/**
	 * Calls excel export for product retail link.
	 * @param outputStream the output stream
	 * @param retailLinkCode the retail link code to get list of product retail link.
	 */
	void exportProductRetailLinksToCsv(ServletOutputStream outputStream, Long retailLinkCode) {
		// Export the header to the file
		try {
			outputStream.println(PRODUCT_RETAIL_LINKS_EXPORT_HEADER);
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		List<ProductMaster> productMasters = this.productInfoRepository.findByRetailLink(retailLinkCode);
		List<ProductRetailLink> productRetailLinkList =  this.convertProductMastersToProductRetailLinks(productMasters);
		this.streamTaskDetailsListPage(outputStream, productRetailLinkList);

	}

	/**
	 * This method takes in a list of product masters, and converts them into a list of product retail links.
	 *
	 * @param productMasters List of product masters to convert.
	 * @return List of product retail links.
	 */
	private List<ProductRetailLink> convertProductMastersToProductRetailLinks(List<ProductMaster> productMasters) {
		List<ProductRetailLink> toReturn = new ArrayList<>();
		for(ProductMaster productMaster : productMasters){
			if(productMaster.getProdItems() != null){
				for(ProdItem prodItem : productMaster.getProdItems()){
					if(prodItem.getItemMaster() != null && prodItem.getItemMaster().getPrimaryUpc() != null &&
							prodItem.getItemMaster().getPrimaryUpc().getAssociateUpcs() != null){
						for(AssociatedUpc associatedUpc : prodItem.getItemMaster().getPrimaryUpc().getAssociateUpcs()){
							if(associatedUpc.getSellingUnit() != null){
								toReturn.add(this.createProductRetailLink(productMaster, prodItem.getItemMaster(), associatedUpc.getSellingUnit()));
							}
						}
					}
				}
			}
		}
		return toReturn;
	}

	/**
	 * This method creates a product retail link given a product master, item master, and selling unit.
	 * From a:
	 * product master, set product id, product description, and sub commodity description;
	 * item master, set item id;
	 * selling unit, set upc, quantity, size, and unit of measure.
	 *
	 * @param productMaster Product master to use.
	 * @param itemMaster Item master to use.
	 * @param sellingUnit Selling unit to use.
	 * @return Product retail link created from given parameters.
	 */
	private ProductRetailLink createProductRetailLink(ProductMaster productMaster, ItemMaster itemMaster, SellingUnit sellingUnit) {
		ProductRetailLink productRetailLink = new ProductRetailLink();
		productRetailLink.setProductId(productMaster.getProdId());
		productRetailLink.setProductDescription(productMaster.getDescription());
		productRetailLink.setSubCommodityDescription(
				productMaster.getSubCommodity() != null ?
						productMaster.getSubCommodity().getDisplayName() : StringUtils.EMPTY);
		productRetailLink.setItemId(itemMaster.getKey().getItemCode());
		productRetailLink.setUpc(sellingUnit.getUpc());
		productRetailLink.setQuantity(sellingUnit.getQuantity());
		productRetailLink.setSize(sellingUnit.getTagSize());
		productRetailLink.setUnitOfMeasure(
				sellingUnit.getRetailUnitOfMeasure() != null ?
						sellingUnit.getRetailUnitOfMeasure().getDescription() : StringUtils.EMPTY);
		return productRetailLink;
	}

	/**
	 * Stream the task details to a CSV output stream
	 * @param outputStream the output stream
	 * @param productRetailLinkList the list of productRetailLink to output
	 */
	private void streamTaskDetailsListPage(ServletOutputStream outputStream, List<ProductRetailLink> productRetailLinkList) {
		StringBuilder csv = new StringBuilder();
		try {
			// UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Last Modified, Last Modified By";
			productRetailLinkList.forEach(productRetailLink -> {
				StringBuilder csvLine = new StringBuilder();
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getProductId()));
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getItemId()));
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getUpc()));
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getProductDescription()));
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getSize()));
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getQuantity()));
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getUnitOfMeasure()));
				csvLine.append(String.format(TEXT_EXPORT_FORMAT, productRetailLink.getSubCommodityDescription()));
				csvLine.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT));
				csv.append(csvLine);
			});
			outputStream.println(csv.toString());
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Validate the new sub commodity.
	 * If the new sub commodity's department is not the same with old sub commodity's department then throw error message:
	 * Stop! Hierarchy changes requires more review.
	 * If the new sub commodity's item class is not the same old sub commodity's item class and product is existing in the warehouse the throw
	 * This product has warehouse items. Please update the sub commodity and vendor in OMI.
	 *
	 * @param originalProductMaster original product master from database
	 * @param productInfoParameters product info parameter from UI
	 */
	private void validateSubCommodity(ProductMaster originalProductMaster, ProductInfoParameters productInfoParameters) {
		// if existing any warehouse item in product, then validate the new sub commodity.
		if (validateProductHasWarehouseItems(originalProductMaster.getProdItems())) {
			//Verify that the new department is same with department of product. If it's difference, then show error
			if (isSameDepartmentOfProduct(productInfoParameters)) {
				// if user change sub commodity that its class is difference, then it show error
				if (!isSameItemClassOfProduct(productInfoParameters)) {
					throw new IllegalArgumentException(PRODUCT_HAS_WAREHOUSE_ITEMS_ERROR_MESSAGE);
				}
			} else {
				throw new IllegalArgumentException(SUB_COMMODITY_NOT_BELONG_TO_DEPARTMENT_ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Check product is existing warehouse items or not.
	 *
	 * @param prodItems the list of product item.
	 * @return true product is existing in warehouse or false it's not existing.
	 */
	public boolean validateProductHasWarehouseItems(List<ProdItem> prodItems) {
		for (ProdItem prodItem : prodItems) {
			if (prodItem.getKey().getItemType().equals(ItemMaster.ITEM_TYPE_CODE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check the item class of new sub commodity is the same or not the same with the item class of old sub commodity
	 *
	 * @param productInfoParameters the productInfoParameters
	 * @return true if item class of new sub commodity is the same with the item class of old sub commodity or false.
	 */
	public boolean isSameItemClassOfProduct(ProductInfoParameters productInfoParameters) {
		//Find sub-commodity that user want to change
		SubCommodity subCommodity = subCommodityRepository.findByKeySubCommodityCode(productInfoParameters.getProductMaster().getSubCommodityCode());
		//Find new class that new sub-commodity tied to
		ItemClass itemClass = itemClassRepository.findOne(subCommodity.getKey().getClassCode());
		//Compare class code of new sub-commodity and current class code of product
		return itemClass != null && itemClass.getItemClassCode() == productInfoParameters.getProductMaster().getClassCode();
	}

	/**
	 * Check the department of new sub commodity is the same or not the same with the department of old sub commodity
	 *
	 * @param productInfoParameters the productInfoParameters
	 * @return true if department of new sub commodity is the same with the department of old sub commodity or false.
	 */
	public boolean isSameDepartmentOfProduct(ProductInfoParameters productInfoParameters) {
		ItemClass itemClass = itemClassRepository.findBySubCommodity(productInfoParameters.getProductMaster().getSubCommodityCode());
		if (itemClass != null
				&& itemClass.getDepartmentId() == Integer.parseInt(productInfoParameters.getProductMaster().getDepartmentCode())
				&& itemClass.getSubDepartmentId().equals(productInfoParameters.getProductMaster().getSubDepartmentCode())) {
			return true;
		}
		return false;
	}
}
