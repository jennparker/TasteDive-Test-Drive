package com.booisajerk.tastedivetester

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.booisajerk.tastedivetester.TextHelpers.formattedResultTitleText
import com.booisajerk.tastedivetester.TextHelpers.replaceSpaces
import com.booisajerk.tastedivetester.models.ResponseData
import com.squareup.moshi.JsonAdapter
import java.io.IOException

class SearchResultActivity : AppCompatActivity() {
    private lateinit var adapter: JsonAdapter<ResponseData>
    private lateinit var moviesResponse: ResponseData
    private lateinit var resultItemText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var movieList: ArrayList<Movie> = ArrayList()
    private lateinit var searchString: String
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        resultItemText = findViewById(R.id.searchItemText)

        // Retrieve the search string from the MainActivity
        searchString = replaceSpaces(intent.getStringExtra(Constants.INTENT_KEY))

        // Show progress bar while results load
        progress = findViewById(R.id.progressBar)
        progress.isIndeterminate = true
        progress.visibility = ProgressBar.VISIBLE

        viewManager = LinearLayoutManager(this)

        viewAdapter = CustomAdapter(movieList)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // improves performance
            // use if changes in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use linear layout manager
            layoutManager = viewManager

            // assign viewAdapter
            adapter = viewAdapter

            requestMovies(searchString)
        }
    }

    private fun requestMovies(enteredMovie: String) {
        Log.d(TAG, "requestMovie() called")
        val requestQueue = Volley.newRequestQueue(this)
        val url =
            Constants.TASTE_DIVE_BASE_URL + Constants.QUERY_KEY + enteredMovie + Constants.INFO_LEVEL + Constants.TASTE_DIVE_API_KEY

        val request = StringRequest(
            Request.Method.GET
            , url
            , Response.Listener<String> { response: String ->
                Log.d(TAG, "Response received")

                val moshi = MoshiBuilder.moshiInstance
                adapter = moshi.adapter(ResponseData::class.java)

                try {
                    moviesResponse = adapter.fromJson(response)!!

                    for ((count, item) in moviesResponse.similar.results.withIndex()) {
                        movieList.add(
                            Movie(
                                moviesResponse.similar.results[count].name,
                                moviesResponse.similar.results[count].type,
                                moviesResponse.similar.results[count].wTeaser,
                                moviesResponse.similar.results[count].wUrl,
                                moviesResponse.similar.results[count].yUrl,
                                moviesResponse.similar.results[count].yID
                            )
                        )

                        println("Adding new Movie to movieList: $item")
                    }


                    resultItemText.text = formattedResultTitleText(
                        moviesResponse.similar.info[0].name,
                        moviesResponse.similar.info[0].type,
                        this)

                    // Don't show title until it is properly formatted
                    resultItemText.visibility = View.VISIBLE

                    // Hide the progress bar now that data is loaded
                    progress.visibility = ProgressBar.INVISIBLE

                    Log.d(TAG, "Response: $moviesResponse")

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

    companion object {
        private val TAG = "SearchResultActivity"
    }
}