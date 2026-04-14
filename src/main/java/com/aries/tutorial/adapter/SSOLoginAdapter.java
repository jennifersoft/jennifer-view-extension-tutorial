package com.aries.tutorial.adapter;

import com.aries.extension.data.UserData;
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;
import com.aries.tutorial.util.AdapterFormatter;

import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    public UserData preHandle(HttpServletRequest request) {
        String subject = PropertyUtil.getValue("sso_login_adapter", "subject", "Unknown subject");
        LogUtil.info("[SSOLoginAdapter] - " + subject);

        String ssoUserId = request.getHeader("SSO_ID");
        String ssoUserPassword = request.getHeader("SSO_PASSWORD");

        UserData result = null;
        if (ssoUserId != null && ssoUserPassword != null) {
            result = new UserData(ssoUserId, ssoUserPassword);
        }

        LogUtil.info(AdapterFormatter.formatSsoLogin(ssoUserId, result));
        return result;
    }
}
