package com.aries.tutorial2.batch

import com.aries.extension.data.BatchData
import com.aries.extension.data.batch.MetricsDataAsBusiness
import com.aries.extension.handler.BatchHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

class BusinessMetricsBatch : BatchHandler {
    override fun preHandle(batchTime: Long): Boolean {
        // TODO: Adding pre-processing code
        return true
    }

    override fun process(batchData: Array<BatchData>) {
        LogUtil.info("[BusinessMetricsBatch] - " + PropertyUtil.getValue("business_metrics_batch", "subject", "Unknown subject"))

        for (d in batchData) {
            val data = d as MetricsDataAsBusiness

            val sb = StringBuilder()
            sb.append("Domain ID : " + data.domainId + ", ")
            sb.append("Domain Name : " + data.domainName + ", ")
            sb.append("Service Count : " + data.serviceCount + ", ")
            sb.append("Max TPS : " + data.maxTps + ", ")
            sb.append("Active Service : " + data.maxTps + ", ")
            sb.append("Error Count : " + data.errorCount)

            LogUtil.info(sb.toString())
        }
    }
}
