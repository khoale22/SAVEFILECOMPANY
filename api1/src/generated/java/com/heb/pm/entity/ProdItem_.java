package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProdItem.class)
public abstract class ProdItem_ {

	public static volatile SingularAttribute<ProdItem, ItemMaster> itemMaster;
	public static volatile SingularAttribute<ProdItem, Long> productId;
	public static volatile ListAttribute<ProdItem, SearchCriteria> searchCriteria;
	public static volatile SingularAttribute<ProdItem, Long> itemCode;
	public static volatile SingularAttribute<ProdItem, ProductMaster> productMaster;
	public static volatile SingularAttribute<ProdItem, Integer> productCount;
	public static volatile SingularAttribute<ProdItem, ProdItemKey> key;

}

