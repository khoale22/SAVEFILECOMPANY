package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ItemMaster.class)
public abstract class ItemMaster_ {

	public static volatile SingularAttribute<ItemMaster, MerchandiseType> merchandiseTypeTwo;
	public static volatile SingularAttribute<ItemMaster, LocalDate> addedDate;
	public static volatile SingularAttribute<ItemMaster, MerchandiseType> merchandiseTypeThree;
	public static volatile SingularAttribute<ItemMaster, String> discontinueReasonCode;
	public static volatile SingularAttribute<ItemMaster, String> subDepartmentIdTwo;
	public static volatile SingularAttribute<ItemMaster, Integer> pssDepartmentCodeOne;
	public static volatile SingularAttribute<ItemMaster, String> criticalItemIndicator;
	public static volatile SingularAttribute<ItemMaster, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<ItemMaster, Character> merchandiseTypeCodeFour;
	public static volatile SingularAttribute<ItemMaster, String> mexicanBorderAuthorizationCode;
	public static volatile SingularAttribute<ItemMaster, LocalDateTime> createdDateTime;
	public static volatile SingularAttribute<ItemMaster, Boolean> catchWeight;
	public static volatile SingularAttribute<ItemMaster, Integer> maxShipQty;
	public static volatile SingularAttribute<ItemMaster, String> itemSizeUom;
	public static volatile SingularAttribute<ItemMaster, LocalDate> discontinueDate;
	public static volatile SingularAttribute<ItemMaster, String> variantCode;
	public static volatile SingularAttribute<ItemMaster, Boolean> displayReadyUnit;
	public static volatile SingularAttribute<ItemMaster, Integer> guaranteeToStoreDays;
	public static volatile SingularAttribute<ItemMaster, Integer> pssDepartmentCodeThree;
	public static volatile SingularAttribute<ItemMaster, Integer> commodityCode;
	public static volatile SingularAttribute<ItemMaster, String> itemSize;
	public static volatile SingularAttribute<ItemMaster, Integer> departmentIdOne;
	public static volatile SingularAttribute<ItemMaster, Integer> pssDepartmentCodeFour;
	public static volatile SingularAttribute<ItemMaster, String> subDepartmentIdOne;
	public static volatile SingularAttribute<ItemMaster, Boolean> newItem;
	public static volatile SingularAttribute<ItemMaster, String> oneTouchTypeCode;
	public static volatile ListAttribute<ItemMaster, RelatedItem> relatedItems;
	public static volatile SingularAttribute<ItemMaster, Boolean> cattle;
	public static volatile SingularAttribute<ItemMaster, Long> pack;
	public static volatile SingularAttribute<ItemMaster, Integer> subCommodityCode;
	public static volatile SingularAttribute<ItemMaster, Integer> warehouseReactionDays;
	public static volatile SingularAttribute<ItemMaster, OneTouchType> oneTouchType;
	public static volatile SingularAttribute<ItemMaster, String> subDepartmentIdFour;
	public static volatile SingularAttribute<ItemMaster, MerchandiseType> merchandiseTypeFour;
	public static volatile SingularAttribute<ItemMaster, String> abcAuthorizationCode;
	public static volatile SingularAttribute<ItemMaster, Integer> onReceiptLifeDays;
	public static volatile SingularAttribute<ItemMaster, String> typeOfDRU;
	public static volatile SingularAttribute<ItemMaster, Long> rowsHigh;
	public static volatile SingularAttribute<ItemMaster, Integer> departmentIdThree;
	public static volatile ListAttribute<ItemMaster, ItemNotDeleted> itemNotDeleted;
	public static volatile ListAttribute<ItemMaster, ProdItem> prodItems;
	public static volatile SingularAttribute<ItemMaster, ItemType> itemType;
	public static volatile SingularAttribute<ItemMaster, PrimaryUpc> primaryUpc;
	public static volatile ListAttribute<ItemMaster, SearchCriteria> searchCriteria;
	public static volatile SingularAttribute<ItemMaster, String> dsv;
	public static volatile SingularAttribute<ItemMaster, String> description;
	public static volatile SingularAttribute<ItemMaster, Long> caseUpc;
	public static volatile SingularAttribute<ItemMaster, Boolean> alwaysSubWhenOut;
	public static volatile SingularAttribute<ItemMaster, Character> merchandiseTypeCodeOne;
	public static volatile SingularAttribute<ItemMaster, String> addedUsrId;
	public static volatile SingularAttribute<ItemMaster, Boolean> variableWeight;
	public static volatile SingularAttribute<ItemMaster, Integer> departmentIdFour;
	public static volatile SingularAttribute<ItemMaster, Character> merchandiseTypeCodeTwo;
	public static volatile SingularAttribute<ItemMaster, Long> orderingUpc;
	public static volatile SingularAttribute<ItemMaster, Character> merchandiseTypeCodeThree;
	public static volatile SingularAttribute<ItemMaster, ItemMasterKey> key;
	public static volatile SingularAttribute<ItemMaster, Short> classCode;
	public static volatile SingularAttribute<ItemMaster, Long> rowsFacing;
	public static volatile SingularAttribute<ItemMaster, Long> orientation;
	public static volatile SingularAttribute<ItemMaster, DiscontinueReason> discontinueReason;
	public static volatile SingularAttribute<ItemMaster, String> discontinuedByUID;
	public static volatile SingularAttribute<ItemMaster, String> lastUpdateUserId;
	public static volatile SingularAttribute<ItemMaster, String> itemTypeCode;
	public static volatile SingularAttribute<ItemMaster, MerchandiseType> merchandiseTypeOne;
	public static volatile SingularAttribute<ItemMaster, String> subDepartmentIdThree;
	public static volatile ListAttribute<ItemMaster, WarehouseLocationItem> warehouseLocationItems;
	public static volatile ListAttribute<ItemMaster, WarehouseLocationItemExtendedAttributes> warehouseLocationItemExtendedAttributes;
	public static volatile ListAttribute<ItemMaster, VendorLocationItem> vendorLocationItems;
	public static volatile SingularAttribute<ItemMaster, Integer> pssDepartmentCodeTwo;
	public static volatile SingularAttribute<ItemMaster, Double> itemSizeQuantity;
	public static volatile SingularAttribute<ItemMaster, Boolean> mrt;
	public static volatile SingularAttribute<ItemMaster, Integer> abcItemCategoryNo;
	public static volatile SingularAttribute<ItemMaster, String> mexicoAuthorizationCode;
	public static volatile SingularAttribute<ItemMaster, Long> rowsDeep;
	public static volatile SingularAttribute<ItemMaster, Integer> usdaNumber;
	public static volatile SingularAttribute<ItemMaster, Integer> departmentIdTwo;
	public static volatile SingularAttribute<ItemMaster, String> displayCreatedName;

}

