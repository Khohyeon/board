package shop.mtcoding.board.module.board.assemble;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import shop.mtcoding.board.module.board.controller.BoardController;
import shop.mtcoding.board.module.board.model.Board;
import shop.mtcoding.board.module.board.model.BoardModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BoardModelAssembler extends RepresentationModelAssemblerSupport<Board, BoardModel> {

    public BoardModelAssembler() {
        super(BoardController.class, BoardModel.class);
    }

    @Override
    public BoardModel toModel(Board board) {
        BoardModel resource = new BoardModel(board);
        resource.add(linkTo(methodOn(BoardController.class).getBoard(board.getId())).withSelfRel());
        return resource;
    }

    @Override
    public CollectionModel<BoardModel> toCollectionModel(Iterable<? extends Board> entities) {
        return super.toCollectionModel(entities);
    }
}
