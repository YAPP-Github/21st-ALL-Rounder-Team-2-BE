package com.yapp.archiveServer.archive.domain;


import com.yapp.archiveServer.global.common.BaseEntity;
import com.yapp.archiveServer.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Exhibit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDraft;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String review;

    @Column(nullable = false)
    private LocalDate postDate;

    private String attachedLink;

    private LocalDate publishedAt;
}
