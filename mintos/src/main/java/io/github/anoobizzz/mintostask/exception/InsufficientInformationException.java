package io.github.anoobizzz.mintostask.exception;

public class InsufficientInformationException extends RuntimeException {
    private static final String MESSAGE = "Failed to retrieve geolocation information, possibly due to IP detection failure.";

    public InsufficientInformationException() {
        super(MESSAGE);
    }
}
