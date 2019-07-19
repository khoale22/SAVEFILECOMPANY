package com.heb.pm.index;

import com.heb.pm.scaleManagement.NutrientUomDocument;
import com.heb.pm.repository.NutritionUomIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for the NutrientUom index batch job.
 *
 * @author m594201
 * @since 2.1.0
 */
public class NutritionWriter implements ItemWriter<NutrientUomDocument> {

	/**
	 * The constant logger.
	 */
	public static final Logger logger = LoggerFactory.getLogger(NutritionWriter.class);

	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called write with null or empty list.";
	private static final String LOG_MESSAGE = "Writing %s nutrition uom starting with %s.";

	@Autowired
	private NutritionUomIndexRepository indexRepository;

	/**
	 * Called by the Spring batch framework to write the NutrientUom to the index.
	 *
	 * @param items The list of NutrientUom to write.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends NutrientUomDocument> items) throws Exception {

		if (items == null || items.isEmpty()) {
			SubDepartmentWriter.logger.debug(NutritionWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		if (SubDepartmentWriter.logger.isDebugEnabled()) {
			SubDepartmentWriter.logger.debug(String.format(NutritionWriter.LOG_MESSAGE,
					items.size(), items.get(0)));
		}

		this.indexRepository.save(items);

	}

	/**
	 * Sets the NutritionUomIndexRepository for this object. THis is mainly for testing.
	 *
	 * @param indexRepository The NutritionUomIndexRepository for this object.
	 */
	public void setIndexRepository(NutritionUomIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
