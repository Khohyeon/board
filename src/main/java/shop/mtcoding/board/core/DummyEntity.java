package shop.mtcoding.board.core;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.util.status.UserStatus;

import java.time.LocalDateTime;

public class DummyEntity {
    public User newUser(String username){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(username+"@nate.com")
                .role("USER")
                .status(UserStatus.ACTIVE)
                .build();
    }

    public User newMockUser(Integer id, String username){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .id(id)
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .email(username+"@nate.com")
                .role("USER")
                .status(UserStatus.ACTIVE)
                .build();
    }
}
