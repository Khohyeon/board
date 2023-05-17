package shop.mtcoding.board.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import shop.mtcoding.board.util.ResponseDTO;

// 권한 없음
@Getter
public class Exception403 extends RuntimeException{
    public Exception403(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        ResponseDTO<String> responseDto = new ResponseDTO<>();
        responseDto.fail(-1, 403, "forbidden", getMessage());
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}
