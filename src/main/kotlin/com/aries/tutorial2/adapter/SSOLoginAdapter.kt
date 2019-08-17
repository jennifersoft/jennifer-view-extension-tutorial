package com.aries.tutorial2.adapter

import com.aries.extension.data.UserData
import com.aries.extension.handler.SSOLoginHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

import javax.servlet.http.HttpServletRequest

class SSOLoginAdapter : SSOLoginHandler {
    override fun preHandle(request: HttpServletRequest): UserData? {
        LogUtil.info("[SSOLoginAdapter] - " +
                PropertyUtil.getValue("sso_login_adapter", "subject", "Unknown subject"))

        val sso_user_id = request.getHeader("SSO_ID")
        val sso_user_password = request.getHeader("SSO_PASSWORD")

        if (sso_user_id == null || sso_user_password == null) {
            LogUtil.error("sso_user_id not found")
            return null
        }

        return UserData(sso_user_id, sso_user_password)
    }
}
