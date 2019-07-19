package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NutrientPanelHeader.class)
public abstract class NutrientPanelHeader_ {

	public static volatile SingularAttribute<NutrientPanelHeader, String> xtrnlId;
	public static volatile SingularAttribute<NutrientPanelHeader, String> servingsPerContainerText;
	public static volatile SingularAttribute<NutrientPanelHeader, LocalDateTime> sourceTime;
	public static volatile SingularAttribute<NutrientPanelHeader, String> servingSizeText;
	public static volatile SingularAttribute<NutrientPanelHeader, NutrientPanelHeaderKey> key;

}

