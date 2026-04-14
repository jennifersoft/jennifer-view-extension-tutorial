package com.aries.tutorial2.adapter

import com.aries.extension.data.EventData
import com.aries.extension.handler.EventHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil
import com.aries.tutorial2.util.EventFormatter

class EventAdapter : EventHandler {
    override fun on(events: Array<EventData>) {
        val subject = PropertyUtil.getValue("event_adapter", "subject", "Unknown subject")
        LogUtil.info("[EventAdapter] - $subject (events=${events.size})")

        events.forEachIndexed { index, data ->
            LogUtil.info(EventFormatter.format(index + 1, data))

            // 쿠버네티스 환경 메타데이터 (1.5.8 이상). instanceData 자체가 null인 빌드도 있어 함께 가드.
            val prefix = "    ↳ "
            val instance = data.instanceData
            when {
                instance == null -> LogUtil.info("${prefix}InstanceData   : <none>")
                instance.k8s == null -> LogUtil.info("${prefix}K8s            : <non-k8s environment>")
                else -> {
                    val k8s = instance.k8s
                    LogUtil.info("${prefix}K8s Pod UID    : ${k8s.podUid}")
                    LogUtil.info("${prefix}K8s Container  : ${k8s.containerName}")
                    LogUtil.info("${prefix}K8s Node       : ${k8s.nodeName}")
                    LogUtil.info("${prefix}K8s CIDHint    : ${k8s.containerIdHint}")
                }
            }
        }
    }
}
