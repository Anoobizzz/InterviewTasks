package io.github.anoobizzz.mintostask.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class GeolocationApiException extends ExternalApiException {
    private static final String MESSAGE = "Failed to retrieve geolocation information from external api";

    public GeolocationApiException(final HttpStatus statusCode) {
        super(MESSAGE, statusCode);
    }

    public GeolocationApiException(Exception e) {
        super(MESSAGE, e, INTERNAL_SERVER_ERROR);
    }
}
