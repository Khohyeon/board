package shop.mtcoding.board.module.user.dto;

import shop.mtcoding.board.module.user.model.User;

public class JoinRequest {

    private String username;
    private String password;
    private String email;

    public User toEntity() {
        return new User(username, password, email);
    }
}
