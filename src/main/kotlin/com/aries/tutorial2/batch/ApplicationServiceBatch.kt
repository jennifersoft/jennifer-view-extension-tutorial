package com.aries.tutorial2.batch

import com.aries.extension.data.BatchData
import com.aries.extension.data.batch.ApplicationServiceData
import com.aries.extension.handler.BatchHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

class ApplicationServiceBatch : BatchHandler {
    override fun preHandle(batchTime: Long): Boolean {
        // TODO: Adding pre-processing code
        return true
    }

    override fun process(batchData: Array<BatchData>) {
        LogUtil.info("[ApplicationServiceBatch] - " + PropertyUtil.getValue("application_service_batch", "subject", "Unknown subject"))

        for (d in batchData) {
            val data = d as ApplicationServiceData

            val sb = StringBuilder()
            sb.append("Domain ID : " + data.domainId + ", ")
            sb.append("Domain Name : " + data.domainName + ", ")
            sb.append("Instance Name : " + data.instanceName + ", ")
            sb.append("Application Name : " + data.applicationName + ", ")
            sb.append("Call Count : " + data.callCount + ", ")
            sb.append("Failure Count : " + data.failureCount)

            LogUtil.info(sb.toString())
        }
    }
}

