package com.notarius.repository;

import com.notarius.model.HandledPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HandledPartRepository extends JpaRepository<HandledPart, Long> {

    HandledPart findHandledPartByOriginalHandledPart(String originalHandledPart);
}