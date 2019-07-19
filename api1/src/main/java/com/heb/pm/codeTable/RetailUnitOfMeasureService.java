package com.heb.pm.codeTable;

import com.heb.pm.entity.RetailUnitOfMeasure;
import com.heb.pm.repository.RetailUnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business functions related to retail units of measure.
 *
 * @author m314029
 * @since 2.12.0
 */
@Service
public class RetailUnitOfMeasureService {

	@Autowired
	private RetailUnitOfMeasureRepository retailUnitOfMeasureRepository;

	/**
	 * Calls the retail unit of measure repository to find all records.
	 *
	 * @return List of all retail units of measure.
	 */
	public List<RetailUnitOfMeasure> findAll() {
		return this.retailUnitOfMeasureRepository.findAll();
	}
}
