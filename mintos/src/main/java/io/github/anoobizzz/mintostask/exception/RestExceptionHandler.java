package io.github.anoobizzz.mintostask.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);
    private static final String EXCEPTION_ENCOUNTERED_MESSAGE = "Encountered api exception";
    public static final String GENERIC_ERROR_RESPONSE = "Service failed to process the request.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiError> runtimeExceptionHandler(final Exception e, final HttpServletRequest request) {
        LOG.error(EXCEPTION_ENCOUNTERED_MESSAGE, e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new RestApiError(INTERNAL_SERVER_ERROR, GENERIC_ERROR_RESPONSE));
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<RestApiError> externalApiExceptionHandler(final ExternalApiException e, final WebRequest request) {
        LOG.error(EXCEPTION_ENCOUNTERED_MESSAGE, e);
        final HttpStatus httpStatus = e.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(new RestApiError(httpStatus, e.getMessage()));
    }


    @ExceptionHandler(InsufficientInformationException.class)
    public ResponseEntity<RestApiError> insufficientInformationExceptionHandler(final InsufficientInformationException e, final HttpServletRequest request) {
        LOG.error(EXCEPTION_ENCOUNTERED_MESSAGE, e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new RestApiError(INTERNAL_SERVER_ERROR, e.getMessage() + " Resolved IP: " + request.getRemoteAddr()));
    }
}
