package com.calendar.app.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    static final String MASTER = "default_database";
    static final String DEFAULT_TENANT = "company1";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = DataBaseHolder.getCurrentDb();
        log.info("current tenant {}", tenant);
        return StringUtils.isNotBlank(tenant) ? tenant : DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
