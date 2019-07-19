package com.heb.pm.taxonomy;

import com.heb.pm.entity.ApplicableType;
import com.heb.pm.repository.ApplicableTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business functions for application type.
 *
 * @author s573181
 * @since 2.22.0
 */
@Service
public class ApplicableTypeService {

	@Autowired
	private ApplicableTypeRepository repository;

	/**
	 * Returns all ApplicableTypes.
	 *
	 * @return all ApplicableTypes.
	 */
	public List<ApplicableType> findAll(){
		return repository.findAll();
	}
}
