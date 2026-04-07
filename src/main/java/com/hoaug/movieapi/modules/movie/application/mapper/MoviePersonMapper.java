package com.hoaug.movieapi.modules.movie.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.domain.model.MoviePerson;
import com.hoaug.movieapi.modules.movie.domain.model.Person;

@Component
public class MoviePersonMapper {

  private final PersonMapper personMapper;

  public MoviePersonMapper(PersonMapper personMapper) {
    this.personMapper = personMapper;
  }

  public MoviePersonResponse toResponse (MoviePerson moviePerson, Person person) {
    MoviePersonResponse response = new MoviePersonResponse();
    response.setId(moviePerson.getId());
    response.setRole(moviePerson.getRole().name());
    response.setCharacterName(moviePerson.getCharacterName());
    response.setDisplayOrder(moviePerson.getDisplayOrder());
    if (person != null) {
      response.setPerson(personMapper.toResponse(person));
    }
    return response;
  }
}
