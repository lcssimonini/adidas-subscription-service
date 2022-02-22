package com.simonini.adidas.emailservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogUtil {

    public static String asJson(Object object) {
        String loggableObject = object.toString();
        try {
            loggableObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException ignored) {
        }
        return loggableObject;
    }
}
