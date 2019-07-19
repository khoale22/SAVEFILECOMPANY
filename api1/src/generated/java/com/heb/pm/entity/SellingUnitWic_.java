package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SellingUnitWic.class)
public abstract class SellingUnitWic_ {

	public static volatile SingularAttribute<SellingUnitWic, WicSubCategory> wicSubCategory;
	public static volatile SingularAttribute<SellingUnitWic, String> wicDescription;
	public static volatile SingularAttribute<SellingUnitWic, Boolean> lebSwitch;
	public static volatile SingularAttribute<SellingUnitWic, Double> wicPackageSize;
	public static volatile SingularAttribute<SellingUnitWic, WicCategory> wicCategory;
	public static volatile SingularAttribute<SellingUnitWic, SellingUnitWicKey> key;
	public static volatile SingularAttribute<SellingUnitWic, SellingUnit> sellingUnit;

}

