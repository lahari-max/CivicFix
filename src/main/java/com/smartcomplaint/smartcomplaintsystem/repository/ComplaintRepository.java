package com.smartcomplaint.smartcomplaintsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcomplaint.smartcomplaintsystem.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    java.util.List<Complaint> findByUserEmail(String userEmail);
}
