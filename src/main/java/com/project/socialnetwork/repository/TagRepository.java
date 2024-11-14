package com.project.socialnetwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTag(String tag);
}
