package com.aries.tutorial2.adapter

import com.aries.extension.data.SystemEventData
import com.aries.extension.handler.SystemEventHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

class SystemEventAdapter : SystemEventHandler {
    override fun on(events: Array<SystemEventData>) {
        LogUtil.info("[SystemEventAdapter] - " +
                PropertyUtil.getValue("event_adapter", "subject", "Unknown subject"))

        for (data in events) {
            LogUtil.info("Subject : " + data.subject)
            LogUtil.info("Message : " + data.message)
            LogUtil.info("Data Server : " + data.dataServer)
        }
    }
}
