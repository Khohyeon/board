package shop.mtcoding.board.module.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.config.auth.JwtProvider;
import shop.mtcoding.board.core.exception.Exception400;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserRepository;
import shop.mtcoding.board.util.status.UserStatus;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Page<User> getPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUser(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User userJoin(JoinRequest joinRequest) {
        User user = new User(joinRequest.username(), joinRequest.password(), joinRequest.email(), "USER", UserStatus.ACTIVE);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public Optional<User> userLogin(LoginRequest loginRequest) {
        return userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
