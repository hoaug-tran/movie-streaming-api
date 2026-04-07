package com.hoaug.movieapi.modules.movie.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.domain.model.Person;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaPersonRepository;

@Component
public class PersonRepositoryAdapter implements PersonRepository {

  private final JpaPersonRepository jpaPersonRepository;

  public PersonRepositoryAdapter(JpaPersonRepository jpaPersonRepository) {
    this.jpaPersonRepository = jpaPersonRepository;
  }

  @Override
  public List<Person> findAll () {
    return jpaPersonRepository.findAll().stream().map(entity -> {
      Person person = new Person();
      person.setId(entity.getId());
      person.setFullName(entity.getFullName());
      person.setStageName(entity.getStageName());
      person.setBiography(entity.getBiography());
      person.setBirthDate(entity.getBirthDate());
      person.setNationality(entity.getNationality());
      person.setAvatarUrl(entity.getAvatarUrl());
      person.setCreatedAt(entity.getCreatedAt());
      person.setUpdatedAt(entity.getUpdatedAt());
      return person;
    }).toList();
  }

  @Override
  public Person findById (Long id) {
    return jpaPersonRepository.findById(id).map(entity -> {
      Person person = new Person();
      person.setId(entity.getId());
      person.setFullName(entity.getFullName());
      person.setStageName(entity.getStageName());
      person.setBiography(entity.getBiography());
      person.setBirthDate(entity.getBirthDate());
      person.setNationality(entity.getNationality());
      person.setAvatarUrl(entity.getAvatarUrl());
      person.setCreatedAt(entity.getCreatedAt());
      person.setUpdatedAt(entity.getUpdatedAt());
      return person;
    }).orElse(null);
  }

  @Override
  public Person save (Person person) {
    com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.PersonEntity entity = new com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.PersonEntity();
    entity.setId(person.getId());
    entity.setFullName(person.getFullName());
    entity.setStageName(person.getStageName());
    entity.setBiography(person.getBiography());
    entity.setBirthDate(person.getBirthDate());
    entity.setNationality(person.getNationality());
    entity.setAvatarUrl(person.getAvatarUrl());
    entity.setCreatedAt(person.getCreatedAt());
    entity.setUpdatedAt(person.getUpdatedAt());

    var savedEntity = jpaPersonRepository.save(entity);

    Person result = new Person();
    result.setId(savedEntity.getId());
    result.setFullName(savedEntity.getFullName());
    result.setStageName(savedEntity.getStageName());
    result.setBiography(savedEntity.getBiography());
    result.setBirthDate(savedEntity.getBirthDate());
    result.setNationality(savedEntity.getNationality());
    result.setAvatarUrl(savedEntity.getAvatarUrl());
    result.setCreatedAt(savedEntity.getCreatedAt());
    result.setUpdatedAt(savedEntity.getUpdatedAt());
    return result;
  }

  @Override
  public void delete (Long id) {
    jpaPersonRepository.deleteById(id);
  }
}
