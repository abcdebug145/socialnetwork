package com.project.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.entity.Account;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostLiked;

@Repository
public interface PostLikedRepository extends JpaRepository<PostLiked, Long> {
    @Query("SELECT pl FROM PostLiked pl WHERE pl.account.id = :accountId")
    List<PostLiked> findByAccount_Id(Long accountId);

    List<PostLiked> findAllByPost_Id(Long postId);

    @Query("SELECT pl FROM PostLiked pl WHERE pl.account = :account AND pl.post = :post")
    PostLiked findByAccountAndPost(Account account, Post post);

    PostLiked findByPostId(Long postId);

    void deleteByPost(Post post);
}
