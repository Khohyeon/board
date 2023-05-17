package shop.mtcoding.board.core.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.board.core.exception.*;
import shop.mtcoding.board.util.ResponseDTO;

@RestControllerAdvice
public class MyExceptionAdvice {

  @ExceptionHandler(Exception400.class)
  public ResponseEntity<?> badRequest(Exception400 e) {
      ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(-1, 400, e.getMessage(), "Null");
      return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
  }

   // unAuthorized
   @ExceptionHandler(Exception401.class)
   public ResponseEntity<?> unAuthorized(Exception401 e){
       ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(-1, 401, "인증이 안되었습니다. 다시 확인해주세요", "Null");
       return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
   }

   // forbidden
   @ExceptionHandler(Exception403.class)
   public ResponseEntity<?> forbidden(Exception403 e){
       ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(-1, 403, "권한이 없습니다. 다시 확인해주세요", "Null");
       return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
   }

   // notFound
   // 자원을 못 찾은 경우
   @ExceptionHandler(Exception404.class)
   public ResponseEntity<?> notFound(Exception404 e){
       ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(-1, 404, "경로가 잘못 되었습니다 다시 확인해주세요", "Null");
       return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
   }


   @ExceptionHandler(Exception500.class)
   public ResponseEntity<?> serverError(Exception500 e) {
      ResponseDTO<?> responseDTO = new ResponseDTO<>().fail(-1, 500, "일시적인 서버 오류입니다.", "Null");
      return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

