package com.jose_santamaria.helpdesk_api.exceptions;
import java.util.stream.Collectors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    
    public ResponseEntity<ErrorResponse>manejarRecursoNoEncontrado(RecursoNoEncontradoException ex){

        ErrorResponse error = new ErrorResponse(404, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionValidacion(MethodArgumentNotValidException ex) {

         String mensajes = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(400, mensajes);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionGenerica(Exception ex) {

        ErrorResponse error = new ErrorResponse(500,"Ocurrio un error interno, intentalo mas tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicados(RecursoDuplicadoException ex) {

        ErrorResponse error = new ErrorResponse(409,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);

    }



    
}