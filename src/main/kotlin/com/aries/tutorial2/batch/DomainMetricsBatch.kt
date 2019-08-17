package com.aries.tutorial2.batch

import com.aries.extension.data.BatchData
import com.aries.extension.data.batch.MetricsDataAsDomain
import com.aries.extension.handler.BatchHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

class DomainMetricsBatch : BatchHandler {
    override fun preHandle(batchTime: Long): Boolean {
        // TODO: Adding pre-processing code
        return true
    }

    override fun process(batchData: Array<BatchData>) {
        LogUtil.info("[DomainMetricsBatch] - " + PropertyUtil.getValue("domain_metrics_batch", "subject", "Unknown subject"))

        for (d in batchData) {
            val data = d as MetricsDataAsDomain

            LogUtil.info("Domain ID : " + data.domainId)
            LogUtil.info("Domain Name : " + data.domainName)
            LogUtil.info("Call Count : " + data.serviceCount)
            LogUtil.info("Max TPS : " + data.maxTps)
            LogUtil.info("Active Service : " + data.activeService)
            LogUtil.info("Error Count : " + data.errorCount + "\n")
        }
    }
}