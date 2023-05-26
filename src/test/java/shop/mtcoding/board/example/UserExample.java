package shop.mtcoding.board.example;

import org.springframework.data.domain.PageRequest;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.status.UserStatus;

public interface UserExample {

    User user = new User(1, "ssar", "1234", "ssar@nate.com", RoleType.USER, UserStatus.ACTIVE);

    PageRequest pageRequest = PageRequest.of(1, 10);

}
