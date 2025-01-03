package com.project.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    //@Query(value = "SELECT * FROM posts ORDER BY RAND()", nativeQuery = true)
    java.util.List<Post> findAll();

    @Query(value = "SELECT p.* FROM posts p JOIN accounts a ON p.account_id = a.id WHERE CONCAT(p.title, a.username) LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    java.util.List<Post> search(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM posts WHERE account_id = :accountId", nativeQuery = true)
    java.util.List<Post> findByAccountId(@Param("accountId") Long accountId);
}
