package com.calendar.app.specification;

import com.calendar.app.entity.Location;
import com.calendar.app.entity.Meeting;
import com.calendar.app.util.SpecificationUtils;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.ZoneId.systemDefault;
import static java.util.Objects.nonNull;

@UtilityClass
public class MeetingSpecification {

    public Specification<Meeting> findByAgendaLike(String agenda) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isNotEmpty(agenda)) {
                return criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("agenda")),
                        SpecificationUtils.getLikeWithUpperCasePrefixAndSufix(agenda),
                        '\\'
                );
            }
            return null;
        };
    }

    public Specification<Meeting> findByNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isNotEmpty(name)) {
                return criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("name")),
                        SpecificationUtils.getLikeWithUpperCasePrefixAndSufix(name),
                        '\\'
                );
            }
            return null;
        };
    }

    public static Specification<Meeting> locationIdEquals(UUID locationId) {
        return (root, query, builder) -> {
            if (nonNull(locationId)) {
                Join<Meeting, Location> location = root.join("location", JoinType.LEFT);
                root.fetch("location", JoinType.LEFT);
                return builder.equal(location.get("id"), locationId);
            }
            return null;
        };
    }

    public static Specification<Meeting> addressEquals(String address) {
        return (root, query, builder) -> {
            if (nonNull(address)) {
                Join<Meeting, Location> location = root.join("location", JoinType.LEFT);
                root.fetch("location", JoinType.LEFT);
                return builder.equal(location.get("address"), address);
            }
            return null;
        };
    }

    public static Specification<Meeting> startDateFrom(LocalDateTime start, String defaultTimeZone) {
        return (root, query, builder) -> {
            if (nonNull(start)) {
                ZoneId defaultTimeZoneId = ZoneId.of(defaultTimeZone != null ? defaultTimeZone : String.valueOf(systemDefault()));
                ZonedDateTime zonedStartTime = start.atZone(defaultTimeZoneId);
                return builder.greaterThanOrEqualTo(root.get("start"), zonedStartTime);
            }
            return null;
        };
    }

    public static Specification<Meeting> endDateTo(LocalDateTime end, String defaultTimeZone) {
        return (root, query, builder) -> {
            if (nonNull(end)) {
                ZoneId defaultTimeZoneId = ZoneId.of(defaultTimeZone != null ? defaultTimeZone : String.valueOf(systemDefault()));
                ZonedDateTime zonedStartTime = end.atZone(defaultTimeZoneId);
                return builder.lessThanOrEqualTo(root.get("end"), zonedStartTime);
            }
            return null;
        };
    }

}
