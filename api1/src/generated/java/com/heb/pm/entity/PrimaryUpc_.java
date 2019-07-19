package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PrimaryUpc.class)
public abstract class PrimaryUpc_ {

	public static volatile ListAttribute<PrimaryUpc, Shipper> shipper;
	public static volatile ListAttribute<PrimaryUpc, Shipper> productShipper;
	public static volatile ListAttribute<PrimaryUpc, AssociatedUpc> associateUpcs;
	public static volatile SingularAttribute<PrimaryUpc, Long> itemCode;
	public static volatile SingularAttribute<PrimaryUpc, Long> upc;
	public static volatile ListAttribute<PrimaryUpc, ItemMaster> itemMasters;

}

