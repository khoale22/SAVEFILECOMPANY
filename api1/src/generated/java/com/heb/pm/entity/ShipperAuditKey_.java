package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShipperAuditKey.class)
public abstract class ShipperAuditKey_ {

	public static volatile SingularAttribute<ShipperAuditKey, Long> upc;
	public static volatile SingularAttribute<ShipperAuditKey, Long> modifiedUpc;
	public static volatile SingularAttribute<ShipperAuditKey, LocalDateTime> changedOn;

}

