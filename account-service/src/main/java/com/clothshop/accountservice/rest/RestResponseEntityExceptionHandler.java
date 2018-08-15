package com.clothshop.accountservice.rest;

import com.clothshop.accountservice.domain.Message;
import com.clothshop.accountservice.exception.EntityConstraintViolationException;
import com.clothshop.accountservice.exception.EntityException;
import com.clothshop.accountservice.exception.RoleNotFoundException;
import com.clothshop.accountservice.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityException.class})
    public ResponseEntity<Object> handleEntityException(Exception e, WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return new ResponseEntity<>(new Message(500, e.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({EntityConstraintViolationException.class})
    public ResponseEntity<Object> handleEntityConstraintViolationException(Exception e, WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return new ResponseEntity<>(new Message(206, e.getMessage()), headers, HttpStatus.PARTIAL_CONTENT);
    }

    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e, WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return new ResponseEntity<>(new Message(404, e.getMessage()), headers, HttpStatus.NOT_FOUND);
    }
}
