package com.hoaug.movieapi.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)

@ActiveProfiles("test")
@Transactional
public abstract class AbstractIntegrationTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  
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
