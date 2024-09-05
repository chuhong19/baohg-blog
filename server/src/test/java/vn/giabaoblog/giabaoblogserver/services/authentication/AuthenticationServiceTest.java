package vn.giabaoblog.giabaoblogserver.services.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.giabaoblog.giabaoblogserver.data.domains.Role;
import vn.giabaoblog.giabaoblogserver.data.domains.Token;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.dto.request.RegisterRequest;
import vn.giabaoblog.giabaoblogserver.data.dto.response.AuthenticationResponse;
import vn.giabaoblog.giabaoblogserver.data.enums.Gender;
import vn.giabaoblog.giabaoblogserver.data.repository.RoleRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.TokenRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.UserRepository;
import vn.giabaoblog.giabaoblogserver.services.UserService;
import vn.giabaoblog.giabaoblogserver.services.validation.PasswordValidatorService;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private PasswordValidatorService passwordValidatorService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void internalRegister_Success() {
        // Mock data
        RegisterRequest request = new RegisterRequest("testUser", "testEmail", "password", "John", "Doe", Gender.MALE);
        User user = User.builder().id(1L).username("testUser").build();
        Role role = new Role(1L, "USER");

        // Mock behavior
        when(roleRepository.findByRole("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        // Invoke method
        AuthenticationResponse response = authenticationService.register(request);

        // Assert result
        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        // Verify interactions
        verify(passwordValidatorService).checkPassword(request.getPassword());
        verify(roleRepository).findByRole("USER");
        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

}
