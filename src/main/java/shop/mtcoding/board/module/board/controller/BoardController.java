package shop.mtcoding.board.module.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.board.core.exception.Exception400;
import shop.mtcoding.board.module.board.dto.BoardDTO;
import shop.mtcoding.board.module.board.dto.BoardRequest;
import shop.mtcoding.board.module.board.dto.BoardResponse;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.board.service.BoardService;
import shop.mtcoding.board.module.user.dto.UserDTO;
import shop.mtcoding.board.module.user.model.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<Page<BoardDTO>> getPage(Pageable pageable) {
        Page<Board> page = boardService.getPage(pageable);
        List<BoardDTO> content = page.getContent().stream().map(Board::toDTO).toList();

        return ResponseEntity.ok(new PageImpl<>(content, pageable, page.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Integer id) {
        Optional<Board> boardOptional = boardService.getBoard(id);

        if (boardOptional.isEmpty()) {
            throw new Exception400("게시판의 정보가 없습니다.");
        }

        return ResponseEntity.ok().body(boardOptional.get().toResponse());
    }

    @PostMapping
    public ResponseEntity<BoardResponse> saveBoard(@Valid @RequestBody BoardRequest boardRequest, Errors errors) {

        if (errors.hasErrors()) {
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        Board save = boardService.save(boardRequest);

        return ResponseEntity.ok().body(save.toResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> updateBoard(@Valid @RequestBody BoardUpdateRequest boardUpdateRequest
            , Errors errors ,@PathVariable Integer id
    ) {

        if (errors.hasErrors()) {
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        Optional<Board> boardOptional = boardService.getBoard(id);

        if (boardOptional.isEmpty()) {
            throw new Exception400("게시판의 정보가 없습니다.");
        }

        Board board = boardService.update(boardUpdateRequest, boardOptional.get());

        return ResponseEntity.ok().body(board.toResponse());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Integer id) {
        Optional<Board> boardOptional = boardService.getBoard(id);

        if (boardOptional.isEmpty()) {
            throw new Exception400("게시판의 정보가 없습니다.");
        }

        boardService.delete(boardOptional.get());

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }


}
