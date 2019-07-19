package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EffectiveDatedMaintenance.class)
public abstract class EffectiveDatedMaintenance_ {

	public static volatile SingularAttribute<EffectiveDatedMaintenance, String> createUserId;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, Long> productId;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, String> textValue;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, LocalDateTime> lastUpdateTimeStamp;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, String> lastUpdateUserId;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, LocalDateTime> createTimeStamp;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, CandidateWorkRequest> workRequest;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, EffectiveDatedMaintenanceKey> key;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, LocalDate> effectiveDate;
	public static volatile SingularAttribute<EffectiveDatedMaintenance, Long> workRequestId;

}

