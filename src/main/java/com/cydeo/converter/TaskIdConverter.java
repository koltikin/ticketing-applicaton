package com.cydeo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskIdConverter implements Converter<String,Long> {

    @Override
    public Long convert(String id) {
        return Long.parseLong(id);
    }
}
