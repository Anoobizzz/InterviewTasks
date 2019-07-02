package io.github.anoobizzz.mintostask.exception;

import org.springframework.http.HttpStatus;

abstract class ExternalApiException extends RuntimeException {
    private final HttpStatus httpStatus;

    ExternalApiException(final String message, final Throwable cause, final HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    ExternalApiException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
