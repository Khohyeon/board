package shop.mtcoding.board.module.board.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import shop.mtcoding.board.module.board.status.BoardStatus;
import shop.mtcoding.board.module.user.assemble.UserModelAssembler;
import shop.mtcoding.board.module.user.model.UserModel;

import java.time.format.DateTimeFormatter;

@Relation(value = "board", collectionRelation = "boards")
@Getter @Setter
public class BoardModel extends RepresentationModel<BoardModel> {
    Integer id;
    String subject;
    String content;
    String createDate;

    BoardStatus boardStatus;

    UserModel user;

    public BoardModel(Board board) {
        this.id = board.getId();
        this.subject = board.getTitle();
        this.content = board.getContent();
        if (board.getCreatedDate() == null ) {
            board.changeCreatedDate(null);
        }
        this.user = new UserModelAssembler().toModel(board.getUser());
        this.createDate = board.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
