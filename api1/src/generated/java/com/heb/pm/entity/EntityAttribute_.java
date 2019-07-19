package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EntityAttribute.class)
public abstract class EntityAttribute_ {

	public static volatile SingularAttribute<EntityAttribute, Long> sequenceNumber;
	public static volatile SingularAttribute<EntityAttribute, String> createUserId;
	public static volatile SingularAttribute<EntityAttribute, String> lastUpdateUserId;
	public static volatile SingularAttribute<EntityAttribute, String> entityAttributeFieldName;
	public static volatile SingularAttribute<EntityAttribute, Attribute> attribute;
	public static volatile SingularAttribute<EntityAttribute, Boolean> baseAttribute;
	public static volatile SingularAttribute<EntityAttribute, EntityAttributeKey> key;
	public static volatile SingularAttribute<EntityAttribute, Boolean> required;

}

