package com.aries.tutorial2.adapter

import com.aries.extension.data.UserData
import com.aries.extension.handler.LoginHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil
import com.aries.tutorial2.util.AdapterFormatter

class LoginAdapter : LoginHandler {
    override fun preHandle(id: String, password: String): UserData? {
        val subject = PropertyUtil.getValue("login_adapter", "subject", "Unknown subject")
        LogUtil.info("[LoginAdapter] - $subject")

        val result: UserData? = if (id == "user1" && password == "password1") {
            UserData(id, password, "admin", "Tester")
        } else null

        LogUtil.info(AdapterFormatter.formatLogin(id, result))
        return result
    }

    override fun redirect(id: String, password: String): String {
        return "/dashboard/realtimeAdmin"
    }
}
