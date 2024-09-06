package vn.giabaoblog.giabaoblogserver.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.config.exception.NotFoundException;
import vn.giabaoblog.giabaoblogserver.config.exception.TokenRefreshException;
import vn.giabaoblog.giabaoblogserver.data.domains.RefreshToken;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.dto.request.AuthenticationRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.request.TokenRefreshRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.AuthenticationResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.request.RegisterRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.response.TokenRefreshResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.response.UserInfoResponse;
import vn.giabaoblog.giabaoblogserver.services.RefreshTokenService;
import vn.giabaoblog.giabaoblogserver.services.authentication.AuthenticationService;
import vn.giabaoblog.giabaoblogserver.services.authentication.EncryptionService;
import vn.giabaoblog.giabaoblogserver.services.authentication.JwtService;


import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    public JwtService jwtService;

    @Autowired
    public RefreshTokenService refreshTokenService;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> internalRegister(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(StandardResponse.create(
                authenticationService.register(request)
        ));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<StandardResponse<AuthenticationResponse>> internalAuthenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(StandardResponse.create(
                authenticationService.authenticate(request)
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    var accessToken = jwtService.generateToken(user);
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
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
