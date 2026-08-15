package com.jose_santamaria.helpdesk_api.exceptions;

import lombok.Data;

@Data
public class RecursoNoEncontradoException extends RuntimeException{

    public RecursoNoEncontradoException(String message){

        super(message);
    }
   
}
