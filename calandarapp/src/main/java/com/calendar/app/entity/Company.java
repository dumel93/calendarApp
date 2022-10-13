package com.calendar.app.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @Column(name = "company_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID companyId;

    @Size(max = 50)
    @Column(name = "company_name",nullable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "company", cascade = {CascadeType.ALL})
    private Set<User> users = new HashSet<>();

}
