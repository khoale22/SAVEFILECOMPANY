package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DiscontinueExceptionParametersAudit.class)
public abstract class DiscontinueExceptionParametersAudit_ {

	public static volatile SingularAttribute<DiscontinueExceptionParametersAudit, String> exceptionTypeId;
	public static volatile SingularAttribute<DiscontinueExceptionParametersAudit, String> exceptionType;
	public static volatile SingularAttribute<DiscontinueExceptionParametersAudit, Boolean> neverDelete;
	public static volatile SingularAttribute<DiscontinueExceptionParametersAudit, DiscontinueParametersCommonAudit> audit;
	public static volatile SingularAttribute<DiscontinueExceptionParametersAudit, DiscontinueExceptionParametersAuditKey> key;

}

