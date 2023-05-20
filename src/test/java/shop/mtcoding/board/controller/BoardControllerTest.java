package shop.mtcoding.board.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.interfaceTest.AbstractIntegrated;
import shop.mtcoding.board.module.board.dto.BoardRequest;
import shop.mtcoding.board.module.board.dto.BoardUpdateRequest;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.board.status.BoardStatus;

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
                fieldWithPath("title").description("게시판 제목"),
                fieldWithPath("content").description("게시판 내용")

        };
    }

    private FieldDescriptor[] getBoardResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("content[].id").description("게시판 id"),
                fieldWithPath("content[].title").description("게시판 제목"),
                fieldWithPath("content[].content").description("게시판 내용"),
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
