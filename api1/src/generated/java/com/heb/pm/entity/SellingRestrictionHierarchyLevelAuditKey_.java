package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SellingRestrictionHierarchyLevelAuditKey.class)
public abstract class SellingRestrictionHierarchyLevelAuditKey_ {

	public static volatile SingularAttribute<SellingRestrictionHierarchyLevelAuditKey, Integer> commodity;
	public static volatile SingularAttribute<SellingRestrictionHierarchyLevelAuditKey, Integer> subCommodity;
	public static volatile SingularAttribute<SellingRestrictionHierarchyLevelAuditKey, Integer> itemClass;
	public static volatile SingularAttribute<SellingRestrictionHierarchyLevelAuditKey, String> subDepartment;
	public static volatile SingularAttribute<SellingRestrictionHierarchyLevelAuditKey, String> department;
	public static volatile SingularAttribute<SellingRestrictionHierarchyLevelAuditKey, String> restrictionGroupCode;
	public static volatile SingularAttribute<SellingRestrictionHierarchyLevelAuditKey, LocalDateTime> changedOn;

}

