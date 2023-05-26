package shop.mtcoding.board.integrated;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import shop.mtcoding.board.module.board.dto.BoardRequest;
import shop.mtcoding.board.module.board.dto.BoardUpdateRequest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BoardController 테스트")
public class BoardControllerTest extends AbstractIntegrated {

    @Test
    @DisplayName("게시판 전체보기")
    void getPage() throws Exception {

        this.mockMvc.perform(
                        get ("/board")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("board-list",
                                responseFields(
                                ).and(getBoardResponseField())
                        )

                );
    }

    @Test
    @DisplayName("게시판 상세보기")
    void getDetailPage() throws Exception {

        this.mockMvc.perform(
                        get ("/board/{id}",1, 1, "id,desc")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("board-detail-list",
                                responseFields(
                                ).and(getBoardDetailResponseField())
                        )

                );
    }

    @Test
    @DisplayName("게시판 상세보기 실패")
    void getDetailPageFail() throws Exception {

        this.mockMvc.perform(
                        get ("/board/{id}",0)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("board-detail-list-fail",
                                responseFields(getFailResponseField())));

    }

    @Test
    @DisplayName("게시판 등록하기")
    void saveBoard() throws Exception {

        BoardRequest request = new BoardRequest("제목", "내용");

        this.mockMvc.perform(
                        post("/user/board")
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Authorization", getUser())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("board-save",
                                requestFields(getBoardRequestField()),
                                responseFields(postBoardResponseField())
                        )
                );
    }

    @Test
    @DisplayName("게시판 등록실패")
//    @WithUserDetails(value = "Jane@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void saveBoardFail() throws Exception {

        BoardRequest request = new BoardRequest("", "내용");

        this.mockMvc.perform(
                        post("/user/board")
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Authorization", getUser())
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("board-save-fail",
                                responseFields(getFailResponseField())));
    }



    @Test
    @DisplayName("게시판 수정하기")
    void updateBoard() throws Exception {

        BoardUpdateRequest request = new BoardUpdateRequest("제목-수정", "내용-수정");

        this.mockMvc.perform(
                        put("/user/board/{id}",1)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Authorization", getUser())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("board-update",
                                requestFields(getBoardRequestField()),
                                responseFields(postBoardResponseField())
                        )
                );
    }

    @Test
    @DisplayName("게시판 수정실패")
    void updateBoardFail() throws Exception {

        BoardUpdateRequest request = new BoardUpdateRequest("", "내용-수정");

        this.mockMvc.perform(
                        put("/user/board/{id}",1)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("Authorization", getUser())
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("board-update-fail",
                                responseFields(getFailResponseField())));
    }

    @Test
    @DisplayName("게시판 삭제")
    void deleteBoard() throws Exception {

        // given
//        Board board = Board.builder().id(1).title("제목").content("내용").user(new User()).status(BoardStatus.ACTIVE).build();

        this.mockMvc.perform(
                        delete("/user/board/{id}",1)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .header("Authorization", getUser())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("board-delete"
//                                responseFields(deleteBoardResponseField())
                        )
                );

    }

    private FieldDescriptor[] getBoardRequestField() {
        return new FieldDescriptor[]{
                fieldWithPath("title").description("게시판 제목"),
                fieldWithPath("content").description("게시판 내용")

        };
    }

    private FieldDescriptor[] postBoardResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("title").description("게시판 제목"),
                fieldWithPath("content").description("게시판 내용")

        };
    }

    private FieldDescriptor[] getBoardDetailResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("id").description("게시판 id"),
                fieldWithPath("subject").description("게시판 제목"),
                fieldWithPath("content").description("게시판 내용"),
                fieldWithPath("createDate").description("게시판 생성시간"),
                fieldWithPath("boardStatus").description("게시판 상태코드"),
                fieldWithPath("_links.self.href").description("url"),
                fieldWithPath("user.id").description("유저 id"),
                fieldWithPath("user.username").description("유저 이름"),
                fieldWithPath("user.password").description("유저 비밀번호"),
                fieldWithPath("user.email").description("유저 이메일"),
                fieldWithPath("user.role").description("유저 권한"),
                fieldWithPath("user.createDate").description("유저 생성시간"),
                fieldWithPath("user.status").description("유저 상태코드"),
                fieldWithPath("user._links.self.href").description("url"),
                fieldWithPath("_links.self.href").description("page url"),
        };
    }

    private FieldDescriptor[] getBoardResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("_embedded.boards[].id").description("게시판 id"),
                fieldWithPath("_embedded.boards[].subject").description("게시판 제목"),
                fieldWithPath("_embedded.boards[].content").description("게시판 내용"),
                fieldWithPath("_embedded.boards[].createDate").description("게시판 생성시간"),
                fieldWithPath("_embedded.boards[].boardStatus").description("게시판 상태코드"),
                fieldWithPath("_embedded.boards[]._links.self.href").description("url"),
                fieldWithPath("_embedded.boards[].user.id").description("유저 id"),
                fieldWithPath("_embedded.boards[].user.username").description("유저 이름"),
                fieldWithPath("_embedded.boards[].user.password").description("유저 비밀번호"),
                fieldWithPath("_embedded.boards[].user.email").description("유저 이메일"),
                fieldWithPath("_embedded.boards[].user.role").description("유저 권한"),
                fieldWithPath("_embedded.boards[].user.createDate").description("유저 생성시간"),
                fieldWithPath("_embedded.boards[].user.status").description("유저 상태코드"),
                fieldWithPath("_embedded.boards[].user._links.self.href").description("url"),
                fieldWithPath("_links.self.href").description("page url"),
                fieldWithPath("page.size").description("page 사이즈"),
                fieldWithPath("page.totalElements").description("page 총 개수"),
                fieldWithPath("page.totalPages").description("page 총 개수"),
                fieldWithPath("page.number").description("page 번호"),

        };
    }
}
