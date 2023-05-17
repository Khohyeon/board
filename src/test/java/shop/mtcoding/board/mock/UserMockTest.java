//package shop.mtcoding.board.mock;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import shop.mtcoding.board.module.user.controller.UserController;
//import shop.mtcoding.board.module.user.service.UserService;
//import shop.mtcoding.board.module.user.model.User;
//
//import java.util.List;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//public class UserMockTest {
//
//    @Autowired
//    MockMvc mvc;
//
//    @MockBean
//    private UserService userService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//
//    @Test
//    @DisplayName("유저 조회")
//    void getPage() throws Exception {
//        Pageable pageable = PageRequest.of(1, 10);
//        Page<User> page = new PageImpl<>(
//                List.of(
//                        new User(1, "ssar", "1234", "ssar@nate.com"),
//                        new User(2, "cos", "1234", "cos@nate.com")
//                )
//        );
//
//        // given
//        given(this.userService.getPage(pageable)).willReturn(page);
//
//
//        // When
//        ResultActions perform = this.mvc.perform(
//                get("/user?page={page}&size={size}", 1, 10)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//
//        // Then
//        perform
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.content[0].id").value(1))
//                .andExpect(jsonPath("$.content[0].username").value("ssar"))
//                .andExpect(jsonPath("$.content[0].password").value("1234"))
//                .andExpect(jsonPath("$.content[0].email").value("ssar@nate.com"))
//
//
//                .andExpect(jsonPath("$.content[1].id").value(2))
//                .andExpect(jsonPath("$.content[1].username").value("cos"))
//                .andExpect(jsonPath("$.content[1].password").value("1234"))
//                .andExpect(jsonPath("$.content[1].email").value("cos@nate.com"))
//        ;
//    }
//
//}
