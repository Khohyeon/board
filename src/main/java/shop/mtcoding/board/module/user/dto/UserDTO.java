package shop.mtcoding.board.module.user.dto;

import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.module.user.model.User;

public record UserDTO(
        Integer id,
        String username,
        String password,
        String email,
        RoleType role
) {

    public User toEntity() {
        return new User(username, password, email);
    }

}
