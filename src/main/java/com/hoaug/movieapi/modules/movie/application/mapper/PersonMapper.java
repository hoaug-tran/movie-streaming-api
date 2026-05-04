package com.hoaug.movieapi.modules.movie.application.mapper;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.modules.movie.application.dto.response.PersonResponse;
import com.hoaug.movieapi.modules.movie.domain.model.Person;
import com.hoaug.movieapi.shared.media.MediaUrlResolver;

@Component
public class PersonMapper {
  private final MediaUrlResolver mediaUrlResolver;

  public PersonMapper (MediaUrlResolver mediaUrlResolver) {
    this.mediaUrlResolver = mediaUrlResolver;
  }

  public PersonResponse toResponse (Person person) {
    PersonResponse response = new PersonResponse();
    response.setId(person.getId());
    response.setFullName(person.getFullName());
    response.setStageName(person.getStageName());
    response.setBiography(person.getBiography());
    response.setBirthDate(person.getBirthDate());
    response.setNationality(person.getNationality());
    response.setAvatarUrl(mediaUrlResolver.resolve(person.getAvatarUrl()));
    return response;
  }
}
