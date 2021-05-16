package com.laboschqpa.imageconverter.api.errorhandling;

import com.laboschqpa.imageconverter.exceptions.TooManyActiveJobsException;
import com.laboschqpa.imageconverter.exceptions.UnAuthorizedException;
import com.laboschqpa.imageconverter.exceptions.apierrordescriptor.ApiErrorDescriptorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {
    private static final Logger loggerOfChild = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        loggerOfChild.error("Cannot parse incoming HTTP message!", ex);

        return new ResponseEntity<>(new ApiErrorResponseBody(ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ApiErrorDescriptorException.class})
    protected ResponseEntity<ApiErrorResponseBody> handleApiErrorDescriptorException(
            ApiErrorDescriptorException e, WebRequest request) {
        loggerOfChild.trace("handleApiErrorDescriptorException() caught exception while executing api request!", e);
        return new ResponseEntity<>(new ApiErrorResponseBody(e.getApiErrorDescriptor(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TooManyActiveJobsException.class)
    protected ResponseEntity<String> handlTooManyActiveJobs(
            Exception e, WebRequest request) {
        loggerOfChild.debug("TooManyActiveJobsException caught while executing api request!", e);
        return new ResponseEntity<>("Too many active jobs", HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<ApiErrorResponseBody> handleUnAuthorized(
            Exception e, WebRequest request) {
        loggerOfChild.error("UnAuthorizedException caught while executing api request!", e);
        return new ResponseEntity<>(new ApiErrorResponseBody(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorResponseBody> handleGenericException(
            Exception e, WebRequest request) {
        loggerOfChild.error("Exception caught while executing api request!", e);
        return new ResponseEntity<>(new ApiErrorResponseBody(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
