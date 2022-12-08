package com.yapp.archiveServer.imagetag.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
public class ImageTagId implements Serializable {

    private Long tagId;
    private Long imageId;
}
