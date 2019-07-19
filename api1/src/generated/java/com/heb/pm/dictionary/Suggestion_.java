package com.heb.pm.dictionary;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Suggestion.class)
public abstract class Suggestion_ {

	public static volatile SingularAttribute<Suggestion, String> lstUpdtUid;
	public static volatile SingularAttribute<Suggestion, Boolean> active;
	public static volatile SingularAttribute<Suggestion, LocalDate> lstUpdtTs;
	public static volatile SingularAttribute<Suggestion, SuggestionKey> key;

}

