package com.ssafy.realestate.user.service;

import com.ssafy.realestate.user.dto.UserLoginRequestDto;
import com.ssafy.realestate.user.dto.UserResponseDto;
import com.ssafy.realestate.user.dto.UserSignupDto;
import com.ssafy.realestate.user.encoder.BCryptPasswordEncoder;
import com.ssafy.realestate.user.entity.Authority;
import com.ssafy.realestate.user.entity.UserAuthority;
import com.ssafy.realestate.user.entity.UserEntity;
import com.ssafy.realestate.user.exception.DuplicatedEmailException;
import com.ssafy.realestate.user.exception.NoUserFoundException;
import com.ssafy.realestate.user.exception.UnmatchedPasswordCheckException;
import com.ssafy.realestate.user.repository.AuthorityRepository;
import com.ssafy.realestate.user.repository.UserAuthorityRepository;
import com.ssafy.realestate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity login(UserLoginRequestDto userLoginDto) {
        UserEntity userEntity = userRepository.findOneWithAuthoritiesByUserEmail(userLoginDto.getUserEmail())
                .orElseThrow(NoUserFoundException::new);
        if (passwordEncoder.isMatch(userLoginDto.getPassword(), userEntity.getPassword())) {
            return userEntity;
        }
        throw new UnmatchedPasswordCheckException();
    }

    @Transactional
    public UserResponseDto save(UserSignupDto userSignupDto) throws Exception {
        if (!validateDuplicatedEmail(userSignupDto.getUserEmail())) {
            throw new DuplicatedEmailException();
        }
        UserEntity signupUser = userSignupDto.toUserEntity();
        UserEntity user = UserEntity.builder()
                .userEmail(signupUser.getUserEmail())
                .password(passwordEncoder.encrypt(signupUser.getPassword()))
                .userName(signupUser.getUserName())
                .nickname(signupUser.getNickname())
                .authorities(signupUser.getAuthorities())
                .build();

        Authority auth = authorityRepository.findById(2L).orElseThrow(Exception::new);
        UserAuthority userAuth = UserAuthority.builder()
                .userEntity(user)
                .auth(auth)
                .build();
        log.info(userAuth.toString());
        userAuthorityRepository.save(userAuth);
        return UserResponseDto.from(userRepository.save(user));
    }

    public boolean validateDuplicatedEmail(String userEmail) {
        if (userRepository.existsByUserEmail(userEmail)) {
            return false;
        }
        return true;
    }
}
