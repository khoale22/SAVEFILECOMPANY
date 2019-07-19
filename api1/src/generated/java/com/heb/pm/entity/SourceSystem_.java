package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SourceSystem.class)
public abstract class SourceSystem_ {

	public static volatile ListAttribute<SourceSystem, TargetSystemAttributePriority> targetSystemAttributePriorities;
	public static volatile ListAttribute<SourceSystem, ImageMetaData> imageMetaData;
	public static volatile SingularAttribute<SourceSystem, String> description;
	public static volatile SingularAttribute<SourceSystem, Long> id;

}

