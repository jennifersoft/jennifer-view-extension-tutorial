package com.aries.tutorial.adapter;

import com.aries.extension.data.UserData;
import com.aries.extension.handler.LoginHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;
import com.aries.tutorial.util.AdapterFormatter;

public class LoginAdapter implements LoginHandler {
    @Override
    public UserData preHandle(String id, String password) {
        String subject = PropertyUtil.getValue("login_adapter", "subject", "Unknown subject");
        LogUtil.info("[LoginAdapter] - " + subject);

        UserData result = null;
        if (id.equals("user1") && password.equals("password1")) {
            result = new UserData(id, password, "admin", "Tester");
        }

        LogUtil.info(AdapterFormatter.formatLogin(id, result));
        return result;
    }

    @Override
    public String redirect(String id, String password) {
        return "/dashboard/realtimeAdmin";
    }
}
