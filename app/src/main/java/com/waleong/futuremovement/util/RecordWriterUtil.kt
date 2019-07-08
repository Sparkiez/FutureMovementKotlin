package com.waleong.futuremovement.util

import android.text.TextUtils

import com.waleong.futuremovement.model.ColumnDefinition

import java.io.BufferedReader

/**
 * A writer that writes the output of a daily summary CSV file
 * by extracting the relevant information from a list of parsed transactional
 * record.
 * Created by raymondleong on 03,July,2019
 */
object RecordWriterUtil {

    fun generateOutput(transactions: List<Map<String, String>>): String {
        val stringBuilder = StringBuilder()

        // Creating the header.
        stringBuilder.append(TextUtils.join(",", arrayOf("Client_Information", "Product_Information", "Total_Transaction_Amount")))

        for (i in transactions.indices) {
            // Append carriage return before writing the next line.
            stringBuilder.append("\n")

            val transaction = transactions[i]
            val clientInfo = generateClientInfo(transaction)
            val productInfo = generateProductInfo(transaction)
            val totalTransactionAmount = generateTotalTransactionAmount(transaction)!!.toString() + ""
            val newLine = TextUtils.join(",", arrayOf(clientInfo, productInfo, totalTransactionAmount))

            stringBuilder.append(newLine)
        }

        return stringBuilder.toString()
    }

    private fun generateClientInfo(transaction: Map<String, String>): String {
        val clientType = transaction[ColumnDefinition.KEY_CLIENT_TYPE]
        val clientNumber = transaction[ColumnDefinition.KEY_CLIENT_NUMBER]
        val clientAccountNumber = transaction[ColumnDefinition.KEY_ACCOUNT_NUMBER]
        val clientSubAccountNumber = transaction[ColumnDefinition.KEY_SUBACCOUNT_NUMBER]

        return clientType + clientNumber + clientAccountNumber + clientSubAccountNumber
    }

    private fun generateProductInfo(transaction: Map<String, String>): String {
        val exchangeCode = transaction[ColumnDefinition.KEY_EXCHANGE_CODE]
        val productGroupCode = transaction[ColumnDefinition.KEY_PRODUCT_GROUP_CODE]
        val symbol = transaction[ColumnDefinition.KEY_SYMBOL]
        val expirationDate = transaction[ColumnDefinition.KEY_EXPIRATION_DATE]

        return exchangeCode + productGroupCode + symbol + expirationDate
    }

    private fun generateTotalTransactionAmount(transaction: Map<String, String>): Int? {
        val quantityLong = transaction[ColumnDefinition.KEY_QUANTITY_LONG]
        val quantityShort = transaction[ColumnDefinition.KEY_QUANTITY_SHORT]

        // Pad it with 0 until it is 10 number.
        val quantityLongAsInt = NumberUtil.parseInteger(quantityLong)
        val quantityShortAsInt = NumberUtil.parseInteger(quantityShort)

        return if (quantityLongAsInt == null || quantityLongAsInt == null) {
            null
        } else quantityLongAsInt!! - quantityShortAsInt!!
    }
}
