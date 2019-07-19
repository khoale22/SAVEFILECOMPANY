package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GenericEntityRelationship.class)
public abstract class GenericEntityRelationship_ {

	public static volatile SingularAttribute<GenericEntityRelationship, String> createUserId;
	public static volatile ListAttribute<GenericEntityRelationship, GenericEntityRelationship> childRelationships;
	public static volatile ListAttribute<GenericEntityRelationship, GenericEntityRelationship> parentRelationships;
	public static volatile SingularAttribute<GenericEntityRelationship, GenericEntityDescription> parentDescription;
	public static volatile SingularAttribute<GenericEntityRelationship, String> lastUpdateUserId;
	public static volatile SingularAttribute<GenericEntityRelationship, Boolean> display;
	public static volatile SingularAttribute<GenericEntityRelationship, Boolean> active;
	public static volatile SingularAttribute<GenericEntityRelationship, String> hierarchyContext;
	public static volatile SingularAttribute<GenericEntityRelationship, Boolean> defaultParent;
	public static volatile SingularAttribute<GenericEntityRelationship, Long> parentEntityId;
	public static volatile SingularAttribute<GenericEntityRelationship, GenericEntityDescription> childDescription;
	public static volatile SingularAttribute<GenericEntityRelationship, GenericEntity> genericParentEntity;
	public static volatile SingularAttribute<GenericEntityRelationship, Long> sequence;
	public static volatile SingularAttribute<GenericEntityRelationship, Long> childEntityId;
	public static volatile SingularAttribute<GenericEntityRelationship, GenericEntity> genericChildEntity;
	public static volatile SingularAttribute<GenericEntityRelationship, GenericEntityRelationshipKey> key;
	public static volatile SingularAttribute<GenericEntityRelationship, LocalDate> effectiveDate;
	public static volatile SingularAttribute<GenericEntityRelationship, LocalDate> expirationDate;
	public static volatile SingularAttribute<GenericEntityRelationship, Integer> countOfProductChildren;

}

