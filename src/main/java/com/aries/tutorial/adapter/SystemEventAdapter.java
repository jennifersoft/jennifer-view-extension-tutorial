package com.aries.tutorial.adapter;

import com.aries.extension.data.SystemEventData;
import com.aries.extension.handler.SystemEventHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;

public class SystemEventAdapter implements SystemEventHandler {
    @Override
    public void on(SystemEventData[] events) {
        LogUtil.info("[SystemEventHandler] - " +
                PropertyUtil.getValue("system_event_adapter", "subject", "Unknown subject"));

        for(SystemEventData data : events) {
            LogUtil.info("Subject : " + data.subject);
            LogUtil.info("Message : " + data.message);
            LogUtil.info("Data Server : " + data.dataServer);
        }
    }
}
