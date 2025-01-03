package com.project.socialnetwork.entity;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String image;
    private int likeCount;
    private Date date = new Date();
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonIncludeProperties({ "id", "username" })
    private Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIncludeProperties({ "id" })
    private Set<PostLiked> postLikeds;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIncludeProperties({ "id", "content", "date", "account.username" })
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @JsonIncludeProperties({ "id", "content", "date", "account.username" })
    private Set<Notification> notifications;
    
}
