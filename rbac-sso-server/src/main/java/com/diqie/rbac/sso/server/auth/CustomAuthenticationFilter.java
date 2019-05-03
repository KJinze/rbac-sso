package com.diqie.rbac.sso.server.auth;

import com.diqie.rbac.sso.server.domain.AuthConstant;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends
        AbstractAuthenticationProcessingFilter {
    // ~ Static fields/initializers
    // =====================================================================================

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    public static final String SPRING_SECURITY_FORM_DEVTYPE_KEY = "devType";
    public static final String SPRING_SECURITY_FORM_LOGINTYPE_KEY = "loginType";
    public static final String SPRING_SECURITY_QR_PAGEID_KEY = "pageId";

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private String devTypeParameter = SPRING_SECURITY_FORM_DEVTYPE_KEY;
    private String loginTypeParameter = SPRING_SECURITY_FORM_LOGINTYPE_KEY;
    private String pageIdParameter = SPRING_SECURITY_QR_PAGEID_KEY;
    private boolean postOnly = false;

    // ~ Constructors
    // ===================================================================================================

    public CustomAuthenticationFilter() {
        super(new OrRequestMatcher(new AntPathRequestMatcher("/rbac/auth", "POST"),
                new AntPathRequestMatcher("/rbac/auth", "GET")));
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String loginType = obtainLoginType(request);
        String credentials = null;
        if(AuthConstant.LOGIN_TPYE_QECODE.equalsIgnoreCase(loginType)){
            username = obtainPageId(request);
        }else{
            username = obtainUsername(request);
            credentials = obtainPassword(request);
        }

        String devType = obtainDevType(request);


        if (username == null) {
            username = "";
        }

        if (credentials == null) {
            credentials = "";
        }

        if (devType == null) {
            devType = "";
        }

        if (loginType == null) {
            loginType = "";
        }

        username = username.trim();

        CustomAuthenticationToken authRequest = new CustomAuthenticationToken(
                username, credentials,devType,loginType);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    protected String obtainDevType(HttpServletRequest request) {
        return request.getParameter(devTypeParameter);
    }

    protected String obtainLoginType(HttpServletRequest request) {
        return request.getParameter(loginTypeParameter);
    }

    protected String obtainPageId(HttpServletRequest request) {
        return request.getParameter(pageIdParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              CustomAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return usernameParameter;
    }

    public final String getPasswordParameter() {
        return passwordParameter;
    }
}
