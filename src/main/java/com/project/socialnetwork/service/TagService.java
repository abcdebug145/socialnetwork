package com.project.socialnetwork.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.socialnetwork.domain.Post;
import com.project.socialnetwork.domain.PostTag;
import com.project.socialnetwork.repository.PostTagRepository;
import com.project.socialnetwork.repository.TagRepository;

@Service
public class TagService {
    @Autowired
    private PostTagRepository postTagRepository;
    @Autowired
    private TagRepository tagRepository;

    public List<PostTag> getAllTagsOfPost(Post post) {
        return postTagRepository.findAllByPost(post);
    }
    // public Optional<Tag> getByTag(String tag) {
    // return tagRepository.findByTag(tag);
    // }
}
