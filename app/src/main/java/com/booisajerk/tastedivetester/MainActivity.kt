package com.booisajerk.tastedivetester

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.booisajerk.tastedivetester.models.ResponseData
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: JsonAdapter<ResponseData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var moviesResponse: ResponseData
    private var movieList: ArrayList<Movie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart called")
        super.onStart()

        requestMovies()
    }


    fun requestMovies() {
        Log.d(TAG, "requestMovie() called")
        val requestQueue = Volley.newRequestQueue(this)
        val url =
            Constants.TASTE_DIVE_BASE_URL + Constants.SAMPLE_MOVIE + Constants.INFO_LEVEL + Constants.TASTE_DIVE_API_KEY
        //"https://tastedive.com/api/similar?q=tombstone&k=322552-MovieGro-VR4OHB0J&info=1"

        val request = StringRequest(
            Request.Method.GET
            , url
            , Listener<String> { response: String ->
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

                    updateMovieList()

                    Log.d(TAG, "Response: ${moviesResponse}")

                    Log.d(TAG, "Response size is ${moviesResponse.similar.results.size}")
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

    fun updateMovieList() {
        Log.d(TAG, "updateMovieList called")

        viewAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val TAG = "MainActivity"
    }
}



