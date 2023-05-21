package shop.mtcoding.board.module.board.dto;

import org.springframework.hateoas.Link;

public record BoardResponse(

        String title,

        String content

) {
}
