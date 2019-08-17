package com.aries.tutorial.batch;

import com.aries.extension.data.BatchData;
import com.aries.extension.data.batch.MetricsDataAsDomain;
import com.aries.extension.handler.BatchHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;

public class DomainMetricsBatch implements BatchHandler {
    @Override
    public boolean preHandle(long batchTime) {
        // TODO: Adding pre-processing code
        return true;
    }

    @Override
    public void process(BatchData[] batchData) {
        LogUtil.info("[DomainMetricsBatch] - " +
                PropertyUtil.getValue("domain_metrics_batch", "subject", "Unknown subject"));

        for(int i = 0; i < batchData.length; i++) {
            MetricsDataAsDomain data = (MetricsDataAsDomain) batchData[i];

            StringBuilder sb = new StringBuilder();
            sb.append("Domain ID : " + data.domainId + ", ");
            sb.append("Domain Name : " + data.domainName + ", ");
            sb.append("Service Count : " + data.serviceCount + ", ");
            sb.append("Max TPS : " + data.maxTps + ", ");
            sb.append("Active Service : " + data.maxTps + ", ");
            sb.append("Error Count : " + data.errorCount);

            LogUtil.info(sb.toString());
        }
    }
}