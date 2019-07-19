package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductPkVarAudit.class)
public abstract class ProductPkVarAudit_ {

	public static volatile SingularAttribute<ProductPkVarAudit, String> productValueDescription;
	public static volatile SingularAttribute<ProductPkVarAudit, String> houseHoldMeasurement;
	public static volatile SingularAttribute<ProductPkVarAudit, String> servingSizeUomCode;
	public static volatile SingularAttribute<ProductPkVarAudit, String> changedBy;
	public static volatile SingularAttribute<ProductPkVarAudit, SourceSystem> sourceSystem;
	public static volatile SingularAttribute<ProductPkVarAudit, LocalDateTime> lastUpdateTs;
	public static volatile SingularAttribute<ProductPkVarAudit, String> servingsPerContainerText;
	public static volatile SingularAttribute<ProductPkVarAudit, String> action;
	public static volatile SingularAttribute<ProductPkVarAudit, String> panelTypeCode;
	public static volatile SingularAttribute<ProductPkVarAudit, Double> servingSizeQuantity;
	public static volatile SingularAttribute<ProductPkVarAudit, ProductPkVarAuditKey> key;
	public static volatile SingularAttribute<ProductPkVarAudit, LocalDateTime> createDate;

}

