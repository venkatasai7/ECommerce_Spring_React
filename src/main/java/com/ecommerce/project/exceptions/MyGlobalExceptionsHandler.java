package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.APIResponse;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;



@RestControllerAdvice

public class MyGlobalExceptionsHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        Map <String,String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName,errorMessage);
        });

        return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e)
    {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);

        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(APIExceptions.class)
    public ResponseEntity<APIResponse> myAPIExceptions(APIExceptions e)
    {
        String errorMessage = e.getMessage();
        APIResponse apiResponse = new APIResponse(errorMessage, false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);

    }

}
