package com.heb.pm.taxonomy;

import com.heb.pm.entity.AttributeStatus;
import com.heb.pm.repository.AttributeStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business functions for attribute status
 * @author s573181
 * @since 2.22.0
 */
@Service
public class AttributeStatusService {

	@Autowired
	private AttributeStateRepository repository;

	/**
	 * Returns all AttributeStatus.
	 *
	 * @return all AttributeStatus.
	 */
	public List<AttributeStatus> findAll() {
		return repository.findAll();
	}
}
