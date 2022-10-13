package com.calendar.app.service;

import com.calendar.app.dto.CompanyRequest;
import com.calendar.app.entity.Company;

import java.util.Optional;

public interface CompanyService {

    void create(CompanyRequest request);
    Optional<Company> findByTenantId(String companyId);
}
