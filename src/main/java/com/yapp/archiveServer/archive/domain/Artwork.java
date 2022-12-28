package com.yapp.archiveServer.archive.domain;

import com.yapp.archiveServer.global.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artwork extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Exhibit exhibit;

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
