package shop.mtcoding.board.mock;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.mtcoding.board.auth.JwtProvider;
import shop.mtcoding.board.auth.MyUserDetails;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.config.SecurityConfig;
import shop.mtcoding.board.core.WithMockCustomUser;
import shop.mtcoding.board.example.UserExample;
import shop.mtcoding.board.module.user.controller.UserController;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.model.UserRepository;
import shop.mtcoding.board.module.user.service.UserService;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.status.UserStatus;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
// Bean 대신에 가짜 객체를 사용하는 어노테이션
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("유저 MOCK 테스트")
@Import(SecurityConfig.class)
public class UserMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("유저 전체 조회")
    void getUser() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        Page<User> page = new PageImpl<>(
                List.of(
                        new User(1, "ssar", "1234", "ssar@nate.com", RoleType.USER, UserStatus.ACTIVE),
                        new User(2, "cos", "1234", "cos@nate.com", RoleType.USER, UserStatus.ACTIVE)
                )
        );

        // given
        given(this.userService.getPage(pageable)).willReturn(page);


        // When
        ResultActions perform = this.mvc.perform(
                get("/users?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)

        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.users[0].id").value(1))
                .andExpect(jsonPath("$._embedded.users[0].username").value("ssar"))
                .andExpect(jsonPath("$._embedded.users[0].password").value("1234"))
                .andExpect(jsonPath("$._embedded.users[0].email").value("ssar@nate.com"))
                .andExpect(jsonPath("$._embedded.users[0].role").value("USER"))
                .andExpect(jsonPath("$._embedded.users[0]._links.self.href").value("http://localhost/users/1"))


                .andExpect(jsonPath("$._embedded.users[1].id").value(2))
                .andExpect(jsonPath("$._embedded.users[1].username").value("cos"))
                .andExpect(jsonPath("$._embedded.users[1].password").value("1234"))
                .andExpect(jsonPath("$._embedded.users[1].email").value("cos@nate.com"))
                .andExpect(jsonPath("$._embedded.users[1].role").value("USER"))
                .andExpect(jsonPath("$._embedded.users[1]._links.self.href").value("http://localhost/users/2"))
        ;
    }

    @Test
    @DisplayName("유저 상세조회 시큐리티에 걸리는 실패(인증 x)")
    void getUserFail() throws Exception {

        // given
        int id = 0;
        given(this.userService.getUser(id)).willReturn(Optional.empty());

        // When
        ResultActions perform = this.mvc.perform(
                get("/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );


        // Then
        perform
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("인증되지 않았습니다"))
        ;
    }

    @Test
    @DisplayName("유저 상세조회 실패(권한이 다름)")
    @WithMockCustomUser(id = 2, username = "cos", role = RoleType.MANAGER)
    void getUserFail2() throws Exception {

        // given
        int id = 0;
        given(this.userService.getUser(id)).willReturn(Optional.empty());

        // When
        ResultActions perform = this.mvc.perform(
                get("/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );


        // Then
        perform
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("권한이 없습니다."))
        ;
    }

    @Test
    @DisplayName("유저 상세조회 실패(유저가 없음)")
    @WithMockCustomUser
    void getUserFail3() throws Exception {

        // given
        int id = 0;
        given(this.userService.getUser(id)).willReturn(Optional.empty());

        // When
        ResultActions perform = this.mvc.perform(
                get("/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("유저의 정보가 존재하지 않습니다."))
        ;
    }

    @Test
    @DisplayName("유저 상세조회")
    @WithMockCustomUser
    void getUserDetail() throws Exception {

        User user = UserExample.user;
        LocalDateTime now = LocalDateTime.now();
        user.changeCreatedDate(now);

        // given
        given(this.userService.getUser(user.getId()))
                .willReturn(
                        Optional.of( new User(1, "ssar", "1234", "ssar@nate.com", RoleType.USER, UserStatus.ACTIVE))
                );

        // When
        ResultActions perform = this.mvc.perform(
                get("/users/id", user.getId())
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username").value("cos"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andExpect(jsonPath("$.email").value("cos@nate.com"))
        ;
    }

    @Test
    @DisplayName("유저 회원가입 성공")
    void joinUser() throws Exception {

        // given
        JoinRequest request = new JoinRequest("ssar", "1234", "ssar@nate.com");
        given(this.userService.userJoin(request)).willReturn(request.toEntity());


        // when
        ResultActions perform = this.mvc.perform(
                post("/users/join")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username").value("ssar"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andExpect(jsonPath("$.email").value("ssar@nate.com"));
    }

    @Test
    @DisplayName("유저 회원가입 실패(Valid)")
    void JoinUserFail() throws Exception {

        // given
        JoinRequest request = new JoinRequest("", "21323", "ssar@nate.com");
        given(this.userService.userJoin(request)).willReturn(request.toEntity());

        // when
        ResultActions perform = this.mvc.perform(
                post("/users/join")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // then
        perform.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("유저 이름을 입력해주세요."));
    }


    @Test
    @DisplayName("유저 로그인 성공")
    void loginUser() throws Exception {

        //given
        LoginRequest request = new LoginRequest("ssar", "1234", UserStatus.ACTIVE);

        //mock test에 레파지토리 사용?
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        given(this.userService.userLogin(request)).willReturn(userOptional);

        if (userOptional.isPresent()) {
            String jwt = JwtProvider.create(userOptional.get());
            // when
            ResultActions perform = this.mvc.perform(
                    post("/users/login")
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            perform.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.msg").value("로그인 성공"));
        }
    }

    @Test
    @DisplayName("유저 로그인 실패(Valid)")
    void loginUserFail() throws Exception {

        //given
        LoginRequest request = new LoginRequest("", "1234", UserStatus.ACTIVE);

        //mock test에 레파지토리 사용?
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (userOptional.isPresent()) {
            String jwt = JwtProvider.create(userOptional.get());

            given(this.userService.userLogin(request)).willReturn(userOptional);



            // when
            ResultActions perform = this.mvc.perform(
                    post("/users/login")
                            .content(objectMapper.writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            // then
            perform.andExpect(status().isBadRequest())
                    .andDo(print())
                    .andExpect(jsonPath("$.msg").value("유저 이름을 입력해주세요."));
        }
    }
}
