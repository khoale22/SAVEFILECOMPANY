package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductGroupType.class)
public abstract class ProductGroupType_ {

	public static volatile SingularAttribute<ProductGroupType, String> subDepartmentId;
	public static volatile SingularAttribute<ProductGroupType, String> departmentNumberString;
	public static volatile ListAttribute<ProductGroupType, CustomerProductGroup> customerProductGroups;
	public static volatile ListAttribute<ProductGroupType, ProductGroupChoiceType> productGroupChoiceTypes;
	public static volatile SingularAttribute<ProductGroupType, String> productGroupTypeCode;
	public static volatile SingularAttribute<ProductGroupType, String> productGroupType;

}

