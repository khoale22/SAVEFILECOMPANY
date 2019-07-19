package com.heb.scaleMaintenance.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ScaleMaintenanceTransmit.class)
public abstract class ScaleMaintenanceTransmit_ {

	public static volatile SingularAttribute<ScaleMaintenanceTransmit, LocalDateTime> createTime;
	public static volatile SingularAttribute<ScaleMaintenanceTransmit, LocalDateTime> lastUpdatedTime;
	public static volatile SingularAttribute<ScaleMaintenanceTransmit, String> responseMessage;
	public static volatile SingularAttribute<ScaleMaintenanceTransmit, String> ePlumTransmissionId;
	public static volatile SingularAttribute<ScaleMaintenanceTransmit, ScaleMaintenanceTransmitKey> key;
	public static volatile SingularAttribute<ScaleMaintenanceTransmit, Long> ePlumBatchId;
	public static volatile SingularAttribute<ScaleMaintenanceTransmit, Integer> statusCode;
	public static volatile SingularAttribute<ScaleMaintenanceTransmit, Status> status;

}

