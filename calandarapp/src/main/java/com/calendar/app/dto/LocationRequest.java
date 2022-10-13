package com.calendar.app.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;
}
