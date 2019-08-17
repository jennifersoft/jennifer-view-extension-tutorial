package com.aries.tutorial.adapter;

import com.aries.extension.data.TransactionData;
import com.aries.extension.handler.TransactionHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;

public class TransactionAdapter implements TransactionHandler {
    @Override
    public void on(TransactionData[] transactions) {
        LogUtil.info("[TransactionAdapter] - " +
                PropertyUtil.getValue("transaction_adapter", "subject", "Unknown subject"));

        for(TransactionData data : transactions) {
            LogUtil.info("Domain ID : " + data.domainId);
            LogUtil.info("Instance Name : " + data.instanceName);
            LogUtil.info("Transaction ID : " + data.txid);
            LogUtil.info("Response Time : " + data.responseTime);
            LogUtil.info("Application : " + data.applicationName + "\n");
        }
    }
}