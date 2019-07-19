package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AttributeDomain.class)
public abstract class AttributeDomain_ {

	public static volatile SingularAttribute<AttributeDomain, String> attributeDomainCode;
	public static volatile SingularAttribute<AttributeDomain, String> attributeDomainDescription;
	public static volatile SingularAttribute<AttributeDomain, String> createUserId;
	public static volatile SingularAttribute<AttributeDomain, String> lastUpdateUserId;
	public static volatile SingularAttribute<AttributeDomain, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<AttributeDomain, LocalDateTime> createDate;

}

