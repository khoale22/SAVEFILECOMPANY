package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientAudit.class)
public abstract class NutrientAudit_ {

	public static volatile SingularAttribute<NutrientAudit, String> changedBy;
	public static volatile SingularAttribute<NutrientAudit, LocalDateTime> lastUpdateTs;
	public static volatile SingularAttribute<NutrientAudit, String> action;
	public static volatile SingularAttribute<NutrientAudit, NutrientAuditKey> key;

}

