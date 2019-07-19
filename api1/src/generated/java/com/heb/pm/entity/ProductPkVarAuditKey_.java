package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductPkVarAuditKey.class)
public abstract class ProductPkVarAuditKey_ {

	public static volatile SingularAttribute<ProductPkVarAuditKey, Long> sequence;
	public static volatile SingularAttribute<ProductPkVarAuditKey, Integer> sourceSystem;
	public static volatile SingularAttribute<ProductPkVarAuditKey, Long> upc;
	public static volatile SingularAttribute<ProductPkVarAuditKey, LocalDateTime> changedOn;

}

