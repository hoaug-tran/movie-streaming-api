package com.hoaug.movieapi.modules.movie.application.usecase;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.request.CreateMoviePersonRequest;
import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MoviePersonMapper;
import com.hoaug.movieapi.modules.movie.domain.model.MoviePerson;
import com.hoaug.movieapi.modules.movie.domain.model.MoviePersonRole;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMoviePersonRepository;

@Component
public class UpdateMoviePersonUseCase {

  private final JpaMoviePersonRepository jpaMoviePersonRepository;
  private final PersonRepository personRepository;
  private final MoviePersonMapper moviePersonMapper;

  public UpdateMoviePersonUseCase(JpaMoviePersonRepository jpaMoviePersonRepository,
      PersonRepository personRepository, MoviePersonMapper moviePersonMapper) {
    this.jpaMoviePersonRepository = jpaMoviePersonRepository;
    this.personRepository = personRepository;
    this.moviePersonMapper = moviePersonMapper;
  }

  public MoviePersonResponse execute(Long moviePersonId, CreateMoviePersonRequest request) {
    var entity = jpaMoviePersonRepository.findById(moviePersonId)
        .orElseThrow(() -> new IllegalArgumentException("MoviePerson not found: " + moviePersonId));

    if (request.getRole() != null) {
      entity.setRole(MoviePersonRole.valueOf(request.getRole()));
    }
    if (request.getCharacterName() != null) {
      entity.setCharacterName(request.getCharacterName());
    }
    if (request.getDisplayOrder() != null) {
      entity.setDisplayOrder(request.getDisplayOrder());
    }

    var saved = jpaMoviePersonRepository.save(entity);

    MoviePerson moviePerson = new MoviePerson();
    moviePerson.setId(saved.getId());
    moviePerson.setMovieId(saved.getMovieId());
    moviePerson.setPersonId(saved.getPersonId());
    moviePerson.setRole(saved.getRole());
    moviePerson.setCharacterName(saved.getCharacterName());
    moviePerson.setDisplayOrder(saved.getDisplayOrder());
    moviePerson.setCreatedAt(saved.getCreatedAt());

    var person = personRepository.findById(saved.getPersonId()).get();
    return moviePersonMapper.toResponse(moviePerson, person);
  }
}
