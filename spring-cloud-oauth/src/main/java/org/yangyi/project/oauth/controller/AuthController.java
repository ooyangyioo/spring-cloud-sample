//package org.yangyi.project.oauth.controller;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.*;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
//import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.yangyi.project.oauth.entity.SysUserEntity;
//import org.yangyi.project.oauth.repository.SysUserRepository;
//
//import java.security.KeyPair;
//import java.security.interfaces.RSAPublicKey;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//public class AuthController implements InitializingBean {
//
//    private final KeyPair keyPair;
//    private final SysUserRepository sysUserRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private ClientDetailsService clientDetailsService;
//    @Autowired
//    private DefaultTokenServices tokenServices;
//
//    private OAuth2RequestFactory oAuth2RequestFactory;
//
//    @Autowired
//    private TokenEndpoint tokenEndpoint;
//
//    public AuthController(KeyPair keyPair,
//                          SysUserRepository sysUserRepository,
//                          PasswordEncoder passwordEncoder) {
//        this.keyPair = keyPair;
//        this.sysUserRepository = sysUserRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
////    @GetMapping("/oauth/token")
////    public ResponseEntity postAccessToken(Principal principal, @RequestParam Map<String, String> params) throws HttpRequestMethodNotSupportedException {
////        System.out.println(principal);
////        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, params).getBody();
////        ResponseEntity responseEntity = ResponseEntity.ok(oAuth2AccessToken);
////        return responseEntity;
////    }
//
//    @GetMapping("/oauth2/token")
//    public ResponseEntity<OAuth2AccessToken> grantToken(@RequestParam("client_id") String clientID,
//                                                        @RequestParam("client_secret") String clientSecret) {
//        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientID);
//        if (passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
//            Map<String, String> parameters = new HashMap<>();
//            parameters.put("client_id", "web");
//            parameters.put("client_secret", "123456");
//            parameters.put("grant_type", "password");
//            parameters.put("username", "yangyi");
//            parameters.put("password", "yangyi126");
//
//            TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, clientDetails);
//            TokenGranter tokenGranter = new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, this.oAuth2RequestFactory);
//            OAuth2AccessToken token = tokenGranter.grant("password", tokenRequest);
//
//            return ResponseEntity.ok(token);
//        }
//        return null;
//    }
//
//    /**
//     * 获取证书公钥
//     */
//    @GetMapping("/pub/key")
//    public Map<String, Object> publicKey() {
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAKey key = new RSAKey.Builder(publicKey).build();
//        return new JWKSet(key).toJSONObject();
//    }
//
//    /**
//     * 获取用户信息，需要验证
//     */
//    @GetMapping("/api/usr/info")
//    public ResponseEntity<SysUserEntity> userInfo(@RequestParam("username") String username) {
//        SysUserEntity userEntity = sysUserRepository.selectByUsername(username);
//        return ResponseEntity.ok(userEntity);
//    }
//
//    /**
//     * 注册不需要登录
//     */
//    @GetMapping("/user/register")
//    public ResponseEntity userRegister() {
//        System.out.println(passwordEncoder.encode("123456"));
//        System.out.println(passwordEncoder.encode("123456"));
//        SysUserEntity userEntity = new SysUserEntity();
//        userEntity.setUsername("yangyi");
//        userEntity.setPassword(passwordEncoder.encode("yangyi126"));
//        userEntity.setEmail("876359828@qq.com");
//        userEntity.setPhone("15527443931");
//        userEntity.setEnable(true);
//        userEntity.setLocked(true);
//        userEntity.setExpired(true);
//        userEntity.setPasswordExpired(true);
//        userEntity.setCreateTime(new Date());
//        String userID = sysUserRepository.save(userEntity).getUuid();
//        return ResponseEntity.ok(userID);
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        oAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
//    }
//
//}
