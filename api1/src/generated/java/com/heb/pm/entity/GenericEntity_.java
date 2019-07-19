package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GenericEntity.class)
public abstract class GenericEntity_ {

	public static volatile SingularAttribute<GenericEntity, String> displayText;
	public static volatile SingularAttribute<GenericEntity, Long> displayNumber;
	public static volatile SingularAttribute<GenericEntity, String> createUserId;
	public static volatile ListAttribute<GenericEntity, EntityAttribute> entityAttributes;
	public static volatile SingularAttribute<GenericEntity, String> lastUpdateUserId;
	public static volatile SingularAttribute<GenericEntity, Long> id;
	public static volatile SingularAttribute<GenericEntity, String> type;
	public static volatile SingularAttribute<GenericEntity, String> abbreviation;
	public static volatile ListAttribute<GenericEntity, GenericEntityDescription> descriptions;
	public static volatile ListAttribute<GenericEntity, ExportGenericEntityRelationship> exportGenericEntityRelationships;

}

