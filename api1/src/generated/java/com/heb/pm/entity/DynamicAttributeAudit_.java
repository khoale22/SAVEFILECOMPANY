package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DynamicAttributeAudit.class)
public abstract class DynamicAttributeAudit_ {

	public static volatile SingularAttribute<DynamicAttributeAudit, String> textValue;
	public static volatile SingularAttribute<DynamicAttributeAudit, String> changedBy;
	public static volatile SingularAttribute<DynamicAttributeAudit, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<DynamicAttributeAudit, LocalDateTime> lastUpdateTs;
	public static volatile SingularAttribute<DynamicAttributeAudit, Long> attributeCodeId;
	public static volatile SingularAttribute<DynamicAttributeAudit, String> action;
	public static volatile SingularAttribute<DynamicAttributeAudit, ProductMaster> productMaster;
	public static volatile SingularAttribute<DynamicAttributeAudit, Attribute> attribute;
	public static volatile SingularAttribute<DynamicAttributeAudit, DynamicAttributeAuditKey> key;
	public static volatile SingularAttribute<DynamicAttributeAudit, SellingUnit> sellingUnit;

}

