package com.aries.tutorial.adapter;

import com.aries.extension.data.EventData;
import com.aries.extension.handler.EventHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;

public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        LogUtil.info("[EventAdapter] - " +
                PropertyUtil.getValue("event_adapter", "subject", "Unknown subject"));

        for(EventData data : events) {
            LogUtil.info("Domain ID : " + data.domainId);
            LogUtil.info("Instance Name : " + data.instanceName);
            LogUtil.info("Transaction ID : " + data.txid);
            LogUtil.info("Service Name : " + data.serviceName);
            LogUtil.info("Error Type : " + data.errorType);
            LogUtil.info("Event Level : " + data.eventLevel + "\n");
        }
    }
}
