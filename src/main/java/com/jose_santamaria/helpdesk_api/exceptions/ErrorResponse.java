package com.jose_santamaria.helpdesk_api.exceptions;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
      private int status;
    private String message;
    private LocalDateTime timeStamp;

    public ErrorResponse(int status,String message){

        this.status= status;
        this.message= message;
        this.timeStamp= LocalDateTime.now();
    }
    
}
