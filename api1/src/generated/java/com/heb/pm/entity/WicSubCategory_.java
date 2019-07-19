package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WicSubCategory.class)
public abstract class WicSubCategory_ {

	public static volatile SingularAttribute<WicSubCategory, Boolean> lebSwitch;
	public static volatile SingularAttribute<WicSubCategory, String> description;
	public static volatile SingularAttribute<WicSubCategory, WicCategory> wicCategory;
	public static volatile SingularAttribute<WicSubCategory, WicSubCategoryKey> key;

}

