package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShippingRestrictionHierarchyLevelAuditKey.class)
public abstract class ShippingRestrictionHierarchyLevelAuditKey_ {

	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAuditKey, Integer> commodity;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAuditKey, Integer> subCommodity;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAuditKey, String> restrictionCode;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAuditKey, Integer> itemClass;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAuditKey, String> subDepartment;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAuditKey, String> department;
	public static volatile SingularAttribute<ShippingRestrictionHierarchyLevelAuditKey, LocalDateTime> changedOn;

}

