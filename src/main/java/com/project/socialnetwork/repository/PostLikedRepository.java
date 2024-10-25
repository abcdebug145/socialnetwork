package com.project.socialnetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.domain.Account;
import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostLiked;

@Repository
public interface PostLikedRepository extends JpaRepository<PostLiked, Long> {
    List<Post> findAllByAccounts_Id(Long accountId);

    List<Account> findAllByPosts_Id(Long postId);
}
