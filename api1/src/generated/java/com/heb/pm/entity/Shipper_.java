package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Shipper.class)
public abstract class Shipper_ {

	public static volatile SingularAttribute<Shipper, PrimaryUpc> realUpc;
	public static volatile SingularAttribute<Shipper, PrimaryUpc> primaryUpc;
	public static volatile SingularAttribute<Shipper, Long> shipperQuantity;
	public static volatile SingularAttribute<Shipper, Character> shipperTypeCode;
	public static volatile SingularAttribute<Shipper, ShipperKey> key;
	public static volatile SingularAttribute<Shipper, SellingUnit> sellingUnit;

}

