package com.heb.pm.repository;

import com.heb.pm.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for state.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface StateRepository extends JpaRepository<State, String> {
}
