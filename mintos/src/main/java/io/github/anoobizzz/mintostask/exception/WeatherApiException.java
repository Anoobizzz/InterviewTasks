package io.github.anoobizzz.mintostask.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class WeatherApiException extends ExternalApiException {
    private static final String MESSAGE = "Failed to retrieve weather information from external api";

    public WeatherApiException(final HttpStatus statusCode) {
        super(MESSAGE, statusCode);
    }

    public WeatherApiException(Exception e) {
        super(MESSAGE, e, INTERNAL_SERVER_ERROR);
    }
}
