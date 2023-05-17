package shop.mtcoding.board.module.user.dto;

public record UserResponse(
        Integer id,
        String username,
        String password,
        String email
) {
}
