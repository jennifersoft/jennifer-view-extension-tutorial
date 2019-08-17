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

            LogUtil.info("Domain ID : " + data.domainId)
            LogUtil.info("Domain Name : " + data.domainName)
            LogUtil.info("Instance Name : " + data.instanceName)
            LogUtil.info("Application Name : " + data.applicationName)
            LogUtil.info("Call Count : " + data.callCount)
            LogUtil.info("Failure Count : " + data.failureCount + "\n")
        }
    }
}

