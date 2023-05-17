package shop.mtcoding.board.module.board.dto;

import jakarta.validation.constraints.NotBlank;
import shop.mtcoding.board.module.board.model.Board;

public record BoardRequest(

        @NotBlank(message = "제목을 입력해주세요.")
        String title,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
        public Board toEntity() {
                return new Board(title, content);
        }
}
