package com.calendar.app.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
    @NotEmpty
    private String companyId;

}
