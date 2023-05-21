package shop.mtcoding.board.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.board.exception.*;

@RestControllerAdvice
public class MyExceptionAdvice {

  @ExceptionHandler(Exception400.class)
  ProblemDetail onException400(Exception400 exception400) {
      return ProblemDetail.forStatusAndDetail(
              HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()),
              exception400.getMessage()
      );
  }

   // unAuthorized
   @ExceptionHandler(Exception401.class)
   ProblemDetail onException401(Exception401 exception401) {
       return ProblemDetail.forStatusAndDetail(
               HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()),
               exception401.getMessage()
       );
   }

   // forbidden
   @ExceptionHandler(Exception403.class)
   ProblemDetail onException403(Exception403 exception403) {
       return ProblemDetail.forStatusAndDetail(
               HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()),
               exception403.getMessage()
       );
   }

   // notFound
   // 자원을 못 찾은 경우
   @ExceptionHandler(Exception404.class)
   ProblemDetail onException404(Exception404 exception404) {
       return ProblemDetail.forStatusAndDetail(
               HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()),
               exception404.getMessage()
       );
   }


   @ExceptionHandler(Exception500.class)
   ProblemDetail onException500(Exception500 exception500) {
       return ProblemDetail.forStatusAndDetail(
               HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
               exception500.getMessage()
       );
   }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> Error(Exception500 e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

