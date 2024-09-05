package vn.giabaoblog.giabaoblogserver.services.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.ForbiddenException;
import vn.giabaoblog.giabaoblogserver.config.exception.InvalidRoleDataException;
import vn.giabaoblog.giabaoblogserver.data.domains.Role;
import vn.giabaoblog.giabaoblogserver.data.domains.Token;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.dto.request.AuthenticationRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.AuthenticationResponse;
import vn.giabaoblog.giabaoblogserver.data.dto.request.RegisterRequest;
import vn.giabaoblog.giabaoblogserver.data.enums.TokenType;
import vn.giabaoblog.giabaoblogserver.data.repository.RoleRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.TokenRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.UserRepository;
import vn.giabaoblog.giabaoblogserver.services.RefreshTokenService;
import vn.giabaoblog.giabaoblogserver.services.UserService;
import vn.giabaoblog.giabaoblogserver.services.validation.PasswordValidatorService;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordValidatorService passwordValidatorService;

    @Autowired
    public RefreshTokenService refreshTokenService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        passwordValidatorService.checkPassword(request.getPassword());
        Optional<Role> userRoleOptional = roleRepository.findByRole("USER");

        if (userRoleOptional.isEmpty()) {
            throw new InvalidRoleDataException("Role not found");
        }

        Role userRole = userRoleOptional.get();

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .roles(Collections.singleton(userRole))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();

        var savedUser = userRepository.save(user);
        return _authentication(savedUser, Long.toString(savedUser.getId()));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsernameOrEmail(request.getUsername()).orElseThrow(() -> new ForbiddenException("Invalid username or password"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword()));
        return _authentication(user, Long.toString(user.getId()));
    }

    private AuthenticationResponse _authentication(final User user, final String uid) {
        Objects.requireNonNull(user, "Invalid user");
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken.getToken()).expiresIn(jwtService.getJwtExpiration()).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
