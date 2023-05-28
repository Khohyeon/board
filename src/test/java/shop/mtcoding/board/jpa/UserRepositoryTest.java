package shop.mtcoding.board.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("유저 JPA 테스트")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUp("ssar", "1234", "ssar@nate.com");
    }

    @Test
    @Transactional
    @DisplayName("유저 조회 테스트")
    void selectAll() {
        List<User> userList = userRepository.findAll();
        Assertions.assertNotEquals(userList.size(), 0);

        User user = userList.get(0);
        Assertions.assertEquals(user.getUsername(), "ssar");

    }

    @Test
    @Transactional
    @DisplayName("유저 id 조회 테스트")
    void selectById() {
        Optional<User> optionalUser = userRepository.findById(1);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Assertions.assertEquals(user.getUsername() , "ssar");
        }
    }

    @Test
    @Transactional
    @DisplayName("유저 수정 테스트")
    void update() {
        Optional<User> optionalUser = userRepository.findById(1);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String email = "ssar1@nate.com";
            user.setEmail(email);
            Assertions.assertEquals(user.getEmail() , "ssar1@nate.com");
        }
    }

    @Test
    @Transactional
    @DisplayName("유저 삽입 및 삭제 테스트")
    void insertAndDelete() {
        User user = setUp("cos", "1234", "cos@nate.com");
        Optional<User> findUser = this.userRepository.findById(user.getId());

        if (findUser.isPresent()) {
            var result = findUser.get();
            Assertions.assertEquals(result.getUsername(), "cos");
            entityManager.remove(result);
            Optional<User> deleteUser = this.userRepository.findById(user.getId());

            deleteUser.ifPresent(Assertions::assertNull);
        }

    }

    public User setUp(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        return entityManager.persist(user);
    }

}
