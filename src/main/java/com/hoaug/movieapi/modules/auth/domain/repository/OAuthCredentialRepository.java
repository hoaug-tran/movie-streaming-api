package com.hoaug.movieapi.modules.auth.domain.repository;

import java.util.List;
import java.util.Optional;

import com.hoaug.movieapi.modules.auth.domain.model.OAuthCredential;

public interface OAuthCredentialRepository {
  OAuthCredential save (OAuthCredential credential);

  Optional<OAuthCredential> findById (Long id);

  Optional<OAuthCredential> findByUserIdAndProvider (Long userId, String provider);

  Optional<OAuthCredential> findByOauthIdAndProvider (String oauthId, String provider);

  List<OAuthCredential> findByUserId (Long userId);

  List<OAuthCredential> findActiveCredentialsByUserId (Long userId);

  void delete (OAuthCredential credential);

  void deleteById (Long id);

  boolean existsByOauthIdAndProvider (String oauthId, String provider);
}
