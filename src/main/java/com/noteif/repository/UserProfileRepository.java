package com.noteif.repository;

import java.util.UUID;

import com.noteif.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    UserProfile findByUserId(UUID userId);
}
