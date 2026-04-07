package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreatePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.PersonMapper;
import com.hoaug.movieapi.modules.movie.domain.model.Person;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.PersonEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaPersonRepository;

@Component
public class CreatePersonUseCase {

  private final JpaPersonRepository jpaPersonRepository;
  private final PersonMapper personMapper;

  public CreatePersonUseCase(JpaPersonRepository jpaPersonRepository, PersonMapper personMapper) {
    this.jpaPersonRepository = jpaPersonRepository;
    this.personMapper = personMapper;
  }

  public PersonResponse execute (CreatePersonRequest request) {
    PersonEntity entity = new PersonEntity();
    entity.setFullName(request.getFullName());
    entity.setStageName(request.getStageName());
    entity.setBiography(request.getBiography());
    entity.setNationality(request.getNationality());
    entity.setAvatarUrl(request.getAvatarUrl());
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());

    if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) {
      entity.setBirthDate(java.time.LocalDate.parse(request.getBirthDate()));
    }

    var savedEntity = jpaPersonRepository.save(entity);

    Person person = new Person();
    person.setId(savedEntity.getId());
    person.setFullName(savedEntity.getFullName());
    person.setStageName(savedEntity.getStageName());
    person.setBiography(savedEntity.getBiography());
    person.setBirthDate(savedEntity.getBirthDate());
    person.setNationality(savedEntity.getNationality());
    person.setAvatarUrl(savedEntity.getAvatarUrl());

    return personMapper.toResponse(person);
  }
}
