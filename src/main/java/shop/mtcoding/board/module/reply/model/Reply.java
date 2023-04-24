package shop.mtcoding.board.module.reply.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.mtcoding.board.module.board.model.Board;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REPLY_LIST")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

}
