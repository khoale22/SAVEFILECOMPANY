package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientAuditKey.class)
public abstract class NutrientAuditKey_ {

	public static volatile SingularAttribute<NutrientAuditKey, Integer> masterId;
	public static volatile SingularAttribute<NutrientAuditKey, Integer> sourceSystem;
	public static volatile SingularAttribute<NutrientAuditKey, Long> upc;
	public static volatile SingularAttribute<NutrientAuditKey, Integer> valPreprdTypCd;
	public static volatile SingularAttribute<NutrientAuditKey, LocalDateTime> changedOn;

}

