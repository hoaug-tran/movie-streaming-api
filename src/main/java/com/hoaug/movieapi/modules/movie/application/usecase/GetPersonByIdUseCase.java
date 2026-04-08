package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.PersonMapper;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;

@Component
public class GetPersonByIdUseCase {

  private final PersonRepository personRepository;
  private final PersonMapper personMapper;

  public GetPersonByIdUseCase(PersonRepository personRepository, PersonMapper personMapper) {
    this.personRepository = personRepository;
    this.personMapper = personMapper;
  }

  public PersonResponse execute (Long id) {
    var person = personRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.PERSON_NOT_FOUND));
    return personMapper.toResponse(person);
  }
}
