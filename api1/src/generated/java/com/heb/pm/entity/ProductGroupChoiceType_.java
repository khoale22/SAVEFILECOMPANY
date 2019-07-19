package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductGroupChoiceType.class)
public abstract class ProductGroupChoiceType_ {

	public static volatile SingularAttribute<ProductGroupChoiceType, ChoiceType> choiceType;
	public static volatile ListAttribute<ProductGroupChoiceType, ProductGroupChoiceOption> productGroupChoiceOptions;
	public static volatile SingularAttribute<ProductGroupChoiceType, String> choiceTypeCode;
	public static volatile SingularAttribute<ProductGroupChoiceType, Boolean> pickerSwitch;
	public static volatile SingularAttribute<ProductGroupChoiceType, String> productGroupTypeCode;
	public static volatile SingularAttribute<ProductGroupChoiceType, ProductGroupType> productGroupType;
	public static volatile SingularAttribute<ProductGroupChoiceType, ProductGroupChoiceTypeKey> key;

}

