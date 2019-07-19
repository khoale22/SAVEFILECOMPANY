package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SellingUnit.class)
public abstract class SellingUnit_ {

	public static volatile SingularAttribute<SellingUnit, RetailUnitOfMeasure> retailUnitOfMeasure;
	public static volatile SingularAttribute<SellingUnit, Boolean> primaryUpc;
	public static volatile ListAttribute<SellingUnit, SearchCriteria> searchCriteria;
	public static volatile SingularAttribute<SellingUnit, Long> prodId;
	public static volatile SingularAttribute<SellingUnit, String> tagSize;
	public static volatile SingularAttribute<SellingUnit, Double> pseGramWeight;
	public static volatile ListAttribute<SellingUnit, NutritionalClaims> nutritionalClaims;
	public static volatile SingularAttribute<SellingUnit, Boolean> dsdDeptOverideSwitch;
	public static volatile SingularAttribute<SellingUnit, LocalDate> discontinueDate;
	public static volatile SingularAttribute<SellingUnit, ProductSubBrand> subBrand;
	public static volatile SingularAttribute<SellingUnit, String> retailUnitOfMeasureCode;
	public static volatile SingularAttribute<SellingUnit, String> scanTypeCode;
	public static volatile SingularAttribute<SellingUnit, LocalDate> lastScanDate;
	public static volatile SingularAttribute<SellingUnit, Double> retailWidth;
	public static volatile SingularAttribute<SellingUnit, Date> lastUpdatedOn;
	public static volatile SingularAttribute<SellingUnit, ProductShippingHandling> productShippingHandling;
	public static volatile SingularAttribute<SellingUnit, ProductMaster> productMaster;
	public static volatile SingularAttribute<SellingUnit, Boolean> processedByScanMaintenance;
	public static volatile SingularAttribute<SellingUnit, GoodsProduct> goodsProduct;
	public static volatile SingularAttribute<SellingUnit, String> lastUpdatedBy;
	public static volatile SingularAttribute<SellingUnit, Double> quantity;
	public static volatile SingularAttribute<SellingUnit, Double> quantity2;
	public static volatile ListAttribute<SellingUnit, ProductNutrient> productNutrients;
	public static volatile SingularAttribute<SellingUnit, Double> retailWeight;
	public static volatile SingularAttribute<SellingUnit, String> tagSizeDescription;
	public static volatile SingularAttribute<SellingUnit, Long> wicApprovedProductListId;
	public static volatile SingularAttribute<SellingUnit, Long> upc;
	public static volatile SingularAttribute<SellingUnit, Double> retailHeight;
	public static volatile SingularAttribute<SellingUnit, Boolean> bonusSwitch;
	public static volatile SingularAttribute<SellingUnit, Double> retailLength;
	public static volatile SingularAttribute<SellingUnit, LocalDate> firstScanDate;
	public static volatile SingularAttribute<SellingUnit, LocalDateTime> createTime;
	public static volatile SingularAttribute<SellingUnit, String> createId;
	public static volatile SingularAttribute<SellingUnit, RetailUnitOfMeasure> retailUnitOfMeasure2;
	public static volatile ListAttribute<SellingUnit, ProductPkVariation> productPkVariations;
	public static volatile ListAttribute<SellingUnit, ProductScanImageURIAudit> productScanImageURIAudits;
	public static volatile SingularAttribute<SellingUnit, Boolean> dsdDeleteSwitch;
	public static volatile SingularAttribute<SellingUnit, Boolean> testScanned;

}

