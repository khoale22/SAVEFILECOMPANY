package com.heb.pm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VendorItemStore.class)
public abstract class VendorItemStore_ {

	public static volatile SingularAttribute<VendorItemStore, String> lstUpdtUsrId;
	public static volatile SingularAttribute<VendorItemStore, String> strSubDeptId;
	public static volatile SingularAttribute<VendorItemStore, LocalDate> authDt;
	public static volatile SingularAttribute<VendorItemStore, VendorLocationItem> vendorLocationItem;
	public static volatile SingularAttribute<VendorItemStore, LocalDate> unathDt;
	public static volatile SingularAttribute<VendorItemStore, BigDecimal> invSeqNbr;
	public static volatile SingularAttribute<VendorItemStore, LocalDateTime> lstUpdtTs;
	public static volatile SingularAttribute<VendorItemStore, VendorItemStoreKey> id;
	public static volatile SingularAttribute<VendorItemStore, Boolean> authnSw;
	public static volatile SingularAttribute<VendorItemStore, String> strDeptNbr;

}

