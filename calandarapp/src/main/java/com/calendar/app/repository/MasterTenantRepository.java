package com.calendar.app.repository;

import com.calendar.app.entity.Company;
import com.calendar.app.entity.MasterTenant;
import com.calendar.app.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface MasterTenantRepository extends CrudRepository<MasterTenant, UUID> {

    Optional<MasterTenant> findByTenantId(UUID companyId);
    List<MasterTenant> findAll();
}
