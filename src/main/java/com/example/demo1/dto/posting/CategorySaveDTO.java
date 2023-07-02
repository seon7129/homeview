package com.example.demo1.dto.posting;

import com.example.demo1.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CategorySaveDTO {

    @NotBlank(message = "카테고리명을 입력해주세요")
    @Pattern(regexp = "^.{2,30}$", message = "2 ~ 30 자리의 이름을 작성해주세요")
    private String name; // 카테고리명

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .build();
    }

}
