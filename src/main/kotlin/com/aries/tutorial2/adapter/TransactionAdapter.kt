package com.aries.tutorial2.adapter

import com.aries.extension.data.TransactionData
import com.aries.extension.handler.TransactionHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil

class TransactionAdapter : TransactionHandler {
    override fun on(transactions: Array<TransactionData>) {
        LogUtil.info("[TransactionAdapter] - " +
                PropertyUtil.getValue("transaction_adapter", "subject", "Unknown subject"))

        for (data in transactions) {
            LogUtil.info("Domain ID : " + data.domainId)
            LogUtil.info("Instance Name : " + data.instanceName)
            LogUtil.info("Transaction ID : " + data.txid)
            LogUtil.info("Response Time : " + data.responseTime)
            LogUtil.info("Application : " + data.applicationName + "\n")
        }
    }
}