package com.yapp.archiveServer.homecategory.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
public class HomeCategoryId implements Serializable {

    private Long categoryId;
    private Long userId;
}
