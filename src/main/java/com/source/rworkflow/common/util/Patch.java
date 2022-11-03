package com.source.rworkflow.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class Patch {
    public static <EntityT, EntityClass, RequestT> EntityClass entityByRequest(EntityT entity, Class<EntityClass> entityClazz, RequestT request){
        final var entityMapper = new ObjectMapper();

        Map<String, Object> entityAsMap = mapper(entity, entityMapper);
        Map<String, Object> requestAsMap = mapper(request);

        entityAsMap.putAll(requestAsMap);

        return entityMapper.convertValue(entityAsMap, entityClazz);
    }

    private static <T> Map<String, Object> mapper(T o, ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());

        return mapper.convertValue(o, new TypeReference<Map<String, Object>>() {});
    }

    private static <T> Map<String, Object> mapper(T o){
        final var mapper = new ObjectMapper();
        return mapper(o, mapper);
    }
}
