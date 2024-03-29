package com.lilkhalil.user.advice;

import com.lilkhalil.user.controller.UserController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = UserController.class)
@Slf4j
public class UserControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleUserControllerException(RuntimeException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return new ResponseEntity<>(AbstractExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getServletPath())
                .build(), HttpStatus.BAD_REQUEST);
    }

}
