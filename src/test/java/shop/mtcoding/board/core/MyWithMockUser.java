//package shop.mtcoding.board.core;
//
//import org.springframework.security.test.context.support.WithSecurityContext;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//@WithSecurityContext(factory = MyWithMockUserFactory.class)
//public @interface MyWithMockUser {
//    int id() default 1;
//    String username() default "cos";
//    String role() default "USER";
//    String email() default "cos@nate.com";
//}