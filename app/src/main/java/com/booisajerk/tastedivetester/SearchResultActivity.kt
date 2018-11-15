package com.booisajerk.tastedivetester

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.booisajerk.tastedivetester.models.ResponseData
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class SearchResultActivity: AppCompatActivity() {
    private lateinit var adapter: JsonAdapter<ResponseData>
    private lateinit var moviesResponse: ResponseData
    private lateinit var resultItemText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var movieList: ArrayList<Movie> = ArrayList()
    private lateinit var searchString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchString = intent.getStringExtra(Constants.INTENT_KEY)


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

    fun requestMovies(enteredMovie:String) {
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

                    var count = 0
                    for (item in moviesResponse.similar.results) {
                        movieList.add(
                            Movie(
                                moviesResponse.similar.results.get(count).name,
                                moviesResponse.similar.results.get(count).type,
                                moviesResponse.similar.results.get(count).wTeaser,
                                moviesResponse.similar.results.get(count).wUrl,
                                moviesResponse.similar.results.get(count).yUrl,
                                moviesResponse.similar.results.get(count).yID
                            )
                        )

                        count++
                        println("Adding new Movie to movieList: $item")
                    }

                    val searchMovieString: String = String.format(
                        resources.getString(R.string.results_for_text)
                        , moviesResponse.similar.info.get(0).name
                    )

                    resultItemText = findViewById(R.id.searchItemText)
                    resultItemText.text = searchMovieString

                    Log.d(TAG, "Response: ${moviesResponse}")

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