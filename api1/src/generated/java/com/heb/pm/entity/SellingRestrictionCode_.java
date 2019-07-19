package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SellingRestrictionCode.class)
public abstract class SellingRestrictionCode_ {

	public static volatile SingularAttribute<SellingRestrictionCode, Integer> minimumRestrictedAge;
	public static volatile SingularAttribute<SellingRestrictionCode, String> restrictionDescription;
	public static volatile SingularAttribute<SellingRestrictionCode, String> restrictionCode;
	public static volatile SingularAttribute<SellingRestrictionCode, String> restrictionAbbreviation;
	public static volatile SingularAttribute<SellingRestrictionCode, String> restrictionGroupCode;
	public static volatile SingularAttribute<SellingRestrictionCode, Double> restrictedQuantity;
	public static volatile SingularAttribute<SellingRestrictionCode, Boolean> dateTimeRestriction;
	public static volatile SingularAttribute<SellingRestrictionCode, SellingRestriction> sellingRestriction;

}

