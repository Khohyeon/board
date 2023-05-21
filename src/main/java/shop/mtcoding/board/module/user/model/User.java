package shop.mtcoding.board.module.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.mtcoding.board.common.BaseTime;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.dto.UserResponse;
import shop.mtcoding.board.module.user.status.UserStatus;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USERS")
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Builder
    public User(Integer id, String username, String password, String email, String role, UserStatus status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, String role, UserStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public UserDTO toDTO() {
        return new UserDTO(id, username, password, email, role);
    }

    public UserResponse toResponse() {
        return new UserResponse(id, username, password, email);
    }
}
