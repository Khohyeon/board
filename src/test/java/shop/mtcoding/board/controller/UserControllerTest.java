package shop.mtcoding.board.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.core.WithMockCustomUser;
import shop.mtcoding.board.interfaceTest.AbstractIntegrated;
import shop.mtcoding.board.module.user.dto.JoinRequest;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.status.UserStatus;

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
    @DisplayName("유저 전체보기 페이지 테스트")
    void getPage() throws Exception {

        this.mockMvc.perform(
                        get("/users/page")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("user-list-page",
                                responseFields(
                                ).and(getUserListPageResponseField())
                        )

                );
    }

    @Test
    @DisplayName("유저 세부정보 테스트")
    void detailUser() throws Exception {

        this.mockMvc.perform(
                        get("/users/detail")
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
                        get("/users/detail")
                                .accept(MediaType.APPLICATION_JSON)
//                                .header("Authorization", getUser())
                )
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(
                        document("user-detail-fail"));
    }

    @Test
    @DisplayName("유저 회원가입 테스트")
    void userJoin() throws Exception {

        JoinRequest joinDTO = new JoinRequest("ssar", "1234", "ssar@nate.com");


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

    private FieldDescriptor[] getUserListResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("[].id").description("유저 id"),
                fieldWithPath("[].username").description("유저 이름"),
                fieldWithPath("[].password").description("유저 비밀번호"),
                fieldWithPath("[].email").description("유저 이메일"),
                fieldWithPath("[].role").description("유저 권한"),
                fieldWithPath("[].status").description("유저 상태"),
                fieldWithPath("[].createdDate").description("가입 시간"),
                fieldWithPath("[].modifiedDate").description("수정 시간"),
        };
    }

    private FieldDescriptor[] getUserResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("id").description("유저 id"),
                fieldWithPath("username").description("유저 이름"),
                fieldWithPath("password").description("유저 비밀번호"),
                fieldWithPath("email").description("유저 이메일"),
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
                fieldWithPath("content[].id").description("유저 id"),
//                fieldWithPath("content[].title").description("게시판 제목"),
//                fieldWithPath("content[].content").description("게시판 내용"),
                fieldWithPath("content[].username").description("유저 이름"),
                fieldWithPath("content[].password").description("유저 비밀번호"),
                fieldWithPath("content[].email").description("유저 이메일"),
                fieldWithPath("content[].role").description("유저 권한"),
                fieldWithPath("pageable.sort").description("정렬 정보"),
                fieldWithPath("pageable.sort.empty").description("정렬 없음"),
                fieldWithPath("pageable.sort.sorted").description("정렬됨"),
                fieldWithPath("pageable.sort.unsorted").description("정렬되지 않음"),
                fieldWithPath("pageable.offset").description("오프셋"),
                fieldWithPath("pageable.pageSize").description("페이지 크기"),
                fieldWithPath("pageable.pageNumber").description("페이지 번호"),
                fieldWithPath("pageable.paged").description("페이징 여부"),
                fieldWithPath("pageable.unpaged").description("페이징 안함"),
                fieldWithPath("last").description("마지막 페이지 여부"),
                fieldWithPath("totalPages").description("전체 페이지 수"),
                fieldWithPath("totalElements").description("전체 요소 수"),
                fieldWithPath("size").description("현재 페이지 크기"),
                fieldWithPath("number").description("현재 페이지 번호"),
                fieldWithPath("sort.empty").description("정렬 정보 없음"),
                fieldWithPath("sort.sorted").description("정렬됨"),
                fieldWithPath("sort.unsorted").description("정렬되지 않음"),
                fieldWithPath("first").description("첫 번째 페이지 여부"),
                fieldWithPath("numberOfElements").description("현재 페이지의 요소 수"),
                fieldWithPath("empty").description("비어 있는지 여부")


        };
    }
}
