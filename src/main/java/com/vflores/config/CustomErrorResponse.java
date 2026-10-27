package com.vflores.config;

import java.time.LocalDateTime;

public record CustomErrorResponse (
        LocalDateTime timestamp,
        String message,
        String path
) {
}
