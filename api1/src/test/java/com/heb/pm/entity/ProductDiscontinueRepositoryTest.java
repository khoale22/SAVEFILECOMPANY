package com.heb.pm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Basic JPA repository to support testing the ProductDiscontinue class.
 *
 * @author d116773
 * @since 2.0.0
 */
public interface ProductDiscontinueRepositoryTest extends JpaRepository<ProductDiscontinue, ProductDiscontinueKey> {
}
