package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DynamicAttribute.class)
public abstract class DynamicAttribute_ {

	public static volatile SingularAttribute<DynamicAttribute, Double> attributeValueNumber;
	public static volatile SingularAttribute<DynamicAttribute, AttributeCode> attributeCode;
	public static volatile SingularAttribute<DynamicAttribute, String> textValue;
	public static volatile SingularAttribute<DynamicAttribute, LocalDateTime> attributeValueTime;
	public static volatile SingularAttribute<DynamicAttribute, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<DynamicAttribute, Long> attributeCodeId;
	public static volatile SingularAttribute<DynamicAttribute, ProductMaster> productMaster;
	public static volatile SingularAttribute<DynamicAttribute, Attribute> attribute;
	public static volatile SingularAttribute<DynamicAttribute, LocalDate> attributeValueDate;
	public static volatile SingularAttribute<DynamicAttribute, DynamicAttributeKey> key;
	public static volatile SingularAttribute<DynamicAttribute, SellingUnit> sellingUnit;

}

