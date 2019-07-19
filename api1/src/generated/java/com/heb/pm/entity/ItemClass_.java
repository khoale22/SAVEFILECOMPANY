package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ItemClass.class)
public abstract class ItemClass_ {

	public static volatile SingularAttribute<ItemClass, String> subDepartmentId;
	public static volatile SingularAttribute<ItemClass, Integer> beginAdLeadTime;
	public static volatile SingularAttribute<ItemClass, String> priceBulletin;
	public static volatile SingularAttribute<ItemClass, Integer> departmentId;
	public static volatile ListAttribute<ItemClass, ClassCommodity> commodityList;
	public static volatile SingularAttribute<ItemClass, Integer> classDirectorId;
	public static volatile SingularAttribute<ItemClass, String> itemClassDescription;
	public static volatile SingularAttribute<ItemClass, Integer> scanMaintenanceGroup;
	public static volatile SingularAttribute<ItemClass, Double> lowGrossMargin;
	public static volatile SingularAttribute<ItemClass, ProductTemperatureControl> temperatureControl;
	public static volatile SingularAttribute<ItemClass, Boolean> billCostEligible;
	public static volatile SingularAttribute<ItemClass, Integer> endAdLeadTime;
	public static volatile SingularAttribute<ItemClass, MerchantType> merchantType;
	public static volatile SingularAttribute<ItemClass, Integer> priceChangePercent;
	public static volatile SingularAttribute<ItemClass, Integer> itemClassCode;
	public static volatile SingularAttribute<ItemClass, String> billAtAd;
	public static volatile SingularAttribute<ItemClass, Integer> genericClass;
	public static volatile SingularAttribute<ItemClass, Double> classVariance;
	public static volatile SingularAttribute<ItemClass, String> temperatureControlCode;
	public static volatile SingularAttribute<ItemClass, String> merchantTypeCode;
	public static volatile SingularAttribute<ItemClass, Integer> scanDepartment;
	public static volatile SingularAttribute<ItemClass, Integer> mixMatchGroup;
	public static volatile SingularAttribute<ItemClass, Integer> dataCheckerDepartment;
	public static volatile SingularAttribute<ItemClass, Double> adPriceChangePercent;
	public static volatile SingularAttribute<ItemClass, Double> hiGrossMargin;

}

