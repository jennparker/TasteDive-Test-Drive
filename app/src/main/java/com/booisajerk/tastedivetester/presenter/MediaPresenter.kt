package com.booisajerk.tastedivetester.presenter

import android.annotation.SuppressLint
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.booisajerk.tastedivetester.MoshiBuilder
import com.booisajerk.tastedivetester.VolleySingleton
import com.booisajerk.tastedivetester.model.Media
import com.booisajerk.tastedivetester.model.ResponseData
import com.booisajerk.tastedivetester.shared.App
import com.booisajerk.tastedivetester.shared.Constants
import com.booisajerk.tastedivetester.shared.TextHelpers
import com.booisajerk.tastedivetester.view.interfaces.IMediaView
import com.squareup.moshi.JsonAdapter
import java.io.IOException

@SuppressLint("Registered")
class MediaPresenter : App() {
    private lateinit var adapter: JsonAdapter<ResponseData>
    private lateinit var mediaResponse: ResponseData

    private var mediaView: IMediaView? = null
    private var mediaList: ArrayList<Media> = ArrayList()

    fun onViewCreated(view: IMediaView) {
        mediaView = view
    }

    fun requestMedia(enteredMedia: String) {
        Log.d(TAG, "requestMedia() called")

        mediaView?.showLoading()
        val singleton = VolleySingleton.getInstance(App.instance)
        val request = StringRequest(
            Request.Method.GET
            , getRequestUrl(enteredMedia)
            , Response.Listener<String> { response: String ->
                Log.d(TAG, "Response received")

                val moshi = MoshiBuilder.moshiInstance
                adapter = moshi.adapter(ResponseData::class.java)

                try {
                    mediaResponse = adapter.fromJson(response)!!

                    println("Response:  $mediaResponse")

                    // Error response received
                    // Either user hasn't entered a vaild key or hourly quota has been reached
                    if (mediaResponse.similar == null) {
                        mediaView?.isServerError()
                    }

                    // A valid response is received
                    else {

                        // If results are empty
                        if (mediaResponse.similar!!.results?.isNullOrEmpty()!!) {
                            Log.d(TAG, "results are empty")

                            // If result type is not unknown (TasteDive has a record of this media, but doesn't have
                            // enough votes to make recommendations for it
                            if (mediaResponse.similar!!.info?.get(0)?.type != "unknown") {
                                mediaView?.isNoResultsError()
                            }

                            // If result type is unknown (TasteDive doesn't know what the search item is)
                            else {
                                mediaView?.isNoSuchMediaError()
                            }
                        }
                        if (mediaResponse.similar!!.results?.isNotEmpty()!!) {

                            // Response with results
                            for ((count, item) in mediaResponse.similar?.results?.withIndex()!!) {
                                mediaList.add(
                                    Media(
                                        mediaResponse.similar?.results?.get(count)?.name,
                                        mediaResponse.similar?.results?.get(count)?.type,
                                        TextHelpers.trimNewLines(mediaResponse.similar?.results?.get(count)?.description)
                                    )
                                )
                            }

                            mediaView?.onMediaLoaded(mediaList)
                            mediaView?.hideLoading()
                            Log.d(TAG, "Response: $mediaResponse")
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error ->
                error.printStackTrace()
                mediaView?.onError(error)
            }
        )
        Log.d(TAG, "Request is: $request")
        singleton.requestQueue.add(request)
    }

    private fun getRequestUrl(searchString: String): String {
        return Constants.BASE_URL + Constants.API + Constants.QUERY_KEY + searchString + Constants.INFO_LEVEL + Constants.TASTE_DIVE_API_KEY
    }

    fun getReturnedName(): String? {
        return mediaResponse.similar?.info?.get(0)?.name
    }

    companion object {
        private const val TAG = "MediaPresenter"
    }
}