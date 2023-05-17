package shop.mtcoding.board.module.user.controller;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.board.config.auth.JwtProvider;
import shop.mtcoding.board.core.exception.Exception400;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.service.UserService;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.dto.UserResponse;
import shop.mtcoding.board.module.user.model.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getPage(Pageable pageable) {
        Page<User> page = userService.getPage(pageable);
        List<UserDTO> content = page.getContent().stream().map(User::toDTO).toList();

        return ResponseEntity.ok(new PageImpl<>(content, pageable, page.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUser(id);
        if (user.isEmpty()) {
            throw new Exception400("유저의 정보가 존재하지 않습니다.");
        }
        return ResponseEntity.ok(user.get().toResponse());
    }

    @PostMapping("/join")
    public ResponseEntity<User> join(@Valid @RequestBody JoinRequest joinRequest, BindingResult result) {

        if (result.hasErrors()) {
            throw new Exception400(result.getAllErrors().get(0).getDefaultMessage());
        }

        User user = userService.userJoin(joinRequest);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {

        if (result.hasErrors()) {
            throw new Exception400(result.getAllErrors().get(0).getDefaultMessage());
        }

        String jwtToken = userService.userLogin(loginRequest);

        return ResponseEntity.ok().header(JwtProvider.HEADER, jwtToken).body("로그인 성공");
    }
}
