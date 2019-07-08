package com.waleong.futuremovement.helper

import android.content.Context

import com.google.gson.reflect.TypeToken
import com.waleong.futuremovement.R
import com.waleong.futuremovement.model.ColumnDefinition
import com.waleong.futuremovement.util.FileParserUtil
import com.waleong.futuremovement.util.FileWriterUtil
import com.waleong.futuremovement.util.RecordParserUtil
import com.waleong.futuremovement.util.RecordWriterUtil

import java.io.IOException
import java.lang.reflect.Type
import java.util.ArrayList

import timber.log.Timber

/**
 * This is a helper that parse in a input file, and then output a CSV file
 * which shows the business user its daily summary report
 * Created by raymondleong on 04,July,2019
 */
class TransactionParser {

    private var mColumnDefinitions: List<ColumnDefinition>? = null

    @Throws(IOException::class)
    fun parseAndCreateReport(context: Context, columnDefResFile: Int, inputResFile: Int, outputFileName: String): String {
        // Read the human readable json file which contains the column definition,
        // and convert it into a list of objects.
        mColumnDefinitions = generateColumnDefinitions(context, columnDefResFile)
        val transactions = generateTransactions(context, mColumnDefinitions, inputResFile)
        return writeToOutput(context, transactions, outputFileName)
    }

    /**
     * @param context
     * @param columnDefinitions
     * @param inputFile         The res id of the transactional input file that is to be read.
     * @return A list of transactions, where the transactions column and value is saved to a key pair.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun generateTransactions(context: Context, columnDefinitions: List<ColumnDefinition>?, inputFile: Int): List<Map<String, String>> {
        // Read the files 'input.txt' and save the data into a list of column mappings (column name, column value).
        val transactions = ArrayList<Map<String, String>>()


        FileParserUtil.readFile(context, inputFile, object: FileParserUtil.OnLineParsedListener {
            override fun onLineParsed(line: String) {
                val transactionMap = columnDefinitions?.let { RecordParserUtil.parse(it, line) }

                if (transactionMap == null || transactionMap.isEmpty()) {
                    // Data is null, therefore its not a valid record.
                    return
                }

                transactions.add(transactionMap)
            }
        })

        return transactions
    }

    /**
     * @param context
     * @param columnDefFile The res id of the column definition file that is to be read.
     * @return A list of column definitions, which tells us how we parse each row of transactions.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun generateColumnDefinitions(context: Context, columnDefFile: Int): List<ColumnDefinition>? {
        val columnDefinition = FileParserUtil.readFile(context, columnDefFile)
        val columnDefinitionsType = object : TypeToken<ArrayList<ColumnDefinition>>() {

        }.type
        return GsonHelper.parse<List<ColumnDefinition>>(columnDefinition, columnDefinitionsType)
    }

    /**
     * @param context
     * @param transactions   A list of transactions, where the transactions column and value is saved to a key pair.
     * @param outputFileName The name of the file to be outputted once the daily summary reported is generated.
     * @return The path to the file.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun writeToOutput(context: Context, transactions: List<Map<String, String>>, outputFileName: String): String {
        // Write to CSV now.
        val csvOutput = RecordWriterUtil.generateOutput(transactions)
        Timber.tag(TransactionParser::class.java.toString()).v("writeToOutput(): Output is \n$csvOutput")
        return FileWriterUtil.writeToFile(context, csvOutput, outputFileName)
    }
}
