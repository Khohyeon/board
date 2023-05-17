package shop.mtcoding.board.config.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.board.config.auth.JwtProvider;
import shop.mtcoding.board.config.auth.LoginUser;

import java.io.IOException;

@Slf4j
public class JwtVerifyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String prefixJwt = req.getHeader(JwtProvider.HEADER);
        String jwt = prefixJwt.replace(JwtProvider.TOKEN_PREFIX, "");
        try {
            DecodedJWT decodedJWT = JwtProvider.verify(jwt);
            int id = decodedJWT.getClaim("id").asInt();
            String role = decodedJWT.getClaim("role").asString();

            // 내부적으로 권한처리
            HttpSession session =  req.getSession();
            LoginUser loginUser = LoginUser.builder().id(id).role(role).build();
            session.setAttribute("loginUser", loginUser);
            chain.doFilter(req, resp);
        }catch (SignatureVerificationException sve){
            log.error("토큰 검증에 실패 했습니다.");
        }catch (TokenExpiredException tee){
            log.error("토큰 기간이 만료 되었습니다.");
        }
    }

}