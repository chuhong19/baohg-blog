package vn.giabaoblog.giabaoblogserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.giabaoblog.giabaoblogserver.data.dto.request.AuthenticationRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.request.TokenRefreshRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.AuthenticationResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.request.RegisterRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.StandardResponse;
import vn.giabaoblog.giabaoblogserver.services.authentication.AuthenticationService;
import vn.giabaoblog.giabaoblogserver.services.authentication.EncryptionService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(StandardResponse.create(
                authenticationService.register(request)
        ));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<StandardResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(StandardResponse.create(
                authenticationService.authenticate(request)
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return authenticationService.refreshToken(request);

    }

    @GetMapping("/identify")
    public ResponseEntity<?> identifyUser(HttpServletRequest request) {
        return authenticationService.identifyUser(request);
    }

    @GetMapping("/salt")
    public ResponseEntity<String> generateSalt() {
        return new ResponseEntity<String>(EncryptionService.generateSalt(32), HttpStatus.CREATED);
    }
}
