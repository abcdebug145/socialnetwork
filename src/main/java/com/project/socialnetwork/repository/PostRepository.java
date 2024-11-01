package com.project.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT * FROM posts ORDER BY RAND()", nativeQuery = true)
    java.util.List<Post> findAll();
}
