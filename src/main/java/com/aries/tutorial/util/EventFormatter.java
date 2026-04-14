package com.aries.tutorial.util;

import com.aries.extension.data.EventData;

public final class EventFormatter {

    private EventFormatter() {
    }

    public static String format(int idx, EventData data) {
        StringBuilder sb = new StringBuilder(192);
        sb.append("▸ #").append(idx).append("  ");
        sb.append('⟨').append(data.eventLevel).append("⟩  ");
        sb.append("domain#").append(data.domainId).append('·').append(data.instanceName);
        sb.append("  │  biz=").append(data.businessName);
        sb.append("  │  tx=").append(data.txid);
        sb.append("  │  svc=").append(data.serviceName);
        sb.append("  │  ").append(data.errorType);
        return sb.toString();
    }
}
