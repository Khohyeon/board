package shop.mtcoding.board.module.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.board.module.user.status.UserStatus;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByRole(String role);

    Optional<User> findByIdAndStatus(Integer id, UserStatus status);

}
