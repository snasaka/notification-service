package com.noteif.repository;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import com.noteif.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Collection<Transaction> findByApplicationIdAndDateCreatedAfter(UUID applicationId, Date date);
}
