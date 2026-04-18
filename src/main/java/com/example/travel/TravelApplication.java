package com.example.travel;

import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SpringBootApplication
public class TravelApplication {
    private static final Set<String> PUBLIC_PATHS = Set.of(
        "/",
        "/register.html",
        "/login.html",
        "/api/login",
        "/api/register",
        "/api/logout",
        "/error",
        "/favicon.ico"
    );

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer authWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        String uri = request.getRequestURI();
                        if (uri == null || uri.isBlank()) {
                            uri = "/";
                        }

                        if (isStaticAsset(uri)) {
                            return true;
                        }

                        HttpSession session = request.getSession(false);
                        boolean isAuthenticated = session != null
                            && session.getAttribute("userEmail") != null
                            && !session.getAttribute("userEmail").toString().trim().isEmpty();

                        if ("/".equals(uri)) {
                            response.sendRedirect(isAuthenticated ? "/index.html" : "/register.html#login");
                            return false;
                        }

                        if (isAuthenticated && isAuthPage(uri)) {
                            response.sendRedirect("/index.html");
                            return false;
                        }

                        if (!isAuthenticated && !isPublicPath(uri)) {
                            response.sendRedirect("/register.html#login");
                            return false;
                        }

                        return true;
                    }
                });
            }
        };
    }

    private static boolean isAuthPage(String uri) {
        return "/register.html".equals(uri) || "/login.html".equals(uri);
    }

    private static boolean isPublicPath(String uri) {
        return PUBLIC_PATHS.contains(uri);
    }

    private static boolean isStaticAsset(String uri) {
        return uri.startsWith("/css/")
            || uri.startsWith("/js/")
            || uri.startsWith("/assets/")
            || uri.startsWith("/images/")
            || uri.startsWith("/webjars/")
            || uri.endsWith(".css")
            || uri.endsWith(".js")
            || uri.endsWith(".png")
            || uri.endsWith(".jpg")
            || uri.endsWith(".jpeg")
            || uri.endsWith(".gif")
            || uri.endsWith(".svg")
            || uri.endsWith(".ico")
            || uri.endsWith(".webp")
            || uri.endsWith(".avif")
            || uri.endsWith(".mp4");
    }
}
