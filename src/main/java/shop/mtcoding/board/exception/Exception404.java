package shop.mtcoding.board.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 리소스 없음
@Getter
public class Exception404 extends RuntimeException{
    public Exception404(String message) {
        super(message);
    }


    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}
