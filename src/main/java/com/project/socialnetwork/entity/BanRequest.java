package com.project.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ban_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanRequest {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "post_id")
    
    private Post post;
    private String reason;
    private boolean isAccepted;
    private boolean isRead;
}
