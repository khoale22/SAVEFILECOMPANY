package com.heb.pm.entity;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ExportGenericEntityRelationship.class)
public abstract class ExportGenericEntityRelationship_ {

	public static volatile SingularAttribute<ExportGenericEntityRelationship, String> createUserId;
	public static volatile ListAttribute<ExportGenericEntityRelationship, ExportGenericEntityRelationship> childRelationships;
	public static volatile ListAttribute<ExportGenericEntityRelationship, ExportGenericEntityRelationship> parentRelationships;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, GenericEntityDescription> parentDescription;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, String> lastUpdateUserId;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, Boolean> display;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, Boolean> active;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, String> hierarchyContext;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, Boolean> defaultParent;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, Long> parentEntityId;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, GenericEntityDescription> childDescription;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, GenericEntity> genericParentEntity;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, Long> sequence;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, Long> childEntityId;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, GenericEntityRelationshipKey> key;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, LocalDate> effectiveDate;
	public static volatile SingularAttribute<ExportGenericEntityRelationship, LocalDate> expirationDate;

}

