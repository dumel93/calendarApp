package com.calendar.app.service;

import com.calendar.app.config.DataBaseHolder;
import com.calendar.app.dto.CompanyRequest;
import com.calendar.app.entity.Company;
import com.calendar.app.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final TenantService tenantService;

    public Optional<Company> findByTenantId(String companyId) {
        return companyRepository.findById(UUID.fromString(companyId));
    }

    public void create(CompanyRequest request) {
        tenantService.initDatabase(request.getName());
        DataBaseHolder.setCurrentDb(request.getName());
    }
}
