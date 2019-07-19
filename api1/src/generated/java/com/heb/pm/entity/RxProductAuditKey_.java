package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RxProductAuditKey.class)
public abstract class RxProductAuditKey_ {

	public static volatile SingularAttribute<RxProductAuditKey, Long> prodId;
	public static volatile SingularAttribute<RxProductAuditKey, LocalDateTime> changedOn;

}

