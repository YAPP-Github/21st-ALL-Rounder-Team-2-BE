package com.yapp.archiveServer.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class DemoTeam {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
