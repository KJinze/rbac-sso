package com.diqie.rbac.sso.server.controller;

import com.diqie.rbac.sso.server.domain.AuthConstant;
import com.diqie.rbac.sso.domain.UserDetail;
import com.diqie.rbac.sso.server.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/rbac/login")
    public String login(Device device, Model model, HttpServletRequest request, String loginType){
        //判断硬件终端类型
        String deviType = "unknown";
        if (device.isNormal()) {
            deviType = "pc";//Pc端
        }
        else if (device.isMobile()) {
            deviType = "mobile";//手机端
        }
        else if (device.isTablet()) {
            deviType = "tablet";//平板
            model.addAttribute("deviType", deviType);
        }

        //软件终端类型
        String softwareType = "unknown";
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            if (userAgent.indexOf("DingTalk") > 0) {
                softwareType = "dingtalk";
            } else {
                softwareType = userAgent.indexOf("WeChat") > 0 ? "wechat" : "web";
            }
        }
        return "sso/login";
    }

    /**
    * app使用token进行登录
     * 登录后将登录信息保存到Redis
     *
    * */
    @PostMapping("/qrcode/login")
    @ResponseBody
    public ResponseEntity<String> qrcodeLogin(String pageId, String token){
        try {
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
            Object principal = oAuth2Authentication.getPrincipal();
            String userName = null;
            if (principal instanceof UserDetail) {
                UserDetail user = (UserDetail) principal;
                user.getUsername();
            } else {
                userName = principal.toString();
            }
            //登录成功存储凭证
            redisUtil.set(pageId, userName, 30 * 1000);
            //更新登录状态
            redisUtil.hset(AuthConstant.LOGIN_STAT_KEY, pageId, AuthConstant.LOGIN_STAT_LOGIN, 3 * 60 * 1000);
            return ResponseEntity.ok("登录成功");
        }catch (InvalidTokenException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TOKEN验证失败");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * */
    @RequestMapping("/qrcode/check")
    @ResponseBody
    public ResponseEntity<Boolean> qrcodeLogin(String pageId){
        try {
            //扫描成功,更新登录状态
            redisUtil.hset(AuthConstant.LOGIN_STAT_KEY, pageId, AuthConstant.LOGIN_STAT_SCANED, 3 * 60 * 1000);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * app使用token进行登录
     * 登录后将登录信息保存到Redis
     * */
    @RequestMapping("/rbac/stat/check")
    @ResponseBody
    public Object loginStatCheck(String pageId){
        //获取登录状态
        Object stat = redisUtil.hget(AuthConstant.LOGIN_STAT_KEY, pageId);
        if(stat==null){
            return "-1";
        }
        return stat;
    }
}
