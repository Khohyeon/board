package shop.mtcoding.board.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.interfaceTest.AbstractIntegrated;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController 테스트")
public class UserControllerTest extends AbstractIntegrated {

    @Test
    @DisplayName("유저 세부정보 테스트")
    void detailUser() throws Exception {

        this.mockMvc.perform(
                        get ("/user/1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user-detail",
                                responseFields(
                                ).and(getPlaceResponseField())
                        )

                );
    }

    @Test
    @DisplayName("유저 회원가입 테스트")
    void userJoin() throws Exception{

        JoinRequest joinDTO = new JoinRequest("ssar", "1234", "ssar@nate.com");


        ResultActions perform = this.mockMvc.perform(
                post("/user/join")
                        .content(objectMapper.writeValueAsString(joinDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user-join",
                                requestFields(getUserJoinRequestField()),
                                responseFields().and(getUserJoinField()
                                )
                        )

                );

    }

    @Test
    @DisplayName("유저 로그인 테스트")
    void userLogin() throws Exception{

        LoginRequest loginDTO = new LoginRequest("cos", "1234");


        ResultActions perform = this.mockMvc.perform(
                post("/user/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        System.out.println("Response:" + response.getHeader("Authorization"));

        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user-login",
                                requestFields(getUserLoginRequestField()),
                                responseFields().and(getUserLoginField()
                                )
                        )

                );

    }

    private FieldDescriptor[] getPlaceResponseField() {
        return new FieldDescriptor[] {
                fieldWithPath("id").description("유저 id"),
                fieldWithPath("username").description("유저 이름"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
        };
    }

    private FieldDescriptor[] getUserJoinRequestField() {
        return new FieldDescriptor[] {
                fieldWithPath("username").description("유저이름"),
                fieldWithPath("password").description("비밀번호"),
                fieldWithPath("email").description("이메일"),
        };
    }

    private FieldDescriptor[] getUserJoinField() {
        return new FieldDescriptor[] {
                fieldWithPath("id").description("유저 id"),
                fieldWithPath("username").description("유저 이름"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
                fieldWithPath("role").description("유저 권한"),
                fieldWithPath("status").description("유저 상태"),
        };
    }

    private FieldDescriptor[] getUserLoginRequestField() {
        return new FieldDescriptor[] {
                fieldWithPath("username").description("유저이름"),
                fieldWithPath("password").description("비밀번호"),
        };
    }

    private FieldDescriptor[] getUserLoginField() {
        return new FieldDescriptor[] {
                fieldWithPath("id").description("유저 id"),
                fieldWithPath("username").description("유저 이름"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
                fieldWithPath("role").description("유저 권한"),
                fieldWithPath("status").description("유저 상태"),
        };
    }
}
