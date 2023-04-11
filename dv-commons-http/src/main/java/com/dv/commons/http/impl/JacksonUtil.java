package com.dv.commons.http.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class JacksonUtil {

    private static ObjectMapper defaultObjectMapper = null;

    static {
        defaultObjectMapper = objectMapper();
    }

    private static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 不包含null字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.FALSE);
        // 针对传统时间处理
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 添加localdatetime支持
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            }
        });
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return new Date(Long.parseLong(p.getValueAsString())).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
            }
        });
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    public static ObjectMapper getObjectMapperDefault() {
        return defaultObjectMapper;
    }

    public static ObjectMapper getObjectMapperNewInstance() {
        return objectMapper();
    }

    @SneakyThrows
    public static <T> T readValue(String json, Class<T> clazz) {
        return defaultObjectMapper.readValue(json, clazz);
    }

    @SneakyThrows
    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        return defaultObjectMapper.readValue(json, typeReference);
    }

    @SneakyThrows
    public static JsonNode readTree(String json) {
        return defaultObjectMapper.readTree(json);
    }

    @SneakyThrows
    public static String writeValueAsString(Object o) {
        return defaultObjectMapper.writeValueAsString(o);
    }

    @SneakyThrows
    public static byte[] writeValueAsBytes(Object o) {
        return defaultObjectMapper.writeValueAsBytes(o);
    }
}
