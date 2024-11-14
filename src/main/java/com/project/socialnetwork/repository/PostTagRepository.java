package com.project.socialnetwork.repository;

import java.util.List;

import com.project.socialnetwork.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostTag;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findAllByPost(Post post);

    List<PostTag> findByTag(Tag tag);
}
