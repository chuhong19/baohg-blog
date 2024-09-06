package vn.giabaoblog.giabaoblogserver.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.config.exception.TokenRefreshException;
import vn.giabaoblog.giabaoblogserver.config.exception.UserNotFoundException;
import vn.giabaoblog.giabaoblogserver.data.domains.RefreshToken;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.repository.RefreshTokenRepository;
import vn.giabaoblog.giabaoblogserver.data.repository.UserRepository;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = userOpt.get();

        revokeRefreshTokens(user);

        Date expiryDate = new Date(System.currentTimeMillis() + refreshTokenDurationMs);

        String jwtToken = Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(expiryDate.toInstant());
        refreshToken.setToken(jwtToken);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new sign in request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    private void revokeRefreshTokens(User user) {
        var validRefreshTokens = refreshTokenRepository.findAllValidRefreshTokenByUser(user.getId());
        System.out.println("Valid refresh token: " + validRefreshTokens);
        if (validRefreshTokens.isEmpty()) return;
        validRefreshTokens.forEach(token -> {
            token.setRevoked(true);
        });
        refreshTokenRepository.saveAll(validRefreshTokens);
    }

}
