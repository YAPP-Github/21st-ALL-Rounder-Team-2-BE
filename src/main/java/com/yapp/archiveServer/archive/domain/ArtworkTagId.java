package com.yapp.archiveServer.archive.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtworkTagId implements Serializable {

    private Long tagId;
    private Long artworkId;
}
