package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AttributeMetaData.class)
public abstract class AttributeMetaData_ {

	public static volatile SingularAttribute<AttributeMetaData, String> domainCode;
	public static volatile SingularAttribute<AttributeMetaData, String> helpText;
	public static volatile SingularAttribute<AttributeMetaData, LocalDate> addedDate;
	public static volatile SingularAttribute<AttributeMetaData, String> createdUserId;
	public static volatile SingularAttribute<AttributeMetaData, Integer> minimumLength;
	public static volatile SingularAttribute<AttributeMetaData, String> codeTableName;
	public static volatile SingularAttribute<AttributeMetaData, Long> sourceSystemId;
	public static volatile SingularAttribute<AttributeMetaData, LocalDateTime> created;
	public static volatile SingularAttribute<AttributeMetaData, String> lastUpdateUserId;
	public static volatile SingularAttribute<AttributeMetaData, Integer> precision;
	public static volatile SingularAttribute<AttributeMetaData, String> description;
	public static volatile SingularAttribute<AttributeMetaData, Boolean> global;
	public static volatile SingularAttribute<AttributeMetaData, String> audienceCode;
	public static volatile SingularAttribute<AttributeMetaData, Boolean> codeTableStandard;
	public static volatile SingularAttribute<AttributeMetaData, Boolean> required;
	public static volatile SingularAttribute<AttributeMetaData, String> attributeStatusCode;
	public static volatile SingularAttribute<AttributeMetaData, LocalDateTime> lastUpdated;
	public static volatile SingularAttribute<AttributeMetaData, String> resourceTied;
	public static volatile SingularAttribute<AttributeMetaData, LocalDate> attributeStatusChangedDate;
	public static volatile SingularAttribute<AttributeMetaData, Integer> maximumLength;
	public static volatile SingularAttribute<AttributeMetaData, String> name;
	public static volatile SingularAttribute<AttributeMetaData, String> applicableTo;
	public static volatile SingularAttribute<AttributeMetaData, AttributeMetaDataKey> key;
	public static volatile SingularAttribute<AttributeMetaData, Boolean> customerFacing;

}

