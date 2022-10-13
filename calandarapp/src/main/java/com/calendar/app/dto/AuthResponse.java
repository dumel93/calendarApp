package com.calendar.app.dto;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
}
