package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductMaster.class)
public abstract class ProductMaster_ {

	public static volatile ListAttribute<ProductMaster, ProductCubiscan> productCubiscan;
	public static volatile SingularAttribute<ProductMaster, SubCommodity> subCommodity;
	public static volatile SingularAttribute<ProductMaster, Boolean> showCalories;
	public static volatile SingularAttribute<ProductMaster, String> departmentCode;
	public static volatile SingularAttribute<ProductMaster, ProductBrand> productBrand;
	public static volatile SingularAttribute<ProductMaster, LocalDateTime> createdDateTime;
	public static volatile SingularAttribute<ProductMaster, String> quantityRequiredFlag;
	public static volatile SingularAttribute<ProductMaster, Integer> commodityCode;
	public static volatile SingularAttribute<ProductMaster, ProductTemperatureControl> temperatureControl;
	public static volatile ListAttribute<ProductMaster, TierPricing> tierPricings;
	public static volatile SingularAttribute<ProductMaster, LocalDate> subscriptionStartDate;
	public static volatile SingularAttribute<ProductMaster, GoodsProduct> goodsProduct;
	public static volatile SingularAttribute<ProductMaster, String> createUid;
	public static volatile SingularAttribute<ProductMaster, String> bdmCode;
	public static volatile ListAttribute<ProductMaster, DistributionFilter> distributionFilters;
	public static volatile ListAttribute<ProductMaster, GenericEntity> genericEntities;
	public static volatile SingularAttribute<ProductMaster, String> tagItemType;
	public static volatile ListAttribute<ProductMaster, ProductOnline> productOnlines;
	public static volatile SingularAttribute<ProductMaster, Long> tagItemCode;
	public static volatile SingularAttribute<ProductMaster, Long> retailLink;
	public static volatile SingularAttribute<ProductMaster, LocalDate> subscriptionEndDate;
	public static volatile SingularAttribute<ProductMaster, String> subDepartmentCode;
	public static volatile ListAttribute<ProductMaster, ProductRestrictions> restrictions;
	public static volatile SingularAttribute<ProductMaster, Integer> pssDepartmentOne;
	public static volatile SingularAttribute<ProductMaster, Integer> subCommodityCode;
	public static volatile ListAttribute<ProductMaster, SellingUnit> sellingUnits;
	public static volatile ListAttribute<ProductMaster, MasterDataExtensionAttribute> masterDataExtensionAttributes;
	public static volatile ListAttribute<ProductMaster, ProductAttributeOverwrite> productAttributeOverwrites;
	public static volatile SingularAttribute<ProductMaster, String> prodTypCd;
	public static volatile ListAttribute<ProductMaster, MasterDataExtensionAttribute> exportUpcMasterDataExtensionAttributes;
	public static volatile SingularAttribute<ProductMaster, String> salesRestrictCode;
	public static volatile SingularAttribute<ProductMaster, String> taxQualifyingCode;
	public static volatile SingularAttribute<ProductMaster, LocalDateTime> lastUpdateTime;
	public static volatile ListAttribute<ProductMaster, ProdItem> prodItems;
	public static volatile SingularAttribute<ProductMaster, Boolean> giftMessageSwitch;
	public static volatile SingularAttribute<ProductMaster, String> description;
	public static volatile SingularAttribute<ProductMaster, Long> prodId;
	public static volatile SingularAttribute<ProductMaster, SubDepartment> subDepartment;
	public static volatile SingularAttribute<ProductMaster, Boolean> subscription;
	public static volatile ListAttribute<ProductMaster, ProductFullfilmentChanel> productFullfilmentChanels;
	public static volatile ListAttribute<ProductMaster, ProductDescription> productDescriptions;
	public static volatile SingularAttribute<ProductMaster, Long> productPrimaryScanCodeId;
	public static volatile ListAttribute<ProductMaster, ProductMarketingClaim> productMarketingClaims;
	public static volatile SingularAttribute<ProductMaster, LocalDateTime> createDate;
	public static volatile SingularAttribute<ProductMaster, Integer> classCode;
	public static volatile SingularAttribute<ProductMaster, String> productSizeText;
	public static volatile SingularAttribute<ProductMaster, String> lastUpdateUserId;
	public static volatile SingularAttribute<ProductMaster, ClassCommodity> classCommodity;
	public static volatile SingularAttribute<ProductMaster, ExportScaleUpc> exportScaleUpc;
	public static volatile ListAttribute<ProductMaster, ProductItemVariant> lstProductItemVariant;
	public static volatile SingularAttribute<ProductMaster, String> spanishDescription;
	public static volatile SingularAttribute<ProductMaster, SellingUnit> productPrimarySellingUnit;
	public static volatile ListAttribute<ProductMaster, ProductScanCodeExtent> productScanCodeExtents;
	public static volatile ListAttribute<ProductMaster, MasterDataExtensionAttribute> exportMasterDataExtensionAttributes;
	public static volatile SingularAttribute<ProductMaster, ItemClass> itemClass;
	public static volatile SingularAttribute<ProductMaster, Boolean> priceRequiredFlag;
	public static volatile SingularAttribute<ProductMaster, Long> prodBrandId;
	public static volatile SingularAttribute<ProductMaster, String> displayCreatedName;
	public static volatile ListAttribute<ProductMaster, CandidateWorkRequest> candidateWorkRequests;

}

