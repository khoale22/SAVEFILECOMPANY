package com.heb.scaleMaintenance.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * Converter for EPlum Message to and from database.
 *
 * @author m314029
 * @since 2.20.0
 */
@Converter(autoApply = true)
public class EPlumMessageConverter implements AttributeConverter<EPlumMessage, String> {

	@Override
	public String convertToDatabaseColumn(EPlumMessage ePlumMessage) {
		try {
			return objectMapper().writeValueAsString(ePlumMessage);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public EPlumMessage convertToEntityAttribute(String dbData) {
		try {
			return objectMapper().readValue(dbData, EPlumMessage.class);
		} catch (IOException e) {
			throw new RuntimeException("Unable to deserialize to json field ", e);
		}
	}

	private static ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return om;
	}
}
