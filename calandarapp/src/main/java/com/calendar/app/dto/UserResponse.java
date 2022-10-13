package com.calendar.app.dto;


import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String userId;
    private String position;
    private String userName;
    private String email;

}
