package io.fagner.product.manager.handler;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public APIError handleResourceBadRequestException(BadRequestException exception) {
        return APIError.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public APIError handleResourceEntityNotFoundException(EntityNotFoundException exception) {
        return APIError.builder()
                .message(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public APIError handleResourceMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String constraintViolationMessages = exception.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(". "));
        return APIError.builder()
                .message(constraintViolationMessages)
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public APIError handleResourceDataIntegrityViolationException(DataIntegrityViolationException exception) {
        return APIError.builder()
                .message("As informações já foram cadastradas previamente.")
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}