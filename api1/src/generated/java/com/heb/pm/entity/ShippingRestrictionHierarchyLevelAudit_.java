package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShippingRestrictionHierarchyLevelAudit.class)
public abstract class ShippingRestrictionHierarchyLevelAudit_ {

	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAudit, Boolean> applicableAtThisLevel;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAudit, String> changedBy;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAudit, String> action;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAudit, ShippingRestrictionHierarchyLevelAuditKey> key;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAudit, SellingRestrictionCode> sellingRestrictionCode;

}

