package com.heb.pm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ChoiceType.class)
public abstract class ChoiceType_ {

	public static volatile ListAttribute<ChoiceType, ChoiceOption> choiceOptions;
	public static volatile SingularAttribute<ChoiceType, String> choiceTypeCode;
	public static volatile ListAttribute<ChoiceType, ProductGroupChoiceType> productGroupChoiceTypes;
	public static volatile SingularAttribute<ChoiceType, String> alternativeDescription;
	public static volatile SingularAttribute<ChoiceType, ParentChoiceType> parentChoiceType;
	public static volatile SingularAttribute<ChoiceType, String> description;
	public static volatile SingularAttribute<ChoiceType, String> abbreviation;

}

