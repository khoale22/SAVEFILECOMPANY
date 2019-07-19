package com.heb.pm.productSearch.predicateBuilders;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Collects all the known PredicateBuilders into one list that can be grabbed by the main search module. When you add
 * a new PredicateBuilder, create a variable here and add it to the list to return.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class PredicateBuilderList {

	@Resource
	AbstractApplicationContext applicationContext;

	private Collection<PredicateBuilder> predicateBuilders = null;

	/**
	 * Returns the list of all available PredicateBuilders.
	 *
	 * @return The list of all available PredicateBuilders.
	 */
	public Collection<PredicateBuilder> getPredicateBuilders() {

		// We only need to build the list once, so, once it's built, just keep re-using the same one.
		if (this.predicateBuilders == null) {

			// Pull list of beans that extend PredicateBuilder from the application context.
			ListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();
			Map<String, PredicateBuilder> predicateBuilderMap = beanFactory.getBeansOfType(PredicateBuilder.class);
			this.predicateBuilders = predicateBuilderMap.values();

		}

		return this.predicateBuilders;
	}
}
