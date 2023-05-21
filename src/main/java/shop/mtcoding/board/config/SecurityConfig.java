package shop.mtcoding.board.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.mtcoding.board.auth.JwtAuthorizationFilter;
import shop.mtcoding.board.exception.Exception401;
import shop.mtcoding.board.exception.Exception403;
import shop.mtcoding.board.util.MyFilterResponseUtil;

import javax.naming.AuthenticationException;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // JWT 필터 등록이 필요함
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }

    // 시큐리티 설정을 비활성화 하기 위한 세팅 - 커스텀
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 토큰을 숨겨두어서 보안검사
        http.csrf().disable(); // postman 으로 접근하기 위해 토큰을 비활성화 - csr 이용 , 반면에 ssr이라면 ?
        // ssr은 기본적으로 csrf 토큰을 이용하지 않지만 적용할 수 있다.

        // 2. iframe 차단설정
        http.headers().frameOptions().disable();

        // 3. cors 재설정
        http.cors().configurationSource(configurationSource());

        // 4. JSESSIONID 응답 x
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /*
         * 또는
         * spring:
         *   security:
         *     session:
         *       creation-policy: stateless
         */

        // 5. Form 로긴 해제
        // OAuth2, SAML 또는 JWT 토큰과 같은 다른 인증 메커니즘을 사용하려는 경우.
        // ajax 요청 처리할 경우
        http.formLogin().disable();

        // 6. http bagic 인증 해제 - 모든 페이지마다 로그인을 해야함.. 안전하지만 너무 불편하다
        // BasinAuthenticationFilter 해제
        // http.httpBasic().disable();

        // 2가지 방법 disable 안하고 addFilterAt 사용해서 바꿔치는 방법도 있음
        // disable 하고 다시 등록하는 방법도 있고

        // 7. xss - lucy 필터 ( 적용 하던가 )

        // 8 .커스텀 필터 적용 ( 시큐리티 필터 교환 )
        http.apply(new CustomSecurityFilterManager());

        // 9. 인증 실패 처리
        http
            .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                log.warn("인증되지 않은 사용자가 자원에 접근하려 합니다 : "+authException.getMessage());
                MyFilterResponseUtil.result(HttpStatus.UNAUTHORIZED, request, response, new Exception401("인증되지 않았습니다"));
            })
        // 10. 권한 실패 처리
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                log.warn("권한이 없는 사용자가 자원에 접근하려 합니다 : "+accessDeniedException.getMessage());
                MyFilterResponseUtil.result(HttpStatus.FORBIDDEN, request, response, new Exception403("권한이 없습니다"));
            });


        // // Form 로그인 설정
        // http.formLogin()
        //         .loginPage("/loginForm")
        //         .usernameParameter("username")
        //         .passwordParameter("password")
        //         .loginProcessingUrl("/login") // 로그인 양식 데이터를 제출해야 하는 URL - post
        //         // .defaultSuccessUrl("/") // 인증 성공후 리다이렉션되는 주소
        //         // .defaultSuccessUrl("/", true); // 상관없이 강제 리다이렉션
        //         .successHandler((req, resp, authentication) -> {
        //             System.out.println("디버그 : 로그인이 완료되었습니다.");
        //             resp.sendRedirect("/");
        //         }) // 로그 기록
        //         .failureHandler((req, resp, exception) -> {
        //             System.out.println("디버그 : 로그인 실패 -> " + exception.getMessage());
        //         }); // 에러 로그

        // 11. 인증 권한 필터 설정
        http.authorizeHttpRequests((authorize) -> {
            authorize.requestMatchers("/users","/users/page","/users/login","/users/join").permitAll() // 필터를 거치지 않음
                    .requestMatchers("/user/**", "/users/detail").authenticated()
//                    .requestMatchers("/user/**","/users/detail").hasRole("ADMIN")
                    .anyRequest().permitAll();
        });
        // users 로 가는 경우는 필터를 거쳐야 한다.
        return http.build();
    }

    // CORS
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        // 자바스크립트 요청만, * 로 해놓으면 안됨
        // 서버 리소스에 액세스할 수 있는 원본(도메인)을 지정합니다.
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        // 브라우저가 Authorization을 읽을 수 있게 허용하는 옵션
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 아닙니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
