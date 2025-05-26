package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blog")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "status", nullable = false)
    private BlogStatus status;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_hidden", nullable = false)
    private boolean isHidden;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}
