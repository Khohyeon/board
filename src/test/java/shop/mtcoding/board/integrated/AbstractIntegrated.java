package shop.mtcoding.board.integrated;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.mtcoding.board.module.user.dto.LoginRequest;
import shop.mtcoding.board.module.user.status.UserStatus;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;


@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegrated {
    // build 위치
    //build/generated-snippets

    protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    protected String getUser() {
        try {
            LoginRequest loginDTO = new LoginRequest("ssar", "1234", UserStatus.ACTIVE);

            ResultActions perform = this.mockMvc.perform(
                    post("/users/login")
                            .content(objectMapper.writeValueAsString(loginDTO))
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            );

            MvcResult mvcResult = perform.andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            return response.getHeader("Authorization");
        } catch (Exception e) {
            return "";
        }
    }

    protected String getAdmin() {
        try {
            LoginRequest loginDTO = new LoginRequest("love", "1234", UserStatus.ACTIVE);

            ResultActions perform = this.mockMvc.perform(
                    post("/users/login")
                            .content(objectMapper.writeValueAsString(loginDTO))
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            );

            MvcResult mvcResult = perform.andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            return response.getHeader("Authorization");
        } catch (Exception e) {
            return "";
        }
    }

    protected FieldDescriptor[] getSuccessResponseField() {
        return new FieldDescriptor[] {
                fieldWithPath("type").description("type"),
                fieldWithPath("title").description("에러 코드 (이름)"),
                fieldWithPath("status").description("에러 코드"),
                fieldWithPath("detail").description("에러 메세지 (중요)"),
                fieldWithPath("instance").description("요청 경로")
        };
    }

    protected FieldDescriptor[] getFailResponseField() {
        return new FieldDescriptor[] {
                fieldWithPath("type").description("에러 코드"),
                fieldWithPath("title").description("에러 메시지"),
                fieldWithPath("detail").description("에러 데이터"),
                fieldWithPath("instance").description("에러 코드"),
                fieldWithPath("status").description("에러 상태코드"),
        };
    }

}
