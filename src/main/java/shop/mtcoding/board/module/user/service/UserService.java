package shop.mtcoding.board.module.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.exception.Exception403;
import shop.mtcoding.board.module.board.model.BoardRepository;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserRepository;
import shop.mtcoding.board.module.user.status.UserStatus;

import java.util.List;
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
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE);
    }

    public User userJoin(JoinRequest joinRequest) {
        User user = new User(joinRequest.username(), joinRequest.password(), joinRequest.email(), RoleType.USER, UserStatus.ACTIVE);
        userRepository.save(user);
        return user;
    }

    public Optional<User> userLogin(LoginRequest loginRequest) {

        Optional<User> userOptional = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if (userOptional.isEmpty()) {
            throw new Exception403("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if (userOptional.get().getStatus().equals(UserStatus.INACTIVE)) {
            throw new Exception403("비활성화된 계정입니다.");
        }

        return userOptional;
    }

    public List<User> userList() {
        return userRepository.findAll();
    }
}
