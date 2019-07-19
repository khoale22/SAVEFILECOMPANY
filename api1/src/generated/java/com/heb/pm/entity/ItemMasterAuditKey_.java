package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ItemMasterAuditKey.class)
public abstract class ItemMasterAuditKey_ {

	public static volatile SingularAttribute<ItemMasterAuditKey, String> itemType;
	public static volatile SingularAttribute<ItemMasterAuditKey, Long> itemCode;
	public static volatile SingularAttribute<ItemMasterAuditKey, LocalDateTime> changedOn;

}

