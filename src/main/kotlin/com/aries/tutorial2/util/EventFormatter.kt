package com.aries.tutorial2.util

import com.aries.extension.data.EventData

object EventFormatter {

    fun format(idx: Int, data: EventData): String = buildString(192) {
        append("▸ #").append(idx).append("  ")
        append('⟨').append(data.eventLevel).append("⟩  ")
        append("domain#").append(data.domainId).append('·').append(data.instanceName)
        append("  │  biz=").append(data.businessName)
        append("  │  tx=").append(data.txid)
        append("  │  svc=").append(data.serviceName)
        append("  │  ").append(data.errorType)
    }
}
