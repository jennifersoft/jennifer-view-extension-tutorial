package com.aries.tutorial.adapter;

import com.aries.extension.data.UserData;
import com.aries.extension.handler.LoginHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;

public class LoginAdapter implements LoginHandler {
    @Override
    public UserData preHandle(String id, String password) {
        LogUtil.info("[LoginAdapter] - " +
                PropertyUtil.getValue("login_adapter", "subject", "Unknown subject"));

        if(id.equals("user1") && password.equals("password1")) {
            return new UserData(id, password, "admin", "Tester");
        }

        return null;
    }

    @Override
    public String redirect(String id, String password) {
        return "/dashboard/realtimeAdmin";
    }
}
