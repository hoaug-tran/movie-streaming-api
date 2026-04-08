package com.hoaug.movieapi.modules.movie.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMoviePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MoviePersonMapper;
import com.hoaug.movieapi.modules.movie.domain.model.MoviePerson;
import com.hoaug.movieapi.modules.movie.domain.model.MoviePersonRole;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.entity.MoviePersonEntity;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMoviePersonRepository;

@Component
public class CreateMoviePersonUseCase {

  private final JpaMoviePersonRepository jpaMoviePersonRepository;
  private final PersonRepository personRepository;
  private final MoviePersonMapper moviePersonMapper;

  public CreateMoviePersonUseCase(JpaMoviePersonRepository jpaMoviePersonRepository,
      PersonRepository personRepository, MoviePersonMapper moviePersonMapper) {
    this.jpaMoviePersonRepository = jpaMoviePersonRepository;
    this.personRepository = personRepository;
    this.moviePersonMapper = moviePersonMapper;
  }

  public MoviePersonResponse execute (Long movieId, CreateMoviePersonRequest request) {
    MoviePersonEntity entity = new MoviePersonEntity();
    entity.setMovieId(movieId);
    entity.setPersonId(request.getPersonId());
    entity.setRole(MoviePersonRole.valueOf(request.getRole()));
    entity.setCharacterName(request.getCharacterName());
    entity.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
    entity.setCreatedAt(LocalDateTime.now());

    var savedEntity = jpaMoviePersonRepository.save(entity);

    MoviePerson moviePerson = new MoviePerson();
    moviePerson.setId(savedEntity.getId());
    moviePerson.setMovieId(savedEntity.getMovieId());
    moviePerson.setPersonId(savedEntity.getPersonId());
    moviePerson.setRole(savedEntity.getRole());
    moviePerson.setCharacterName(savedEntity.getCharacterName());
    moviePerson.setDisplayOrder(savedEntity.getDisplayOrder());
    moviePerson.setCreatedAt(savedEntity.getCreatedAt());

    var person = personRepository.findById(savedEntity.getPersonId()).get();
    return moviePersonMapper.toResponse(moviePerson, person);
  }
}
