package shop.mtcoding.board.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.board.model.BoardRepository;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserRepository;
import shop.mtcoding.board.util.status.BoardStatus;
import shop.mtcoding.board.util.status.UserStatus;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("게시판 JPA 테스트")
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUp("제목", "내용", BoardStatus.ACTIVE);
    }

    @Test
    @Transactional
    @DisplayName("게시판 조회 테스트")
    void selectAll() {
        List<Board> boardList = boardRepository.findAll();
        Assertions.assertNotEquals(boardList.size(), 0);

        Board board = boardList.get(0);
        Assertions.assertEquals(board.getTitle(), "제목1");

    }

    @Test
    @Transactional
    @DisplayName("게시판 id 조회 테스트")
    void selectById() {
        Optional<Board> optionalBoard = boardRepository.findById(1);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            Assertions.assertEquals(board.getTitle() , "제목1");
        }
    }

    @Test
    @Transactional
    @DisplayName("게시판 수정 테스트")
    void update() {
        Optional<Board> optionalBoard = boardRepository.findById(1);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            String content = "내용3333";
            board.setContent(content);
            Assertions.assertEquals(board.getContent() , "내용3333");
        }
    }

    @Test
    @Transactional
    @DisplayName("게시판 삽입 및 삭제 테스트")
    void insertAndDelete() {
        Board board = setUp("제목-수정", "내용-수정", BoardStatus.ACTIVE);
        Optional<Board> findBoard = this.boardRepository.findById(board.getId());

        if (findBoard.isPresent()) {
            var result = findBoard.get();
            Assertions.assertEquals(result.getTitle(), "제목-수정");
            entityManager.remove(result);
            Optional<Board> deleteBoard = this.boardRepository.findById(board.getId());

            if (deleteBoard.isPresent()) {
                Assertions.assertNull(deleteBoard.get());
            }
        } else {
            Assertions.assertNotNull(findBoard.get());
        }

    }

    public Board setUp(String title, String content, BoardStatus status) {

        User user = new User().builder().username("love").password("1234").email("love@nate.com").role("USER").status(UserStatus.ACTIVE).build();
        this.entityManager.persist(user);

        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setUser(user);
        board.setStatus(status);
        return entityManager.persist(board);
    }

}
