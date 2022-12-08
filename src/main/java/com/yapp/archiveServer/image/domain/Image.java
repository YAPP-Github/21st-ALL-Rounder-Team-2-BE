package com.yapp.archiveServer.image.domain;

import com.yapp.archiveServer.global.common.BaseEntity;
import com.yapp.archiveServer.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private int seq;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isMainImage;

    @Column(columnDefinition = "text")
    private String description;
}
