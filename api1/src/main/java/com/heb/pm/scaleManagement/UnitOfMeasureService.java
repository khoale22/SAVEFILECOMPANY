package com.heb.pm.scaleManagement;

import com.heb.pm.entity.UnitOfMeasure;
import com.heb.pm.repository.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Holds all business logic related for NLEA 2016 nutrient.
 * @author vn70529
 * @since 2.21.0
 */
@Service
public class UnitOfMeasureService {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;

	/**
	 * Find all Unit Of Measures.
	 *
	 * @return the List<UnitOfMeasure>
	 */

	public List<UnitOfMeasure> findAllUnitOfMeasures(){
		return this.unitOfMeasureRepository.findAll();
	}
}
