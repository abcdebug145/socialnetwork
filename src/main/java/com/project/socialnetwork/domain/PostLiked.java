package com.project.socialnetwork.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "posts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostLiked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "account_post_liked", joinColumns = @JoinColumn(name = "post_liked_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts;

    @ManyToMany
    @JoinTable(name = "account_post_liked", joinColumns = @JoinColumn(name = "post_liked_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> posts;
}
