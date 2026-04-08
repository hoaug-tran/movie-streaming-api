package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;

@Component
public class DeletePersonUseCase {

  private final PersonRepository personRepository;

  public DeletePersonUseCase(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public void execute (Long id) {
    personRepository.findById(id).orElseThrow( () -> new AppException(ErrorCode.PERSON_NOT_FOUND));
    personRepository.deleteById(id);
  }
}
