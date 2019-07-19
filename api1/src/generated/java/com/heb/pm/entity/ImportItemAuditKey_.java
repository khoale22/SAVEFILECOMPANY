package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ImportItemAuditKey.class)
public abstract class ImportItemAuditKey_ {

	public static volatile SingularAttribute<ImportItemAuditKey, String> vendorType;
	public static volatile SingularAttribute<ImportItemAuditKey, String> itemType;
	public static volatile SingularAttribute<ImportItemAuditKey, Long> itemCode;
	public static volatile SingularAttribute<ImportItemAuditKey, Integer> vendorNumber;
	public static volatile SingularAttribute<ImportItemAuditKey, LocalDateTime> changedOn;

}

