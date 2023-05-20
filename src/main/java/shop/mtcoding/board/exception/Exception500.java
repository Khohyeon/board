package shop.mtcoding.board.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }


    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}