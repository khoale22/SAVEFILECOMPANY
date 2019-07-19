package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductMasterAudit.class)
public abstract class ProductMasterAudit_ {

	public static volatile SingularAttribute<ProductMasterAudit, SubCommodity> subCommodity;
	public static volatile SingularAttribute<ProductMasterAudit, Boolean> giftMessageSwitch;
	public static volatile SingularAttribute<ProductMasterAudit, Boolean> showCalories;
	public static volatile SingularAttribute<ProductMasterAudit, String> departmentCode;
	public static volatile SingularAttribute<ProductMasterAudit, ProductBrand> productBrand;
	public static volatile SingularAttribute<ProductMasterAudit, String> description;
	public static volatile SingularAttribute<ProductMasterAudit, SubDepartment> subDepartment;
	public static volatile SingularAttribute<ProductMasterAudit, Boolean> subscription;
	public static volatile SingularAttribute<ProductMasterAudit, String> quantityRequiredFlag;
	public static volatile SingularAttribute<ProductMasterAudit, Long> productPrimaryScanCodeId;
	public static volatile ListAttribute<ProductMasterAudit, ProductMarketingClaim> productMarketingClaims;
	public static volatile SingularAttribute<ProductMasterAudit, Integer> commodityCode;
	public static volatile SingularAttribute<ProductMasterAudit, String> action;
	public static volatile SingularAttribute<ProductMasterAudit, ProductTemperatureControl> temperatureControl;
	public static volatile ListAttribute<ProductMasterAudit, TierPricing> tierPricings;
	public static volatile SingularAttribute<ProductMasterAudit, LocalDate> subscriptionStartDate;
	public static volatile SingularAttribute<ProductMasterAudit, GoodsProduct> goodsProduct;
	public static volatile SingularAttribute<ProductMasterAudit, ProductMasterAuditKey> key;
	public static volatile SingularAttribute<ProductMasterAudit, String> bdmCode;
	public static volatile ListAttribute<ProductMasterAudit, DistributionFilter> distributionFilters;
	public static volatile SingularAttribute<ProductMasterAudit, LocalDate> createDate;
	public static volatile SingularAttribute<ProductMasterAudit, String> tagItemType;
	public static volatile SingularAttribute<ProductMasterAudit, Integer> classCode;
	public static volatile SingularAttribute<ProductMasterAudit, Long> tagItemCode;
	public static volatile SingularAttribute<ProductMasterAudit, String> productSizeText;
	public static volatile SingularAttribute<ProductMasterAudit, Long> retailLink;
	public static volatile SingularAttribute<ProductMasterAudit, String> lastUpdateUserId;
	public static volatile SingularAttribute<ProductMasterAudit, ClassCommodity> classCommodity;
	public static volatile SingularAttribute<ProductMasterAudit, LocalDate> subscriptionEndDate;
	public static volatile SingularAttribute<ProductMasterAudit, String> subDepartmentCode;
	public static volatile ListAttribute<ProductMasterAudit, ProductRestrictions> restrictions;
	public static volatile SingularAttribute<ProductMasterAudit, Bdm> bdm;
	public static volatile SingularAttribute<ProductMasterAudit, Integer> pssDepartmentOne;
	public static volatile SingularAttribute<ProductMasterAudit, String> spanishDescription;
	public static volatile SingularAttribute<ProductMasterAudit, SellingUnit> productPrimarySellingUnit;
	public static volatile SingularAttribute<ProductMasterAudit, Integer> subCommodityCode;
	public static volatile ListAttribute<ProductMasterAudit, MasterDataExtensionAttribute> masterDataExtensionAttributes;
	public static volatile SingularAttribute<ProductMasterAudit, String> prodTypCd;
	public static volatile SingularAttribute<ProductMasterAudit, String> createdBy;
	public static volatile SingularAttribute<ProductMasterAudit, ItemClass> itemClass;
	public static volatile SingularAttribute<ProductMasterAudit, Boolean> priceRequiredFlag;
	public static volatile SingularAttribute<ProductMasterAudit, Long> prodBrandId;
	public static volatile SingularAttribute<ProductMasterAudit, String> salesRestrictCode;
	public static volatile SingularAttribute<ProductMasterAudit, String> taxQualifyingCode;
	public static volatile SingularAttribute<ProductMasterAudit, LocalDateTime> lastUpdateTime;

}

