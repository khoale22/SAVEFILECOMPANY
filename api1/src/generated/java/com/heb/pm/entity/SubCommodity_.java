package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SubCommodity.class)
public abstract class SubCommodity_ {

	public static volatile SingularAttribute<SubCommodity, String> imsSubCommodityCode;
	public static volatile ListAttribute<SubCommodity, SubCommodityStateWarning> stateWarningList;
	public static volatile SingularAttribute<SubCommodity, Boolean> taxEligible;
	public static volatile SingularAttribute<SubCommodity, ClassCommodity> commodityMaster;
	public static volatile SingularAttribute<SubCommodity, Integer> imsClassCode;
	public static volatile SingularAttribute<SubCommodity, Integer> imsCommodityCode;
	public static volatile SingularAttribute<SubCommodity, String> nonTaxCategoryCode;
	public static volatile SingularAttribute<SubCommodity, Integer> subCommodityCode;
	public static volatile SingularAttribute<SubCommodity, ProductCategory> productCategory;
	public static volatile ListAttribute<SubCommodity, ProductPreferredUnitOfMeasure> productPreferredUnitOfMeasureList;
	public static volatile SingularAttribute<SubCommodity, Integer> productCategoryId;
	public static volatile SingularAttribute<SubCommodity, Character> subCommodityActive;
	public static volatile SingularAttribute<SubCommodity, Double> lowMarginPercent;
	public static volatile SingularAttribute<SubCommodity, String> name;
	public static volatile SingularAttribute<SubCommodity, Boolean> foodStampEligible;
	public static volatile SingularAttribute<SubCommodity, String> taxCategoryCode;
	public static volatile SingularAttribute<SubCommodity, Double> highMarginPercent;
	public static volatile SingularAttribute<SubCommodity, SubCommodityKey> key;

}

