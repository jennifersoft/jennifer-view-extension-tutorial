package com.aries.tutorial2.adapter

import com.aries.extension.data.SystemEventData
import com.aries.extension.handler.SystemEventHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil
import com.aries.tutorial2.util.AdapterFormatter

class SystemEventAdapter : SystemEventHandler {
    override fun on(events: Array<SystemEventData>) {
        val subject = PropertyUtil.getValue("system_event_adapter", "subject", "Unknown subject")
        LogUtil.info("[SystemEventAdapter] - $subject (events=${events.size})")

        events.forEachIndexed { index, data ->
            LogUtil.info(AdapterFormatter.formatSystemEvent(index + 1, data))
        }
    }
}
