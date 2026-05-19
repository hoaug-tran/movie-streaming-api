package com.hoaug.movieapi.support;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Base service/usecase test class providing common setup for all service layer tests. Uses Mockito
 * for mocking dependencies.
 */
@ExtendWith(MockitoExtension.class)
public abstract class AbstractServiceTest {

  protected ObjectMapper objectMapper = JsonMapper.builder().build();

  /**
   * Converts an object to JSON string.
   *
   * @param obj the object to convert
   * @return JSON string representation of the object
   * @throws RuntimeException if JSON serialization fails
   */
  protected String toJson (Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize object to JSON", e);
    }
  }

  /**
   * Converts a JSON string to an object of the specified type.
   *
   * @param json the JSON string to convert
   * @param type the target class type
   * @return deserialized object of the specified type
   * @throws RuntimeException if JSON deserialization fails
   */
  protected <T> T fromJson (String json, Class<T> type) {
    try {
      return objectMapper.readValue(json, type);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize JSON to object", e);
    }
  }
}

