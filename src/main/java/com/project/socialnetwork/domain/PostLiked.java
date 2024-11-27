package com.project.socialnetwork.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "posts_liked")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostLiked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date = new Date();

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIncludeProperties("id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIncludeProperties({ "id", "username" })
    private Account account;
}
