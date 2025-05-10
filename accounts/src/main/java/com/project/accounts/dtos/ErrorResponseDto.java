package com.project.accounts.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDto {


    private String apiPath;


    private HttpStatus errorCode;


    private String errorMessage;


    private LocalDateTime errorTime;

}