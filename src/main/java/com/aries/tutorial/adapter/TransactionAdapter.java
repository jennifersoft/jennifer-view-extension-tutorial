package com.aries.tutorial.adapter;

import com.aries.extension.data.TransactionData;
import com.aries.extension.handler.TransactionHandler;
import com.aries.extension.util.PropertyUtil;
import com.aries.extension.util.LogUtil;
import com.aries.tutorial.util.AdapterFormatter;

public class TransactionAdapter implements TransactionHandler {
    @Override
    public void on(TransactionData[] transactions) {
        String subject = PropertyUtil.getValue("transaction_adapter", "subject", "Unknown subject");
        LogUtil.info("[TransactionAdapter] - " + subject + " (transactions=" + transactions.length + ")");

        int idx = 0;
        for (TransactionData data : transactions) {
            idx++;
            LogUtil.info(AdapterFormatter.formatTransaction(idx, data));
        }
    }
}
