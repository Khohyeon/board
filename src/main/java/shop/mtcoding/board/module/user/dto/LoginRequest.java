package shop.mtcoding.board.module.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {

    private String username;

    private String password;

}
