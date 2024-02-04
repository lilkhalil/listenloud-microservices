package com.lilkhalil.music.advice;

import com.lilkhalil.music.controller.AudioController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = AudioController.class)
@Slf4j
public class AudioControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<?> handleAudioControllerException(RuntimeException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return new ResponseEntity<>(AbstractExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now().toString())
                .path(request.getServletPath())
                .build(), HttpStatus.BAD_REQUEST);
    }

}
