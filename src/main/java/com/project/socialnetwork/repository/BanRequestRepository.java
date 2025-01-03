package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.BanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BanRequestRepository extends JpaRepository<BanRequest, Long> {
    
    
}
