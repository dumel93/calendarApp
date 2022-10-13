package com.calendar.app.service;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TenantServiceImpl implements TenantService {

    private DataSource dataSource;

    public TenantServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initDatabase(String schema) {
        Flyway flyway = Flyway.configure()
                .locations("db/migration/tenants")
                .dataSource(dataSource)
                .schemas(schema)
                .load();
        flyway.migrate();
    }

}
