package com.hoaug.movieapi.modules.streaming.application.dto.request;

import java.nio.file.Path;

public record HlsTranscodeRequest(
    Path sourcePath,
    Path outputDirectory,
    Path keyPath,
    String keyUri,
    String segmentPrefix
) {
  public HlsTranscodeRequest(Path sourcePath, Path outputDirectory, Path keyPath, String keyUri) {
    this(sourcePath, outputDirectory, keyPath, keyUri, null);
  }
}
