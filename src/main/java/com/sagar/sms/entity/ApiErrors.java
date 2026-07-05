package com.sagar.sms.entity;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApiErrors {

    private String msg;
    private List<String> details;
    private HttpStatusCode status;
    private LocalDateTime localDateTime;

}
