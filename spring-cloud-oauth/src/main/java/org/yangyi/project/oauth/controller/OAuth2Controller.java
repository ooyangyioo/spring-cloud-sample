//package org.yangyi.project.oauth.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@SessionAttributes({"authorizationRequest"})
//public class OAuth2Controller {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Controller.class);
//
//    private final RequestCache requestCache = new HttpSessionRequestCache();
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
////    @RequestMapping("/oauth/login")
////    public String login(Model model, @Nullable Boolean error, HttpServletRequest request) {
////        if (error != null) {
////            model.addAttribute("error", error);
////        }
////        return "login";
////    }
//
//
////    @GetMapping("/auth/login")
////    @ResponseBody
////    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
////    public String loginAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {
////        SavedRequest savedRequest = requestCache.getRequest(request, response);
////        if (null != savedRequest) {
////            String targetUrl = savedRequest.getRedirectUrl();
////            LOGGER.info("引发跳转的请求: {}", targetUrl);
////            redirectStrategy.sendRedirect(request, response, "/auth/page");
////        }
////        return "哦吼！请按规矩来，不要调皮哟！";
////    }
//
////    @GetMapping("/test/login")
////    public void redirect(HttpServletResponse response) throws IOException {
////        response.sendRedirect("http://localhost:9090/oauth/authorize?response_type=code&client_id=web&redirect_uri=https://www.baidu.com");
////    }
//
////    @RequestMapping("/oauth/confirm_access")
////    public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) {
////        List<String> scopeList = new ArrayList<>();
////        String scope = request.getParameter("scope");
////        if (scope != null) {
////            String[] split = scope.split(" ");
////            for (String s : split) {
////                scopeList.add(s);
////            }
////        }
////        model.put("scopeList", scopeList);
////        return "confirm_access";
////    }
//
//}
