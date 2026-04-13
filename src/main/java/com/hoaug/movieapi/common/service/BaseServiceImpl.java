package com.hoaug.movieapi.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

  protected abstract JpaRepository<T, ID> getRepository ();

  protected abstract String getEntityNotFoundMessage ();

  @Override
  public T create (T entity) {
    return getRepository().save(entity);
  }

  @Override
  public T update (ID id, T entity) {
    if (!exists(id)) {
      throw new IllegalArgumentException(getEntityNotFoundMessage());
    }
    return getRepository().save(entity);
  }

  @Override
  public void delete (ID id) {
    if (!exists(id)) {
      throw new IllegalArgumentException(getEntityNotFoundMessage());
    }
    getRepository().deleteById(id);
  }

  @Override
  public Optional<T> findById (ID id) {
    return getRepository().findById(id);
  }

  @Override
  public List<T> findAll () {
    return getRepository().findAll();
  }

  @Override
  public Page<T> findAll (Pageable pageable) {
    return getRepository().findAll(pageable);
  }

  @Override
  public boolean exists (ID id) {
    return getRepository().existsById(id);
  }

  @Override
  public long count () {
    return getRepository().count();
  }
}
