package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
