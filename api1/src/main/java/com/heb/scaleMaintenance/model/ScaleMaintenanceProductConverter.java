package com.heb.scaleMaintenance.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * Database field to scale maintenance product converter.
 *
 * @author m314029
 * @since 1.0.0
 */
@Converter(autoApply = true)
public class ScaleMaintenanceProductConverter implements AttributeConverter<ScaleMaintenanceProduct, String> {

	@Override
	public String convertToDatabaseColumn(ScaleMaintenanceProduct attribute) {
		try {
			return objectMapper().writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public ScaleMaintenanceProduct convertToEntityAttribute(String dbData) {
		try {
			return objectMapper().readValue(dbData, ScaleMaintenanceProduct.class);
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
