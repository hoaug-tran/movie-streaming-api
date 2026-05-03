package com.hoaug.movieapi.modules.streaming.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.stereotype.Component;

import com.hoaug.movieapi.common.enums.ErrorCode;
import com.hoaug.movieapi.common.exception.AppException;

@Component
public class HlsEncryptionKeyService {
  private static final int AES_128_KEY_BYTES = 16;
  private final SecureRandom secureRandom = new SecureRandom();

  public String writeNewKey (Path keyPath) {
    byte[] key = new byte[AES_128_KEY_BYTES];
    secureRandom.nextBytes(key);

    try {
      Files.createDirectories(keyPath.getParent());
      Files.write(keyPath, key);
      return newIvHex();
    } catch (IOException exception) {
      throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  private String newIvHex () {
    byte[] iv = new byte[AES_128_KEY_BYTES];
    secureRandom.nextBytes(iv);
    return HexFormat.of().formatHex(iv);
  }
}
