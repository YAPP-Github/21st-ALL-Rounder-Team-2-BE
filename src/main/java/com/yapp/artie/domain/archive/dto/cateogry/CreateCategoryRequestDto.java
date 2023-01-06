package com.yapp.artie.domain.archive.dto.cateogry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCategoryRequestDto {

  private final String name;
}
