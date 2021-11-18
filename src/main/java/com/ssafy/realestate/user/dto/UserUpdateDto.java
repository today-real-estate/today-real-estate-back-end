package com.ssafy.realestate.user.dto;


import com.ssafy.realestate.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    @Id
    private Long userId;

    @NotBlank(message = "이름 입력하세요.")
    @Length(min = 2, max = 10, message = "2~10자리의 이름을 입력하세요.")
    private String userName;

//    @NotBlank(message = "닉네임을 입력하세요.")
//    @Length(min = 2, max = 10, message = "2~10자리의 닉네임을 입력하세요.")
//    private String nickname;

    public UserEntity toUserEntity() {
        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .userName(userName)
                .build();
        return userEntity;
    }
}