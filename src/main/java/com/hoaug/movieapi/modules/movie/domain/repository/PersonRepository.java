package com.hoaug.movieapi.modules.movie.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.movie.domain.model.Person;

public interface PersonRepository {
  List<Person> findAll ();

  Optional<Person> findById (Long id);

  Person save (Person person);

  void deleteById (Long id);
}
