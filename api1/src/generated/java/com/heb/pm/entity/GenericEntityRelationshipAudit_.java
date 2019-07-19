package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GenericEntityRelationshipAudit.class)
public abstract class GenericEntityRelationshipAudit_ {

	public static volatile SingularAttribute<GenericEntityRelationshipAudit, GenericEntity> genericChildEntity;
	public static volatile SingularAttribute<GenericEntityRelationshipAudit, GenericEntityDescription> parentDescription;
	public static volatile SingularAttribute<GenericEntityRelationshipAudit, String> changedBy;
	public static volatile SingularAttribute<GenericEntityRelationshipAudit, String> action;
	public static volatile SingularAttribute<GenericEntityRelationshipAudit, Boolean> defaultParent;
	public static volatile SingularAttribute<GenericEntityRelationshipAudit, GenericEntityRelationshipAuditKey> key;

}

