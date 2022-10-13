package com.calendar.app.repository;

import com.calendar.app.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByUserName(String username);

    @Query("select u from User u where u.email in :emails")
    Set<User> findByEmails(Set<String> emails);
}
