package com.aries.tutorial.batch;

import com.aries.extension.data.BatchData;
import com.aries.extension.data.batch.MetricsDataAsBusiness;
import com.aries.extension.handler.BatchHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;

public class BusinessMetricsBatch implements BatchHandler {
    @Override
    public boolean preHandle(long batchTime) {
        // TODO: Adding pre-processing code
        return true;
    }

    @Override
    public void process(BatchData[] batchData) {
        LogUtil.info("[BusinessMetricsBatch] - " +
                PropertyUtil.getValue("business_metrics_batch", "subject", "Unknown subject"));

        for(int i = 0; i < batchData.length; i++) {
            MetricsDataAsBusiness data = (MetricsDataAsBusiness) batchData[i];

            LogUtil.info("Domain ID : " + data.domainId);
            LogUtil.info("Domain Name : " + data.domainName);
            LogUtil.info("Call Count : " + data.serviceCount);
            LogUtil.info("Max TPS : " + data.maxTps);
            LogUtil.info("Active Service : " + data.activeService);
            LogUtil.info("Error Count : " + data.errorCount + "\n");
        }
    }
}
