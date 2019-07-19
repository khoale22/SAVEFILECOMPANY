package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GenericEntityRelationshipAuditKey.class)
public abstract class GenericEntityRelationshipAuditKey_ {

	public static volatile SingularAttribute<GenericEntityRelationshipAuditKey, Long> childEntityId;
	public static volatile SingularAttribute<GenericEntityRelationshipAuditKey, String> hierarchyContext;
	public static volatile SingularAttribute<GenericEntityRelationshipAuditKey, Long> parentEntityId;
	public static volatile SingularAttribute<GenericEntityRelationshipAuditKey, LocalDateTime> changedOn;

}

