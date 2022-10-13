package com.calendar.app.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component("tenantConnectionProvider")
@Slf4j
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

    private DataSource datasource;
    private final TenantIdentifierResolver tenantIdentifierResolver;

    public TenantConnectionProvider(DataSource dataSource, TenantIdentifierResolver tenantIdentifierResolver) {
        this.datasource = dataSource;
        this.tenantIdentifierResolver = tenantIdentifierResolver;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.info("getConnection "+tenantIdentifier);
        final Connection connection = getAnyConnection();
        String sql = "use " + tenantIdentifierResolver.resolveCurrentTenantIdentifier()  + ";";
        connection.createStatement().execute(sql);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.info("releaseConnection");
        String sql = "use " + tenantIdentifierResolver.resolveCurrentTenantIdentifier() + ";";
        connection.createStatement().execute(sql);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
