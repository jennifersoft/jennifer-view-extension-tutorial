package com.aries.tutorial.batch;

import com.aries.extension.data.BatchData;
import com.aries.extension.data.batch.ApplicationServiceData;
import com.aries.extension.handler.BatchHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;

public class ApplicationServiceBatch implements BatchHandler {
    @Override
    public boolean preHandle(long batchTime) {
        // TODO: Adding pre-processing code
        return true;
    }

    @Override
    public void process(BatchData[] batchData) {
        LogUtil.info("[ApplicationServiceBatch] - " +
                PropertyUtil.getValue("application_service_batch", "subject", "Unknown subject"));

        for(int i = 0; i < batchData.length; i++) {
            ApplicationServiceData data = (ApplicationServiceData) batchData[i];

            LogUtil.info("Domain ID : " + data.domainId);
            LogUtil.info("Domain Name : " + data.domainName);
            LogUtil.info("Instance Name : " + data.instanceName);
            LogUtil.info("Application Name : " + data.applicationName);
            LogUtil.info("Call Count : " + data.callCount);
            LogUtil.info("Failure Count : " + data.failureCount + "\n");
        }
    }
}

