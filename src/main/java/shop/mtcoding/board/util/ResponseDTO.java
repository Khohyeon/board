package shop.mtcoding.board.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO<T> {
    private Integer code;
    private Integer status;
    private String msg;
    private T data;

    public ResponseDTO() {
        this.code = 1;
        this.status = 200;
        this.msg = "성공";
    }

    public ResponseDTO<?> data(T data){
        this.data = data;
        return this;
    }

    public ResponseDTO<?> fail(Integer code , Integer status, String msg, T data){
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ResponseDTO(Integer code, Integer status, String msg, T data) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
