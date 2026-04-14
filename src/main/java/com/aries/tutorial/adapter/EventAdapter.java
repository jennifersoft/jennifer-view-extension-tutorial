package com.aries.tutorial.adapter;

import com.aries.extension.data.EventData;
import com.aries.extension.data.InstanceData;
import com.aries.extension.data.K8s;
import com.aries.extension.handler.EventHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;
import com.aries.tutorial.util.AdapterFormatter;

public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        String subject = PropertyUtil.getValue("event_adapter", "subject", "Unknown subject");
        LogUtil.info("[EventAdapter] - " + subject + " (events=" + events.length + ")");

        int idx = 0;
        for (EventData data : events) {
            idx++;
            LogUtil.info(AdapterFormatter.formatEvent(idx, data));

            // 쿠버네티스 환경 메타데이터 (1.5.8 이상). instanceData 자체가 null인 빌드도 있어 함께 가드.
            String prefix = "    ↳ ";
            InstanceData instance = data.instanceData;
            if (instance == null) {
                LogUtil.info(prefix + "InstanceData   : <none>");
            } else if (instance.k8s == null) {
                LogUtil.info(prefix + "K8s            : <non-k8s environment>");
            } else {
                K8s k8s = instance.k8s;
                LogUtil.info(prefix + "K8s Pod UID    : " + k8s.podUid);
                LogUtil.info(prefix + "K8s Container  : " + k8s.containerName);
                LogUtil.info(prefix + "K8s Node       : " + k8s.nodeName);
                LogUtil.info(prefix + "K8s CIDHint    : " + k8s.containerIdHint);
            }
        }
    }
}
