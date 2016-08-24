/**
 * Copyright 2014 Unicon (R)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.ksu.lti.launch.oauth;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth.provider.ConsumerAuthentication;
import org.springframework.security.oauth.provider.OAuthAuthenticationHandler;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;

@Component
public class LTIOAuthAuthenticationHandler implements OAuthAuthenticationHandler {

    private static final Logger LOG = Logger.getLogger(LTIOAuthAuthenticationHandler.class);

    public static SimpleGrantedAuthority userGA = new SimpleGrantedAuthority("ROLE_USER");
    public static SimpleGrantedAuthority learnerGA = new SimpleGrantedAuthority("ROLE_LEARNER");
    public static SimpleGrantedAuthority instructorGA = new SimpleGrantedAuthority("ROLE_INSTRUCTOR");
    public static SimpleGrantedAuthority adminGA = new SimpleGrantedAuthority("ROLE_ADMIN");

    @PostConstruct
    public void init() {
        LOG.info("INIT");
    }

    @Override
    public Authentication createAuthentication(HttpServletRequest request, ConsumerAuthentication authentication, OAuthAccessProviderToken authToken) {
        LOG.debug("createAuthentication called");
        Collection<GrantedAuthority> authorities = new HashSet<>(authentication.getAuthorities());
        // attempt to create a user Authority
        String username = request.getParameter("custom_canvas_user_login_id");
        LOG.debug("got user name while creating authentication: " + username);
        if (StringUtils.isBlank(username)) {
            username = authentication.getName();
        }

        // TODO store lti context and user id in the principal
        Principal principal = new MyOAuthAuthenticationHandler.NamedOAuthPrincipal(username, authorities,
                authentication.getConsumerCredentials().getConsumerKey(),
                authentication.getConsumerCredentials().getSignature(),
                authentication.getConsumerCredentials().getSignatureMethod(),
                authentication.getConsumerCredentials().getSignatureBaseString(),
                authentication.getConsumerCredentials().getToken()
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        LOG.info("Authenticating user " + username);
        return auth;
    }

}
