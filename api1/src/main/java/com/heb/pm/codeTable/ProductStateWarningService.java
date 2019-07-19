package com.heb.pm.codeTable;

import com.heb.pm.entity.ProductStateWarning;
import com.heb.pm.entity.ProductWarning;
import com.heb.pm.repository.ProductStateWarningRepository;
import com.heb.pm.repository.ProductWarningsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business functions related to product state warnings.
 *
 * @author m314029
 * @since 2.12.0
 */
@Service
public class ProductStateWarningService {

	@Autowired
	private ProductStateWarningRepository productStateWarningRepository;

	@Autowired
	private ProductWarningsRepository productWarningsRepository;

	/**
	 * Calls the product state warning repository to find all records.
	 *
	 * @return List of all product state warnings.
	 */
	public List<ProductStateWarning> findAll() {
		return this.productStateWarningRepository.findAll();
	}

	/**
	 * Gets product state warnings by product id.
	 *
	 * @param prodId the prod id
	 * @return the product state warnings by product id
	 */
	public List<ProductWarning> getProductStateWarningsByProductId(Long prodId) {
		return this.productWarningsRepository.findByKeyProdId(prodId);
	}

	/**
	 * Find all state warnings list.
	 *
	 * @return the list
	 */
	public List<ProductStateWarning> findAllStateWarnings() {
		return this.productStateWarningRepository.findAll();
	}
}
