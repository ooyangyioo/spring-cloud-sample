package org.yangyi.project.oauth.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;
import org.yangyi.project.oauth.util.ResponseUtil;
import org.yangyi.project.oauth.vo.ResponseVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private boolean useForward = false;
    private String redirectUrl;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (useForward) {
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        } else {
            ResponseUtil.okResponse(response, new ResponseVO("0", "请登录！"));
        }
    }

    public boolean isUseForward() {
        return useForward;
    }

    public void setUseForward(boolean useForward) {
        this.useForward = useForward;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
