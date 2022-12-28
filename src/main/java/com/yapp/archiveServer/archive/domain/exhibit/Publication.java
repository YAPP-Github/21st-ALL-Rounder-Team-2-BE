package com.yapp.archiveServer.archive.domain.exhibit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Publication {

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDraft;

    private LocalDate publishedAt;
}
