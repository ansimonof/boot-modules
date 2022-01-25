package org.myorg.modules.web.security.authentication.resolver;

import org.myorg.modules.web.security.authentication.token.CustomAbstractAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationTokenResolver {

    boolean supports(HttpServletRequest request);

    CustomAbstractAuthenticationToken createToken(HttpServletRequest request);

    String getName();
}
