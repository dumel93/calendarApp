package com.calendar.app.dto;


import com.calendar.app.entity.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotEmpty
    @Size(max = 10)
    private String position;

    @NotEmpty
    @Size(max = 50)
    private String userName;

    @NotEmpty
    @Size(max = 50)
    private String email;

    @NotEmpty
    @Size(max = 100)
    private String password;

    private String defaultTimeZone;
    @NotEmpty
    private String companyId;
}
