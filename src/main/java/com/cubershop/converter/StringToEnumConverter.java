package com.cubershop.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

@SuppressWarnings("ALL")
public class StringToEnumConverter implements ConverterFactory<String, Enum> {

    private static class StringToEnum<T extends Enum> implements Converter<String, T> {

        private Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            return (T) Enum.valueOf(this.enumType, source.trim().toUpperCase());
        }
    }

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter.StringToEnum(targetType);
    }
}
