package com.noteif.repository;

import java.util.UUID;

import com.noteif.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByExternalProviderId(String externalProviderId);
}
