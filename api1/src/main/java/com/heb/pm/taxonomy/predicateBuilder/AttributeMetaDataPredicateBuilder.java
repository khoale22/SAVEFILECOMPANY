package com.heb.pm.taxonomy.predicateBuilder;

import com.heb.pm.entity.AttributeMetaData;
import com.heb.pm.repository.SimplePredicateBuilder;
import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Predicate builder for attribute meta data.
 *
 * @author m314029
 * @since 2.21.0
 */
public class AttributeMetaDataPredicateBuilder implements SimplePredicateBuilder<AttributeMetaData> {

	private static final String LIKE_REGEX_EXPRESSION = "%%%s%%";
	private String name;
	private Boolean customerFacing;
	private Boolean global;
	private String attributeStateCode;
	private Boolean hasStandardCodeTable;

	public AttributeMetaDataPredicateBuilder(String name, Boolean customerFacing, Boolean global, String attributeStateCode, Boolean hasStandardCodeTable) {
		this.name = name;
		this.customerFacing = customerFacing;
		this.global = global;
		this.attributeStateCode = attributeStateCode;
		this.hasStandardCodeTable = hasStandardCodeTable;
	}

	/**
	 * This method will generate a list of predicates that constrain directly on attribute meta data.
	 *
	 * @param root The root from clause of the main query (this will be used to grab the criteria to join the
	 *               sub-query to).
	 * @param builder Used to construct the various parts of the SQL statement.
	 * @return A list of predicates that will constrain attribute meta data.
	 */
	@Override
	public List<Predicate> buildWhereClause(Root<AttributeMetaData> root, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<>();
		if(StringUtils.isNotBlank(this.name)){
			predicates.add(
					builder.like(builder.lower(root.get("name")),
							String.format(AttributeMetaDataPredicateBuilder.LIKE_REGEX_EXPRESSION,
									this.name.toLowerCase())));
		}
		if(this.customerFacing != null){
			predicates.add(
					builder.equal(root.get("customerFacing"),this.customerFacing));
		}
		if(this.global != null){
			predicates.add(
					builder.equal(root.get("global"),this.global));
		}
		if(StringUtils.isNotBlank(this.attributeStateCode)){
			predicates.add(
					builder.like(builder.lower(root.get("attributeStateCode")),
							String.format(AttributeMetaDataPredicateBuilder.LIKE_REGEX_EXPRESSION,
									this.attributeStateCode.toLowerCase())));
		}
		if(this.hasStandardCodeTable != null){
			predicates.add(
					builder.equal(root.get("codeTableStandard"),this.hasStandardCodeTable));
		}

		return predicates;
	}
}
