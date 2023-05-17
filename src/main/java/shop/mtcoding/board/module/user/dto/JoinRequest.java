package shop.mtcoding.board.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import shop.mtcoding.board.module.user.model.User;

public record JoinRequest(
        @NotBlank(message = "유저 이름을 입력해주세요.")
        String username,
        @NotBlank(message = "유저 비밀번호를 입력해주세요.")
        String password,
        @NotBlank(message = "유저 이메일을 입력해주세요.")
        String email

) {

    public User toEntity() {
        return new User(username, password, email);
    }
}
