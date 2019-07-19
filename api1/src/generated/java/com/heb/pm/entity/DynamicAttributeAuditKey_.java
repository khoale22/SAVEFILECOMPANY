package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DynamicAttributeAuditKey.class)
public abstract class DynamicAttributeAuditKey_ {

	public static volatile SingularAttribute<DynamicAttributeAuditKey, Integer> attributeId;
	public static volatile SingularAttribute<DynamicAttributeAuditKey, Integer> sequenceNumber;
	public static volatile SingularAttribute<DynamicAttributeAuditKey, Integer> sourceSystem;
	public static volatile SingularAttribute<DynamicAttributeAuditKey, String> keyType;
	public static volatile SingularAttribute<DynamicAttributeAuditKey, Long> key;
	public static volatile SingularAttribute<DynamicAttributeAuditKey, LocalDateTime> changedOn;

}

