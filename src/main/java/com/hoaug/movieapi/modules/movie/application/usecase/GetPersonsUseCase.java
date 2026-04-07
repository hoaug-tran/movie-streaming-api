package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.PersonMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;

@Component
public class GetPersonsUseCase {

  private final PersonRepository personRepository;
  private final PersonMapper personMapper;

  public GetPersonsUseCase(PersonRepository personRepository, PersonMapper personMapper) {
    this.personRepository = personRepository;
    this.personMapper = personMapper;
  }

  public List<PersonResponse> execute () {
    return personRepository.findAll().stream().map(personMapper::toResponse).toList();
  }
}
