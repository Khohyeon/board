package shop.mtcoding.board.integrated;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.status.UserStatus;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("UserController 테스트")
public class UserControllerTest extends AbstractIntegrated {

    @Test
    @DisplayName("유저 전체보기 테스트")
    void ListUser() throws Exception {

        this.mockMvc.perform(
                        get("/users")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user-list",
                                responseFields(
                                ).and(getUserListPageResponseField())
                        )

                );
    }

    @Test
    @DisplayName("유저 세부정보 테스트")
    void detailUser() throws Exception {

        this.mockMvc.perform(
                        get("/users/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", getUser())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user-detail",
                                responseFields(
                                ).and(getUserResponseField())
                        )

                );
    }

    @Test
    @DisplayName("비로그인 유저 조회 실패 테스트")
    void detailUserFail() throws Exception {

        this.mockMvc.perform(
                        get("/users/{id}", 1)
                                .accept(MediaType.APPLICATION_JSON)
//                                .header("Authorization", getUser())
                )
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(
                        document("user-detail-fail",
                                responseFields(getFailResponseField())));
    }

    @Test
    @DisplayName("유저 회원가입 테스트")
    void userJoin() throws Exception {

        JoinRequest joinDTO = new JoinRequest("ho", "1234", "ho@nate.com");


        ResultActions perform = this.mockMvc.perform(
                post("/users/join")
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
    @DisplayName("유저 회원가입 실패 테스트")
    void userJoinFail() throws Exception {

        JoinRequest joinDTO = new JoinRequest("", "1234", "ssar@nate.com");


        ResultActions perform = this.mockMvc.perform(
                post("/users/join")
                        .content(objectMapper.writeValueAsString(joinDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("user-join-fail",
                                responseFields(getFailResponseField())));

    }

    @Test
    @DisplayName("유저 로그인 테스트")
    void userLogin() throws Exception {

        LoginRequest loginDTO = new LoginRequest("cos", "1234", UserStatus.ACTIVE);


        ResultActions perform = this.mockMvc.perform(
                post("/users/login")
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

    @Test
    @DisplayName("유저 로그인 실패 테스트")
    void userLoginFail() throws Exception {

        LoginRequest loginDTO = new LoginRequest("", "1234", UserStatus.ACTIVE);


        ResultActions perform = this.mockMvc.perform(
                post("/users/login")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        System.out.println("Response:" + response.getHeader("Authorization"));

        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("user-login-fail",
                                responseFields(getFailResponseField())));

    }

    private FieldDescriptor[] getUserResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("id").description("유저 id"),
                fieldWithPath("username").description("유저 이름"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
                fieldWithPath("role").description("유저 권한"),
                fieldWithPath("createDate").description("유저 생성 시간"),
                fieldWithPath("status").description("유저 활성화 상태"),
                fieldWithPath("_links.self.href").description("유저 상세보기 URL"),
        };
    }

    private FieldDescriptor[] getUserJoinRequestField() {
        return new FieldDescriptor[]{
                fieldWithPath("username").description("유저이름"),
                fieldWithPath("password").description("비밀번호"),
                fieldWithPath("email").description("이메일"),
        };
    }

    private FieldDescriptor[] getUserJoinField() {
        return new FieldDescriptor[]{
                fieldWithPath("id").description("유저 id"),
                fieldWithPath("username").description("유저 이름"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
                fieldWithPath("role").description("유저 권한"),
                fieldWithPath("status").description("유저 활성화상태"),
                fieldWithPath("createdDate").description("가입 시간"),
                fieldWithPath("modifiedDate").description("수정 시간"),
        };
    }

    private FieldDescriptor[] getUserLoginRequestField() {
        return new FieldDescriptor[]{
                fieldWithPath("username").description("유저이름"),
                fieldWithPath("password").description("비밀번호"),
                fieldWithPath("status").description("유저 활성화상태"),
        };
    }

    private FieldDescriptor[] getUserLoginField() {
        return new FieldDescriptor[]{
                fieldWithPath("id").description("유저 id"),
                fieldWithPath("username").description("유저 이름"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
                fieldWithPath("role").description("유저 권한"),
                fieldWithPath("status").description("유저 활성화상태"),
                fieldWithPath("createdDate").description("가입 시간"),
                fieldWithPath("modifiedDate").description("수정 시간"),
        };
    }


    private FieldDescriptor[] getUserListPageResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("_embedded.users[].id").description("유저 id"),
                fieldWithPath("_embedded.users[].username").description("유저 이름"),
                fieldWithPath("_embedded.users[].password").description("유저 비밀번호"),
                fieldWithPath("_embedded.users[].email").description("유저 이메일"),
                fieldWithPath("_embedded.users[].role").description("유저 권한"),
                fieldWithPath("_embedded.users[].createDate").description("유저 생성시간"),
                fieldWithPath("_embedded.users[].status").description("유저 상태코드"),
                fieldWithPath("_embedded.users[]._links.self.href").description("url"),
                fieldWithPath("_links.self.href").description("page url"),
                fieldWithPath("page.size").description("page 사이즈"),
                fieldWithPath("page.totalElements").description("page 총 개수"),
                fieldWithPath("page.totalPages").description("page 총 개수"),
                fieldWithPath("page.number").description("page 번호"),


        };
    }
}
