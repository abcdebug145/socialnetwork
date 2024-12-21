package com.project.socialnetwork.entity;

import java.util.List;
import java.util.Set;

import com.project.socialnetwork.enums.AccountStatus;
import com.project.socialnetwork.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String fullName;
    private String address;
    private String email;
    private String avatar;
    private String about;
    private AccountStatus status;
    private int unreadNoti = 0;
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<PostLiked> postLikeds;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<Notification> notifications;

}
