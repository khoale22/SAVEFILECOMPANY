package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CostOwner.class)
public abstract class CostOwner_ {

	public static volatile SingularAttribute<CostOwner, Integer> topToTopId;
	public static volatile SingularAttribute<CostOwner, String> costOwnerName;
	public static volatile ListAttribute<CostOwner, ProductBrandCostOwner> productBrandCostOwners;
	public static volatile SingularAttribute<CostOwner, Integer> costOwnerId;
	public static volatile SingularAttribute<CostOwner, TopToTop> topToTop;

}

