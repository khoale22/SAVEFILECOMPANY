package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AttributeCode.class)
public abstract class AttributeCode_ {

	public static volatile SingularAttribute<AttributeCode, String> createUserId;
	public static volatile SingularAttribute<AttributeCode, String> attributeValueText;
	public static volatile SingularAttribute<AttributeCode, String> attributeValueXtrnlId;
	public static volatile SingularAttribute<AttributeCode, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<AttributeCode, String> lastUpdateUserId;
	public static volatile SingularAttribute<AttributeCode, Boolean> xtrnlCodeSwitch;
	public static volatile SingularAttribute<AttributeCode, Long> attributeCodeId;
	public static volatile SingularAttribute<AttributeCode, String> attributeValueCode;
	public static volatile SingularAttribute<AttributeCode, LocalDateTime> createDate;

}

