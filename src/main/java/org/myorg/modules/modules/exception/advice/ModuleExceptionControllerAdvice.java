package org.myorg.modules.modules.exception.advice;

import org.myorg.modules.modules.exception.ModuleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.function.Function;

import static org.myorg.modules.modules.exception.ModuleExceptionBuilder.INTERNAL_SERVER_ERROR_CODE;

@RestControllerAdvice
public class ModuleExceptionControllerAdvice {

    @ExceptionHandler(value = ModuleException.class)
    public ResponseEntity<?> handle(ModuleException e) {
        Function<HttpStatus, ResponseEntity<?>> generateResponse = (status) -> new ResponseEntity<>(e, status);
        String code = e.getCode();
        if (INTERNAL_SERVER_ERROR_CODE.equals(code)) {
            return generateResponse.apply(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return generateResponse.apply(HttpStatus.CONFLICT);
        }
    }
}
