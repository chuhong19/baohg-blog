package vn.giabaoblog.giabaoblogserver.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.giabaoblog.giabaoblogserver.data.repository.TokenRepository;
import vn.giabaoblog.giabaoblogserver.services.authentication.JwtService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;
        final Claims claims;
        long startTime = System.currentTimeMillis();
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            jwt = authHeader.substring(7);
            userId = jwtService.extractId(jwt);
            claims = jwtService.extractAllClaims(jwt);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
                var isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new JwtDetails(jwt, claims, new WebAuthenticationDetailsSource().buildDetails(request)));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

//                    String uri = request.getRequestURI();
//                    String method = request.getMethod();
//                    Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
//                            .collect(Collectors.toMap(Function.identity(), request::getHeader));
//                    String body = new BufferedReader(new InputStreamReader(request.getInputStream())).lines().collect(Collectors.joining("\n"));
//                    String queryParams = request.getQueryString();
//                    Enumeration<String> attributeNames = request.getAttributeNames();
//                    List<String> attributeNameList = Collections.list(attributeNames);
//                    String pathVariables = attributeNameList.toString();
//                    String ipAddress = request.getRemoteAddr();
//                    String userAgent = request.getHeader("User-Agent");
//                    String sessionId = request.getRequestedSessionId();
//                    String requestTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
//
//                    String responseStatus = String.valueOf(response.getStatus());
//                    Map<String, String> responseHeaders = response.getHeaderNames().stream()
//                            .collect(Collectors.toMap(Function.identity(), response::getHeader));
//                    String responseBody = "";
//                    long responseDuration = System.currentTimeMillis() - startTime;
//                    String responseSize = String.valueOf(response.getBufferSize());
//                    String redirectUrl = response.getHeader("Location");
//                    String serverInfo = response.getHeader("Server");
//
//                    System.out.println("Request: {");
//                    System.out.println("  \"uri\": \"" + uri + "\",");
//                    System.out.println("  \"method\": \"" + method + "\",");
//                    System.out.println("  \"headers\": " + headers + ",");
//                    System.out.println("  \"body\": \"" + body + "\",");
//                    System.out.println("  \"queryParams\": \"" + queryParams + "\",");
//                    System.out.println("  \"pathVariables\": \"" + pathVariables + "\",");
//                    System.out.println("  \"ipAddress\": \"" + ipAddress + "\",");
//                    System.out.println("  \"userAgent\": \"" + userAgent + "\",");
//                    System.out.println("  \"sessionId\": \"" + sessionId + "\",");
//                    System.out.println("  \"requestTime\": \"" + requestTime + "\"");
//                    System.out.println("}");
//
//                    System.out.println("Response: {");
//                    System.out.println("  \"status\": \"" + responseStatus + "\",");
//                    System.out.println("  \"headers\": " + responseHeaders + ",");
//                    System.out.println("  \"body\": " + responseBody + ",");
//                    System.out.println("  \"responseDuration\": \"" + responseDuration + "ms\",");
//                    System.out.println("  \"responseSize\": \"" + responseSize + "\",");
//                    System.out.println("  \"redirectUrl\": \"" + redirectUrl + "\",");
//                    System.out.println("  \"serverInfo\": \"" + serverInfo + "\"");
//                    System.out.println("}");
//
//                    System.out.println("Auth header: " + authHeader);
//                    System.out.println("User id: " + userId);
//                    System.out.println("Claims: " + claims);
//                    System.out.println("User details: " + userDetails);
//                    System.out.println("Auth token: " + authToken);
//                    System.out.println("Context: " + SecurityContextHolder.getContext());
                }
            }
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class JwtDetails {
        private String token;
        private Claims claims;
        private WebAuthenticationDetails webAuthenticationDetails;
    }

}
