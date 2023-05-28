package shop.mtcoding.board.module.user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.module.user.status.UserStatus;

import java.time.format.DateTimeFormatter;

@Relation(value = "user", collectionRelation = "users")
@Getter
@Setter
public class UserModel extends RepresentationModel<UserModel> {

    Integer id;

    String username;

    String password;

    String email;

    RoleType role;

    String createDate;

    UserStatus status;


    public UserModel(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.status = user.getStatus();
        if (user.getCreatedDate() == null ) {
            user.changeCreatedDate(null);
        }
        this.createDate = user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
