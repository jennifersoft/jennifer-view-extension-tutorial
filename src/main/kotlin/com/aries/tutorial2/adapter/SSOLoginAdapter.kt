package com.aries.tutorial2.adapter

import com.aries.extension.data.UserData
import com.aries.extension.handler.SSOLoginHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil
import com.aries.tutorial2.util.AdapterFormatter

import javax.servlet.http.HttpServletRequest

class SSOLoginAdapter : SSOLoginHandler {
    override fun preHandle(request: HttpServletRequest): UserData? {
        val subject = PropertyUtil.getValue("sso_login_adapter", "subject", "Unknown subject")
        LogUtil.info("[SSOLoginAdapter] - $subject")

        val ssoUserId: String? = request.getHeader("SSO_ID")
        val ssoUserPassword: String? = request.getHeader("SSO_PASSWORD")

        val result: UserData? = if (ssoUserId != null && ssoUserPassword != null) {
            UserData(ssoUserId, ssoUserPassword)
        } else null

        LogUtil.info(AdapterFormatter.formatSsoLogin(ssoUserId, result))
        return result
    }
}
