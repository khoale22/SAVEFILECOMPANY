package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShipperAudit.class)
public abstract class ShipperAudit_ {

	public static volatile SingularAttribute<ShipperAudit, String> changedBy;
	public static volatile SingularAttribute<ShipperAudit, Long> shipperQuantity;
	public static volatile SingularAttribute<ShipperAudit, String> action;
	public static volatile SingularAttribute<ShipperAudit, Character> shipperTypeCode;
	public static volatile SingularAttribute<ShipperAudit, ShipperAuditKey> key;

}

