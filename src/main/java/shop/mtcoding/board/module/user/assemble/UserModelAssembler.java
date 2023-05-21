package shop.mtcoding.board.module.user.assemble;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import shop.mtcoding.board.auth.MyUserDetails;
import shop.mtcoding.board.module.user.controller.UserController;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.model.UserModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

public UserModelAssembler() {
        super(UserModel.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User user) {
        MyUserDetails myUserDetails = new MyUserDetails(user);
        UserModel userModel = new UserModel(user);
        userModel.add(linkTo(methodOn(UserController.class).getUser(myUserDetails)).withSelfRel());
        return userModel;
    }

    @Override
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities) {
        return super.toCollectionModel(entities);
    }
}
