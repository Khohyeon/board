package shop.mtcoding.board.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception400 extends RuntimeException {

    private String key;
    private String value;

    public Exception400(String value) {
        super(value);
        this.value = value;
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}
