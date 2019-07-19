/*
 * com.heb.pm.ApplicationConfiguration
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.heb.util.elasticsearch.CustomEntityMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.io.File;


/**
 * Application configuration for things that cannot be configured in XML.
 *
 * @author d116773
 * @since 2.0.0
 */
@Configuration
@EnableAutoConfiguration
@EnableCaching
@EnableAsync
@EnableElasticsearchRepositories(basePackages = {"com.heb.pm.vendor", "com.heb.pm.subDepartment",
		"com.heb.pm.productHierarchy", "com.heb.pm.store", "com.heb.pm.repository", "com.heb.pm.nutrients"})
@ImportResource("classpath:config.xml")
public class ApplicationConfiguration extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

	@Value("${applicationConfiguration.JSON.indent}")
	private boolean indentJson;

	@Value("${elasticsearch.root}")
	private String elasticsearchRoot;

	/**
     * Spring Boot Runner.
     *
     * @param args Optional parameters from command-line.
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationConfiguration.class);
    }

	/**
	 * Constructs a ServletListenerRegistrationBean to listen for state changes to HTTP sessions. To capture
	 * these events, construct a ApplicationListener<HttpSessionDestroyedEvent>, annotate it with @Component,
	 * and have Spring scan the package of the class. It will automatically events published by the bean created here.
	 *
	 * @return A ServletListenerRegistrationBean to listen for state changes to HTTP sessions.
	 */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> getSessionDestroyedListener() {
		ApplicationConfiguration.logger.info("loading session destroyed listener.");
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

	/**
	 * Creates the application's JSON object mapper. The goal is to match the default behaviour as much as possible
	 * with the following changes:
	 *
	 * 1. Send Java 8 dates in a format JavaScript can handle.
	 * 2. Not throw errors on un-resolved lazy loaded Hibernate entities.
	 *
	 * @return The application's JSON object mapper.
	 */
	@Primary
	@Bean
	public ObjectMapper getJacksonBuilder() {

		ApplicationConfiguration.logger.info("loading Jackson mappers.");

		Jackson2ObjectMapperBuilder objectMapperBuilder = new Jackson2ObjectMapperBuilder();

		// The first line makes dates be written as dates and not an array of integers.
		// The other two are turned off by Spring by default, so this is mapping that.
		// Note that turning off DEFAULT_VIEW_INCLUSION means that if you annotate a controller as
		// having a JSON view, then anything not annotated will not be included.
		objectMapperBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
				MapperFeature.DEFAULT_VIEW_INCLUSION,
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


		objectMapperBuilder.featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);


		// JSON can be indented to make it easier to read in the development environment.
		objectMapperBuilder.indentOutput(this.indentJson);

		// This module knows about Java 8 dates.
		ApplicationConfiguration.logger.info("Loading Java 8 date aware Jackson module.");
		JavaTimeModule javaTimeModule = new JavaTimeModule();

		// This module knows about Hibernate.
		ApplicationConfiguration.logger.info("Loading Hibernate aware Jackson module.");
		Hibernate4Module hibernate4Module = new Hibernate4Module();

		// This is needed to serialize properties annotated as @Transient
		hibernate4Module.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);

		objectMapperBuilder.modules(javaTimeModule, hibernate4Module);

		return objectMapperBuilder.build();
	}

	/**
	 * Constructs the Elasticsearch Node for the application.
	 *
	 * @return The Elasticsearch Node for the application.
	 */
	@Bean
	public Client getSearchTemplate() {

		ApplicationConfiguration.logger.info("creating Elastisearch node in " + this.elasticsearchRoot);

		// If the directory to store the Elasticsearch files does not exist, create it.
		File rootDir = new File(this.elasticsearchRoot);
		if (!rootDir.isDirectory()) {
			boolean success = rootDir.mkdir();
			if (!success) {
				throw new RuntimeException("could not create directory " + this.elasticsearchRoot);
			}
		}

		// Configure Elasticsearch.
		ImmutableSettings.Builder settings =
				ImmutableSettings.settingsBuilder().put("http.enabled", "false")
						.put("path.data", this.elasticsearchRoot);

		return new NodeBuilder().local(true).settings(settings.build()).node().client();
	}

	/**
	 * Returns the ElasticsearchTemplate that the rest of the application will use to interact with Elasticsearch.
	 *
	 * @return The ElasticsearchTemplate that the rest of the application will use to interact with Elasticsearch.
	 */
	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() {
		return new ElasticsearchTemplate(this.getSearchTemplate(), new CustomEntityMapper());
	}
	/**
	 * Cache manager cache manager.
	 *
	 * @return the cache manager
	 */
	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}

	/**
	 * Eh cache cache manager eh cache manager factory bean.
	 *
	 * @return the eh cache manager factory bean
	 */
	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource("\\ehCache.xml"));
		cmfb.setShared(true);
		return cmfb;
	}

	/**
	 * Sets whether or not the application should serialize objects to JSON with nice indentations.
	 * This method is here for unit testing and calling it a (almost) any time durint the application's
	 * run will have no affect.
	 *
	 * @param indentJson True if you want JSON serialized with nice indentations and false otherwise.
	 */
	public void setIndentJson(boolean indentJson) {
		this.indentJson = indentJson;
	}

	/**
	 * Sets the root directory to store Elasticsearch files.
	 *
	 * @param elasticsearchRoot The root directory to store Elasticsearch files.
	 */
	public void setElasticsearchRoot(String elasticsearchRoot) {
		this.elasticsearchRoot = elasticsearchRoot;
	}
}
