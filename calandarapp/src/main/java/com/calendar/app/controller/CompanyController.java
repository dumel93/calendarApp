package com.calendar.app.controller;


import com.calendar.app.dto.CompanyRequest;
import com.calendar.app.service.CompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/companies")
public class CompanyController {

    private final CompanyServiceImpl companyService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createCompany(@RequestBody @Valid CompanyRequest request) {
        log.info("createCompany {}", request);
        companyService.create(request);
    }
}
