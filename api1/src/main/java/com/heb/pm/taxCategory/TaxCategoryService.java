package com.heb.pm.taxCategory;

/**
 * Contains business logic related to Vertex tax category.
 *
 * @author d116773
 * @since 2.13.0
 */

import com.heb.pm.entity.VertexTaxCategory;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.VertexServiceClient;
import com.heb.util.ws.SoapException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class TaxCategoryService {

	private static final Logger logger = LoggerFactory.getLogger(TaxCategoryService.class);

	@Value("${app.taxCategoryCache.ttl}")
	private long cacheTimeToLive;

	@Autowired
	private VertexServiceClient vertexServiceClient;

	private Map<String, VertexTaxCategory> allTaxCategories = new HashMap<>();
	private Map<String, VertexTaxCategory> allQualifyingConditions = new HashMap<>();

	/**
	 * Class to clear out the service's cache of tax categories.
	 */
	private class CacheEmptier extends TimerTask {

		private TaxCategoryService taxCategoryService;

		public CacheEmptier(TaxCategoryService taxCategoryService) {
			this.taxCategoryService = taxCategoryService;
		}

		/**
		 * Called when the timer goes off to clear the cache.
		 */
		@Override
		public void run() {
			this.taxCategoryService.clearCache();
		}
	}

	/**
	 * Returns the tax category corresponding to a DVR code. Will return null if it does not exists.
	 *
	 * @param drvCode the code to look for.
	 * @return The VertexTaxCategory for that DVR code.
	 * @throws CheckedSoapException
	 */
	public VertexTaxCategory fetchOneTaxCategory(String drvCode) throws CheckedSoapException {
		if (this.allTaxCategories.isEmpty()) {
			try {
				this.populateLists();
			} catch (CheckedSoapException e) {
				TaxCategoryService.logger.error(e.getMessage());
				throw e;
			}
		}
		VertexTaxCategory taxCategory =  this.allTaxCategories.get(drvCode.trim());
		if (taxCategory == null) {
			taxCategory = new VertexTaxCategory();
			taxCategory.setCategoryName("Unknown ID");
			taxCategory.setDvrCode(drvCode);
		}
		return taxCategory;
	}

	/**
	 * Returns the qualiifying condition corresponding to a DVR code. Will return null if it does not exists.
	 *
	 * @param drvCode the code to look for.
	 * @return The Qualifying condition for that DVR code.
	 * @throws CheckedSoapException
	 */
	public VertexTaxCategory fetchOneQualifyingCondition(String drvCode) throws CheckedSoapException {

		if (this.allQualifyingConditions.isEmpty()) {
			try {
				this.populateLists();
			} catch (CheckedSoapException e) {
				TaxCategoryService.logger.error(e.getMessage());
				throw e;
			}
		}
		VertexTaxCategory taxCategory = this.allQualifyingConditions.get(StringUtils.trimToEmpty(drvCode));
		if (taxCategory == null) {
			taxCategory = new VertexTaxCategory();
			taxCategory.setCategoryName("Unknown ID");
			taxCategory.setDvrCode(drvCode);
		}
		return taxCategory;
	}

	/**
	 * Returns a list of all Vertex tax categories.
	 *
	 * @return A list of all Vertex tax categories.
	 */
	public Collection<VertexTaxCategory> fetchAllTaxCategories() {

		try {
			if (this.allTaxCategories.isEmpty()) {
				this.populateLists();
			}
			return this.allTaxCategories.values();
		} catch (CheckedSoapException e) {
			TaxCategoryService.logger.error(e.getMessage());
			throw new SoapException(e.getMessage());
		}
	}

	/**
	 * Returns a list of all qualifying conditions.
	 *
	 * @return A list of all qualifying conditions.
	 */
	public Collection<VertexTaxCategory> fetchAllQualifyingConditions() {
		try {
			if (this.allQualifyingConditions.isEmpty()) {
				this.populateLists();
			}
			return this.allQualifyingConditions.values();
		} catch (CheckedSoapException e) {
			TaxCategoryService.logger.error(e.getMessage());
			throw new SoapException(e.getMessage());
		}
	}

	/**
	 * Loads the cache of tax categories.
	 *
	 * @throws CheckedSoapException
	 */
	private void populateLists() throws CheckedSoapException {

		TaxCategoryService.logger.info("Populating cache of Vertex tax categories.");

		List<VertexTaxCategory> vertexTaxCategories = this.vertexServiceClient.fetchAll();
		vertexTaxCategories.forEach((v) -> this.allTaxCategories.put(StringUtils.trim(v.getDvrCode()), v));

		List<VertexTaxCategory> qualifyingConditions = this.vertexServiceClient.fetchAllQualifyingTaxConditions();
		qualifyingConditions.forEach((v) -> this.allQualifyingConditions.put(StringUtils.trim(v.getDvrCode()), v));

		Timer timer = new Timer();
		timer.schedule(new CacheEmptier(this), this.cacheTimeToLive);
	}

	/**
	 * Empties the cache.
	 */
	protected void clearCache() {
		TaxCategoryService.logger.info("Clearing cache of Vertex tax categories.");
		this.allTaxCategories.clear();
		this.allQualifyingConditions.clear();
	}

	/**
	 * Finds vertex tax categories that match a given list of tax category codes.
	 *
	 * @param taxCategoryCodes List of tax category codes to look for.
	 * @return List of Vertex tax categories that match the given codes.
	 */
	public List<VertexTaxCategory> findVertexTaxCategoriesByTaxCategoryCodes(List<String> taxCategoryCodes) {
		List<VertexTaxCategory> toReturn = new ArrayList<>();
		Collection<VertexTaxCategory> allVertexTaxCategories = this.fetchAllTaxCategories();
		for(VertexTaxCategory vertexTaxCategory : allVertexTaxCategories){
			if(taxCategoryCodes.contains(vertexTaxCategory.getDvrCode())){
				toReturn.add(vertexTaxCategory);
			}
		}
		return toReturn;
	}
}
