package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ParentChoiceType.class)
public abstract class ParentChoiceType_ {

	public static volatile SingularAttribute<ParentChoiceType, String> choiceTypeCode;
	public static volatile SingularAttribute<ParentChoiceType, String> alternativeDescription;
	public static volatile ListAttribute<ParentChoiceType, ChoiceType> choiceTypeList;
	public static volatile SingularAttribute<ParentChoiceType, String> description;
	public static volatile SingularAttribute<ParentChoiceType, String> abbreviation;

}

