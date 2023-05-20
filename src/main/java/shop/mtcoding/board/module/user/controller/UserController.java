package shop.mtcoding.board.module.user.controller;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.board.auth.JwtProvider;
import shop.mtcoding.board.auth.MyUserDetails;
import shop.mtcoding.board.exception.Exception400;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.service.UserService;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.dto.UserResponse;
import shop.mtcoding.board.module.user.model.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> UserList() {
        List<User> users = userService.userList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserDTO>> getPage(Pageable pageable) {
        Page<User> page = userService.getPage(pageable);
        List<UserDTO> content = page.getContent().stream().map(User::toDTO).toList();

        return ResponseEntity.ok(new PageImpl<>(content, pageable, page.getTotalElements()));
    }

    @GetMapping("/detail")
    public ResponseEntity<UserResponse> getUser(
        @AuthenticationPrincipal MyUserDetails myUserDetails) {

        Optional<User> userOptional = userService.getUser(myUserDetails.getUser().getId());

        if (userOptional.isEmpty()) {
            throw new Exception400("유저의 정보가 존재하지 않습니다.");
        } else {
            User user = userOptional.get();

            return ResponseEntity.ok(user.toResponse());
        }

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
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {

        if (result.hasErrors()) {
            throw new Exception400(result.getAllErrors().get(0).getDefaultMessage());
        }

        Optional<User> userOptional = userService.userLogin(loginRequest);

        if (userOptional.isEmpty()) {
            throw new Exception400("username과 password를 다시 확인해주세요.");

        } else {
            String jwt = JwtProvider.create(userOptional.get());

            User user = userOptional.get();

            return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(user);
        }

    }
}
