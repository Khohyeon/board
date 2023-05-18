package shop.mtcoding.board.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.interfaceTest.AbstractIntegrated;
import shop.mtcoding.board.module.board.dto.BoardRequest;

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
                                ).and(getPlaceResponseField())
                        )

                );
    }

    @Test
    @DisplayName("게시판 상세보기")
    @Transactional
    void getDetailPage() throws Exception {

        this.mockMvc.perform(
                        get ("/board/1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("board-detail-list",
                                responseFields(
                                ).and(getPlaceDetailResponseField())
                        )

                );
    }

    @Test
    @DisplayName("게시판 등록하기")
//    @WithUserDetails(value = "Jane@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void saveBoard() throws Exception {

        BoardRequest request = new BoardRequest("제목", "내용");

        this.mockMvc.perform(
                        post("/user/board")
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
    @DisplayName("게시판 수정하기")
//    @WithUserDetails(value = "Jane@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateBoard() throws Exception {

        BoardRequest request = new BoardRequest("제목", "내용");

        this.mockMvc.perform(
                        put("/user/board/1")
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
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

//    @Test
//    @DisplayName("게시판 삭제")
////    @WithUserDetails(value = "Jane@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//    void deleteBoard() throws Exception {
//        this.mockMvc.perform(
//                        delete("/board/1")
//                                .accept(MediaType.APPLICATION_JSON_VALUE)
//                                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andDo(
//                        document("board-delete",
//                                responseFields(deleteBoardResponseField())
//                        )
//                );
//
//    }


//    private FieldDescriptor[] deleteBoardResponseField() {
//        return new FieldDescriptor[]{
//                fieldWithPath("title").description("게시판 제목"),
//                fieldWithPath("content").description("게시판 내용")
//
//        };
//    }
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

    private FieldDescriptor[] getPlaceDetailResponseField() {
        return new FieldDescriptor[]{
                fieldWithPath("title").description("게시판 제목"),
                fieldWithPath("content").description("게시판 내용")

        };
    }

    private FieldDescriptor[] getPlaceResponseField() {
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
