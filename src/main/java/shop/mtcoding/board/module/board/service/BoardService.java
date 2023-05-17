package shop.mtcoding.board.module.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.mtcoding.board.module.board.dto.BoardRequest;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.user.model.User;

import java.util.Optional;

@Service
public class BoardService {
    public Page<Board> getPage(Pageable pageable) {
        return null;
    }

    public Optional<Board> getBoard(Integer id) {
        return null;
    }

    public Board save(BoardRequest boardRequest) {
        return null;
    }
}
