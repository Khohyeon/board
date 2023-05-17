package shop.mtcoding.board.mock;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.mtcoding.board.config.auth.JwtProvider;
import shop.mtcoding.board.module.user.controller.UserController;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.model.UserRepository;
import shop.mtcoding.board.module.user.service.UserService;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.util.status.UserStatus;

import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
public class UserMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("유저 조회")
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
                get("/user?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)
//                        .with(csrf())

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
    void getUserFail() throws Exception {

        // given
        int id = 0;
        given(this.userService.getUser(id)).willReturn(Optional.empty());

        // When
        ResultActions perform = this.mvc.perform(
                get("/user/{id}", id)
//                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
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
    void getUserDetail() throws Exception {

        // given
        int id = 0;
        given(this.userService.getUser(id))
                .willReturn(
                        Optional.of(new User(1, "유저네임", "비밀번호", "이메일", "USER", UserStatus.ACTIVE))
                );


        // When
        ResultActions perform = this.mvc.perform(
                get("/user/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
//                        .with(csrf())
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username").value("유저네임"))
                .andExpect(jsonPath("$.password").value("비밀번호"))
                .andExpect(jsonPath("$.email").value("이메일"))
        ;
    }

    @Test
    @DisplayName("유저 회원가입 성공")
    void JoinUser() throws Exception {

        // given
        JoinRequest request = new JoinRequest("ssar", "1234", "ssar@nate.com");
        given(this.userService.userJoin(request)).willReturn(request.toEntity());


        // when
        ResultActions perform = this.mvc.perform(
                post("/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf())
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

        // stub
//        User cos = newMockUser(1, "cos");
//        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(cos));


        // when
        ResultActions perform = this.mvc.perform(
                post("/user/join")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf())
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

        if (userOptional.isPresent()) {
            String jwt = JwtProvider.create(userOptional.get());

            given(this.userService.userLogin(request)).willReturn(userOptional);


            // when
            ResultActions perform = this.mvc.perform(
                    post("/user/login")
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
                    post("/user/login")
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
