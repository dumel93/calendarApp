package com.calendar.app.controller.handler;

import com.calendar.app.dto.ApiError;
import com.calendar.app.exceptions.InvalidInputParameterException;
import com.calendar.app.exceptions.CompanyAlreadyExistsException;
import com.calendar.app.exceptions.LocationNotFoundException;
import com.calendar.app.exceptions.MeetingNotFoundException;
import com.calendar.app.i18n.I18nLocalizedRuntimeException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;


    @ExceptionHandler({
            CompanyAlreadyExistsException.class,
            InvalidInputParameterException.class
    })
    public ResponseEntity<Object> handleExceptions(I18nLocalizedRuntimeException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessageCode(), ex.getErrorMessageArguments(), locale);
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), errorMessage), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({
            LocationNotFoundException.class,
            MeetingNotFoundException.class
    })
    public ResponseEntity<Object> handle404Exceptions(I18nLocalizedRuntimeException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessageCode(), ex.getErrorMessageArguments(), locale);
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND.value(), errorMessage), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            BadCredentialsException.class,
            ExpiredJwtException.class,
            IllegalArgumentException.class,
            JwtException.class
    })
    public ResponseEntity<Object> handle401Exceptions(RuntimeException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(),ex.getStackTrace(), locale);
        return new ResponseEntity<>(new ApiError(HttpStatus.UNAUTHORIZED.value(), errorMessage), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFound(RuntimeException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(),ex.getStackTrace(), locale);
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND.value(), errorMessage), HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return handleMethodArgumentNotValid(ex, null);
    }

    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Locale locale) {
        String errorMessage = getErrorMessageFromMethodArgumentNotValidException(ex, locale);
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.value(), errorMessage), HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessageFromMethodArgumentNotValidException(MethodArgumentNotValidException ex, Locale locale) {
        String errorMessage;
        try {
            BindingResult result = ex.getBindingResult();
            List<FieldError> fieldErrors = result.getFieldErrors();
            String tempErrorMessage = fieldErrors.stream().map(e -> "Error in field: '" + e.getField() + "' : '" + e.getDefaultMessage() + "'")
                    .collect(Collectors.joining(". "));
            errorMessage = messageSource.getMessage(tempErrorMessage, ex.getSuppressed(), locale);
        } catch (Exception e) {
            log.error("Error during preparing exception message: " + e.getMessage());
            errorMessage = messageSource.getMessage(ex.getMessage(), ex.getSuppressed(), locale);
        }
        return errorMessage;
    }


}
