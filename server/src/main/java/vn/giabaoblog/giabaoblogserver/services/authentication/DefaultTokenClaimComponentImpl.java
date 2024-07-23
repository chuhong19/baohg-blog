package vn.giabaoblog.giabaoblogserver.services.authentication;

import vn.giabaoblog.giabaoblogserver.data.domains.User;

import java.util.Map;

public class DefaultTokenClaimComponentImpl implements ITokenClaimComponent {

    @Override
    public Map<String, Object> getClaims(Map<String, Object> extraClaims, User userDetails, long expiration) {
        return extraClaims;
    }
}
