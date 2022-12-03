package com.yapp.archiveServer.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class DemoTeam {

    @Id @GeneratedValue
    private Long id;

    private String name;

}
