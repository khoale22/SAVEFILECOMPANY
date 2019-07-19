package com.heb.pm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MasterDataExtensionAttribute.class)
public abstract class MasterDataExtensionAttribute_ {

	public static volatile SingularAttribute<MasterDataExtensionAttribute, String> createUserId;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, String> lastUdateUserId;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, String> attributeValueText;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, LocalDateTime> attributeValueTime;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, Long> attributeCodeId;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, LocalDate> lstUpdtTs;
	public static volatile ListAttribute<MasterDataExtensionAttribute, GroupAttribute> groupAttributes;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, Double> attributeValueNumber;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, AttributeCode> attributeCode;
	public static volatile ListAttribute<MasterDataExtensionAttribute, EntityAttribute> entityAttribute;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, LocalDateTime> createTime;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, Attribute> attribute;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, LocalDate> attributeValueDate;
	public static volatile SingularAttribute<MasterDataExtensionAttribute, MasterDataExtensionAttributeKey> key;
	public static volatile ListAttribute<MasterDataExtensionAttribute, EcommerUserGroupAttribute> ecommerUserGroupAttributes;

}

