package com.booisajerk.tastedivetester.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.booisajerk.tastedivetester.MoshiBuilder
import com.booisajerk.tastedivetester.R
import com.booisajerk.tastedivetester.model.Media
import com.booisajerk.tastedivetester.shared.TextHelpers.encodeQueryString
import com.booisajerk.tastedivetester.shared.TextHelpers.formattedResultTitleText
import com.booisajerk.tastedivetester.model.ResponseData
import com.booisajerk.tastedivetester.shared.Constants
import com.booisajerk.tastedivetester.view.adapters.CustomAdapter
import com.squareup.moshi.JsonAdapter
import java.io.IOException

class SearchResultActivity : BaseActivity() {
    private lateinit var adapter: JsonAdapter<ResponseData>
    private lateinit var mediaResponse: ResponseData
    private lateinit var resultItemText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var mediaList: ArrayList<Media> = ArrayList()
    private lateinit var searchString: String
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        resultItemText = findViewById(R.id.searchResultText)

        // Retrieve the search string from the MainActivity
        searchString = encodeQueryString(intent.getStringExtra(Constants.INTENT_KEY))

        // Show progress bar while results load
        progress = findViewById(R.id.progressBar)
        progress.isIndeterminate = true
        progress.visibility = ProgressBar.VISIBLE

        viewManager = LinearLayoutManager(this)

        viewAdapter = CustomAdapter(mediaList)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // improves performance
            // use if changes in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use linear layout manager
            layoutManager = viewManager

            // assign viewAdapter
            adapter = viewAdapter

            requestMedia(searchString)
        }
    }

    private fun requestMedia(enteredMedia: String) {
        Log.d(TAG, "requestMedia() called")
        val requestQueue = Volley.newRequestQueue(this)
        val url =
            Constants.BASE_URL + Constants.API + Constants.QUERY_KEY + enteredMedia + Constants.INFO_LEVEL + Constants.TASTE_DIVE_API_KEY

        val request = StringRequest(
            Request.Method.GET
            , url
            , Listener<String> { response: String ->
                Log.d(TAG, "Response received")

                val moshi = MoshiBuilder.moshiInstance
                adapter = moshi.adapter(ResponseData::class.java)

                try {
                    mediaResponse = adapter.fromJson(response)!!

                    println("Response:  $mediaResponse")

                    // Error response received
                    // Either user hasn't entered a vaild key or hourly quota has been reached
                    if (mediaResponse.similar == null) {

                        showNoResponseMessage()
                    }

                    // A valid response is received
                    else {

                        // If results are empty
                        if (mediaResponse.similar!!.results?.isNullOrEmpty()!!) {
                            Log.d(TAG, "results are empty")

                            // If result type is not unknown (TasteDive has a record of this media, but doesn't have
                            // enough votes to make recommendations for it
                            if (mediaResponse.similar!!.info?.get(0)?.type != "unknown") {
                                showNoResultsMessage()
                            }

                            // If result type is unknown (TasteDive doesn't know what the search item is)
                            else {
                                showNoSuchMediaMessage()

                            }
                            showTastediveButton()
                        }
                        if (mediaResponse.similar!!.results?.isNotEmpty()!!) {

                            // Response with results
                            for ((count, item) in mediaResponse.similar?.results?.withIndex()!!) {
                                mediaList.add(
                                    Media(
                                        mediaResponse.similar?.results?.get(count)?.name,
                                        mediaResponse.similar?.results?.get(count)?.type,
                                        mediaResponse.similar?.results?.get(count)?.description
                                    )
                                )
                                println("Adding new Media to mediaList: $item")
                            }

                            showSuccessfulResultMessage()

                            Log.d(TAG, "Response: $mediaResponse")
                        }
                    }
                    // Don't show title until it is properly formatted
                    resultItemText.visibility = View.VISIBLE

                    // Hide the progress bar now that data is loaded
                    progress.visibility = ProgressBar.INVISIBLE

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )
        Log.d(TAG, "Request is: $request")
        requestQueue.add(request)
    }

    private fun showNoResponseMessage() {
        resultItemText.setText(R.string.search_fail_error)
    }

    private fun showNoResultsMessage() {
        resultItemText.text = formattedResultTitleText(
            mediaResponse.similar?.info?.get(0)?.name,
            R.string.no_results_error,
            this
        )
        Log.d(TAG, "No results for item ${resultItemText.text}")

    }

    private fun showNoSuchMediaMessage() {
        resultItemText.text = formattedResultTitleText(
            mediaResponse.similar?.info?.get(0)?.name,
            R.string.no_such_item,
            this
        )
        Log.d(TAG, "unknown item ${resultItemText.text}")

    }

    private fun showSuccessfulResultMessage() {
        resultItemText.text = formattedResultTitleText(
            mediaResponse.similar?.info?.get(0)?.name,
            R.string.results_for_text,
            this
        )
    }

    private fun showTastediveButton() {
        val tasteDiveButton: Button = findViewById(R.id.openTasteDive)
        tasteDiveButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.BASE_URL)
            startActivity(intent)
        }
        tasteDiveButton.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "SearchResultActivity"
    }
}