package com.aries.tutorial.adapter;

import com.aries.extension.data.EventData;
import com.aries.extension.handler.EventHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;

public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        var subject = PropertyUtil.getValue("event_adapter", "subject", "Unknown subject");
        LogUtil.info("[EventAdapter] - " + subject);

        for (var data : events) {
            LogUtil.info("Domain ID : " + data.domainId);
            LogUtil.info("Instance Name : " + data.instanceName);
            LogUtil.info("Service Name : " + data.serviceName);
            LogUtil.info("Event Level : " + data.eventLevel);

            // 쿠버네티스 환경인 경우 메타데이터 출력 (1.5.8 이상 지원)
            if (data.instanceData != null && data.instanceData.k8s != null) {
                var k8s = data.instanceData.k8s;
                LogUtil.info("Pod UID: " + k8s.podUid);
                LogUtil.info("Container: " + k8s.containerName);
            }
        }
    }
}
