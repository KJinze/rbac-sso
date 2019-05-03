package com.diqie.rbac.sso.server.auth;

import com.diqie.rbac.sso.server.domain.AuthConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class CustomProvider implements AuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());

    private PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailService;

    protected boolean hideUserNotFoundExceptions = true;


    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();


    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(CustomAuthenticationToken.class, authentication,
                messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // Determine username
        /*String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                : authentication.getName();*/
        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken)authentication;
        String username = customAuthenticationToken.getName();
        String devType = customAuthenticationToken.getDevType();
        String loginType = customAuthenticationToken.getLoginType();

        UserDetails user = null;
        try {
            user = retrieveUser(username);
        } catch (UsernameNotFoundException notFound) {
            logger.debug("User '" + username + "' not found");

            if (hideUserNotFoundExceptions) {
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
            } else {
                throw notFound;
            }
        }

        preAuthenticationChecks.check(user);
        additionalAuthenticationChecks(user,
                (CustomAuthenticationToken) authentication,loginType);

        postAuthenticationChecks.check(user);

        return createSuccessAuthentication(user,devType,loginType, authentication, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (CustomAuthenticationToken.class
                .isAssignableFrom(authentication));
    }

    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

    public boolean isHideUserNotFoundExceptions() {
        return hideUserNotFoundExceptions;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetailsService getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    protected final UserDetails retrieveUser(String username)
            throws AuthenticationException {
        try {
            UserDetails loadedUser = userDetailService.loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  CustomAuthenticationToken authentication, String loginType)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        if(AuthConstant.LOGIN_TPYE_QECODE.equalsIgnoreCase(loginType)){
            //未抛出UsernameNotFoundException表示认证已经通过
        }else {
            String presentedPassword = authentication.getCredentials().toString();

            if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                logger.debug("Authentication failed: password does not match stored value");

                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
            }
        }

    }

    protected Authentication createSuccessAuthentication(Object principal, String devType, String loginType,
                                                         Authentication authentication, UserDetails user) {
        CustomAuthenticationToken result = new CustomAuthenticationToken(
                principal, authentication.getCredentials(), devType, loginType,user.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");

                throw new LockedException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"));
            }

            if (!user.isEnabled()) {
                logger.debug("User account is disabled");

                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"));
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                logger.debug("User account credentials have expired");

                throw new CredentialsExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                        "User credentials have expired"));
            }
        }
    }
}
