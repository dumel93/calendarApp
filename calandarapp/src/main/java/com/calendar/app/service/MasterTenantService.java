package com.calendar.app.service;

import com.calendar.app.entity.MasterTenant;
import com.calendar.app.repository.MasterTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class MasterTenantService {

    private final MasterTenantRepository masterTenantRepository;

    public Optional<MasterTenant> findByTenantId(String companyId) {
        return masterTenantRepository.findByTenantId(UUID.fromString(companyId));
    }
}
