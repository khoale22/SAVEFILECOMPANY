package com.heb.scaleMaintenance.entity;

import com.heb.scaleMaintenance.model.ScaleMaintenanceLoadParameters;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ScaleMaintenanceTracking.class)
public abstract class ScaleMaintenanceTracking_ {

	public static volatile SingularAttribute<ScaleMaintenanceTracking, ScaleTransactionType> scaleTransactionType;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, LocalDateTime> createTime;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, Integer> scaleTransactionTypeCode;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, String> jsonVersion;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, LocalDateTime> lastUpdatedTime;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, String> userId;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, Long> transactionId;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, ScaleMaintenanceLoadParameters> loadParametersAsJson;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, Integer> statusCode;
	public static volatile SingularAttribute<ScaleMaintenanceTracking, Status> status;

}

