package io.github.anoobizzz.mintostask.exception;

import org.springframework.http.HttpStatus;

class RestApiError {
    private HttpStatus status;
    private String message;

    RestApiError(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
