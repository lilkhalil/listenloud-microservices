package com.lilkhalil.user.advice;

import com.lilkhalil.user.controller.UserController;
import com.lilkhalil.user.exception.UserAlreadyExistsException;
import com.lilkhalil.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerAdvice {

    @ExceptionHandler({UserNotFoundException.class, UserAlreadyExistsException.class})
    public ResponseEntity<?> handleUserControllerException(RuntimeException e, HttpServletRequest request) {
        return new ResponseEntity<>(AbstractExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getServletPath())
                .build(), HttpStatus.BAD_REQUEST);
    }

}
