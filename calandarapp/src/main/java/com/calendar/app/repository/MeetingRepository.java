package com.calendar.app.repository;

import com.calendar.app.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface MeetingRepository extends JpaRepository<Meeting, UUID>, JpaSpecificationExecutor<Meeting> {

}
