package vn.giabaoblog.giabaoblogserver.services.authentication;

import vn.giabaoblog.giabaoblogserver.data.domains.User;

import java.util.Map;

public interface ITokenClaimComponent {
    Map<String, Object> getClaims(Map<String, Object> extraClaims, User userDetails, long expiration);
}
