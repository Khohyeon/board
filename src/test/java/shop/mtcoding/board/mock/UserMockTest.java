package shop.mtcoding.board.mock;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.mtcoding.board.config.auth.JwtProvider;
import shop.mtcoding.board.config.security.SecurityConfig;
import shop.mtcoding.board.core.WithMockCustomUser;
import shop.mtcoding.board.module.user.controller.UserController;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.model.UserRepository;
import shop.mtcoding.board.module.user.service.UserService;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.util.status.UserStatus;

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

    @BeforeEach
    public void setup() {
        // 인증된 Mock 사용자 설정
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("유저 전체조회 성공")
    void ListUser() throws Exception {

        // given
        List<User> users = userRepository.findAll();
        given(this.userService.userList()).willReturn(users);

        // When
        ResultActions perform = this.mvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code").value("1"))
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.msg").value("유저전체보기"))
        ;
    }

    @Test
    @DisplayName("유저 조회 페이지")
    void getUser() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        Page<User> page = new PageImpl<>(
                List.of(
                        new User(1, "ssar", "1234", "ssar@nate.com", "USER", UserStatus.ACTIVE),
                        new User(2, "cos", "1234", "cos@nate.com", "USER", UserStatus.ACTIVE)
                )
        );

        // given
        given(this.userService.getPage(pageable)).willReturn(page);


        // When
        ResultActions perform = this.mvc.perform(
                get("/users/page?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)

        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].username").value("ssar"))
                .andExpect(jsonPath("$.content[0].password").value("1234"))
                .andExpect(jsonPath("$.content[0].email").value("ssar@nate.com"))
                .andExpect(jsonPath("$.content[0].role").value("USER"))


                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].username").value("cos"))
                .andExpect(jsonPath("$.content[1].password").value("1234"))
                .andExpect(jsonPath("$.content[1].email").value("cos@nate.com"))
                .andExpect(jsonPath("$.content[1].role").value("USER"))
        ;
    }

    @Test
    @DisplayName("유저 상세조회 실패")
    @WithMockCustomUser
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
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.msg").value("유저의 정보가 존재하지 않습니다."))
        ;
    }

    @Test
    @DisplayName("유저 상세조회")
    @WithMockCustomUser()
    void getUserDetail() throws Exception {

        // given
        int id = 1;
        given(this.userService.getUser(id))
                .willReturn(
                        Optional.of(new User(1, "cos", "1234", "cos@nate.com", "USER", UserStatus.ACTIVE))
                );


        // When
        ResultActions perform = this.mvc.perform(
                get("/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.username").value("cos"))
                .andExpect(jsonPath("$.data.password").value("1234"))
                .andExpect(jsonPath("$.data.email").value("cos@nate.com"))
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
                .andExpect(jsonPath("$.data.username").value("ssar"))
                .andExpect(jsonPath("$.data.password").value("1234"))
                .andExpect(jsonPath("$.data.email").value("ssar@nate.com"));
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
                .andExpect(jsonPath("$.msg").value("유저 이름을 입력해주세요."));
    }


    @Test
    @DisplayName("유저 로그인 성공")
    void loginUser() throws Exception {

        //given
        LoginRequest request = new LoginRequest("ssar", "1234");

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
        LoginRequest request = new LoginRequest("", "1234");

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
