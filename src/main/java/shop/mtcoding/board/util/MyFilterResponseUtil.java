package shop.mtcoding.board.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shop.mtcoding.board.exception.Exception401;
import shop.mtcoding.board.exception.Exception403;

import java.io.IOException;
public class MyFilterResponseUtil {
    public static void result(HttpStatus status, HttpServletRequest request, HttpServletResponse resp, Exception e) throws IOException {
        resp.setStatus(status.value());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(
                "{\n" +
                        "    \"type\": \"about:blank\",\n" +
                        "    \"title\": \""+ status.getReasonPhrase() +"\",\n" +
                        "    \"status\": "+ resp.getStatus() +",\n" +
                        "    \"detail\": \"" + e.getMessage() + "\",\n" +
                        "    \"instance\": \"" + request.getRequestURI() + "\"\n"
                        + "}"
        );
    }
}