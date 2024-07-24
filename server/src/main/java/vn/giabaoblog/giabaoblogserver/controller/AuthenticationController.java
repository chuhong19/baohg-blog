package vn.giabaoblog.giabaoblogserver.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.data.dto.request.AuthenticationRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.AuthenticationResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.request.RegisterRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.response.UserInfoResponse;
import vn.giabaoblog.giabaoblogserver.data.enums.Gender;
import vn.giabaoblog.giabaoblogserver.services.authentication.AuthenticationService;
import vn.giabaoblog.giabaoblogserver.services.authentication.EncryptionService;
import vn.giabaoblog.giabaoblogserver.services.authentication.JwtService;


import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    public JwtService jwtService;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> internalRegister(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(StandardResponse.create(
                authenticationService.internalRegister(request)
        ));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<StandardResponse<AuthenticationResponse>> internalAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(StandardResponse.create(
                authenticationService.internalAuthenticate(request)
        ));
    }

    @PostMapping("/internal/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping("/identify")
    public ResponseEntity<?> identifyUser(HttpServletRequest request) {
        try {
            String headerAuth = request.getHeader("Authorization");
            String jwtToken = null;
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                jwtToken = headerAuth.split(" ")[1].trim();
            }
            String userId = "";
            String username = "";
            String email = "";
            String firstname = "";
            String lastname = "";
            String gender = "";


            if (StringUtils.hasText(jwtToken)) {
                Claims claims = jwtService.extractAllClaims(jwtToken);
                Object userIdObject = claims.get("userId");
                if (userIdObject != null) {
                    userId = userIdObject.toString();
                }
                username = (String) claims.get("username");
                email = (String) claims.get("email");
                firstname = (String) claims.get("firstname");
                lastname = (String) claims.get("lastname");
                gender = (String) claims.get("gender");
            }
            UserInfoResponse userInfoResponse = new UserInfoResponse(userId, username, email, firstname, lastname, gender);
            return ResponseEntity.ok(new StandardResponse<>("200", "Check successfully", userInfoResponse));
        } catch (Exception e) {
            throw new NotFoundException("Not found");
        }
    }

    // salt generation
    @GetMapping("/salt")
    public ResponseEntity<String> generateSalt() {
        return new ResponseEntity<String>(EncryptionService.generateSalt(32), HttpStatus.CREATED);
    }
}
