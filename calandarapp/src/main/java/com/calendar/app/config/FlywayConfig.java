package com.calendar.app.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .locations("db/migration/default")
                .dataSource(dataSource)
                .schemas(TenantIdentifierResolver.MASTER)
                .load();
        flyway.migrate();
        return flyway;
    }
//
//    @Bean
//    CommandLineRunner commandLineRunner(MasterTenantRepository repository, DataSource dataSource) {
//        return args -> repository.findAll().forEach(masterTenant -> {
//            String tenant = masterTenant.getSchemaName();
//            Flyway flyway = Flyway.configure()
//                    .locations("db/migration/tenants")
//                    .dataSource(dataSource)
//                    .schemas(tenant)
//                    .load();
//            flyway.migrate();
//        });
//    }
}
