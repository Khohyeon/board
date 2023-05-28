package shop.mtcoding.board.module.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.exception.Exception403;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserRepository;
import shop.mtcoding.board.module.user.status.UserStatus;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Page<User> getPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUser(Integer id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE);
    }

    @Transactional
    public User userJoin(JoinRequest joinRequest) {
//        String encodePassword = passwordEncoder.encode(joinRequest.password());

        User user = new User(joinRequest.username(), joinRequest.password(), joinRequest.email(), RoleType.USER, UserStatus.ACTIVE);
        userRepository.save(user);
        return user;
    }

    public Optional<User> userLogin(LoginRequest loginRequest) {
//        String encodePassword = passwordEncoder.encode(loginRequest.password());
        return userRepository.findByUsernameAndPassword(loginRequest.username(), loginRequest.password());
    }

}
