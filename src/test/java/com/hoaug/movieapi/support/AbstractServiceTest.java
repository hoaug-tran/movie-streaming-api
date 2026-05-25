package com.hoaug.movieapi.support;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;


@ExtendWith(MockitoExtension.class)
public abstract class AbstractServiceTest {

  protected ObjectMapper objectMapper = JsonMapper.builder().build();

  
  protected String toJson (Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize object to JSON", e);
    }
  }

  
  protected <T> T fromJson (String json, Class<T> type) {
    try {
      return objectMapper.readValue(json, type);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize JSON to object", e);
    }
  }
}

