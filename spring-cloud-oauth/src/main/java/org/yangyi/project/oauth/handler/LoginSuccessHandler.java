package org.yangyi.project.oauth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.yangyi.project.oauth.util.ResponseUtil;
import org.yangyi.project.oauth.vo.ResponseVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 参考 SavedRequestAwareAuthenticationSuccessHandler
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            ResponseUtil.okResponse(response, new ResponseVO<>("1", "成功"));
        } else {
            clearAuthenticationAttributes(request);
            String targetUrl = savedRequest.getRedirectUrl();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }

}
