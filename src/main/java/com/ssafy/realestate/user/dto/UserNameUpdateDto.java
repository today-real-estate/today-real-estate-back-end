package com.ssafy.realestate.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserNameUpdateDto {
    @Id
    private Long userId;

    @NotBlank(message = "이름 입력하세요.")
    @Length(min = 2, max = 10, message = "2~10자리의 이름을 입력하세요.")
    private String userName;

}