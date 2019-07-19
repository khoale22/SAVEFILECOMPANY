package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Attribute.class)
public abstract class Attribute_ {

	public static volatile SingularAttribute<Attribute, Boolean> dynamicAttributeSwitch;
	public static volatile SingularAttribute<Attribute, String> createUserId;
	public static volatile SingularAttribute<Attribute, ApplicableType> applicableType;
	public static volatile SingularAttribute<Attribute, Long> sourceSystemId;
	public static volatile SingularAttribute<Attribute, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<Attribute, String> lastUpdateUserId;
	public static volatile SingularAttribute<Attribute, Long> precision;
	public static volatile SingularAttribute<Attribute, String> externalId;
	public static volatile SingularAttribute<Attribute, String> attributeDescription;
	public static volatile SingularAttribute<Attribute, String> logicalOrPhysical;
	public static volatile SingularAttribute<Attribute, String> required;
	public static volatile ListAttribute<Attribute, LogicalPhysicalRelationship> logicalPhysicalRelationships;
	public static volatile SingularAttribute<Attribute, Long> attributeId;
	public static volatile SingularAttribute<Attribute, Boolean> attributeValueList;
	public static volatile SingularAttribute<Attribute, String> attributeDomainCode;
	public static volatile SingularAttribute<Attribute, Boolean> multipleValueSwitch;
	public static volatile SingularAttribute<Attribute, String> applicableTypeCode;
	public static volatile SingularAttribute<Attribute, String> attributeInstantDescription;
	public static volatile SingularAttribute<Attribute, Long> maximumLength;
	public static volatile SingularAttribute<Attribute, String> optionalOrRequired;
	public static volatile SingularAttribute<Attribute, String> attributeName;
	public static volatile ListAttribute<Attribute, EcommerUserGroupAttribute> ecommerUserGroupAttributes;

}

