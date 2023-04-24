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
import shop.mtcoding.board.module.reply.model.Reply;
import shop.mtcoding.board.module.reply.model.ReplyRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("댓글 JPA 테스트")
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUp("댓글");
    }

    @Test
    @Transactional
    @DisplayName("댓글 조회 테스트")
    void selectAll() {
        List<Reply> replyList = replyRepository.findAll();
        Assertions.assertNotEquals(replyList.size(), 0);

        Reply reply = replyList.get(0);
        Assertions.assertEquals(reply.getComment(), "댓글1");

    }

    @Test
    @Transactional
    @DisplayName("댓글 id 조회 테스트")
    void selectById() {
        Optional<Reply> optionalReply = replyRepository.findById(1);

        if (optionalReply.isPresent()) {
            Reply reply = optionalReply.get();
            Assertions.assertEquals(reply.getComment() , "댓글1");
        }
    }

    @Test
    @Transactional
    @DisplayName("댓글 수정 테스트")
    void update() {
        Optional<Reply> optionalReply = replyRepository.findById(1);

        if (optionalReply.isPresent()) {
            Reply reply = optionalReply.get();

            String comment = "댓글3333";
            reply.setComment(comment);
            Assertions.assertEquals(reply.getComment() , "댓글3333");
        }
    }

    @Test
    @Transactional
    @DisplayName("댓글 삽입 및 삭제 테스트")
    void insertAndDelete() {
        Reply reply = setUp("댓글-수정");
        Optional<Reply> findReply = this.replyRepository.findById(reply.getId());

        if (findReply.isPresent()) {
            var result = findReply.get();
            Assertions.assertEquals(result.getComment(), "댓글-수정");
            entityManager.remove(result);
            Optional<Reply> deleteReply = this.replyRepository.findById(reply.getId());

            if (deleteReply.isPresent()) {
                Assertions.assertNull(deleteReply.get());
            }
        } else {
            Assertions.assertNotNull(findReply.get());
        }

    }

    public Reply setUp(String comment) {
        Reply reply = new Reply();
        reply.setComment(comment);
        return entityManager.persist(reply);
    }

}
