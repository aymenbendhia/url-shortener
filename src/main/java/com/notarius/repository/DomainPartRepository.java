package com.notarius.repository;

import com.notarius.model.DomainPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainPartRepository extends JpaRepository<DomainPart, Long> {
    DomainPart findDomainPartByFixedUrl(String domainPart);
}