package com.calendar.app.dto;


import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponse {

    private String id;
    private String name;
    private String address;
    private UserResponse manager;
}
