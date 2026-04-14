package com.aries.tutorial2.adapter

import com.aries.extension.data.TransactionData
import com.aries.extension.handler.TransactionHandler
import com.aries.extension.util.LogUtil
import com.aries.extension.util.PropertyUtil
import com.aries.tutorial2.util.AdapterFormatter

class TransactionAdapter : TransactionHandler {
    override fun on(transactions: Array<TransactionData>) {
        val subject = PropertyUtil.getValue("transaction_adapter", "subject", "Unknown subject")
        LogUtil.info("[TransactionAdapter] - $subject (transactions=${transactions.size})")

        transactions.forEachIndexed { index, data ->
            LogUtil.info(AdapterFormatter.formatTransaction(index + 1, data))
        }
    }
}
