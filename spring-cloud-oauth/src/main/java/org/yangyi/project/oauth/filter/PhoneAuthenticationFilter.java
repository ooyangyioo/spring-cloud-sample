package org.yangyi.project.oauth.filter;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.yangyi.project.oauth.token.PhoneAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义手机号登录
 * 参考
 *
 * @see UsernamePasswordAuthenticationFilter
 */
public class PhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String SPRING_SECURITY_PHONE_KEY = "phone";
    private static final String SPRING_SECURITY_VERIFY_CODE_KEY = "smsCode";
    private String phoneParameter = SPRING_SECURITY_PHONE_KEY;
    private String smsCodeParameter = SPRING_SECURITY_VERIFY_CODE_KEY;
    private boolean postOnly = true;

    public PhoneAuthenticationFilter() {
        super(new AntPathRequestMatcher("/phone-login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String phone = obtainPhone(request);
        String smsCode = obtainSmsCode(request);

        PhoneAuthenticationToken authRequest = new PhoneAuthenticationToken(phone, smsCode);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, PhoneAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(phoneParameter);
    }

    protected String obtainSmsCode(HttpServletRequest request) {
        return request.getParameter(smsCodeParameter);
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public void setPhoneParameter(String phoneParameter) {
        this.phoneParameter = phoneParameter;
    }

    public String getSmsCodeParameter() {
        return smsCodeParameter;
    }

    public String getPhoneParameter() {
        return phoneParameter;
    }

    public void setSmsCodeParameter(String smsCodeParameter) {
        this.smsCodeParameter = smsCodeParameter;
    }

}
