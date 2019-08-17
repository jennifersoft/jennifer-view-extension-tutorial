package com.aries.tutorial2.adapter

import com.aries.extension.data.EventData
import com.aries.extension.handler.EventHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

class EventAdapter : EventHandler {
    override fun on(events: Array<EventData>) {
        LogUtil.info("[EventAdapter] - " +
                PropertyUtil.getValue("event_adapter", "subject", "Unknown subject"))

        for (data in events) {
            LogUtil.info("Domain ID : " + data.domainId)
            LogUtil.info("Instance Name : " + data.instanceName)
            LogUtil.info("Transaction ID : " + data.txid)
            LogUtil.info("Service Name : " + data.serviceName)
            LogUtil.info("Error Type : " + data.errorType)
            LogUtil.info("Event Level : " + data.eventLevel + "\n")
        }
    }
}
