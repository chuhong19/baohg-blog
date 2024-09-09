package vn.giabaoblog.giabaoblogserver.services.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.giabaoblog.giabaoblogserver.data.domains.Role;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.services.authentication.ITokenClaimComponent;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Getter
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Autowired
    private ITokenClaimComponent tokenClaimComponent;

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(User userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, User userDetails, long expiration) {

        Set<Role> userRoles = userDetails.getRoles();

        String rolesAsString = userRoles.stream()
                .map(Role::getRole)
                .collect(Collectors.joining(","));

        extraClaims.put("userId", userDetails.getId());
        extraClaims.put("roles", rolesAsString);
        extraClaims.put("username", userDetails.getUsername());
        extraClaims.put("email", userDetails.getEmail());
        extraClaims.put("firstname", userDetails.getFirstname());
        extraClaims.put("lastname", userDetails.getLastname());
        extraClaims.put("gender", userDetails.getGender());

        tokenClaimComponent.getClaims(extraClaims, userDetails, expiration);

        return Jwts.builder().
                setClaims(extraClaims).
                setSubject(String.valueOf(userDetails.getId())).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() + expiration)).
                signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userIdGetFromToken = extractId(token);
        User user = (User) userDetails;
        final String userId = String.valueOf(user.getId());
        return userIdGetFromToken.equals(userId) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
