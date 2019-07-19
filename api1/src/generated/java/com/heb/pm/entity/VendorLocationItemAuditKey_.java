package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VendorLocationItemAuditKey.class)
public abstract class VendorLocationItemAuditKey_ {

	public static volatile SingularAttribute<VendorLocationItemAuditKey, String> vendorType;
	public static volatile SingularAttribute<VendorLocationItemAuditKey, String> itemType;
	public static volatile SingularAttribute<VendorLocationItemAuditKey, Long> itemCode;
	public static volatile SingularAttribute<VendorLocationItemAuditKey, Integer> vendorNumber;
	public static volatile SingularAttribute<VendorLocationItemAuditKey, LocalDateTime> changedOn;

}

