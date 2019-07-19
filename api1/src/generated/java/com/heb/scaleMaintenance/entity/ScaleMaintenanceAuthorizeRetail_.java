package com.heb.scaleMaintenance.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ScaleMaintenanceAuthorizeRetail.class)
public abstract class ScaleMaintenanceAuthorizeRetail_ {

	public static volatile SingularAttribute<ScaleMaintenanceAuthorizeRetail, LocalDateTime> createTime;
	public static volatile SingularAttribute<ScaleMaintenanceAuthorizeRetail, Boolean> authorized;
	public static volatile SingularAttribute<ScaleMaintenanceAuthorizeRetail, Boolean> weighed;
	public static volatile SingularAttribute<ScaleMaintenanceAuthorizeRetail, String> message;
	public static volatile SingularAttribute<ScaleMaintenanceAuthorizeRetail, ScaleMaintenanceAuthorizeRetailKey> key;
	public static volatile SingularAttribute<ScaleMaintenanceAuthorizeRetail, Double> retail;
	public static volatile SingularAttribute<ScaleMaintenanceAuthorizeRetail, Integer> byCountQuantity;

}

