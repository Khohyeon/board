package shop.mtcoding.board.module.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.module.board.dto.BoardResponse;
import shop.mtcoding.board.module.board.dto.BoardUpdateRequest;
import shop.mtcoding.board.module.board.dto.BoardRequest;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.board.model.BoardRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getPage(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Optional<Board> getBoard(Integer id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public BoardResponse save(BoardRequest boardRequest) {
        return boardRepository.save(boardRequest.toEntity()).toResponse();
    }

    @Transactional
    public BoardResponse update(BoardUpdateRequest boardUpdateRequest, Board board) {
        board.setTitle(boardUpdateRequest.title());
        board.setContent(boardUpdateRequest.content());
        return boardRepository.save(board).toResponse();
    }

    public void delete(Board board) {
        boardRepository.delete(board);
    }
}
