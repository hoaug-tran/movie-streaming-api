package com.hoaug.movieapi.modules.streaming.application.dto.response;

import java.nio.file.Path;

public record HlsTranscodeResult(
    Path playlistPath,
    String playlistUrl
) {
}
