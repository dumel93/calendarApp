package com.calendar.app.entity;

import com.calendar.app.entity.Company;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "master_tenants")
public class MasterTenant {

    @Id
    @Column(name = "tenant_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID tenantId;

    @Size(max = 50)
    @Column(name = "schema_name",nullable = false)
    private String schemaName;

}
