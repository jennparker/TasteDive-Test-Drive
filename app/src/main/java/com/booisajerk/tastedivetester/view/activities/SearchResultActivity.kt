package com.booisajerk.tastedivetester.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.booisajerk.tastedivetester.R
import com.booisajerk.tastedivetester.model.Media
import com.booisajerk.tastedivetester.presenter.MediaPresenter
import com.booisajerk.tastedivetester.shared.Constants
import com.booisajerk.tastedivetester.shared.TextHelpers.formattedResultTitleText
import com.booisajerk.tastedivetester.shared.hide
import com.booisajerk.tastedivetester.shared.show
import com.booisajerk.tastedivetester.view.adapters.MediaAdapter
import com.booisajerk.tastedivetester.view.interfaces.MediaContract

class SearchResultActivity : BaseActivity(), MediaContract {

    private val mediaAdapter = MediaAdapter()

    private val recyclerView: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    private val resultItemText: TextView by lazy {
        findViewById<TextView>(R.id.searchResultText)
    }

    private val tasteDiveButton: Button by lazy {
        findViewById<Button>(R.id.openTasteDive)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById<ProgressBar>(R.id.progressBar)
    }

    private lateinit var requestedTitle: String

    private val mediaPresenter = MediaPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        prepareRecyclerView()

        recyclerView.adapter = mediaAdapter

        mediaPresenter.onViewCreated(this)
        mediaPresenter.requestMedia(intent.getStringExtra(Constants.INTENT_KEY))
    }

    override fun onMediaLoaded(media: List<Media>) {
        mediaAdapter.setDataSource(media)
        showSuccessfulResultMessage()
    }

    override fun requestedTitle(title: String) {
        requestedTitle = title
    }

    override fun onError(throwable: Throwable) {
        Log.e(TAG, "IOException: $throwable")
        resultItemText.text = getString(R.string.general_error)
    }

    override fun showLoading() {
        progressBar.show()
    }

    override fun hideLoading() {
        progressBar.hide()
    }

    override fun isNoSuchMediaError() {
        showNoSuchMediaMessage()
        showTasteDiveButton()
    }

    override fun isNoResultsError() {
        showNoResultsMessage()
        showTasteDiveButton()
    }

    override fun isServerError() {
        showNoResponseMessage()
    }

    private fun prepareRecyclerView() {
        val viewManager = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManager
        recyclerView.setHasFixedSize(true)
    }

    private fun showSuccessfulResultMessage() {
        resultItemText.text = formattedResultTitleText(
            requestedTitle,
            R.string.results_for_text,
            this
        )
    }

    private fun showNoResponseMessage() {
        resultItemText.setText(R.string.search_fail_error)
    }

    private fun showNoResultsMessage() {
        resultItemText.text = formattedResultTitleText(
            requestedTitle,
            R.string.no_results_error,
            this
        )
    }

    private fun showNoSuchMediaMessage() {
        resultItemText.text = formattedResultTitleText(
            requestedTitle,
            R.string.no_such_item,
            this
        )
        showTasteDiveButton()
    }

    private fun showTasteDiveButton() {
        tasteDiveButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.BASE_URL)
            startActivity(intent)
        }
        tasteDiveButton.show()
    }

    companion object {
        private const val TAG = "SearchResultActivity"
    }
}