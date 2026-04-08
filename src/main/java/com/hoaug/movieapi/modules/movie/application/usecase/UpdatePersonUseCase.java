package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;
import com.hoaug.movieapi.modules.movie.application.dto.request.UpdatePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.PersonMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Person;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;

@Component
public class UpdatePersonUseCase {

  private final PersonRepository personRepository;
  private final PersonMapper personMapper;

  public UpdatePersonUseCase(PersonRepository personRepository, PersonMapper personMapper) {
    this.personRepository = personRepository;
    this.personMapper = personMapper;
  }

  public PersonResponse execute (Long id, UpdatePersonRequest request) {
    Person person = personRepository.findById(id)
        .orElseThrow( () -> new AppException(ErrorCode.PERSON_NOT_FOUND));

    person.setFullName(request.getFullName());
    person.setStageName(request.getStageName());
    person.setBiography(request.getBiography());
    person.setBirthDate(request.getBirthDate());
    person.setNationality(request.getNationality());
    person.setAvatarUrl(request.getAvatarUrl());

    Person saved = personRepository.save(person);
    return personMapper.toResponse(saved);
  }
}
