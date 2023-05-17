package shop.mtcoding.board.module.board.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.mtcoding.board.module.board.dto.BoardDTO;
import shop.mtcoding.board.module.board.dto.BoardResponse;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.dto.UserResponse;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.util.status.BoardStatus;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOARD")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;
    @ManyToOne
    private User user;

    private BoardStatus status;

    public BoardDTO toDTO() {
        return new BoardDTO(id, title, content);
    }

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public BoardResponse toResponse() {
        return new BoardResponse(title, content);
    }
}
