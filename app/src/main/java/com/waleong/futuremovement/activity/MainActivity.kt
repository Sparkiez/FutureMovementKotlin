package com.waleong.futuremovement.activity

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.waleong.futuremovement.R
import com.waleong.futuremovement.helper.TransactionParser

import java.io.File

import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    @BindView(R.id.loading_text)
    lateinit var mLoadingTextView: TextView
    @BindView(R.id.error_text)
    lateinit var mErrorTextView: TextView
    @BindView(R.id.result_text)
    lateinit var mResultTextView: TextView
    @BindView(R.id.generate_button)
    lateinit var mGenerateButton: Button

    private var mTransactionParser: TransactionParser? = null
    private var mIsGeneratingReport: Boolean = false
    private var mOutputFilePath: String? = null

    private val TAG = MainActivity::class.simpleName;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        Timber.tag(TAG)

//        titleText.text = "hi";
        mTransactionParser = TransactionParser()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        mGenerateButton.setOnClickListener { tryToGenerateReport() }

        mResultTextView.setOnClickListener { openFile(mOutputFilePath) }
    }

    private fun tryToGenerateReport() {
        if (mIsGeneratingReport) {
            // We don't want to generate the report if it is
            // doing it now.
            return
        }

        hideAllMessages()
        showTextViewWithMessage(mLoadingTextView, "Loading, please wait")

        mIsGeneratingReport = true

        // Using threading operation to ensure conccurency on the UI
        // as we handle file reading and writing operation.
        Observable.fromCallable {
            mTransactionParser?.parseAndCreateReport(this@MainActivity, R.raw.col_definition,
                    R.raw.input, "Output.csv")
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { mIsGeneratingReport = false }
                .subscribe({ fileOutputPath ->
                    mOutputFilePath = fileOutputPath
                    showTextViewWithMessage(mResultTextView, "Report has been successfully generated. Click here to access your file at $fileOutputPath")
                }, { throwable -> showTextViewWithMessage(mErrorTextView, "There has been an error. " + throwable.message) })
    }

    private fun hideAllMessages() {
        mLoadingTextView.visibility = View.GONE
        mErrorTextView.visibility = View.GONE
        mResultTextView.visibility = View.GONE
    }

    private fun showTextViewWithMessage(textView: TextView?, message: String) {
        hideAllMessages()
        textView?.visibility = View.VISIBLE
        textView?.text = message
    }

    private fun openFile(filePath: String?) {
        if (filePath == null) {
            return
        }

        try {
            val uri = Uri.fromFile(File(filePath))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "text/plain")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        } catch (e: Exception) {
            showTextViewWithMessage(mErrorTextView, "There has been an error with opening the file. " + e.message)
        }

    }
}
