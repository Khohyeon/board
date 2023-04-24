package shop.mtcoding.board.module.user.dto;

public record UserResponse(
        String username,
        String password,
        String email
) {
}
