package com.yapp.archiveServer.imagetag.domain;

import com.yapp.archiveServer.image.domain.Image;
import com.yapp.archiveServer.tag.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageTag {

    @EmbeddedId
    private ImageTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(nullable = false)
    private int seq;
}
