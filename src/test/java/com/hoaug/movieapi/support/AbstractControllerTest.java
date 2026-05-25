package com.hoaug.movieapi.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


public abstract class AbstractControllerTest extends AbstractIntegrationTest {

  protected static final String API_PREFIX = "/api/v1";

  
  protected ResultActions performGet (String endpoint) throws Exception {
    return mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON));
  }

  
  protected ResultActions performGet (String endpoint, Object pathVar) throws Exception {
    return mockMvc.perform(get(endpoint, pathVar).contentType(MediaType.APPLICATION_JSON));
  }

  
  protected ResultActions performGet (String endpoint, Object... pathVars) throws Exception {
    return mockMvc.perform(get(endpoint, pathVars).contentType(MediaType.APPLICATION_JSON));
  }

  
  protected ResultActions performPost (String endpoint, Object body) throws Exception {
    return mockMvc
        .perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  
  protected ResultActions performPost (String endpoint) throws Exception {
    return mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON));
  }

  
  protected ResultActions performPut (String endpoint, Object body) throws Exception {
    return mockMvc
        .perform(put(endpoint).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  
  protected ResultActions performPut (String endpoint, Object pathVar, Object body)
      throws Exception {
    return mockMvc.perform(
        put(endpoint, pathVar).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  
  protected ResultActions performPatch (String endpoint, Object body) throws Exception {
    return mockMvc
        .perform(patch(endpoint).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  
  protected ResultActions performDelete (String endpoint) throws Exception {
    return mockMvc.perform(delete(endpoint).contentType(MediaType.APPLICATION_JSON));
  }

  
  protected ResultActions performDelete (String endpoint, Object pathVar) throws Exception {
    return mockMvc.perform(delete(endpoint, pathVar).contentType(MediaType.APPLICATION_JSON));
  }

  
  protected ResultActions performDeleteWithAuth (String endpoint, String token) throws Exception {
    return mockMvc.perform(delete(endpoint).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token));
  }

  
  protected ResultActions performGetWithAuth (String endpoint, String token) throws Exception {
    return mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token));
  }

  
  protected ResultActions performPostWithAuth (String endpoint, Object body, String token)
      throws Exception {
    return mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)).header("Authorization", "Bearer " + token));
  }

  
  protected ResultActions performPutWithAuth (String endpoint, Object body, String token)
      throws Exception {
    return mockMvc.perform(put(endpoint).contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)).header("Authorization", "Bearer " + token));
  }

  
  protected ResultActions perform (MockHttpServletRequestBuilder builder) throws Exception {
    return mockMvc.perform(builder);
  }
}

