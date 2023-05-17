//package shop.mtcoding.board.config.filter;
//
//import com.auth0.jwt.exceptions.SignatureVerificationException;
//import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import shop.mtcoding.board.config.auth.JwtProvider;
//import shop.mtcoding.board.config.auth.MyUserDetails;
//import shop.mtcoding.board.module.user.model.User;
//
//
//import java.io.IOException;
//
//// 모든 주소에서 발동
//
//@Slf4j
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String prefixJwt = request.getHeader(JwtProvider.HEADER);
//
//        if (prefixJwt == null) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String jwt = prefixJwt.replace(JwtProvider.TOKEN_PREFIX, "");
//        try {
//            DecodedJWT decodedJWT = JwtProvider.verify(jwt);
//            Integer id = decodedJWT.getClaim("id").asInt();
//            String role = decodedJWT.getClaim("role").asString();
//
//            User user = User.builder().id(id).role(role).build();
//            MyUserDetails myUserDetails = new MyUserDetails(user);
//            Authentication authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            myUserDetails,
//                            myUserDetails.getPassword(),
//                            myUserDetails.getAuthorities()
//                    );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } catch (SignatureVerificationException sve) {
//            log.error("토큰 검증 실패");
//        } catch (TokenExpiredException tee) {
//            log.error("토큰 만료됨");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Token expired");
//            return;
//        }
//        chain.doFilter(request, response);
//    }
//}
//
//// 토큰이 있으면 홀더에 임시 세션을 넣는다.
//// 컨트롤러로 요청은 들어가지만 세션의 권한을 얻을 수는 없다.