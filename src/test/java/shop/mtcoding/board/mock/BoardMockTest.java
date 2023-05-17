package shop.mtcoding.board.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import shop.mtcoding.board.interfaceTest.AbstractIntegrated;
import shop.mtcoding.board.module.board.controller.BoardController;
import shop.mtcoding.board.module.board.dto.BoardUpdateRequest;
import shop.mtcoding.board.module.board.dto.BoardRequest;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.board.model.BoardRepository;
import shop.mtcoding.board.module.board.service.BoardService;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.util.status.BoardStatus;
import shop.mtcoding.board.util.status.UserStatus;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("게시판 MOCK 테스트")
public class BoardMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private BoardRepository boardRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("게시판 조회")
    void getBoard() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        User user1 = new User(1, "ssar", "1234", "ssar@nate.com", "USER", UserStatus.ACTIVE);
        User user2 = new User(2, "cos", "1234", "cos@nate.com", "USER", UserStatus.ACTIVE);
        Page<Board> page = new PageImpl<>(
                List.of(

                        new Board(1, "제목1", "내용1", user1, BoardStatus.ACTIVE),
                        new Board(2, "제목2", "내용2", user2, BoardStatus.ACTIVE)
                )
        );

        // given
        given(this.boardService.getPage(pageable)).willReturn(page);


        // When
        ResultActions perform = this.mvc.perform(
                get("/board?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("제목1"))
                .andExpect(jsonPath("$.content[0].content").value("내용1"))


                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("제목2"))
                .andExpect(jsonPath("$.content[1].content").value("내용2"))
        ;
    }

    @Test
    @DisplayName("게시판 상세 조회 실패")
    void getBoardFail() throws Exception {

        // given
        int id = 0;
        given(this.boardService.getBoard(id)).willReturn(Optional.empty());

        // When
        ResultActions perform = this.mvc.perform(
                get("/board/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)

        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.msg").value("게시판의 정보가 없습니다."))
        ;
    }

    @Test
    @DisplayName("게시판 상세 조회")
    void getUserDetail() throws Exception {

        // given
        int id = 0;
        given(this.boardService.getBoard(id))
                .willReturn(
                        Optional.of(new Board(1, "제목", "내용", new User(), BoardStatus.ACTIVE))
                );


        // When
        ResultActions perform = this.mvc.perform(
                get("/board/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
        ;
    }

    @Test
    @DisplayName("게시판 글쓰기 성공")
    void saveBoard() throws Exception {

        // given
        BoardRequest request = new BoardRequest("제목", "내용");
        given(this.boardService.save(request)).willReturn(request.toEntity());


        // when
        ResultActions perform = this.mvc.perform(
                post("/board")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"));
    }

    @Test
    @DisplayName("게시판 글쓰기 실패(Valid)")
    void saveBoardFail() throws Exception {

        // given
        BoardRequest request = new BoardRequest("", "내용");
        given(this.boardService.save(request)).willReturn(request.toEntity());


        // when
        ResultActions perform = this.mvc.perform(
                post("/board")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.msg").value("제목을 입력해주세요."));
    }

    @Test
    @DisplayName("게시판 수정 실패(Valid)")
    void updateBoardFail() throws Exception {


        // given
        int id = 0;
        BoardUpdateRequest request = new BoardUpdateRequest("", "내용");

        // When
        ResultActions perform = this.mvc.perform(
                put("/board/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.msg").value("제목을 입력해주세요."))
        ;
    }

    @Test
    @DisplayName("게시판 수정 성공")
    void updateBoard() throws Exception {


        // given
        int id = 1;
        BoardUpdateRequest request = new BoardUpdateRequest("제목-수정", "내용-수정");

        Optional<Board> boardOptional = Optional.of(new Board(1, "제목", "내용", new User(), BoardStatus.ACTIVE));
        given(this.boardService.getBoard(id)).willReturn(boardOptional);

        given(boardService.update(request, boardOptional.get())).willReturn(new Board(1, "제목-수정", "내용-수정", new User(), BoardStatus.ACTIVE));


        // When
        ResultActions perform = this.mvc.perform(
                put("/board/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value("제목-수정"))
                .andExpect(jsonPath("$.content").value("내용-수정"));
    }

    @Test
    @DisplayName("게시판 삭제 실패")
    void deleteBoardFail() throws Exception {

        // given
        int id = 0;
        given(this.boardService.getBoard(id)).willReturn(Optional.empty());


        // When
        ResultActions perform = this.mvc.perform(
                delete("/board/{id}", id)
        );

        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.msg").value("게시판의 정보가 없습니다."))

        ;
    }

    @Test
    @DisplayName("게시판 삭제 성공")
    void deleteBoard() throws Exception {

        // given
        int id = 0;
        Optional<Board> boardOptional = Optional.of(new Board());
        given(this.boardService.getBoard(id)).willReturn(boardOptional);


        // When
        ResultActions perform = this.mvc.perform(
                delete("/board/{id}", id)
        );

        // Then
        MvcResult mvcResult = perform
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Assertions.assertEquals(mvcResult.getResponse().getContentAsString(), "삭제가 완료되었습니다.");

    }
}
