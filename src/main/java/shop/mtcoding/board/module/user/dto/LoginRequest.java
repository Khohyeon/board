package shop.mtcoding.board.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {

    @NotBlank(message = "유저 이름을 입력해주세요.")
    private String username;

    @NotBlank(message = "유저  비밀번호를 입력해주세요.")
    private String password;

}
