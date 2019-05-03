package com.diqie.rbac.sso.server.auth;

import com.diqie.rbac.sso.server.util.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    private static Logger logger = LoggerFactory.getLogger(CustomPasswordEncoder.class);
    @Override
    public String encode(CharSequence rawPassword) {

        return Md5Utils.hash(rawPassword.toString());
    }
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        logger.debug("rawPassword传入密码：{}，数据库加密密码：{}",rawPassword,encodedPassword);
        boolean equals = encodedPassword.equals(encode(rawPassword));
        return equals;
    }
}
