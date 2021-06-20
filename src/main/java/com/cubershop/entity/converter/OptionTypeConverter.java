package com.cubershop.entity.converter;

import com.cubershop.entity.Type;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;

@Converter
public class OptionTypeConverter implements Serializable, AttributeConverter<Type.Name, String> {

	@Override
	public String convertToDatabaseColumn(Type.Name attribute) {
		return attribute.name().substring(1);
	}

	@Override
	public Type.Name convertToEntityAttribute (String dbData) {
		return Type.Name.valueOf("_"+dbData.toLowerCase());
	}
}
