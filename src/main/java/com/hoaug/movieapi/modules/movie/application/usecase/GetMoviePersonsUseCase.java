package com.hoaug.movieapi.modules.movie.application.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.MoviePersonResponse;
import com.hoaug.movieapi.modules.movie.application.mapper.MoviePersonMapper;
import com.hoaug.movieapi.modules.movie.domain.model.MoviePerson;
import com.hoaug.movieapi.modules.movie.domain.repository.PersonRepository;
import com.hoaug.movieapi.modules.movie.infrastructure.persistence.repository.JpaMoviePersonRepository;

@Component
public class GetMoviePersonsUseCase {

  private final JpaMoviePersonRepository jpaMoviePersonRepository;
  private final PersonRepository personRepository;
  private final MoviePersonMapper moviePersonMapper;

  public GetMoviePersonsUseCase(JpaMoviePersonRepository jpaMoviePersonRepository,
      PersonRepository personRepository, MoviePersonMapper moviePersonMapper) {
    this.jpaMoviePersonRepository = jpaMoviePersonRepository;
    this.personRepository = personRepository;
    this.moviePersonMapper = moviePersonMapper;
  }

  public List<MoviePersonResponse> execute (Long movieId) {
    return jpaMoviePersonRepository.findByMovieIdOrderByDisplayOrderAsc(movieId).stream()
        .map(entity -> {
          MoviePerson moviePerson = new MoviePerson();
          moviePerson.setId(entity.getId());
          moviePerson.setMovieId(entity.getMovieId());
          moviePerson.setPersonId(entity.getPersonId());
          moviePerson.setRole(entity.getRole());
          moviePerson.setCharacterName(entity.getCharacterName());
          moviePerson.setDisplayOrder(entity.getDisplayOrder());
          moviePerson.setCreatedAt(entity.getCreatedAt());

          var person = personRepository.findById(entity.getPersonId());
          return moviePersonMapper.toResponse(moviePerson, person);
        }).toList();
  }
}
