package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MasterDataExtensionAuditKey.class)
public abstract class MasterDataExtensionAuditKey_ {

	public static volatile SingularAttribute<MasterDataExtensionAuditKey, Long> attributeId;
	public static volatile SingularAttribute<MasterDataExtensionAuditKey, Long> keyId;
	public static volatile SingularAttribute<MasterDataExtensionAuditKey, Long> dataSourceSystem;
	public static volatile SingularAttribute<MasterDataExtensionAuditKey, LocalDateTime> changedOn;

}

