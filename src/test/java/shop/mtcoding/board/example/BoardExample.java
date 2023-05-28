package shop.mtcoding.board.example;

import org.springframework.data.domain.PageRequest;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.board.status.BoardStatus;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.status.UserStatus;

public interface BoardExample {

    Board board = new Board(1, "제목", "내용",
            new User(1, "ssar", "1234", "ssar@nate.com", RoleType.USER, UserStatus.ACTIVE)
            , BoardStatus.ACTIVE);

    PageRequest pageRequest = PageRequest.of(1, 10);

}
