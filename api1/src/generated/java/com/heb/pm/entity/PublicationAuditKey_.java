package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PublicationAuditKey.class)
public abstract class PublicationAuditKey_ {

	public static volatile SingularAttribute<PublicationAuditKey, String> itemProductKeyCode;
	public static volatile SingularAttribute<PublicationAuditKey, String> entityTypeCode;
	public static volatile SingularAttribute<PublicationAuditKey, LocalDateTime> publishDate;
	public static volatile SingularAttribute<PublicationAuditKey, Long> itemProductId;

}

