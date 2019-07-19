package com.heb.pm.repository;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.Nutrient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * JPA repository implementation for data from the PD_NUTRIENT table for queries.
 *
 * @author m594201
 * @since 2.1.0
 */
public class NutrientRepositoryImpl implements NutrientRepositoryDelegate {

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	/**
	 *
	 * @param nutrientDescriptions the nutrient descriptions
	 * @param nutrientRequest      the nutrient request
	 * @return A populated List of Nutrients object based of nutrientDescriptions.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Nutrient> findByNutrientDescriptionContains(List<String> nutrientDescriptions, Pageable nutrientRequest) {
		String baseQuery = QUERY;
		for(String s: nutrientDescriptions){
			if(nutrientDescriptions.indexOf(s)!= 0){
				baseQuery = baseQuery.concat(OR);
			}
			baseQuery =  baseQuery.concat(String.format(DESCRIPTION, s));
		}
		Query returnQuery;

		returnQuery = entityManager.createQuery(SELECT.concat(baseQuery), Nutrient.class);
		returnQuery.setFirstResult(nutrientRequest.getPageNumber() * nutrientRequest.getPageSize());
		returnQuery.setMaxResults(nutrientRequest.getPageSize());
		return returnQuery.getResultList();
	}

}
