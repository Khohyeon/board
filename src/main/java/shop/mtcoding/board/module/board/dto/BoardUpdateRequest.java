package shop.mtcoding.board.module.board.dto;

import jakarta.validation.constraints.NotBlank;

public record BoardUpdateRequest(

        @NotBlank(message = "제목을 입력해주세요.")
        String title,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
}
