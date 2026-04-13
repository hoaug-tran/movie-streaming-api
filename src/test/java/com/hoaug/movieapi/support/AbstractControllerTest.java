package com.hoaug.movieapi.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Base controller test class providing convenient HTTP request methods. Extends
 * AbstractIntegrationTest to inherit Spring Boot test configuration and JSON utilities.
 */
public abstract class AbstractControllerTest extends AbstractIntegrationTest {

  protected static final String API_PREFIX = "/api/v1";

  /**
   * Performs a GET request to the specified endpoint.
   *
   * @param endpoint the API endpoint path
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performGet (String endpoint) throws Exception {
    return mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Performs a GET request with path parameters.
   *
   * @param endpoint the API endpoint path
   * @param pathVar  the path variable value
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performGet (String endpoint, Object pathVar) throws Exception {
    return mockMvc.perform(get(endpoint, pathVar).contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Performs a GET request with multiple path parameters.
   *
   * @param endpoint the API endpoint path
   * @param pathVars the path variable values
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performGet (String endpoint, Object... pathVars) throws Exception {
    return mockMvc.perform(get(endpoint, pathVars).contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Performs a POST request with a request body.
   *
   * @param endpoint the API endpoint path
   * @param body     the request body object
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performPost (String endpoint, Object body) throws Exception {
    return mockMvc
        .perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  /**
   * Performs a POST request without a request body.
   *
   * @param endpoint the API endpoint path
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performPost (String endpoint) throws Exception {
    return mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Performs a PUT request with a request body.
   *
   * @param endpoint the API endpoint path
   * @param body     the request body object
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performPut (String endpoint, Object body) throws Exception {
    return mockMvc
        .perform(put(endpoint).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  /**
   * Performs a PUT request with path variable and body.
   *
   * @param endpoint the API endpoint path template
   * @param pathVar  the path variable value
   * @param body     the request body object
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performPut (String endpoint, Object pathVar, Object body)
      throws Exception {
    return mockMvc.perform(
        put(endpoint, pathVar).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  /**
   * Performs a PATCH request with a request body.
   *
   * @param endpoint the API endpoint path
   * @param body     the request body object
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performPatch (String endpoint, Object body) throws Exception {
    return mockMvc
        .perform(patch(endpoint).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
  }

  /**
   * Performs a DELETE request to the specified endpoint.
   *
   * @param endpoint the API endpoint path
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performDelete (String endpoint) throws Exception {
    return mockMvc.perform(delete(endpoint).contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Performs a DELETE request with a path variable.
   *
   * @param endpoint the API endpoint path template
   * @param pathVar  the path variable value
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performDelete (String endpoint, Object pathVar) throws Exception {
    return mockMvc.perform(delete(endpoint, pathVar).contentType(MediaType.APPLICATION_JSON));
  }

  /**
   * Performs a DELETE request with authentication header.
   *
   * @param endpoint the API endpoint path
   * @param token    the authentication token
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performDeleteWithAuth (String endpoint, String token) throws Exception {
    return mockMvc.perform(delete(endpoint).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token));
  }

  /**
   * Performs a GET request with authentication header.
   *
   * @param endpoint the API endpoint path
   * @param token    the authentication token
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performGetWithAuth (String endpoint, String token) throws Exception {
    return mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token));
  }

  /**
   * Performs a POST request with authentication header and body.
   *
   * @param endpoint the API endpoint path
   * @param body     the request body object
   * @param token    the authentication token
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performPostWithAuth (String endpoint, Object body, String token)
      throws Exception {
    return mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)).header("Authorization", "Bearer " + token));
  }

  /**
   * Performs a PUT request with authentication header and body.
   *
   * @param endpoint the API endpoint path
   * @param body     the request body object
   * @param token    the authentication token
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions performPutWithAuth (String endpoint, Object body, String token)
      throws Exception {
    return mockMvc.perform(put(endpoint).contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)).header("Authorization", "Bearer " + token));
  }

  /**
   * Performs a custom request with the provided builder.
   *
   * @param builder the MockHttpServletRequestBuilder
   * @return ResultActions for assertions and chaining
   * @throws Exception if the request fails
   */
  protected ResultActions perform (MockHttpServletRequestBuilder builder) throws Exception {
    return mockMvc.perform(builder);
  }
}

