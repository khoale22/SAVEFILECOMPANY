package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductMasterAuditKey.class)
public abstract class ProductMasterAuditKey_ {

	public static volatile SingularAttribute<ProductMasterAuditKey, Long> prodId;
	public static volatile SingularAttribute<ProductMasterAuditKey, LocalDateTime> changedOn;

}

