package com.noteif.repository;

import java.util.UUID;

import com.noteif.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ApplicationRespository extends JpaRepository<Application, UUID> {
}
