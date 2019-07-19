package com.heb.scaleMaintenance.entity;

import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ScaleMaintenanceUpc.class)
public abstract class ScaleMaintenanceUpc_ {

	public static volatile SingularAttribute<ScaleMaintenanceUpc, LocalDateTime> createTime;
	public static volatile SingularAttribute<ScaleMaintenanceUpc, ScaleMaintenanceProduct> scaleProductAsJson;
	public static volatile SingularAttribute<ScaleMaintenanceUpc, String> jsonVersion;
	public static volatile SingularAttribute<ScaleMaintenanceUpc, String> message;
	public static volatile SingularAttribute<ScaleMaintenanceUpc, ScaleMaintenanceUpcKey> key;

}

