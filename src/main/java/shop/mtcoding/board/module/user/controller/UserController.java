package shop.mtcoding.board.module.user.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.board.auth.JwtProvider;
import shop.mtcoding.board.auth.MyUserDetails;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.exception.Exception400;
import shop.mtcoding.board.exception.Exception403;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.user.assemble.UserModelAssembler;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserModel;
import shop.mtcoding.board.module.user.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@ExposesResourceFor(UserModel.class)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UserModel>> getPage(
            Pageable pageable,
                   PagedResourcesAssembler<User> assembler) {
        Page<User> page = userService.getPage(pageable);
        return ResponseEntity.ok(assembler.toModel(page, new UserModelAssembler()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUser(
        @AuthenticationPrincipal MyUserDetails myUserDetails,
        @PathVariable Integer id
    ) {
        // 관리자는 모든 유저를 볼 수 있음
        if (!myUserDetails.getUser().getId().equals(id)) {
            if (!myUserDetails.getUser().getRole().equals(RoleType.ADMIN) && !myUserDetails.getUser().getRole().equals(RoleType.USER)) {
                throw new Exception403("권한이 없습니다.");
            }
        }

        // 일반 사용자는 자기 데이터가 맞는지 체크하고 exception 발생
        Optional<User> userOptional = userService.getUser(myUserDetails.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new Exception400("유저의 정보가 존재하지 않습니다.");
        }
        User user = userOptional.get();
        return ResponseEntity.ok(new UserModelAssembler().toModel(user));
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

        }
        String jwt = JwtProvider.create(userOptional.get());
        User user = userOptional.get();
        return ResponseEntity.ok().header(JwtProvider.HEADER, jwt).body(user);

    }
}
