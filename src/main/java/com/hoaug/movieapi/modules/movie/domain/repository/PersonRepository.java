package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;

import com.hoaug.movieapi.modules.movie.domain.model.Person;

public interface PersonRepository {
  List<Person> findAll ();

  Person findById (Long id);

  Person save (Person person);

  void delete (Long id);
}
