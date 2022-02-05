package org.myorg.modules.web.security.configuration;

import org.myorg.modules.web.security.authentication.provider.AnonymousAuthenticationProvider;
import org.myorg.modules.web.security.authentication.provider.ApiKeyAuthenticationProvider;
import org.myorg.modules.web.security.authentication.provider.SessionAuthenticationProvider;
import org.myorg.modules.web.security.authorization.PrivilegeAccessDecisionVoter;
import org.myorg.modules.web.security.filter.AuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private PrivilegeAccessDecisionVoter privilegeAccessDecisionVoter;

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(privilegeAccessDecisionVoter);
        return new UnanimousBased(decisionVoters);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
               // .maximumSessions(2)
               // .and()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .accessDecisionManager(accessDecisionManager())
                .and()
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }

    @Autowired
    private AnonymousAuthenticationProvider anonymousAuthenticationProvider;

    @Autowired
    private ApiKeyAuthenticationProvider apiKeyAuthenticationProvider;

    @Autowired
    private SessionAuthenticationProvider sessionAuthenticationProvider;

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(anonymousAuthenticationProvider);
        auth.authenticationProvider(apiKeyAuthenticationProvider);
        auth.authenticationProvider(sessionAuthenticationProvider);
    }

}
