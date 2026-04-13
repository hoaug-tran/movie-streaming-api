package com.hoaug.movieapi.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseService<T, ID> {

  T create (T entity);

  T update (ID id, T entity);

  void delete (ID id);

  Optional<T> findById (ID id);

  List<T> findAll ();

  Page<T> findAll (Pageable pageable);

  boolean exists (ID id);

  long count ();
}
