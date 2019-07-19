package com.heb.pm.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CustomerProductGroup.class)
public abstract class CustomerProductGroup_ {

	public static volatile SingularAttribute<CustomerProductGroup, String> custProductGroupName;
	public static volatile SingularAttribute<CustomerProductGroup, String> custProductGroupDescription;
	public static volatile SingularAttribute<CustomerProductGroup, String> createdUserId;
	public static volatile SingularAttribute<CustomerProductGroup, Long> custProductGroupId;
	public static volatile ListAttribute<CustomerProductGroup, ImageMetaData> imageData;
	public static volatile SingularAttribute<CustomerProductGroup, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<CustomerProductGroup, String> lastUpdateUserId;
	public static volatile SingularAttribute<CustomerProductGroup, String> productGroupTypeCode;
	public static volatile SingularAttribute<CustomerProductGroup, String> custProductGroupDescriptionLong;
	public static volatile SingularAttribute<CustomerProductGroup, ProductGroupType> productGroupType;
	public static volatile SingularAttribute<CustomerProductGroup, LocalDateTime> createDate;

}

