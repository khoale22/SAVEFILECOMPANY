package com.heb.util.elasticsearch;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

/**
 * The default implementation of Spring Elasticsearch uses the default entity mapper, and you cannot override its
 * behaviour. This class allows us to use a different ObjectMapper and override it. This is needed because it does
 * not support LocalDate (possibly other types as well). This implementation was built based on the thread here:
 * https://github.com/jhipster/generator-jhipster/issues/2241, though there are tweaks.
 *
 * Note I tried to use the same object mapper that was created in the ApplicationConfiguration file, but it didn't work.
 *
 * @author d116773
 * @since 2.7.0
 */
public class CustomEntityMapper implements EntityMapper {

	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(CustomEntityMapper.class);

	/**
	 * Constructs a new CustomEntityMapper.
	 */
	public CustomEntityMapper() {

		CustomEntityMapper.logger.info("Constructing EntityMapper for use with Elasticsearch");

		ObjectMapper objectMapper = new ObjectMapper();

		// This is to handle the Java 8 dates.
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.registerModule(new JavaTimeModule());

		// This was added to keep it from trying to serialize the lazy object handler, though
		// it may stop other properties from being serialized.
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		this.objectMapper = objectMapper;
	}

	/**
	 * Takes an object and turns it into a JSON string.
	 *
	 * @param object The object to convert to JSON.
	 * @return The JSON that represents the object.
	 * @throws IOException
	 */
	@Override
	public String mapToString(Object object) throws IOException {
		return objectMapper.writeValueAsString(object);
	}

	/**
	 * Takes a JSON string and creates an object.
	 *
	 * @param source The JSON string to convert.
	 * @param clazz The type of class to create.
	 * @param <T> The type of class to create.
	 * @return The JSON string converted to an object.
	 * @throws IOException
	 */
	@Override
	public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
		return objectMapper.readValue(source, clazz);
	}
}
