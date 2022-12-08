package com.yapp.archiveServer.demo.dto;

import com.yapp.archiveServer.demo.domain.DemoMember;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DemoSignupRequestDto {

    @NotNull
    private String name;

    public DemoMember toEntity() {
        return DemoMember.builder()
                .name(name)
                .build();
    }
}
