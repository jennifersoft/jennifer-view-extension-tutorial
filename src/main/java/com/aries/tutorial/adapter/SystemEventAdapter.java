package com.aries.tutorial.adapter;

import com.aries.extension.data.SystemEventData;
import com.aries.extension.handler.SystemEventHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;
import com.aries.tutorial.util.AdapterFormatter;

public class SystemEventAdapter implements SystemEventHandler {
    @Override
    public void on(SystemEventData[] events) {
        String subject = PropertyUtil.getValue("system_event_adapter", "subject", "Unknown subject");
        LogUtil.info("[SystemEventAdapter] - " + subject + " (events=" + events.length + ")");

        int idx = 0;
        for (SystemEventData data : events) {
            idx++;
            LogUtil.info(AdapterFormatter.formatSystemEvent(idx, data));
        }
    }
}
