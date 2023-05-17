package shop.mtcoding.board.module.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.mtcoding.board.config.auth.JwtProvider;
import shop.mtcoding.board.core.exception.Exception400;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserRepository;

import java.util.Optional;

@Service
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

    public String userLogin(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOptional.isPresent()) {
            String jwt = JwtProvider.create(userOptional.get());
            return jwt;
        } else {
            throw new Exception400("username과 password를 다시 확인해주세요.");
        }
    }

    public User userJoin(UserDTO userDTO) {
        User user = userRepository.save(userDTO.toEntity());
        return user;
    }
}
