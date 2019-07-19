package com.heb.pm.repository;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.Nutrient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * JPA repository implementation for data from the PD_NUTRIENT table for queries by nutrient code with count.
 *
 * @author m594201
 * @since 2.1.0
 */
public class NutrientRepositoryWithCountImpl implements NutrientRepositoryDelegateWithCount{

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	/**
	 * Method for custom query to return by description
	 * @param nutrientDescriptions the nutrient descriptions
	 * @param nutrientRequest      the nutrient request
	 * @return Page of nutrients
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Page<Nutrient> findByNutrientDescriptionContains(List<String> nutrientDescriptions, Pageable nutrientRequest) {
		String baseQuery = QUERY;
		for(String s: nutrientDescriptions){
			if(nutrientDescriptions.indexOf(s)!= 0){
				baseQuery = baseQuery.concat(OR);
			}
			baseQuery =  baseQuery.concat(String.format(DESCRIPTION, s));
		}
		Query returnQuery;

		long count = this.getCount(baseQuery);

		returnQuery = entityManager.createQuery(SELECT.concat(baseQuery), Nutrient.class);
		returnQuery.setFirstResult(nutrientRequest.getPageNumber() * nutrientRequest.getPageSize());
		returnQuery.setMaxResults(nutrientRequest.getPageSize());
		List<Nutrient> returnList = returnQuery.getResultList();
		return new PageImpl<Nutrient>(returnList, nutrientRequest, count);
	}

	/**
	 * Return the count from specified query.
	 *
	 * @param baseQuery Base query to call db.
	 * @return The count of records based on supplied query.
	 */
	private Long getCount(String baseQuery){
		return (Long)entityManager.createQuery(COUNT.concat(baseQuery)).getSingleResult();
	}
}
