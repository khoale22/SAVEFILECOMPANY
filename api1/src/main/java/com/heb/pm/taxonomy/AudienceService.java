package com.heb.pm.taxonomy;

import com.heb.pm.entity.Audience;
import com.heb.pm.repository.AudienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business functions for Audience.
 *
 * @author s573181
 * @since 2.22.0
 */
@Service
public class AudienceService {

	@Autowired
	private AudienceRepository repository;

	/**
	 * Returns all audiences.
	 *
	 * @return all audiences.
	 */
	public List<Audience> findAll() {
		return repository.findAll();
	}
}
