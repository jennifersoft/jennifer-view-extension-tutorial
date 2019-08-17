package com.aries.tutorial.adapter;

import com.aries.extension.data.UserData;
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;

import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    public UserData preHandle(HttpServletRequest request) {
        LogUtil.info("[SSOLoginAdapter] - " +
                PropertyUtil.getValue("sso_login_adapter", "subject", "Unknown subject"));

        String sso_user_id = request.getHeader("SSO_ID");
        String sso_user_password = request.getHeader("SSO_PASSWORD");

        if(sso_user_id == null || sso_user_password == null) {
            LogUtil.error("sso_user_id not found") ;
            return null;
        }

        return new UserData(sso_user_id, sso_user_password);
    }
}
