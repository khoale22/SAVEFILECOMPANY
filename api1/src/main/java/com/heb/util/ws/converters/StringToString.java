package com.heb.util.ws.converters;

import java.lang.reflect.Field;

/**
 * Created by d116773 on 4/20/2016.
 */
@SuppressWarnings("rawtypes")
class StringToString implements Converter {
	@Override
	public void convert(Object source, Field field, Object object) throws IllegalAccessException {

		boolean accessible = field.isAccessible();
		try {
			field.setAccessible(true);
			if (field.getType().equals(String.class)) {
				field.set(object, source);
			}
		}catch (IllegalAccessException e) {
			throw e;
		}
		finally {
			field.setAccessible(accessible);
		}
	}

	@Override
	public boolean supports(Field field) {

		if (field == null) {
			return false;
		}

		return field.getType().equals(String.class);
	}
}
