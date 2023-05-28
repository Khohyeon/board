package shop.mtcoding.board.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import shop.mtcoding.board.module.user.status.UserStatus;


public record LoginRequest (

    @NotBlank(message = "유저 이름을 입력해주세요.")
    String username,

    @NotBlank(message = "유저  비밀번호를 입력해주세요.")
    String password,

    UserStatus status
) {}
