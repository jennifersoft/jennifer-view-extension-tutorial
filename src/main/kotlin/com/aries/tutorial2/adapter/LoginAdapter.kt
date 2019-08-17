package com.aries.tutorial2.adapter

import com.aries.extension.data.UserData
import com.aries.extension.handler.LoginHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

class LoginAdapter : LoginHandler {
    override fun preHandle(id: String, password: String): UserData? {
        LogUtil.info("[LoginAdapter] - " +
                PropertyUtil.getValue("login_adapter", "subject", "Unknown subject"))

        return if (id == "user1" && password == "password1") {
            UserData(id, password, "admin", "Tester")
        } else null

    }

    override fun redirect(id: String, password: String): String {
        return "/dashboard/realtimeAdmin"
    }
}
