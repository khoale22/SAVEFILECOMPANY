package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WarehouseLocationItemAuditKey.class)
public abstract class WarehouseLocationItemAuditKey_ {

	public static volatile SingularAttribute<WarehouseLocationItemAuditKey, String> itemType;
	public static volatile SingularAttribute<WarehouseLocationItemAuditKey, Integer> warehouseNumber;
	public static volatile SingularAttribute<WarehouseLocationItemAuditKey, Long> itemCode;
	public static volatile SingularAttribute<WarehouseLocationItemAuditKey, String> warehouseType;
	public static volatile SingularAttribute<WarehouseLocationItemAuditKey, LocalDateTime> changedOn;

}

