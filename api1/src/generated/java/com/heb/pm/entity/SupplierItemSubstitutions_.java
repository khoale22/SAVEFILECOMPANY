package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SupplierItemSubstitutions.class)
public abstract class SupplierItemSubstitutions_ {

	public static volatile SingularAttribute<SupplierItemSubstitutions, Long> subToItemId;
	public static volatile SingularAttribute<SupplierItemSubstitutions, SubTypeCode> supplierSubTypeCode;
	public static volatile SingularAttribute<SupplierItemSubstitutions, String> subTypeCode;
	public static volatile SingularAttribute<SupplierItemSubstitutions, Long> subRuleSequenceNumber;
	public static volatile SingularAttribute<SupplierItemSubstitutions, WarehouseLocationItem> supItemsWarehouseLocationItem;
	public static volatile SingularAttribute<SupplierItemSubstitutions, SupplierItemSubstitutionsKey> key;

}

