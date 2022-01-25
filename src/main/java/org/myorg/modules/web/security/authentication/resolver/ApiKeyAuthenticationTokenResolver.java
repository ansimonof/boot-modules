package org.myorg.modules.web.security.authentication.resolver;

import org.myorg.modules.web.security.authentication.token.ApiKeyAuthenticationToken;
import org.myorg.modules.web.security.authentication.token.CustomAbstractAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class ApiKeyAuthenticationTokenResolver implements AuthenticationTokenResolver {

    private static final String API_KEY = "api_key";

    private String getApiKey(HttpServletRequest request) {
        String apiKey = request.getParameter(API_KEY);
        if (apiKey == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (Objects.equals(cookie.getName(), API_KEY)) {
                        return cookie.getValue();
                    }
                }
            }
        }

        return apiKey;
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return getApiKey(request) != null;
    }

    @Override
    public CustomAbstractAuthenticationToken createToken(HttpServletRequest request) {
        return new ApiKeyAuthenticationToken(getApiKey(request));
    }

    @Override
    public String getName() {
        return "Api key resolver";
    }
}
