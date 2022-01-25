package org.myorg.modules.web.security.authentication.resolver;

import org.myorg.modules.web.security.authentication.token.CustomAbstractAuthenticationToken;
import org.myorg.modules.web.security.authentication.token.SessionAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class SessionAuthenticationTokenResolver implements AuthenticationTokenResolver {

    private static final String SESSION = "session";

    private String getSession(HttpServletRequest request) {
        String session = request.getParameter(SESSION);
        if (session == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (Objects.equals(cookie.getName(), SESSION)) {
                        return cookie.getValue();
                    }
                }
            }
        }

        return session;
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return getSession(request) != null;
    }

    @Override
    public CustomAbstractAuthenticationToken createToken(HttpServletRequest request) {
        return new SessionAuthenticationToken(getSession(request));
    }

    @Override
    public String getName() {
        return "Session resolver";
    }
}
