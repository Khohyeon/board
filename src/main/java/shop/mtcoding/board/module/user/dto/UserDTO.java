package shop.mtcoding.board.module.user.dto;

import shop.mtcoding.board.module.user.model.User;

public record UserDTO(
        String username,
        String password,
        String email
) {

    public User toEntity() {
        return new User(username, password, email);
    }

}
