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

            StringBuilder sb = new StringBuilder();
            sb.append("Domain ID : " + data.domainId + ", ");
            sb.append("Domain Name : " + data.domainName + ", ");
            sb.append("Instance Name : " + data.instanceName + ", ");
            sb.append("Application Name : " + data.applicationName + ", ");
            sb.append("Call Count : " + data.callCount + ", ");
            sb.append("Failure Count : " + data.failureCount);

            LogUtil.info(sb.toString());
        }
    }
}

