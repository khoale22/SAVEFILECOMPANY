package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RelatedItem.class)
public abstract class RelatedItem_ {

	public static volatile SingularAttribute<RelatedItem, String> relatedItemKeyType;
	public static volatile SingularAttribute<RelatedItem, String> itemRelationshipType;
	public static volatile SingularAttribute<RelatedItem, ItemMaster> itemMasterByRelatedItemCode;
	public static volatile SingularAttribute<RelatedItem, RelatedItemKey> key;

}

