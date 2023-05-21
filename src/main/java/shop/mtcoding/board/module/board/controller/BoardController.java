package shop.mtcoding.board.module.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.board.auth.MyUserDetails;
import shop.mtcoding.board.exception.Exception400;
import shop.mtcoding.board.module.board.dto.*;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.board.model.BoardModel;
import shop.mtcoding.board.module.board.service.BoardService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@ExposesResourceFor(BoardModel.class)
public class BoardController {

    private final BoardService boardService;

    private final EntityLinks entityLinks;

    @GetMapping("/board")
    public ResponseEntity<Page<BoardDTO>> getPage(Pageable pageable) {
        Page<Board> page = boardService.getPage(pageable);
        List<BoardDTO> content = page.getContent().stream().map(Board::toDTO).toList();

        return ResponseEntity.ok(new PageImpl<>(content, pageable, page.getTotalElements()));
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardLinkResponse> getBoard(@PathVariable Integer id) {

        Link link = entityLinks.linkToItemResource(BoardModel.class, id);
        Optional<Board> boardOptional = boardService.getBoard(id);

        System.out.println("디버그 " + link.getHref());

        if (boardOptional.isEmpty()) {
            throw new Exception400("게시판의 정보가 없습니다.");
        }

        return ResponseEntity.ok().body(boardOptional.get().toResponse1(link.getHref()));
    }

    @PostMapping("/user/board")
    public ResponseEntity<BoardResponse> saveBoard(@Valid @RequestBody BoardRequest boardRequest, Errors errors) {

        if (errors.hasErrors()) {
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        BoardResponse save = boardService.save(boardRequest);

        return ResponseEntity.ok().body(save);
    }

    @PutMapping("/user/board/{id}")
    public ResponseEntity<BoardResponse> updateBoard(@Valid @RequestBody BoardUpdateRequest boardUpdateRequest
            , Errors errors , @PathVariable Integer id , @AuthenticationPrincipal MyUserDetails myUserDetails
                                                     ) {

        if (errors.hasErrors()) {
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        Optional<Board> boardOptional = boardService.getBoard(myUserDetails.getUser().getId());

        if (boardOptional.isEmpty()) {
            throw new Exception400("게시판의 정보가 없습니다.");
        }

        BoardResponse board = boardService.update(boardUpdateRequest, boardOptional.get());

        return ResponseEntity.ok().body(board);
    }

    @DeleteMapping("/user/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Integer id) {
        Optional<Board> boardOptional = boardService.getBoard(id);

        if (boardOptional.isEmpty()) {
            throw new Exception400("게시판의 정보가 없습니다.");
        }

        boardService.delete(boardOptional.get());

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }


}
